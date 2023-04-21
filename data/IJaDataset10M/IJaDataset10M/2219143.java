package org.vizzini.util.tree;

import org.vizzini.VizziniRuntimeException;

/**
 * Provides a runtime exception to signal that a tree node does not allow
 * children.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class ChildNotAllowedException extends VizziniRuntimeException {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Construct this object.
     *
     * @since  v0.3
     */
    public ChildNotAllowedException() {
        super();
    }

    /**
     * Construct this object with the given message.
     *
     * @param  message  Message to include with the exception.
     *
     * @since  v0.3
     */
    public ChildNotAllowedException(String message) {
        super(message);
    }

    /**
     * Construct this object with the given cause.
     *
     * @param  cause  The cause.
     *
     * @since  v0.3
     */
    public ChildNotAllowedException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct this object with the given message and cause.
     *
     * @param  message  Message to include with the exception.
     * @param  cause    The cause.
     *
     * @since  v0.3
     */
    public ChildNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
