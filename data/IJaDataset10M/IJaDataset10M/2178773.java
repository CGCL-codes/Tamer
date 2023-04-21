package com.aionengine.chatserver.network.gameserver.clientpackets;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import com.aionengine.chatserver.model.ChatClient;
import com.aionengine.chatserver.network.gameserver.AbstractGameClientPacket;
import com.aionengine.chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionengine.chatserver.network.netty.handler.GameChannelHandler;
import com.aionengine.chatserver.service.ChatService;

/**
 * @author ATracer
 */
public class CM_PLAYER_AUTH extends AbstractGameClientPacket {

    private static final Logger log = Logger.getLogger(CM_PLAYER_AUTH.class);

    private int playerId;

    private String playerLogin;

    public CM_PLAYER_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler) {
        super(buf, gameChannelHandler, 0x01);
    }

    @Override
    protected void readImpl() {
        playerId = readD();
        playerLogin = readS();
    }

    @Override
    protected void runImpl() {
        ChatClient chatClient = null;
        try {
            chatClient = ChatService.getInstance().registerPlayer(playerId, playerLogin);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error registering player on ChatServer: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Error registering player on ChatServer: " + e.getMessage());
        }
        if (chatClient != null) {
            gameChannelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE(chatClient));
        } else {
            log.info("Player was not authed " + playerId);
        }
    }
}
