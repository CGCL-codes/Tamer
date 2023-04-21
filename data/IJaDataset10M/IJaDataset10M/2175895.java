package es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor.extended;

import es.unizar.tecnodiscap.osgi4ami.device.sensor.SensorListener;
import es.unizar.tecnodiscap.osgi4ami.device.sensor.extended.BloodPressureSensor;
import es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor.UnizarSupervisedSensor;

/**
 *
 */
public class UnizarBloodPressureSensor extends UnizarSupervisedSensor implements BloodPressureSensor {

    private double systolicBP = 0.0;

    private double diastolicBP = 25.0;

    public UnizarBloodPressureSensor(String deviceid) {
        super(deviceid);
    }

    public void setDiastolicBP(double diastolicBP) {
        this.diastolicBP = diastolicBP;
        this.fireSensor(SensorListener.EVENT_VALUE_CHANGED);
    }

    public void setSystolicBP(double systolicBP) {
        this.systolicBP = systolicBP;
        this.fireSensor(SensorListener.EVENT_VALUE_CHANGED);
    }

    @Override
    public double getSystolicBP() {
        return systolicBP;
    }

    @Override
    public double getDiastolicBP() {
        return diastolicBP;
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
