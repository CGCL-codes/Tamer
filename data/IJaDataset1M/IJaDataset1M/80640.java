package org.kablink.teaming.module.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.kablink.teaming.domain.ChangeLog;
import org.kablink.teaming.domain.WorkflowState;
import org.kablink.teaming.domain.WorkflowSupport;

public class TimerAction extends AbstractActionHandler {

    protected Log logger = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 1L;

    public void execute(ExecutionContext executionContext) throws Exception {
        ContextInstance ctx = executionContext.getContextInstance();
        Token current = executionContext.getToken();
        WorkflowSupport entry = loadEntry(ctx);
        WorkflowState ws = entry.getWorkflowState(new Long(current.getId()));
        if (ws != null) {
            if (debugEnabled) logger.debug("Timeout begin: at state " + ws.getState() + " thread " + ws.getThreadName());
            String toState = WorkflowProcessUtils.processConditions(executionContext, entry, ws);
            WorkflowProcessUtils.processChangeLog(toState, ChangeLog.MODIFYWORKFLOWSTATE, entry);
            if (toState != null) {
                if (debugEnabled) logger.debug("Timeout transition(" + ws.getThreadName() + "): " + ws.getState() + "." + toState);
                executionContext.leaveNode(ws.getState() + "." + toState);
                return;
            }
            if (debugEnabled) logger.debug("Timeout wait: at state " + ws.getState() + " thread " + ws.getThreadName());
        }
    }
}
