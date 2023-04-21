package org.spark.util.ext.bpm;

public class BpmTask {

    String id;

    String taskName;

    String taskShowName;

    String startDate;

    String endDate;

    boolean end;

    String actor;

    String pooledActor;

    String processId;

    BpmToken token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskShowName() {
        return taskShowName;
    }

    public void setTaskShowName(String taskShowName) {
        this.taskShowName = taskShowName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPooledActor() {
        return pooledActor;
    }

    public void setPooledActor(String pooledActor) {
        this.pooledActor = pooledActor;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public BpmToken getToken() {
        return token;
    }

    public void setToken(BpmToken token) {
        this.token = token;
    }
}
