package protopeer;

import protopeer.network.*;

/**
 * The interface that all peerlets must implement. Peerlets have the
 * <code>init()</code>, <code>start()</code>, <code>stop()</code>,
 * <code>start()</code>, <code>stop()</code>... lifecycle.
 * 
 */
public interface Peerlet {

    /**
	 * Intializes the peerlet.
	 * 
	 * @param peer
	 *            the peer to which this peerlet was added
	 */
    public abstract void init(Peer peer);

    /**
	 * Starts the peerlet.
	 * 
	 */
    public abstract void start();

    /**
	 * Stops the peerlet.
	 * 
	 */
    public abstract void stop();

    /**
	 * Called by the peer after receiving the <code>message</code>
	 * 
	 */
    public abstract void handleIncomingMessage(Message message);

    /**
	 * Called by the peer before sending the <code>message</code>.
	 * 
	 */
    public abstract void handleOutgoingMessage(Message message);

    /**
	 * Called by the peer when the <code>massage</code> was successfully sent
	 * 
	 * @param message
	 */
    public abstract void messageSent(Message message);

    /**
	 * Called by the peer when a network exception happens.
	 * @param remoteAddress the remote address involved in the exception (either the source or destination), might be null
	 * @param message the message that was involved in the network exception or null
	 * @param cause 
	 *            
	 */
    public abstract void networkExceptionHappened(NetworkAddress remoteAddress, Message message, Throwable cause);
}
