package org.stanwood.media.source.xbmc.expression;

/**
 * This exception is thrown by the antlr generated code. It inherits runtime exception
 * as their are no exceptions defined on the interfaces.
 */
public class ExpressionParserException extends RuntimeException {

    private static final long serialVersionUID = 8626682231398024461L;

    /** Constructs a new exception with <code>null</code> as its
     * detail message.  The cause is not initialised, and may subsequently be
     * initialised by a call to {@link #initCause}.
     */
    public ExpressionParserException() {
    }

    /** Constructs a new exception with the specified detail message.
     * The cause is not initialised, and may subsequently be initialised by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ExpressionParserException(String message) {
        super(message);
    }

    /** Constructs a new exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ExpressionParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ExpressionParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
