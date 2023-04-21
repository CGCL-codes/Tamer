package org.streets.workflow.engine.extension;

import org.streets.workflow.engine.IActivity;
import org.streets.workflow.engine.WorkflowEngine;
import org.streets.workflow.engine.WorkflowException;
import org.streets.workflow.engine.event.INodeEventListener;
import org.streets.workflow.engine.event.NodeEvent;
import org.streets.workflow.engine.mem.Activity;
import org.streets.workflow.engine.persistence.IWorkflowDAO;
import org.streets.workflow.engine.plugin.INodeNetExtension;

/**
 *
 */
public class ActivityExtension implements INodeNetExtension, INodeEventListener {

    public String getExtentionPointName() {
        return Activity.ExtensionPointNodeEventListener;
    }

    public String getExtentionTargetName() {
        return Activity.ExtensionTargetName;
    }

    /**
     * (non-Javadoc)
     * @see org.INodeEventListener.kernel.event.INodeInstanceEventListener#onNodeEventFired(org.fireflow.kernel.event.NodeInstanceEvent)
     */
    public void onNodeEventFired(WorkflowEngine engine, NodeEvent event) throws WorkflowException {
        if (event.getEventType() == NodeEvent.NODEINSTANCE_FIRED) {
            IWorkflowDAO persistenceService = engine.getRuntimeContext().getPersistenceService();
            persistenceService.saveOrUpdateToken(event.getToken());
            engine.createTaskInstances(event.getToken(), (IActivity) event.getSource());
        } else if (event.getEventType() == NodeEvent.NODEINSTANCE_COMPLETED) {
        }
    }
}
