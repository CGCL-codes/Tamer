package net.sourceforge.transumanza.task;

import java.util.ArrayList;
import java.util.List;

public class TaskSequence {

    List list = new ArrayList();

    int currentTask = 0;

    public void addTask(LoaderTask task) {
        list.add(task);
    }

    public LoaderTask getNextTask() {
        return (LoaderTask) list.get(currentTask++);
    }

    public int getCurrentTaskIndex() {
        return currentTask;
    }

    public int getTaskListSize() {
        return list.size();
    }

    public boolean hasNextTask() {
        return (currentTask < list.size());
    }

    public void reset() {
        currentTask = -1;
    }
}
