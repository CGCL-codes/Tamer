package es.usc.citius.servando.android.medim.model.devices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import es.usc.citius.servando.android.models.MIT.MITSignalSpecification;

/**
 * 
 * @author Ángel Piñeiro
 *
 */
@Root(name = "deviceInfo")
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Element
    private String deviceId;

    @Element
    private DeviceType deviceType;

    @Element
    private String model;

    @Element
    private String deviceName;

    @Element
    private String serviceName;

    @Element
    private int acquisitionFrecuency;

    @ElementMap(keyType = String.class, valueType = Sensor.class)
    private Map<String, Sensor> sensors;

    @ElementList(type = MITSignalSpecification.class)
    private List<MITSignalSpecification> signalSpecs;

    public DeviceInfo() {
        signalSpecs = new ArrayList<MITSignalSpecification>();
        sensors = new HashMap<String, Sensor>();
    }

    public DeviceInfo(int frec) {
        acquisitionFrecuency = frec;
        signalSpecs = new ArrayList<MITSignalSpecification>();
        sensors = new HashMap<String, Sensor>();
    }

    /**
	 * Anadir una senal (canal) nueva al dispositivo
	 * 
	 * @param senal
	 */
    public void addSignal(MITSignalSpecification senal) {
        if (signalSpecs == null) signalSpecs = new ArrayList<MITSignalSpecification>();
        signalSpecs.add(senal);
    }

    public void addSensor(Sensor s) {
        if (sensors == null) sensors = new HashMap<String, Sensor>();
        sensors.put(s.getName(), s);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAcquisitionFrecuency() {
        return acquisitionFrecuency;
    }

    public void setAcquisitionFrecuency(int acquisitionFrecuency) {
        this.acquisitionFrecuency = acquisitionFrecuency;
    }

    /**
	 * Numero de canales Totales del dispositivo.
	 * 
	 * @return
	 */
    public int getNumberOfChannels() {
        return signalSpecs.size();
    }

    /**
	 * Numero de canales Usados del dispositivo.
	 */
    public int getNumberOfUsedChannels() {
        int num = 0;
        for (MITSignalSpecification specs : signalSpecs) {
            if (specs.isUsed()) num++;
        }
        return num;
    }

    /**
	 * El tipo de dispositivo (ECG,Pulse,etc)
	 * 
	 * @return
	 */
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Map<String, Sensor> getSensors() {
        return sensors;
    }

    public String[] getSensorNames() {
        String[] names = new String[sensors.size()];
        int i = 0;
        for (String s : sensors.keySet()) {
            names[i++] = s;
        }
        return names;
    }

    public void setSensors(Map<String, Sensor> sensors) {
        this.sensors = sensors;
    }

    public void setDeviceType(DeviceType value) {
        deviceType = value;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String value) {
        model = value;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String value) {
        deviceName = value;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String value) {
        serviceName = value;
    }

    public List<MITSignalSpecification> getSignals() {
        return signalSpecs;
    }

    public float getAdquisitionFrecuency() {
        return acquisitionFrecuency;
    }

    public void updateSensors(String[] names, SensorState[] values) {
        for (int i = 0; i < names.length; i++) {
            sensors.get(names[i]).setState(values[i]);
        }
    }

    public String getSensorStates() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String sname : sensors.keySet()) {
            sb.append(sname).append(": ").append(sensors.get(sname).getState()).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(DeviceInfo.class.getSimpleName());
        sb.append(":\n\tid: ");
        sb.append(deviceId);
        sb.append("\n\ttype: ");
        sb.append(deviceType);
        sb.append("\n\tmodel: ");
        sb.append(model);
        sb.append("\n\tname: ");
        sb.append(deviceName);
        sb.append("\n\tserviceName: ");
        sb.append(serviceName);
        sb.append("\n\tsensorCount: ");
        sb.append(sensors != null ? sensors.size() : 0);
        sb.append("\n\tsignalCount: ");
        sb.append(signalSpecs != null ? signalSpecs.size() : 0);
        if (signalSpecs.size() > 0) {
            for (MITSignalSpecification ss : signalSpecs) sb.append("\n\t" + ss.toString());
        }
        sb.append("\n]");
        return sb.toString();
    }
}
