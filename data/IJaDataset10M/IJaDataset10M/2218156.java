package org.red5.server.net.rtmp;

import org.red5.server.net.ProtocolException;

public class HandshakeFailedException extends ProtocolException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8255789603304183796L;

    /**
     * Create handshake failed exception with given message
	 *
     * @param message message
     */
    public HandshakeFailedException(String message) {
        super(message);
    }
}
