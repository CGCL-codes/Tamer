package org.jgenesis.beanset;

/**
 *
 * @author  root
 */
public class BeanSetException extends java.lang.RuntimeException {

    /**
     * Creates a new instance of <code>DTOSetException</code> without detail message.
     */
    public BeanSetException() {
    }

    /**
     * Constructs an instance of <code>DTOSetException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BeanSetException(String msg) {
        super(msg);
    }

    public BeanSetException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
