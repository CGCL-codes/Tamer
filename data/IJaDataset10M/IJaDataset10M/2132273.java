package hu.sztaki.lpds.APP_SPEC.services;

import hu.sztaki.lpds.pgportal.services.pgrade.SZGWorkflow;

public class WorkflowForPublishBean {

    private SZGWorkflow workflow;

    public WorkflowForPublishBean(SZGWorkflow wf) {
        workflow = wf;
    }

    public String getName() {
        return this.workflow.getId().toString();
    }

    public String getOwner() {
        return this.workflow.getUserId();
    }
}
