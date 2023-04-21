package uk.co.q3c.deplan.rcp.model.task;

import java.util.EventObject;
import uk.co.q3c.deplan.domain.task.Task;

public class TaskManagerEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final Task[] added;

    private final Task[] removed;

    private final Task parent;

    public Task getParent() {
        return parent;
    }

    public TaskManagerEvent(TaskManager source, Task parentTask, Task[] tasksAdded, Task[] tasksRemoved) {
        super(source);
        added = tasksAdded;
        removed = tasksRemoved;
        parent = parentTask;
    }

    public Task[] getAdded() {
        return added;
    }

    public Task[] getRemoved() {
        return removed;
    }
}
