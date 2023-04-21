package org.odmg;

/**
 * This exception is thrown when accessing an object that was deleted.
 * @author	David Jordan (as Java Editor of the Object Data Management Group)
 * @version ODMG 3.0
 */
public class ObjectDeletedException extends ODMGRuntimeException {

    /**
     * Construct an instance of the exception.
     */
    public ObjectDeletedException() {
        super();
    }

    /**
     * Construct an instance of the exception.
     * @param	msg	A string providing a description of the exception.
     */
    public ObjectDeletedException(String msg) {
        super(msg);
    }

    /**
     * Construct with an error message and a cause.
     * @param msg	The error message associated with this exception.
     * @param cause the original cause.
     */
    public ObjectDeletedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
