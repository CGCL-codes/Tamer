package info.monami.osgi.osgi4ami.service.personalMonitoring;

import info.monami.osgi.osgi4ami.service.Service;

public interface FallDetection extends Service {

    public static final int EVENT_FALL_DETECTED = 0;

    public boolean registerFallDetectionListener(FallDetectionListener listener);

    public boolean unregisterFallDetectionListener(FallDetectionListener listener);
}
