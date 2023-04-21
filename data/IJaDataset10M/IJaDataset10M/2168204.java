package org.vardb.tasks;

public abstract class CAbstractTaskParams implements ITaskParams {

    protected String taskId;

    protected String user_id;

    protected String redirect;

    protected String email = null;

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    public String getRedirect() {
        return this.redirect;
    }

    public void setRedirect(final String redirect) {
        this.redirect = redirect;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public abstract String getTaskType();
}
