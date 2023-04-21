package uk.ac.manchester.cs.snee.compiler.queryplan;

public class AgendaLengthException extends Exception {

    private static final long serialVersionUID = -1617566489882511749L;

    /**
     * Construct a new task schedule exception with
     * the given message.
     */
    public AgendaLengthException(final String message) {
        super(message);
    }

    /**
     * Construct a new task schedule exception with 
     * the given message and cause.
     */
    public AgendaLengthException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
