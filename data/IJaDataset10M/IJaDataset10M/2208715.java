package mujmail.tasks;

/**
 *
 * Represents events that sends BackgroundTask and it's descendatnts to
 * Observers registered to it.
 * 
 * @author David Hauzar
 */
public class TaskEvents {

    private TaskEvents() {
    }

    ;

    /** The progress was updated. This means that method {@link BackgroundTask#updateProgress} was called. */
    public static final TaskEvents UPDATE_PROGRESS = new TaskEvents();

    /** Actual progress was incremented. */
    public static final TaskEvents INC_ACTUAL = new TaskEvents();

    /** New title was setted. */
    public static final TaskEvents SET_TITLE = new TaskEvents();
}
