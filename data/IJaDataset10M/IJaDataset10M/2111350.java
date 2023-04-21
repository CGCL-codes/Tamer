package com.sitescape.team.module.workflow;

import java.util.Map;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import com.sitescape.team.domain.WorkflowState;
import com.sitescape.team.domain.WorkflowSupport;
import com.sitescape.util.Validator;

public class DecisionAction extends AbstractActionHandler {

    protected Log logger = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 1L;

    public void execute(ExecutionContext executionContext) throws Exception {
        ContextInstance ctx = executionContext.getContextInstance();
        Token current = executionContext.getToken();
        WorkflowSupport entry = loadEntry(ctx);
        WorkflowState ws = entry.getWorkflowState(new Long(current.getId()));
        if (ws != null) {
            if (infoEnabled) logger.info("Decision begin: at state " + ws.getState() + " thread " + ws.getThreadName());
            if (WorkflowUtils.isThreadEndState(ws.getDefinition(), ws.getState(), ws.getThreadName())) {
                if (infoEnabled) logger.info("Decision: end thread");
                if (!current.isRoot()) {
                    current.end(false);
                } else {
                    executionContext.getProcessInstance().end();
                }
                Map children = current.getChildren();
                if (children != null) {
                    for (Iterator iter = children.values().iterator(); iter.hasNext(); ) {
                        Token child = (Token) iter.next();
                        WorkflowState w = entry.getWorkflowState(new Long(child.getId()));
                        if (w != null) {
                            entry.removeWorkflowState(w);
                        }
                    }
                }
                if (!current.isRoot()) {
                    entry.removeWorkflowState(ws);
                    TransitionUtils.processConditions(entry, current);
                }
                return;
            }
            String toState = TransitionUtils.processConditions(executionContext, entry, ws);
            if (toState != null) {
                if (infoEnabled) logger.info("Decision transition(" + ws.getThreadName() + "): " + ws.getState() + "." + toState);
                executionContext.leaveNode(ws.getState() + "." + toState);
                return;
            }
            if (infoEnabled) logger.info("Decision wait: at state " + ws.getState() + " thread " + ws.getThreadName());
        }
    }
}
