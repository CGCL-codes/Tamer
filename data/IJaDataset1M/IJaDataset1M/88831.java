package com.continuent.tungsten.manager.exception;

/**
 * This class defines a HandlerException
 * 
 * @author <a href="mailto:alexey.yurchenko@continuent.com">Alex Yurchenko</a>
 * @version 1.0
 */
public class HandlerException extends Exception {

    private static final long serialVersionUID = 2174164187202619721L;

    public enum Type {

        BUSY, STATE, COMMAND, HANDLER, RESOURCE, SYSTEM
    }

    public final Type type;

    /**
     * Creates a new <code>HandlerException</code> object
     * 
     * @param arg0
     */
    public HandlerException(Type type, String arg0) {
        super(arg0);
        this.type = type;
    }

    /**
     * Creates a new <code>HandlerException</code> object
     * 
     * @param arg0
     */
    public HandlerException(Type type, Throwable arg0) {
        super(arg0);
        this.type = type;
    }

    /**
     * Creates a new <code>HandlerException</code> object
     * 
     * @param arg0
     * @param arg1
     */
    public HandlerException(Type type, String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.type = type;
    }

    public String toString() {
        String cause = getCause() != null ? getCause().getClass().getSimpleName() : "UNDEFINED";
        String causeMessage = getCause() != null ? getCause().getLocalizedMessage() : "";
        return (String.format("Handler Exception: %s\nCause:%s\nMessage:%s", type, cause, causeMessage));
    }
}
