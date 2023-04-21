package org.dwgsoftware.raistlin.composition.model;

/**
 * Exception to indicate that a service defintioon is unknown within
 * the scope of a service manager.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:17 $
 */
public final class ServiceUnknownException extends ServiceException {

    /**
     * Construct a new <code>ServiceUnknownException</code> instance.
     *
     * @param message The detail message for this exception.
     */
    public ServiceUnknownException(final String message) {
        this(message, null);
    }

    /**
     * Construct a new <code>ServiceUnknownException</code> instance.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public ServiceUnknownException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
