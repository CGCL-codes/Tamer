package net.sf.nxqd;

/**
 * The class <code>NxqdException</code> is the encapsulating exception for exceptions
 * thrown by this package.
 *
 * @author <a href="mailto:webhiker@sourceforge.net">webhiker</a>
 * @version 1.0
 */
public class NxqdException extends Exception {

    /**
     * Create a new <code>NxqdException</code> instance.
     *
     * @param message a <code>String</code> value
     */
    public NxqdException(String message) {
        super(message);
    }

    /**
     * Create a new <code>NxqdException</code> instance.
     *
     * @param nestedException an <code>Exception</code> value
     */
    public NxqdException(Exception nestedException) {
        super(nestedException);
    }
}
