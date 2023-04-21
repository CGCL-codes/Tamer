package org.apache.jdo.impl.enhancer;

/**
 * Thrown to indicate that the class-file enhancer failed to perform an
 * operation due to a serious error.  The enhancer is not guaranteed to
 * be in a consistent state anymore.
 */
public class EnhancerFatalError extends Exception {

    /**
     * An optional nested exception.
     */
    public final Throwable nested;

    /**
     * Constructs an <code>EnhancerFatalError</code> with no detail message.
     */
    public EnhancerFatalError() {
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with the specified
     * detail message.
     */
    public EnhancerFatalError(String msg) {
        super(msg);
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with an optional
     * nested exception.
     */
    public EnhancerFatalError(Throwable nested) {
        super(nested.toString());
        this.nested = nested;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with the specified
     * detail message and an optional nested exception.
     */
    public EnhancerFatalError(String msg, Throwable nested) {
        super(msg);
        this.nested = nested;
    }
}
