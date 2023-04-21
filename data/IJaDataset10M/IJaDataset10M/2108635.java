package org.slasoi.isslam.commons;

import org.slasoi.gslam.commons.plan.Task;
import org.slasoi.infrastructure.servicemanager.types.ProvisionRequestType;

/**
 * @author Beatriz Fuentes (TID)
 * 
 */
public class InfrastructureTask extends Task {

    private ProvisionRequestType provisionRequest = null;

    public InfrastructureTask(String taskId, String slaId, String actionName, String serviceManagerId) {
        super(taskId, slaId, actionName, serviceManagerId);
    }

    public void setProvisionRequest(ProvisionRequestType request) {
        this.provisionRequest = request;
    }

    public ProvisionRequestType getProvisionRequest() {
        return provisionRequest;
    }
}
