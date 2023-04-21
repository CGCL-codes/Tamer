package com.openbravo.pos.forms;

/**
 *
 * @author adrianromero
 */
public class BeanFactoryException extends java.lang.RuntimeException {

    /**
     * Creates a new instance of <code>BeanFactoryException</code> without detail message.
     */
    public BeanFactoryException() {
    }

    /**
     * Constructs an instance of <code>BeanFactoryException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BeanFactoryException(String msg) {
        super(msg);
    }

    public BeanFactoryException(Throwable e) {
        super(e);
    }
}
