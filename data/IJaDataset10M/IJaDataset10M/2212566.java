package info.monami.osgi.osgi4ami.device.sensor.extended;

import info.monami.osgi.osgi4ami.device.sensor.Sensor;

/**
 * Interface <b>proposal</b> for a ligth sensor.
 * @author unizar
 */
public interface LightSensor extends Sensor {

    /**
     * This method redefines the <b>getValue</b> method in Sensor, providing a
     * typed data representacion for light level
     *
     * @return light level measured in percentage (double)
     */
    public double getLightLevel();
}
