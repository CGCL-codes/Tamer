package org.simpleframework.transport.connect;

import java.io.IOException;

/**
 * The <code>ConnectionException</code> is thrown if there is a problem
 * establishing a connection to the server. Such a problem can occur 
 * if the server has been stopped when a new connection arrives. This
 * can also be thrown if some other connection related issue occurs.
 * 
 * @author Niall Gallagher
 */
class ConnectionException extends IOException {

    /**
    * Constructor for the <code>ConnectionException</code> object. This
    * is used to represent an exception that is thrown when an error
    * occurs during the connect process. Typically this is thrown if
    * there is a problem connecting or accepting from a socket.
    * 
    * @param message this is the message describing the exception
    */
    public ConnectionException(String message) {
        super(message);
    }

    /**
    * Constructor for the <code>ConnectionException</code> object. This
    * is used to represent an exception that is thrown when an error
    * occurs during the connect process. Typically this is thrown if
    * there is a problem connecting or accepting from a socket.
    * 
    * @param message this is the message describing the exception
    * @param cause this is the cause of the producer exception
    */
    public ConnectionException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
