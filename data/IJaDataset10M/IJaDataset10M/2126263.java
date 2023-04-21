package org.jaffa.persistence.exceptions;

/** This exception is thrown if an addition of an object to the persistent store fails.
 */
public class AddFailedException extends UOWException {

    private static final String ERROR_CODE = "exception.org.jaffa.persistence.exceptions.AddFailedException";

    /** Creates an exception with the errorCode.
     */
    public AddFailedException() {
        this(null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     */
    public AddFailedException(Object[] arguments) {
        this(arguments, null);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    public AddFailedException(Object[] arguments, Throwable cause) {
        this(ERROR_CODE, arguments, cause);
    }

    /** Creates an exception with the errorCode and a cause.
     * @param errorCode the errorCode.
     * @param arguments the arguments, if any, that need to be merged into the error message from the resource bundle.
     * @param cause the cause.
     */
    protected AddFailedException(String errorCode, Object[] arguments, Throwable cause) {
        super(errorCode, arguments, cause);
    }
}
