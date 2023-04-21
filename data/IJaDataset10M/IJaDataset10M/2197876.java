package com.wizzer.m3g.viewer.domain;

/**
 * The <code>SceneGraphException</code> is used to quantify exceptions thrown by the
 * Scene Graph Viewer application.
 * 
 * @author Mark Millard
 */
public class SceneGraphException extends Exception {

    static final long serialVersionUID = 20080810L;

    /**
	 * Constructs a new exception with <b>null</b> as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a call to
	 * <code>Throwable.initCause(java.lang.Throwable)</code>.
	 */
    public SceneGraphException() {
        super();
    }

    /**
	 * Constructs a new exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a call to
	 * <code>Throwable.initCause(java.lang.Throwable)</code>. 
	 * 
	 * @param msg The detail message. The detail message is saved for later retrieval
	 * by the <code>Throwable.getMessage()</code> method.

	 */
    public SceneGraphException(String msg) {
        super(msg);
    }

    /**
	 * Constructs a new exception with the specified cause and a detail message of
	 * (cause==null ? null : cause.toString()) (which typically contains the class and
	 * detail message of cause). This constructor is useful for exceptions that are
	 * little more than wrappers for other throwables (for example, <code>PrivilegedActionException</code>).
	 * 
	 * @param cause The cause (which is saved for later retrieval by the <code>Throwable.getCause()</code>
	 * method). (A <b>null</b> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
    public SceneGraphException(Throwable cause) {
        super(cause);
    }

    /**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with cause is not automatically incorporated
	 * in this exception's detail message. 
	 * </p>
	 * 
	 * @param msg The detail message (which is saved for later retrieval by the
	 * <code>Throwable.getMessage()</code> method).

	 * @param cause The cause (which is saved for later retrieval by the <code>Throwable.getCause()</code>
	 * method). (A <b>null</b> value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
    public SceneGraphException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
