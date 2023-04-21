package org.apache.lucene.store;

/**
 * This exception is thrown when there is an attempt to
 * access something that has already been closed.
 */
public class AlreadyClosedException extends IllegalStateException {

    public AlreadyClosedException(String message) {
        super(message);
    }
}
