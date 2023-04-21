package jp.co.uchida.discretelibraryheap.task;

import java.util.List;

public class TaskResolver {

    private List<TaskSupport> tasks;

    private String cronExpression;

    public void setTasks(List<TaskSupport> tasks) {
        this.tasks = tasks;
    }

    public List<TaskSupport> getTasks() {
        return tasks;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return cronExpression;
    }
}
