package com.mysolution.core.web.controller.scenario.management.task;

import com.mysolution.persistence.domain.Task;
import com.mysolution.persistence.domain.Site;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * DKu
 * Date: Apr 6, 2009
 * Time: 10:12:59 PM
 */
public class TaskManagementBean {

    private Task task = new Task();

    private Site site = new Site();

    private Task loginTask = new Task();

    private Set<Task> allTasks = new HashSet<Task>();

    private List<Task> taskList = new ArrayList<Task>();

    private String action;

    private String linkedTaskId;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Task getLoginTask() {
        return loginTask;
    }

    public void setLoginTask(Task loginTask) {
        this.loginTask = loginTask;
    }

    public Set<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(Set<Task> allTasks) {
        this.allTasks = allTasks;
    }

    public String getLinkedTaskId() {
        return linkedTaskId;
    }

    public void setLinkedTaskId(String linkedTaskId) {
        this.linkedTaskId = linkedTaskId;
    }

    public Task.Type[] getTypeValues() {
        return Task.Type.values();
    }
}
