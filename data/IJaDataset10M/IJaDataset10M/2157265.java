package com.sun.jini.norm;

/**
 * Exception thrown by <code>PersistentStore</code> when it discovers the store
 * has become corrupted.
 * 
 * @author Sun Microsystems, Inc.
 * 
 */
class CorruptedStoreException extends StoreException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs an <code>CorruptedStoreException</code> with a detail message.
	 * 
	 * @param s
	 *            the detailed message
	 */
    CorruptedStoreException(String s) {
        super(s);
    }

    /**
	 * Constructs an <code>CorruptedStoreException</code> with a detail message
	 * and a nested exception.
	 * 
	 * @param s
	 *            the detailed message
	 * @param t
	 *            root cause for exception, may be <code>null</code>
	 */
    CorruptedStoreException(String s, Throwable t) {
        super(s, t);
    }
}
