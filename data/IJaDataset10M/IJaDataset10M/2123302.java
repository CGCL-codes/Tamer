package com.mockturtlesolutions.snifflib.graphics;

import java.util.Set;

public interface ObjectReportable extends Reportable {

    public ObjectReporter getObjectReporter(String key);

    public void addObjectReporter(String key, ObjectReporter rep);

    public void removeObjectReporter(String key);

    public boolean objectReporterHasListeners(String key);

    public Set getObjectReporters();
}
