package net.sf.mzmine.taskcontrol;

import java.util.EventObject;

/**
 * A class for relaying Changes in TaskStatus to listeners
 */
public class TaskEvent extends EventObject {

    private TaskStatus status;

    /**
	 * Creates a new TaskEvent
	 * 
	 * @param source
	 *            The Task which caused this event.
	 */
    public TaskEvent(Task source) {
        super(source);
        this.status = source.getStatus();
    }

    /**
	 * Creates a new TaskEvent
	 * 
	 * @param source
	 *            The Task which caused this event.
	 * @param status
	 *            The new TaskStatus of the Task.
	 */
    public TaskEvent(Task source, TaskStatus status) {
        super(source);
        this.status = status;
    }

    /**
	 * Get the source of this TaskEvent
	 * 
	 * @return The Task which caused this event
	 */
    public Task getSource() {
        return (Task) this.source;
    }

    /**
	 * Get the new TaskStatus of the source Task
	 * 
	 * @return The new TaskStatus
	 */
    public TaskStatus getStatus() {
        return status;
    }
}
