package de.ui.sushi.metadata;

/** Indicates a problem creating a simple type from a String */
public class SimpleTypeException extends Exception {

    private static final long serialVersionUID = 0;

    public SimpleTypeException(String msg) {
        super(msg);
    }

    public SimpleTypeException(String msg, Throwable cause) {
        this(msg);
        initCause(cause);
    }
}
