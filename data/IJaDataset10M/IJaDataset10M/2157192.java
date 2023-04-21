package engine.taskmanagement;

import java.util.ArrayList;

public final class TaskManager {

    private final ArrayList<Runnable> tasksQueue;

    public TaskManager() {
        tasksQueue = new ArrayList<Runnable>();
    }

    public final void enqueueTask(final Runnable task) {
        tasksQueue.add(task);
    }

    public final int getTaskCount() {
        return (tasksQueue.size());
    }

    public final void executeFirstTask() {
        if (!tasksQueue.isEmpty()) tasksQueue.remove(0).run();
    }

    public final void executeAllTasks() {
        while (getTaskCount() > 0) executeFirstTask();
    }
}
