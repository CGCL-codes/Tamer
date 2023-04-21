package info.monami.osgi.osgi4ami.service.personalMonitoring;

import info.monami.osgi.osgi4ami.service.Service;

public interface UserActivityDetection extends Service {

    public static final int EVENT_USER_ACTIVITY_FINISHED = 0;

    public static final int EVENT_USER_ACTIVITY_STARTED = 1;

    public boolean registerUserActivityDetectionListener(UserActivityDetectionListener listener);

    public boolean unregisterUserActivityDetectionListener(UserActivityDetectionListener listener);
}
