package toxTree.exceptions;

import java.io.Serializable;

/**
 * 
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-8-5
 */
public class DecisionResultUIDException extends DecisionResultIOException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -453946774551761094L;

    public static final String _mismatchMessage = "Serial Version UID mismatch! ";

    /**
     * 
     */
    public DecisionResultUIDException(Serializable object) {
        super(_mismatchMessage + object.getClass().getName());
    }

    /**
     * @param message
     */
    public DecisionResultUIDException(String message, Serializable object) {
        super(_mismatchMessage + object.getClass().getName() + message);
    }

    /**
     * @param cause
     */
    public DecisionResultUIDException(Throwable cause, Serializable object) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DecisionResultUIDException(String message, Throwable cause, Serializable object) {
        super(_mismatchMessage + object.getClass().getName() + message, cause);
    }
}
