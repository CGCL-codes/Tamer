package koala.dynamicjava.interpreter.throwable;

import koala.dynamicjava.interpreter.error.ExecutionError;
import koala.dynamicjava.tree.Node;

/**
 * This error is thrown by an interpreted throw statement
 *
 * @author Stephane Hillion
 * @version 1.0 - 1999/05/26
 */
public class ReturnException extends ExecutionError {

    /**
     * Whether the return has a value
     * @serial
     */
    private boolean withValue;

    /**
     * The returned object
     * @serial
     */
    private Object value;

    /**
     * Constructs an <code>ReturnException</code> with a value
     * @serial
     */
    public ReturnException(String s, Node n) {
        super(s, n);
        withValue = false;
    }

    /**
     * Constructs an <code>ReturnExceptionError</code> with the specified 
     * detail message, filename, line, column and exception.
     * @param e  the return exception
     * @param n  the node in the syntax tree where the error occurs
     */
    public ReturnException(String s, Object o, Node n) {
        super(s, n);
        withValue = true;
        value = o;
    }

    /**
     * Returns the value returned
     */
    public Object getValue() {
        return value;
    }

    /**
     * Whether or not the return statement had a value
     */
    public boolean hasValue() {
        return withValue;
    }
}
