package org.ejc.persist.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TaskType implements Serializable {

    /** identifier field */
    private Integer taskTypeId;

    /** nullable persistent field */
    private String taskTypeDesc;

    /** persistent field */
    private Set tasks;

    /** full constructor */
    public TaskType(Integer taskTypeId, String taskTypeDesc, Set tasks) {
        this.taskTypeId = taskTypeId;
        this.taskTypeDesc = taskTypeDesc;
        this.tasks = tasks;
    }

    /** default constructor */
    public TaskType() {
    }

    /** minimal constructor */
    public TaskType(Integer taskTypeId, Set tasks) {
        this.taskTypeId = taskTypeId;
        this.tasks = tasks;
    }

    public Integer getTaskTypeId() {
        return this.taskTypeId;
    }

    public void setTaskTypeId(Integer taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskTypeDesc() {
        return this.taskTypeDesc;
    }

    public void setTaskTypeDesc(String taskTypeDesc) {
        this.taskTypeDesc = taskTypeDesc;
    }

    public Set getTasks() {
        return this.tasks;
    }

    public void setTasks(Set tasks) {
        this.tasks = tasks;
    }

    public String toString() {
        return new ToStringBuilder(this).append("taskTypeId", getTaskTypeId()).toString();
    }
}
