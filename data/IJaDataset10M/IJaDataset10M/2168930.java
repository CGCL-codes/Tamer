package net.stogi.common.gwt.dispatch.shared.exception;

import net.stogi.common.gwt.dispatch.shared.core.Action;

/**
 * These are thrown by {@link Dispatch#execute(Action)} if there is a problem
 * executing a particular {@link Action}.
 */
public class ActionException extends Exception {

    private static final long serialVersionUID = -4133938014331900L;

    public ActionException() {
    }

    public ActionException(String message) {
        super(message);
    }

    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
