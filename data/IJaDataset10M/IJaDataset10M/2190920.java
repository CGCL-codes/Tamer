package es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor.extended;

import es.unizar.tecnodiscap.osgi4ami.driver.simulator.device.sensor.UnizarEventSensor;
import es.unizar.tecnodiscap.osgi4ami.driver.simulator.*;
import es.unizar.tecnodiscap.osgi4ami.device.sensor.extended.FloodSensor;

/**
 *
 */
public class UnizarFloodSensor extends UnizarEventSensor implements FloodSensor {

    public UnizarFloodSensor(String deviceid) {
        super(deviceid, FloodSensor.EVENT_FLOOD_DETECTED);
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
