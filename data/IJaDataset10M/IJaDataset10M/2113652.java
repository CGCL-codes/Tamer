package taskList.views;

public class TaskItem {

    public int id;

    public String description;

    public int incrementId;

    public int project_member_id;

    public int story_id;

    public int effortestimated;

    public int effortleft;

    public int effort;

    public String projectMemberName;

    public String notes;

    public String creation_date;

    public String projectName;

    public int projectId;

    public int index;

    public int status;

    public TaskItem(int taskId) {
        this.id = taskId;
    }
}
