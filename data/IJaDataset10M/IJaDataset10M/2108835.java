package takatuka.drivers.interfaces.sensors;

/**
 * <p>Title: </p>
 * <p>Description:
 *  To read value form a sensor.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public interface IRead {

    /**
     * to read the sensor value
     * @return the sensor value
     */
    public short read();
}
