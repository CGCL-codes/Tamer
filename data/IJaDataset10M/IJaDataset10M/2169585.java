package com.ivis.xprocess.workflowserver.monitors.alf;

import java.util.SortedSet;
import java.util.logging.Logger;
import com.ivis.xprocess.workflowserver.ExternalEvent;
import com.ivis.xprocess.workflowserver.WorkflowException;
import com.ivis.xprocess.workflowserver.WorkflowServer;
import com.ivis.xprocess.workflowserver.monitors.MonitorImpl;

/**
 * A basic monitor stub for ALF (Application Lifecycle Framework)
 *
 */
public class ALFMonitor extends MonitorImpl {

    protected static final Logger logger = Logger.getLogger(ALFMonitor.class.getName());

    public static String EXTERNAL_SYSTEM_NAME = "ALF";

    public static String[] EXTERNAL_EVENT_NAMES = new String[] { "ALF event" };

    @Override
    public String[] getEventNames() {
        return EXTERNAL_EVENT_NAMES;
    }

    @Override
    public String getSystemName() {
        return EXTERNAL_SYSTEM_NAME;
    }

    public void init(WorkflowServer server) throws WorkflowException {
        logger.fine("ALF monitor init fired");
        super.init(server.getDataSource());
    }

    @Override
    protected SortedSet<ExternalEvent> findEvents() throws WorkflowException {
        return null;
    }
}
