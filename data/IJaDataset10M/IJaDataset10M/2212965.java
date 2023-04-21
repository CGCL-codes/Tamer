package org.dwgsoftware.raistlin.composition.model;

import org.apache.avalon.framework.CascadingRuntimeException;

/**
 * Exception to indicate that there was a model related error.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:17 $
 */
public class ModelRuntimeException extends CascadingRuntimeException {

    /**
     * Construct a new <code>ModelRuntimeException</code> instance.
     *
     * @param message The detail message for this exception.
     */
    public ModelRuntimeException(final String message) {
        this(message, null);
    }

    /**
     * Construct a new <code>ModelRuntimeException</code> instance.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public ModelRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
