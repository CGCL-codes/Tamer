package org.simpleframework.http.core;

import java.io.IOException;

/**
 * The <code>TransferException</code> object is used to represent an
 * exception that is thrown when there is a problem producing the
 * response body. This can be used to wrap <code>IOException</code>
 * objects that are thrown from the underlying transport.
 * 
 * @author Niall Gallagher
 */
class TransferException extends IOException {

    /**
    * Constructor for the <code>TransferException</code> object. This
    * is used to represent an exception that is thrown when producing
    * the response body. The producer exception is an I/O exception
    * and thus exceptions can propagate out of stream methods.
    * 
    * @param message this is the message describing the exception
    */
    public TransferException(String message) {
        super(message);
    }

    /**
    * Constructor for the <code>TransferException</code> object. This
    * is used to represent an exception that is thrown when producing
    * the response body. The producer exception is an I/O exception
    * and thus exceptions can propagate out of stream methods.
    * 
    * @param message this is the message describing the exception
    * @param cause this is the cause of the producer exception
    */
    public TransferException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
