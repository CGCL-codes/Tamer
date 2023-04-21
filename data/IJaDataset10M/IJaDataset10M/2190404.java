package net.afternoonsun.imaso.core.events;

/**
 * Event object used to inform subscribed classes of the traverse progress.
 *
 * @author Sergey Pisarenko aka drseergio (drseergio AT gmail DOT com)
 */
public class EventTraverseProgress implements ImasoEvent {

    /** Describes a task result. */
    public enum TaskResult {

        /** Successful execution. */
        OK, /** Failed execution. */
        TRASH
    }

    /** Traverse event result. */
    private TaskResult action;

    /** Message from the traversal processor. */
    private String message;

    /**
     * Default public constructor.
     *
     * @param action result of the traversal
     * @param message message from the traversal processor
     */
    public EventTraverseProgress(TaskResult action, String message) {
        this.action = action;
        this.message = message;
    }

    /**
     * Returns task execution result.
     *
     * @return task execution result
     */
    public TaskResult getAction() {
        return action;
    }

    /**
     * Returns the message from the traversal processor.
     *
     * @return message from traversal processor
     */
    public String getMessage() {
        return message;
    }
}
