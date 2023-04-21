package org.kablink.teaming.jobs;

public interface LicenseMonitor {

    public static final String LICENSE_MONITOR_GROUP = "license-monitor";

    public static final String LICENSE_MONITOR_DESCRIPTION = "Gather licensing statistics";

    public static final String LICENSE_JOB = "license.job";

    public static final String LICENSE_HOUR = "timeout.hour";

    public void schedule(Long zoneId, int hour);

    public void remove(Long zoneId);
}
