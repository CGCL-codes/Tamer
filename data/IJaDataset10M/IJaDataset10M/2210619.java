package info.monami.osgi.osgi4ami.service.personalMonitoring;

import info.monami.osgi.osgi4ami.service.ServiceListener;
import java.util.Map;

public interface UserLocationMonitoringListener extends ServiceListener {

    /**
     * Event raised when the monitored person enters in a defined area
     */
    public static final int EVENT_AREA_ENTERED = 0;

    /**
     * Event raised when the monitored person leaves a defined area
     */
    public static final int EVENT_AREA_LEFT = 1;

    public void userLocationMonitoringEvent(UserLocationMonitoring service, int event, Map info);
}
