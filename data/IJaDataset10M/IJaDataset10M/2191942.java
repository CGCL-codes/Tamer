package info.monami.osgi.osgi4ami.service.ambientMonitoring;

import info.monami.osgi.osgi4ami.service.ServiceListener;
import java.util.Map;

public interface UnusualPresenceInAreaListener extends ServiceListener {

    /**
     * Event raised when presence is detected
     */
    public static final int EVENT_PRESENCE_DETECTED = 0;

    /**
     * Event raised when no presence is detected
     */
    public static final int EVENT_NO_PRESENCE_DETECTED = 1;

    public void unusualPresenceInAreaEvent(UnusualPresenceInArea service, int event, Map info);
}
