package org.apache.catalina;

/**
 * General purpose exception that is thrown to indicate a lifecycle related
 * problem.  Such exceptions should generally be considered fatal to the
 * operation of the application containing this component.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public final class LifecycleException extends Exception {

    /**
     * Construct a new LifecycleException with no other information.
     */
    public LifecycleException() {
        this(null, null);
    }

    /**
     * Construct a new LifecycleException for the specified message.
     *
     * @param message Message describing this exception
     */
    public LifecycleException(String message) {
        this(message, null);
    }

    /**
     * Construct a new LifecycleException for the specified throwable.
     *
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(Throwable throwable) {
        this(null, throwable);
    }

    /**
     * Construct a new LifecycleException for the specified message
     * and throwable.
     *
     * @param message Message describing this exception
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(String message, Throwable throwable) {
        super();
        this.message = message;
        this.throwable = throwable;
    }

    /**
     * The error message passed to our constructor (if any)
     */
    protected String message = null;

    /**
     * The underlying exception or error passed to our constructor (if any)
     */
    protected Throwable throwable = null;

    /**
     * Returns the message associated with this exception, if any.
     */
    public String getMessage() {
        return (message);
    }

    /**
     * Returns the throwable that caused this exception, if any.
     */
    public Throwable getThrowable() {
        return (throwable);
    }

    /**
     * Return a formatted string that describes this exception.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("LifecycleException:  ");
        if (message != null) {
            sb.append(message);
            if (throwable != null) {
                sb.append(":  ");
            }
        }
        if (throwable != null) {
            sb.append(throwable.toString());
        }
        return (sb.toString());
    }
}
