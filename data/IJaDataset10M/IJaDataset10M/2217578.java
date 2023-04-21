package org.elogistics.action.event;

import org.elogistics.action.IAction;

/**
 * Event to report that an action was cancelled.
 * 
 * Contains an Object to pass something that caused the cancel.
 * The cause must be a substitute of Object.
 * 
 * @author d6hawp
 *
 */
public class CancelEvent extends ProgressEvent implements IActionEvent {

    public Object cause;

    /**
	 * Creates a new  cancel-event.
	 * @param progress	The actual progress of the action in percent.
	 * @param cause		The cause of the cancelling.
	 * @param source	The action.
	 */
    public CancelEvent(int progress, Object cause, IAction source) {
        super(progress, source);
        this.cause = cause;
    }

    /**
	 * Creates a new cancel-event. With the progress set to 0 %.
	 * 
	 * @param cause		The cause of the cancelling.
	 * @param source	The action.
	 */
    public CancelEvent(Object cause, IAction source) {
        super(0, source);
        this.cause = cause;
    }

    /**
	 * Gets cause
	 * 
	 * @return The cause
	 */
    public Object getCause() {
        return this.cause;
    }
}
