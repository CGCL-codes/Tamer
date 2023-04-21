package net.io;

import java.io.IOException;
import java.nio.channels.*;

/**
 * "OP_CONNECT" events Dispatcher
 *
 * @author    <a href="mailto:vsuman@gmail.com">Viorel Suman</a>
 * @version   $Id: ConnectDispatcher.java,v 1.1 2007/03/14 14:11:11 viorel_suman Exp $
 */
public final class ConnectDispatcher extends SelectDispatcher {

    /**
	 * Constructor for the ConnectDispatcher object
	 *
 	 * @param  name         the name of this dispatcher
	 * @throws IOException  if an IOException occures
	 */
    public ConnectDispatcher(String name) throws IOException {
        super(name);
    }

    /**
	 * Unregister listener from this dispatcher
	 *
	 * @param listener  listener to be unregistered
	 */
    public void unregister(SocketChannelListener listener) {
        super.unregister(listener, SelectionKey.OP_CONNECT);
    }

    /**
	 * Register a listener to be notified about "OP_CONNECT" events
	 *
	 * @param listener                 listener to be registered
	 * @throws ClosedChannelException  if the listener's channel is closed
	 */
    public void register(SocketChannelListener listener) throws ClosedChannelException {
        super.register(listener, SelectionKey.OP_CONNECT);
    }

    /**
	 * Dispatch "OP_CONNECT" event to the listener
	 *
	 * @param key                     SelectionKey on which the event occures
	 * @throws CancelledKeyException  if the passed key was cancelled
	 */
    protected void performDispatch(SelectionKey key) throws CancelledKeyException {
        ((SocketChannelListener) key.attachment()).connect(key);
    }
}
