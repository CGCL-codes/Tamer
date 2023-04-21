package es.unizar.tecnodiscap.osgi4ami.device.sensor;

import java.util.ArrayList;

/**
 * This cluster provides methods for accessing meassures stored on a sensor
 * @author unizar
 */
public interface DataloggerSensorCluster {

    /**
     * Key to extend the map of the value with the timestamp
     */
    public static final String VALUE_TIMESTAMP = "VALUE_TIMESTAMP";

    /**
     * Returns the last available meassures gathered by the sensor
     * (first value is the newest). Each element must be a map, codified in the
     * same way that in the Sensor.getValue() method, extended with the pair
     * {DataloggerSensorCluster.VALUE_TIMESTAMP, Sensor.getValueTimestamp()} for
     * the relative value
     * @return ArrayList containing map values (the first value is the newest).
     */
    public ArrayList getDatalog();

    /**
     * Deletes the first (oldest) values stored in the datalogger, or all of
     * them if numValues is 0.
     * @param numValues Ammount of the oldest values to deleted (0 means all).
     * @return 0 if sucess, other value means error
     */
    public int clearDatalog(int numValues);

    /**
     * Size of the datalog, num of values stored
     * @return Number of values stored
     */
    public int datalogSize();
}
