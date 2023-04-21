package es.usc.citius.servando.android.medim.Drivers.events;

import java.util.Calendar;
import java.util.Map;
import es.usc.citius.servando.android.medim.Drivers.ECG.Corscience.Protocols.Commands.BatteryStatus;
import es.usc.citius.servando.android.medim.model.devices.DeviceType;
import es.usc.citius.servando.android.medim.model.devices.Sensor;

public interface IDriverEventListener {

    void onBluetoothDisconnection(DeviceType device);

    void onBluetoothCoverage(DeviceType device, int estado);

    void onBatteryStatus(DeviceType device, BatteryStatus status);

    void onSensorConnectivityChange(DeviceType device, boolean desconexion, Map<String, Sensor> sensors);

    void onPacketLost(DeviceType device, int length, Calendar when, int sampleIndexInDriver);

    void onMessage(String message);

    void onMessage(DeviceType device, String message);

    void onAcquisitionStarted(DeviceType device);

    void onAcquisitionError(DeviceType device);

    void onAcquisitionError(DeviceType device, String message);

    void onAcquisitionComplete(DeviceType device);
}
