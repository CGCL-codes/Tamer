package com.rpc.core;

/**
 * General Error class with ability to wrap the underlying nested
 * error/exception cause
 * 
 * @author Ihor
 */
public class RPCError extends Error {

    private static final long serialVersionUID = 1L;

    private Throwable detailThrowable;

    /**
     * Creates an error that contains no information about the exception that
     * occurred.
     */
    public RPCError() {
    }

    /**
     * Creates an error with the specified error message.
     * 
     * @param message
     *            java.lang.String The error message.
     */
    public RPCError(String message) {
        super(message);
    }

    /**
     * Creates an error with the specified error message and containing the
     * specified Throwable object as the root exception, which may be used in
     * logging or for debugging purposes.
     * 
     * @param message
     *            java.lang.String The error message.
     * @param ex
     *            java.lang.Throwable The contained exception that triggered
     *            this fatal exception.
     */
    public RPCError(String message, Throwable ex) {
        super(message);
        detailThrowable = ex;
    }

    /**
     * Creates an exception containing the specified Throwable object as the
     * root exception, which may be used in logging or for debugging purposes.
     * 
     * @param message
     *            java.lang.String The error message.
     * @param ex
     *            java.lang.Throwable The contained exception that triggered
     *            this fatal exception.
     */
    public RPCError(Throwable ex) {
        super();
        detailThrowable = ex;
    }

    public String getMessage() {
        if (detailThrowable == null) {
            return super.getMessage();
        }
        return super.getMessage() + "; nested exception is: \n\t" + detailThrowable.toString();
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(java.io.PrintStream ps) {
        if (detailThrowable == null) {
            super.printStackTrace(ps);
        } else {
            synchronized (ps) {
                ps.println(this);
                detailThrowable.printStackTrace(ps);
            }
        }
    }

    public void printStackTrace(java.io.PrintWriter pw) {
        if (detailThrowable == null) {
            super.printStackTrace(pw);
        } else {
            synchronized (pw) {
                pw.println(this);
                detailThrowable.printStackTrace(pw);
            }
        }
    }
}
