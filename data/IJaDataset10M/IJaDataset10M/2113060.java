package org.perfmon4j.extras.tomcat7;

import org.perfmon4j.instrument.snapshot.GeneratedData;

public interface ThreadPoolMonitor extends GeneratedData {

    public long getCurrentThreadsBusy();

    public long getCurrentThreadCount();

    public String getInstanceName();
}
