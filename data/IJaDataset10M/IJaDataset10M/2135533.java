package es.unizar.tecnodiscap.osgi4ami.simulator.driver.sensors;

import es.unizar.tecnodiscap.osgi4ami.device.sensor.SensorListener;
import es.unizar.tecnodiscap.osgi4ami.device.sensor.extended.PulseOximeterSensor;
import es.unizar.tecnodiscap.osgi4ami.simulator.driver.UnizarSupervisedSensor;

/**
 *
 * @author joaquin
 */
public class UnizarPulseOximeterSensor extends UnizarSupervisedSensor implements PulseOximeterSensor {

    private double heartRate = 78;

    private double oxigenSaturation = 25.2;

    public UnizarPulseOximeterSensor(String deviceid) {
        super(deviceid);
    }

    @Override
    public double getHeartRate() {
        return heartRate;
    }

    @Override
    public double getOxigenSaturation() {
        return oxigenSaturation;
    }

    @Override
    public double[] getPlesthymography() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
        this.fireSensor(SensorListener.EVENT_VALUE_CHANGED);
    }

    public void setOxigenSaturation(double oxigenSaturation) {
        this.oxigenSaturation = oxigenSaturation;
        this.fireSensor(SensorListener.EVENT_VALUE_CHANGED);
    }

    @Override
    public String[] getClusters() {
        String[] interHeredadas = super.getClusters();
        Class[] interfaces = getClass().getInterfaces();
        String[] inter = new String[interfaces.length + interHeredadas.length];
        for (int i = 0; i < interfaces.length; i++) {
            inter[i] = interfaces[i].getName();
        }
        System.arraycopy(interHeredadas, 0, inter, interfaces.length, interHeredadas.length);
        return inter;
    }
}
