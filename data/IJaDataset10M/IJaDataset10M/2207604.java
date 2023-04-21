package org.dwgsoftware.raistlin.composition.model;

/**
 * Exception to indicate that there was a type related error.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:17 $
 */
public class TypeException extends ModelException {

    /**
     * Construct a new <code>TypeException</code> instance.
     *
     * @param message The detail message for this exception.
     */
    public TypeException(final String message) {
        this(message, null);
    }

    /**
     * Construct a new <code>TypeException</code> instance.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public TypeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
