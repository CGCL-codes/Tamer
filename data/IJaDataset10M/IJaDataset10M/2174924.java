package velcro.messaging;

/**
 *
 * @author  heathm@users.sourceforge.net
 * @version $Revision: 1.1 $
 */
public class NoIdException extends java.lang.RuntimeException {

    /**
     * Creates a new instance of <code>NoIdException</code> without detail message.
     */
    public NoIdException() {
    }

    /**
     * Constructs an instance of <code>NoIdException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoIdException(String msg) {
        super(msg);
    }
}
