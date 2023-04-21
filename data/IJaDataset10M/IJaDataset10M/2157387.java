package net.sf.balm.workflow.jbpm3.business;

import org.jbpm.taskmgmt.exe.TaskInstance;

public class BusinessIntegrationTaskInstanceExtensionStrategy implements BusinessIntegrationStrategy {

    public String getBusinessId(TaskInstance taskInstance) {
        return ((net.sf.balm.workflow.jbpm3.model.TaskInstance) taskInstance).getBusinessId();
    }
}
