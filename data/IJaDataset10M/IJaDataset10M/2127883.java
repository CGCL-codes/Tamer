package es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor;

import es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor.UnizarSensor;
import es.unizar.tecnodiscap.osgi4ami.device.sensor.SupervisedSensorCluster;

/**
 *
 */
public class UnizarSupervisedSensor extends UnizarSensor implements SupervisedSensorCluster {

    public UnizarSupervisedSensor(String deviceid) {
        super(deviceid);
    }

    @Override
    public int startSensor() {
        return 0;
    }

    @Override
    public int startMeasure() {
        return 0;
    }

    @Override
    public int stopSensor() {
        return 0;
    }

    @Override
    public String[] getClusters() {
        String[] interHeredadas = super.getClusters();
        Class[] interfaces = UnizarSupervisedSensor.class.getInterfaces();
        String[] inter = new String[interfaces.length + interHeredadas.length];
        for (int i = 0; i < interfaces.length; i++) {
            inter[i] = interfaces[i].getName();
        }
        System.arraycopy(interHeredadas, 0, inter, interfaces.length, interHeredadas.length);
        return inter;
    }
}
