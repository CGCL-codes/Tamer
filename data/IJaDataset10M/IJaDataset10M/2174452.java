package annone.ui;

import annone.util.AbortEventException;
import annone.util.EventListenerList;

public abstract class TaskRunnable implements Runnable {

    private Task task;

    public TaskRunnable() {
    }

    public abstract String getName();

    public Task getTask() {
        if (task == null) {
            task = new Task(getName());
            layer.addTask(task);
        }
        return task;
    }
}
