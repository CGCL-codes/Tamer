package atodolist.gui;

import java.util.EventObject;
import atodolist.model.ToDoTask;

/**
 *
 * @author Usuario
 */
public class TaskEvent extends EventObject {

    private static final long serialVersionUID = -4266160712338494149L;

    private ToDoTask task;

    /** Creates a new instance of TaskEvent */
    public TaskEvent(Object source, ToDoTask task) {
        super(source);
        this.task = task;
    }

    public ToDoTask getTask() {
        return this.task;
    }
}
