package org.jaffa.persistence.exceptions;

/** This exception is thrown if an error occurs in the PreAdd trigger of the persistent object.
 */
public class PreAddFailedException extends AddFailedException {

    private static final String ERROR_CODE = "exception.org.jaffa.persistence.exceptions.PreAddFailedException";

    /** Creates an exception with the errorCode.
     */
    public PreAddFailedException() {
        this(null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     */
    public PreAddFailedException(Object[] arguments) {
        this(arguments, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public PreAddFailedException(Object[] arguments, Throwable cause) {
        super(ERROR_CODE, arguments, cause);
    }
}
