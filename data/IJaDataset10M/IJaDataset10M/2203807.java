package org.tanso.ts.mts;

import org.tanso.fountain.core.eventmodel.GenericEventFilter;

/**
 * This interface defines the MTS interfaces which is working in event based
 * mode.
 * 
 * @author Haiping Huang
 */
public interface EventBasedMTS {

    /**
	 * Send a new message. The address information is contained in the msg.<br />
	 * <strong>NOTE:</strong> Return true doesn't mean the receiver has received
	 * the message.
	 * 
	 * @param msg
	 *            The message to be sent out.
	 * @param targetIp
	 *            Target IP address
	 * @param targetPort
	 *            Target Port
	 * @return true: if send out. false: send failed
	 */
    boolean sendMessage(String msg, String targetIp, int targetPort);

    /**
	 * Register a message listener.
	 * 
	 * @param ml
	 *            The message listener.
	 * @param filter
	 *            The message filter. Actually, a MTSMessage will be the
	 *            argument for the filter. null means receive all events.
	 */
    void addMessageListener(EventBasedMTSListener ml, GenericEventFilter filter);

    /**
	 * Remove a registered handler.
	 * 
	 * @param ml
	 *            The registered handler.
	 */
    void removeMessageListner(EventBasedMTSListener ml);

    /**
	 * Set the protocol for the MTS. Once listening started, the mode can't be
	 * changed.
	 * 
	 * @param mode
	 *            SocketProtocol.TCP or SocketProtocol.UDP
	 */
    void setMode(int mode);

    /**
	 * Start listen on a port.
	 * 
	 * @param mode
	 *            Specify using TCP or UDP. SEE org.tanso.ts.base.SocketProtocol
	 * @param port
	 *            The port number.
	 * @return true: listening started. false: error happens (already running,
	 *         port in use, etc.)
	 */
    boolean listen(int mode, int portNum);

    /**
	 * Stop the server
	 */
    void stop();
}
