package org.jwebsocket.packetProcessors;

import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.token.Token;

/**
 *
 * @author aschulze
 */
public interface WebSocketPacketProcessor {

    /**
	 * 
	 * @param aDataPacket
	 * @return
	 */
    Token packetToToken(WebSocketPacket aDataPacket);

    /**
	 *
	 * @param aToken
	 * @return
	 */
    WebSocketPacket tokenToPacket(Token aToken);
}
