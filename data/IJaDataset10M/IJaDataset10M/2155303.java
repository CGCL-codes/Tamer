package org.dishevelled.timer;

/**
 * Runtime exception thrown in the event that a timer
 * is stopped before it has been started.
 *
 * @see Timer#start
 * @see Timer#stop
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public class TimerException extends RuntimeException {

    /**
     * Create a new timer expection with <code>null</code> as its
     * detail message.
     */
    public TimerException() {
        super();
    }

    /**
     * Create a new timer exception with the specified detail message.
     *
     * @param message detail message
     */
    public TimerException(final String message) {
        super(message);
    }

    /**
     * Create a new timer exception with the specified detail message and cause.
     *
     * @param message detail message
     * @param cause cause
     */
    public TimerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new timer exception with the specified cause.
     *
     * @param cause cause
     */
    public TimerException(final Throwable cause) {
        super(cause);
    }
}
