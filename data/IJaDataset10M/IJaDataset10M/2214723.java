package domino.logic;

/**
* <code>IllegalNodeTypeException</code> is thrown when a method that only
* works for some node types is called on a node that is unsupported by the
* method.
*/
public class IllegalNodeTypeException extends RuntimeException {

    /** Constructs an <code>IllegalNodeTypeException</code> with no detail message. */
    public IllegalNodeTypeException() {
    }

    ;

    /** Constructs an <code>IllegalNodeTypeException</code> with the specified 
  * detail message */
    public IllegalNodeTypeException(String s) {
        super(s);
    }

    ;
}

;
