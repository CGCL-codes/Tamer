package org.jaffa.exceptions;

/** This ApplicationException will be thrown by the finderComponent2 in the event of a problem reading data for the query
 */
public class SetQueryDataException extends ApplicationException {

    private static final String ERROR_CODE = "exception.org.jaffa.exceptions.GetQueryDataException.invokationFailure";

    /** Constructs a new SetQueryDataException.
     */
    public SetQueryDataException() {
        this(null);
    }

    /** Constructs a new SetQueryDataException.
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public SetQueryDataException(Throwable cause) {
        super(ERROR_CODE, null, cause);
    }
}
