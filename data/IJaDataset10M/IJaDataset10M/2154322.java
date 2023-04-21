package purej.exception;

/**
 * Nexted exception implementation. Thanks Claus.
 */
public class NestedRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7859145830372859337L;

    private static final String CAUSED_BY = "\n ---- Caused by :  ";

    private Throwable cause = null;

    /**
     * Constructor
     */
    public NestedRuntimeException() {
    }

    /**
     * Constructor
     * 
     * @param msg
     *                error message
     */
    public NestedRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Constructor
     * 
     * @param cause
     *                the nested exception (caused by)
     */
    public NestedRuntimeException(Throwable cause) {
        super();
        this.cause = cause;
    }

    /**
     * Constructor
     * 
     * @param msg
     *                error message
     * @param cause
     *                the nested exception (caused by)
     */
    public NestedRuntimeException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    /**
     * Gets the causing exception, if any.
     * 
     * @return The cause of the exception
     */
    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * Converts the exception to a string representation
     * 
     * @return The string representation of the exception
     */
    @Override
    public String toString() {
        if (cause == null) {
            return super.toString();
        } else {
            return super.toString() + CAUSED_BY + cause.toString();
        }
    }

    /**
     * Sends a stack trace to System.err (including the root cause, if any)
     */
    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if (cause != null) {
            System.err.println(CAUSED_BY);
            cause.printStackTrace();
        }
    }

    /**
     * Sends a stack trace to the PrintStream passed in (including the root
     * cause, if any)
     * 
     * @param ps -
     *                the PrintStream to send the output to
     */
    @Override
    public void printStackTrace(java.io.PrintStream ps) {
        super.printStackTrace(ps);
        if (cause != null) {
            ps.println(CAUSED_BY);
            cause.printStackTrace(ps);
        }
    }

    /**
     * Sends a stack trace to the PrintWriter passed in (including the root
     * cause, if any)
     * 
     * @param pw -
     *                the PrintWriter to send the output to
     */
    @Override
    public void printStackTrace(java.io.PrintWriter pw) {
        super.printStackTrace(pw);
        if (cause != null) {
            pw.println(CAUSED_BY);
            cause.printStackTrace(pw);
        }
    }
}
