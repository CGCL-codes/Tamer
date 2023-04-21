package org.openremote.web.console.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openremote.web.console.client.WebConsole;
import org.openremote.web.console.service.AsyncControllerCallback;

public class PollingHelper {

    private Set<Integer> monitoredSensorIds = new HashSet<Integer>();

    private boolean monitorActive = false;

    private boolean monitorRunning = false;

    AsyncControllerCallback<Map<Integer, String>> callback;

    public PollingHelper(Set<Integer> sensorIds, AsyncControllerCallback<Map<Integer, String>> callback) {
        monitoredSensorIds = sensorIds;
        this.callback = callback;
    }

    public void addMonitoredSensor(Integer sensorId) {
        if (!monitoredSensorIds.contains(sensorId)) {
            monitoredSensorIds.add(sensorId);
        }
    }

    public void startSensorMonitoring() {
        monitorActive = true;
        if (!monitorRunning) {
            WebConsole.getConsoleUnit().getControllerService().getSensorValues(monitoredSensorIds.toArray(new Integer[0]), new AsyncControllerCallback<Map<Integer, String>>() {

                @Override
                public void onSuccess(Map<Integer, String> result) {
                    callback.onSuccess(result);
                }
            });
            monitorSensors();
        }
    }

    private void monitorSensors() {
        monitorRunning = true;
        WebConsole.getConsoleUnit().getControllerService().monitorSensors(monitoredSensorIds.toArray(new Integer[0]), new AsyncControllerCallback<Map<Integer, String>>() {

            @Override
            public void onFailure(Throwable exception) {
                monitorRunning = false;
                if (monitorActive) {
                    monitorSensors();
                }
            }

            @Override
            public void onSuccess(Map<Integer, String> result) {
                monitorRunning = false;
                if (monitorActive) {
                    if (result != null) {
                        callback.onSuccess(result);
                    }
                    monitorSensors();
                }
            }
        });
    }

    public void stopMonitoring() {
        monitorActive = false;
    }
}
