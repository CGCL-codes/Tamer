package edu.psu.citeseerx.exec.provider;

import edu.psu.citeseerx.utility.*;
import edu.psu.citeseerx.exec.protocol.TaskImpl;

public class TaskPool extends ObjectPool<TaskImpl> {

    private final TaskImpl prototype;

    public TaskImpl getPrototype() {
        return prototype;
    }

    public String getName() {
        return prototype.getName();
    }

    public TaskPool(TaskImpl task, int poolSize) {
        prototype = task;
        for (int i = 0; i < poolSize; i++) {
            add(task.newInstance());
        }
    }
}
