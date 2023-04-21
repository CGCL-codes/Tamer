package java.io;

/**
 * This error is thrown when a severe I/O error has happened.
 * 
 * @since 1.6
 */
public class IOError extends Error {

    private static final long serialVersionUID = 67100927991680413L;

    /**
     * Constructs a new instance with its cause filled in.
     * 
     * @param cause
     *            The detail cause for the error.
     */
    public IOError(Throwable cause) {
        super(cause);
    }
}
