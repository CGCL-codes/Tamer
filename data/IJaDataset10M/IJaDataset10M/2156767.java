package net.sf.alc.connection.tcp;

/**
 * This class is used to provide a base class for SelectorHandler.  All methods inherited from the interface
 * {@link SelectorHandler} throw a run-time exception, except handleClosed whose default implementation is to do
 * nothing.
 * @author alain.caron
 */
public class SelectorHandlerAdapter implements SelectorHandler {

    /**
     * Called when the associated socket is ready to be written.
     */
    public void handleWrite() {
        throw new UnsupportedOperationException("Sub-class needs to implement handleWrite()");
    }

    /**
     * Called when the associated socket is ready to be read.
     */
    public void handleRead() {
        throw new UnsupportedOperationException("Sub-class needs to implement handleRead()");
    }

    /**
     * Called by Reactor when the socket associated with the
     * class implementing this interface finishes establishing a
     * connection.
     */
    public void handleConnect() {
        throw new UnsupportedOperationException("Sub-class needs to implement handleConnect()");
    }

    /**
     * Called by Reactor when the server socket associated
     * with the class implementing this interface receives a request
     * for establishing a connection.
     */
    public void handleAccept() {
        throw new UnsupportedOperationException("Sub-class needs to implement handleAccept()");
    }

    /**
     * Called when the associated socket is closed.
     */
    public void handleClosed() {
    }
}
