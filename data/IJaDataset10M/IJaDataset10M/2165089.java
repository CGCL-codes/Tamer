package net.kano.joscar.snac;

/**
 * Provides an empty implementation of <code>SnacRequestListener</code>. This is
 * convenient in that one can subclass <code>SnacRequestAdapter</code> to avoid
 * having to write rather ugly empty methods to fulfill the
 * <code>SnacRequestListener</code> contract, handling events you don't
 * particularly care about.
 */
public abstract class SnacRequestAdapter implements SnacRequestListener {

    public void handleSent(SnacRequestSentEvent e) {
    }

    public void handleResponse(SnacResponseEvent e) {
    }

    public void handleTimeout(SnacRequestTimeoutEvent event) {
    }
}
