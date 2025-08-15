package com.davoice.vad;

import com.davoice.vadetection.vadandroidlibrary.VADetection;
import androidx.annotation.Nullable;
import ai.onnxruntime.*;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class VADRNAPI  {

    private final String TAG = "VADetection";
    private final Context context;

    // Map to hold multiple instances
    private Map<String, VADetection> instances = new HashMap<>();

    public VADAPI(Context _context) {
        super(context);
        Log.d(TAG, "VADAPI constructor");
        context = _context;
    }

    public void setVADDetectionLicense(String instanceId, String licenseKey) {
        Log.d(TAG, "setVADDetectionLicense()");

        VADetection instance = instances.get(instanceId);
        boolean isLicensed = false;
        if (instance != null) {
            isLicensed = instance.setLicenseKey(licenseKey);
        }
        else {
            Log.e(TAG, "instance == null!!!! ");
        }

        Log.d(TAG, "setVADDetectionLicense(): " + (isLicensed ? "Licensed" : "Not Licensed"));
    }

    public void createInstance(String instanceId, float threshold, int msWindow) {
        Log.d(TAG, "createInstance(): ");

        if (instances.containsKey(instanceId)) {
            return;
        }
        Log.d(TAG, "createInstance(): 2");

        try {
            VADetection instance = new VADetection(context, threshold, msWindow);
            Log.d(TAG, "createInstance(): 3 VADetection: " + instance);
            Log.d(TAG, "createInstance(): 3 instanceId: " + instanceId);
            instances.put(instanceId, instance);
        } catch (Exception e) {
            Log.e(TAG, "Exception(): 3 VADetection = new VADetection: ");
        }
    }

    // Start detection for a specific instance
    public void startVADDetection(String instanceId) {
        for (Map.Entry<String, VADetection> entry : instances.entrySet()) {
            String key = entry.getKey();
            VADetection value = entry.getValue();
            Log.d(TAG, "Key: " + key + ", Value: " + value.toString());
        }

        VADetection instance = this.instances.get(instanceId);
        if (instance != null) {
            instance.startListening();
        }
    }

    // Stop detection for a specific instance
    public void stopForegroundService(String instanceId) {
        VADetection instance = instances.get(instanceId);
        if (instance != null) {
            instance.stopForegroundService();
        }
    }
    
    // Stop detection for a specific instance
    public void startForegroundService(String instanceId) {
        VADetection instance = instances.get(instanceId);
        if (instance != null) {
            instance.startForegroundService();
        } else {
        }
    }

    public void getVoiceProps(String instanceId) {
        VADetection instance = instances.get(instanceId);
        if (instance != null) {
            try {
                Map<String, Object> voiceProps = instance.getVoiceProps();
                WritableMap result = Arguments.createMap();

                result.putString("error", (String) voiceProps.get("error"));
                result.putDouble("voiceProbability", (float) voiceProps.get("voiceProbability"));
                result.putDouble("lastTimeHumanVoiceHeard", (long) voiceProps.get("lastTimeHumanVoiceHeard"));

                promise.resolve(result);
            } catch (Exception e) {
            }
        } else {
        }
    }

    // Stop detection for a specific instance
    public void stopVADDetection(String instanceId) {
        VADetection instance = instances.get(instanceId);
        if (instance != null) {
            instance.stopListening();
        } else {
        }
    }

    // Destroy an instance
    public void destroyInstance(String instanceId) {
        VADetection instance = instances.remove(instanceId);
        if (instance != null) {
            instance.stopListening();
            // Additional cleanup if necessary
        } else {
        }
    }

    // Handle vad detection event
    private void onVADetected(String instanceId, Boolean detected) {
        if (detected) {
            WritableMap params = Arguments.createMap();
            params.putString("instanceId", instanceId);
            params.putString("phrase", instanceId);
        }
    }

    public void addListener(String eventName) {
        // Set up any upstream listeners or background tasks as necessary
    }

    public void removeListeners(Integer count) {
        // Remove upstream listeners, stop unnecessary background tasks
    }
    // Implement other methods as needed, ensuring to use instanceId
}

