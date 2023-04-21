package org.apache.jdo.model;

/**
 * This exception indicates a problem during model update.
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class ModelVetoException extends ModelException {

    /**
     * Creates new <code>ModelVetoException</code> without detail message.
     */
    public ModelVetoException() {
    }

    /**
     * Constructs a <code>ModelVetoException</code> with the specified
     * detail message.
     * @param msg the detail message.
     */
    public ModelVetoException(String msg) {
        super(msg);
    }

    /** 
     * Constructs a new <code>ModelVetoException</code> with the specified
     * cause.
     * @param cause the cause <code>Throwable</code>.
     */
    public ModelVetoException(Throwable cause) {
        super("", cause);
    }

    /** 
     * Constructs a new <code>ModelVetoException</code> with the specified
     * detail message and cause.
     * @param msg the detail message.
     * @param cause the cause <code>Throwable</code>.
     */
    public ModelVetoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
