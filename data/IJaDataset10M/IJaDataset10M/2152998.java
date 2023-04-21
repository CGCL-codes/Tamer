package koala.dynamicjava.interpreter.context;

/**
 * Thrown when a particular method cannot be found.
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/10/18
 */
public class NoSuchFunctionException extends Exception {

    /**
     * Constructs a <code>NoSuchFunctionException</code> without a detail message.
     */
    public NoSuchFunctionException() {
        super();
    }

    /**
     * Constructs a <code>NoSuchMethodException</code> with a detail message. 
     * @param s the detail message.
     */
    public NoSuchFunctionException(String s) {
        super(s);
    }
}
