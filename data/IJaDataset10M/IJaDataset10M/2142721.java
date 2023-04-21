package us.wthr.jdem846.tasks;

/** Thread class for the asynchronous execution of tasks.
 * 
 * @author Kevin M. Gill
 *
 */
public class TaskThread extends Thread {

    private TaskContainer taskContainer;

    public TaskThread(TaskContainer taskContainer) {
        this.taskContainer = taskContainer;
    }

    public void run() {
        taskContainer.runTask();
    }
}
