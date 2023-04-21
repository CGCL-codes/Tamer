package reports;

import java.util.Date;
import model.task.Priority;
import model.task.TaskState;

public class TaskBean implements Comparable {

    private String name;

    private Date startDate;

    private Date finishDate;

    private TaskState state;

    private String stateName;

    private Priority priority;

    private String priorityName;

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date endDate) {
        this.finishDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int compareTo(Object o) {
        TaskBean task = (TaskBean) o;
        return this.getState().compareTo(task.getState());
    }
}
