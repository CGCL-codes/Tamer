package com.techstar.dmis.entity;

public class Jbpm_Promission {

    private long process_id;

    private long task_id;

    private String task_name;

    private String task_role;

    private String agency_role;

    private long neccesory;

    private long premission_id;

    public String getAgency_role() {
        return agency_role;
    }

    public void setAgency_role(String agency_role) {
        this.agency_role = agency_role;
    }

    public long getNeccesory() {
        return neccesory;
    }

    public void setNeccesory(long neccesory) {
        this.neccesory = neccesory;
    }

    public long getPremission_id() {
        return premission_id;
    }

    public void setPremission_id(long premission_id) {
        this.premission_id = premission_id;
    }

    public long getProcess_id() {
        return process_id;
    }

    public void setProcess_id(long process_id) {
        this.process_id = process_id;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_role() {
        return task_role;
    }

    public void setTask_role(String task_role) {
        this.task_role = task_role;
    }
}
