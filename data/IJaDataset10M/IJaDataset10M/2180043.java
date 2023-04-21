package org.tripcom.security.exceptions;

/**
 * A runtime exception thrown by the security manager logic.
 * <p>
 * This class models a generic exception which can be thrown by security manager
 * code to signal abnormal or unexpected error conditions.
 * </p>
 * <p>
 * Note that erroneous and rejected requests are handled in the normal workflow
 * of the security manager, without the need of propagating a
 * <tt>SecurityManager</tt> exception (indeed, the possibility to reject a
 * client request falls in the normal functioning of the component, while
 * SecurityManagerException serves as a way to signal and handle other abnormal
 * situations perhaps caused by internal errors or by the wrong behavior of
 * related kernel components).
 * </p>
 * <p>
 * Note also that this exception is not checked because we do not expect to
 * specifically handle and recover from this kind of unexpected errors (we
 * simply abort the handling of the request communicating an internal failure to
 * the invoking client).
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class SecurityManagerException extends RuntimeException {

    /** Version identification code for serialization purposes. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new security manager exception, without an associated cause or
     * message.
     */
    public SecurityManagerException() {
        super();
    }

    /**
     * Create a security exception with the specified message.
     * 
     * @param message an error message providing further information about this
     *            occurrence of the exception.
     */
    public SecurityManagerException(String message) {
        super(message);
    }

    /**
     * Create a security exception with the provided cause and error message.
     * 
     * @param message an error message providing further information about this
     *            occurrence of the exception.
     * @param cause the original exception which caused this security manager
     *            exception.
     */
    public SecurityManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
