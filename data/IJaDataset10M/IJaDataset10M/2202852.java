package com.volantis.synergetics;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Wraps a throwable in a runtime exception to allow it to be rethrown, even
 * when it has not been declared. <p> This class is provided in JDK1.3 but as
 * we need to compile it on JDK1.2 we need to provide our own implementation.
 * </p> <p> This should not be used to circumvent having to declare the
 * exceptions which are thrown by a method but is available to use in those
 * situations when an exception cannot / should not occur and you don't want to
 * have to expose those internal exceptions to the user, e.g. extending
 * Object.clone on an object which implements the Cloneable interface should
 * not result in a CloneNotSupportedException but it is safer to wrap it in
 * this exception than to simply ignore it. </p>
 *
 * @deprecated Use standard exception now.
 */
public class UndeclaredThrowableException extends RuntimeException {

    /**
     * The undeclared checked exception that was thrown.
     */
    private Throwable undeclaredThrowable;

    /**
     * Constructs an <code>UndeclaredThrowableException</code> with the
     * specifed <code>Throwable</code>.
     *
     * @param undeclaredThrowable The undeclared checked exception that was
     *                            thrown.
     */
    public UndeclaredThrowableException(Throwable undeclaredThrowable) {
        super();
        this.undeclaredThrowable = undeclaredThrowable;
    }

    /**
     * Constructs an <code>UndeclaredThrowableException</code> with the
     * specified <code>Throwable</code> and a detail message.
     *
     * @param undeclaredThrowable The undeclared checked exception that was
     *                            thrown.
     * @param s                   The detail message.
     */
    public UndeclaredThrowableException(Throwable undeclaredThrowable, String s) {
        super(s);
        this.undeclaredThrowable = undeclaredThrowable;
    }

    /**
     * Returns the <code>Throwable</code> instance wrapped in this
     * <code>UndeclaredThrowableException</code>.
     *
     * @return The undeclared checked exception that was thrown.
     */
    public Throwable getUndeclaredThrowable() {
        return undeclaredThrowable;
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the standard error stream.
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the specified <code>PrintStream</code>.
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            if (undeclaredThrowable != null) {
                ps.print("java.lang.reflect.UndeclaredThrowableException: ");
                undeclaredThrowable.printStackTrace(ps);
            } else {
                super.printStackTrace(ps);
            }
        }
    }

    /**
     * Prints this <code>UndeclaredThrowableException</code> and its backtrace
     * to the specified <code>PrintWriter</code>.
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            if (undeclaredThrowable != null) {
                pw.print("java.lang.reflect.UndeclaredThrowableException: ");
                undeclaredThrowable.printStackTrace(pw);
            } else {
                super.printStackTrace(pw);
            }
        }
    }
}
