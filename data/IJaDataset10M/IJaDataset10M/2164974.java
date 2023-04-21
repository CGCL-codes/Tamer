package org.dpes.compare.compareManager;

import java.util.HashMap;
import java.util.Map;

public class SimpleTaskManagerImpl extends TaskManager {

    private Map<Long, Task> tasks = new HashMap<Long, Task>();

    private long currentTask = 0;

    @Override
    public long addTask(Task task) {
        tasks.put(currentTask, task);
        return currentTask++;
    }

    @Override
    public Task getTask(long id) {
        return tasks.get(id);
    }

    @Override
    public void removeTask(long id) {
        tasks.remove(id);
    }
}
