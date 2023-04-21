package de.schlund.pfixxml.perflogging;

import java.util.Map;

/**
 * @author mleidig@schlund.de
 */
public interface PerfLoggingMBean {

    public boolean isPerfLoggingEnabled();

    public boolean isPerfLoggingRunning();

    public void startPerfLogging();

    public String stopPerfLogging();

    public Map<String, Map<String, int[]>> stopPerfLoggingMap();
}
