package net.lukemurphey.nsia.web;

/**
 * This class represents exceptions that occur when the browser terminates the network connection.
 * @author Luke Murphey
 *
 */
public class ClientAbortException extends Exception {

    private static final long serialVersionUID = 4090928686198968041L;

    public ClientAbortException() {
        super();
    }

    public ClientAbortException(Throwable t) {
        super(t);
    }
}
