package net.sf.opensftp;

/**
 * SFTP access exception.
 * 
 * @author BurningXFlame@gmail.com
 * 
 */
public class SftpException extends Exception {

    /**
	 * Constructs a new <code>SftpException</code> with <code>null</code> as its
	 * detailed message. The cause is not initialized, and may be initialized by
	 * a subsequent call to {@link #initCause(Throwable)}.
	 */
    public SftpException() {
        super();
    }

    /**
	 * Constructs a new <code>SftpException</code> with <code>null</code> as its
	 * detailed message. The cause is not initialized, and may be initialized by
	 * a subsequent call to {@link #initCause(Throwable)}.
	 * 
	 * @param message
	 *            the detailed message.
	 */
    public SftpException(String message) {
        super(message);
    }

    /**
	 * Constructs a new <code>SftpException</code> with the specified cause and
	 * a detailed message of <tt>(cause==null ? null : cause.toString())</tt>
	 * (which typically contains the class and detailed message of <tt>cause</tt>
	 * ). This constructor is useful for exceptions that are little more than
	 * wrappers for other throwables (for example,
	 * {@link java.security.PrivilegedActionException}).
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
    public SftpException(Throwable cause) {
        super(cause);
    }

    /**
	 * Constructs a new <code>SftpException</code> with the specified detail
	 * message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is
	 * <i>not</i> automatically incorporated in this exception's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
    public SftpException(String message, Throwable cause) {
        super(message, cause);
    }
}
