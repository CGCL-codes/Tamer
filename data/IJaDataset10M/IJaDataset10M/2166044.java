package de.tud.kom.nat.comm.msg;

/**
 * When we try to establish a UDP connection to a target <tt>InetSocketAddress</tt>,
 * this host has to verify its existence by answering a <tt>UDPPing</tt>-Message with
 * a <tt>UDPPong</tt>-message.
 * 
 * TODO Maybe the concrete UDPPing message should be implemented by the application
 *
 * @author Matthias Weinert
 */
public class UDPPing extends GeneralMessage {

    /**
	 * Creates a ping message with our senderID. The receiver ID is usually unknown, consequently it
	 * is set to null.
	 * 
	 * @param senderID ID of the sender
	 */
    public UDPPing(IPeerID senderID) {
        super(senderID, null);
    }

    /** serial ID */
    private static final long serialVersionUID = 4429523990046837386L;
}
