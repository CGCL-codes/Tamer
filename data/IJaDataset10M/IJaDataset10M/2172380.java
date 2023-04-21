package org.gamio.comm;

import org.gamio.buffer.BufferReader;

/**
 * Represents an incoming message and provides the access to read the message
 * data.
 * 
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 23 $ $Date: 2008-10-05 21:00:52 -0400 (Sun, 05 Oct 2008) $
 */
public interface InMsg extends Message {

    /**
	 * Gets the Internet Protocal (IP) address of the interface on which the
	 * message was received.
	 * 
	 * @return a {@code String} containing the IP address on which the message
	 *         was received
	 */
    public String getLocalAddr();

    /**
	 * Gets the Internet Protocal (IP) port number of the interface on which the
	 * message was received.
	 * 
	 * @return an integer specifying the port number
	 */
    public int getLocalPort();

    /**
	 * Gets the Internet Protocal (IP) address of the client or last proxy that
	 * sent the message.
	 * 
	 * @return a {@code String} containing the IP address of the client or last
	 *         proxy that sent the message
	 */
    public String getRemoteAddr();

    /**
	 * Gets the Internet Protocal (IP) source port of the client or last proxy
	 * that sent the message.
	 * 
	 * @return an integer specifying the port number
	 */
    public int getRemotePort();

    /**
	 * Gets the {@code BufferReader} via which the message raw data can be read.
	 * 
	 * @return the {@code BufferReader} providing the read-only access to the
	 *         message raw data
	 */
    public BufferReader getBufferReader();
}
