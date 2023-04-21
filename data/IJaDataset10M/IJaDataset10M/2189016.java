package koala.dynamicjava.util;

/**
 * This exception is thrown when a lookup for a field
 * is ambiguous.
 *
 * @author Stephane Hillion
 * @version 1.0 - 1999/06/06
 */
public class AmbiguousFieldException extends Exception {

    /**
     * Constructs an <code>AmbiguousFieldException</code> with no detail message. 
     */
    public AmbiguousFieldException() {
        this("");
    }

    /**
     * Constructs an <code>AmbiguousFieldException</code> with the specified 
     * detail message. 
     * @param s the detail message (a key in a resource file).
     */
    public AmbiguousFieldException(String s) {
        super(s);
    }
}
