package whf.framework.workflow.engine;

import org.jbpm.graph.def.Transition;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class ExtendedTaskInstance extends TaskInstance {

    private static final long serialVersionUID = -3172366766256423090L;

    protected String workflowName;

    protected String sid;

    protected String entityName;

    protected String transition;

    protected String comment;

    protected String statusId;

    protected boolean rejected;

    public void end(Transition transition) {
        this.setStatusId(ExtendedTaskInstanceFactoryImpl.STATUS_ENDED);
        super.end(transition);
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
