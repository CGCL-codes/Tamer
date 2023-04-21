package info.monami.osgi.osgi4ami.device.actuator;

import java.sql.Time;
import java.util.Map;

/**
 * Defines methods to <b>retrieve actuator status</b>
 * @author unizar
 */
public interface GetStatusActuatorCluster {

    /**
     * Returns actuator status codified into a map variable. Fields in the map 
     * will depend on the particular actuator implementation.
     * @return Map codifying status. Codification depends on the implementation
     */
    public Map getActuatorStatus();

    /**
     * Returns last timestamp when the status was retrieved.
     * @return Timestamp when the status was retrieved.
     */
    public Time getActuatorStatusTimestamp();

    /**
     * Ask actuator to <b>refresh its status</b>, so it can be readed with a 
     * get status call.
     * @return Returns 0 if sucess, other value means error.
     */
    public int refreshActuatorStatus();
}
