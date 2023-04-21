package org.axsl.text;

/**
 * Exception thrown when an axslText implementation has a problem.
 */
public class TextException extends Exception {

    /** Constant for serialization. */
    private static final long serialVersionUID = 844043692256461582L;

    /**
     * Create a new TextException.
     * Identical to {@link Exception#Exception(java.lang.String)}
     * @param message The detail message. The detail message is saved for later
     * retrieval by the {@link #getMessage()} method.
     * @see Exception#Exception(java.lang.String)
     */
    public TextException(final String message) {
        super(message);
    }

    /**
     * Create a new TextException.
     * Identical to {@link Exception#Exception(java.lang.Throwable)}
     * @param cause The cause (which is saved for later retrieval by the
     * {@link #getCause()} method).
     * (A <tt>null</tt> value is permitted, and indicates that the cause is
     * nonexistent or unknown.)
     * @see Exception#Exception(java.lang.Throwable)
     */
    public TextException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create a new TextException.
     * Identical to {@link Exception#Exception(java.lang.String,
     * java.lang.Throwable)}
     * @param message The detail message. The detail message is saved for later
     * retrieval by the {@link #getMessage()} method.
     * @param cause The cause (which is saved for later retrieval by the
     * {@link #getCause()} method).
     * (A <tt>null</tt> value is permitted, and indicates that the cause is
     * nonexistent or unknown.)
     * @see Exception#Exception(java.lang.String, java.lang.Throwable)
     */
    public TextException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
