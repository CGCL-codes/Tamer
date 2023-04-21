package org.vizzini.game.action;

import org.vizzini.game.GameException;

/**
 * Provides an exception which indicates an illegal action has been encountered.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class IllegalActionException extends GameException {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Construct this object.
     *
     * @since  v0.1
     */
    public IllegalActionException() {
        super();
    }

    /**
     * Construct this object with the given message.
     *
     * @param  message  Message to include with the exception.
     *
     * @since  v0.1
     */
    public IllegalActionException(String message) {
        super(message);
    }

    /**
     * Construct this object with the given cause.
     *
     * @param  cause  The cause.
     *
     * @since  v0.1
     */
    public IllegalActionException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct this object with the given message and cause.
     *
     * @param  message  Message to include with the exception.
     * @param  cause    The cause.
     *
     * @since  v0.1
     */
    public IllegalActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
