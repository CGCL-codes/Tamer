package org.apache.jdo.impl.enhancer.meta;

/**
 * Thrown to indicate that an access to JDO meta-data failed; the
 * meta-data component is assured to remain in consistent state.
 */
public class EnhancerMetaDataUserException extends RuntimeException {

    /**
     * An optional nested exception.
     */
    public final Throwable nested;

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with no detail
     * message.
     */
    public EnhancerMetaDataUserException() {
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with the specified
     * detail message.
     */
    public EnhancerMetaDataUserException(String msg) {
        super(msg);
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with an optional
     * nested exception.
     */
    public EnhancerMetaDataUserException(Throwable nested) {
        super("nested exception: " + nested);
        this.nested = nested;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with the specified
     * detail message and an optional nested exception.
     */
    public EnhancerMetaDataUserException(String msg, Throwable nested) {
        super(msg);
        this.nested = nested;
    }
}
