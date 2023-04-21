package sun.security.pkcs11.wrapper;

/**
 * This is the superclass of all runtime exception used by this library.
 * For instance, Runtime exceptions occur, if an internal error in the native
 * part of the wrapper occurs.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @invariants
 */
public class PKCS11RuntimeException extends RuntimeException {

    /**
     * Empty constructor.
     *
     * @preconditions
     * @postconditions
     */
    public PKCS11RuntimeException() {
        super();
    }

    /**
     * Constructor taking a string that describes the reason of the exception
     * in more detail.
     *
     * @param message A descrption of the reason for this exception.
     * @preconditions
     * @postconditions
     */
    public PKCS11RuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor taking an other exception to wrap.
     *
     * @param encapsulatedException The other exception the wrap into this.
     * @preconditions
     * @postconditions
     */
    public PKCS11RuntimeException(Exception encapsulatedException) {
        super(encapsulatedException);
    }

    /**
     * Constructor taking a message for this exception and an other exception to
     * wrap.
     *
     * @param message The message giving details about the exception to ease
     *                debugging.
     * @param encapsulatedException The other exception the wrap into this.
     * @preconditions
     * @postconditions
     */
    public PKCS11RuntimeException(String message, Exception encapsulatedException) {
        super(message, encapsulatedException);
    }
}
