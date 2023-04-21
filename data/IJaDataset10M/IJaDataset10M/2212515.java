package cz.startnet.utils.pgdiff.loader;

/**
 * Exception thrown if problem occurred while reading or writing file.
 *
 * @author fordfrog
 */
public class FileException extends RuntimeException {

    /**
     * Serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>FileException</code> without
     * detail message.
     */
    public FileException() {
    }

    /**
     * Constructs an instance of <code>FileException</code> with the
     * specified detail message.
     *
     * @param msg the detail message
     */
    public FileException(final String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>FileException</code> with the
     * specified detail message.
     *
     * @param msg the detail message
     * @param cause cause of the exception
     */
    public FileException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
