package net.jini.io;

import java.io.IOException;

/**
 * Typically used as the nested exception of a
 * {@link java.rmi.ConnectIOException} if the constraints for a remote call
 * cannot be satisfied. Such an exception can be thrown at the point a remote
 * method is invoked for a variety of reasons, including:
 * <ul>
 * <li>A client requirement is not supported by the server.
 * <li>A client or server requirement conflicts with some other client or server
 * requirement.
 * <li>A client or server requirement cannot be satisfied by the proxy
 * implementation.
 * <li>The local subject that would be used for authentication does not contain
 * sufficient credentials to satisfy a client or server requirement.
 * <li>For a client or server requirement, the proxy implementation does not
 * implement any algorithm in common with the server (for example, because the
 * proxy implementation only uses algorithms that are available in the client
 * environment rather than downloading algorithm implementations).
 * <li>A delegated remote call is being attempted, and the current time is
 * either earlier than the granted delegation start time or later than the
 * granted delegation stop time.
 * </ul>
 * 
 * @author Sun Microsystems, Inc.
 * @since 2.0
 * @see net.jini.core.constraint.RemoteMethodControl
 */
public class UnsupportedConstraintException extends IOException {

    private static final long serialVersionUID = -5220259094045769772L;

    /**
	 * Creates an instance with the specified detail message.
	 * 
	 * @param s
	 *            the detail message
	 */
    public UnsupportedConstraintException(String s) {
        super(s);
    }

    /**
	 * Creates an instance with the specified detail message and cause.
	 * 
	 * @param s
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
    public UnsupportedConstraintException(String s, Throwable cause) {
        super(s);
        initCause(cause);
    }
}
