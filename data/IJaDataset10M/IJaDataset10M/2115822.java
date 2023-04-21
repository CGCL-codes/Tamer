package org.eclipse.ant.internal.core;

import java.util.Map;
import org.eclipse.ant.core.AntCorePlugin;

public abstract class AbstractEclipseBuildLogger {

    /**
     * Process identifier - used to link the Eclipse Ant build
     * loggers to a process.
     */
    public static final String ANT_PROCESS_ID = AntCorePlugin.PI_ANTCORE + ".ANT_PROCESS_ID";

    protected String fProcessId = null;

    public void configure(Map userProperties) {
        fProcessId = (String) userProperties.remove(ANT_PROCESS_ID);
    }
}
