package be.vds.jtb.taskmanager.model;

public interface TaskListener {

    public void taskStarted(Task task, String message);

    public void taskProgress(Task task, String message);

    public void taskFinished(Task task, String message);
}
