package org.openaion.gameserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.serverpackets.SM_PONG;

/**
 * I have no idea wtf is this
 * 
 * @author -Nemsiss-
 * 
 */
public class CM_PING extends AionClientPacket {

    private static final Logger log = Logger.getLogger(CM_PING.class);

    private static boolean firstPing = true;

    /**
	 * Constructs new instance of <tt>CM_PING </tt> packet
	 * 
	 * @param opcode
	 */
    public CM_PING(int opcode) {
        super(opcode);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void readImpl() {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void runImpl() {
        long lastMS = getConnection().getLastPingTimeMS();
        if (lastMS > 0) {
            long pingInterval = System.currentTimeMillis() - lastMS;
            if (pingInterval < CustomConfig.KICK_PINGINTERVAL) {
                String ip = getConnection().getIP();
                String name = "[unknown]";
                if (getConnection().getActivePlayer() != null) name = getConnection().getActivePlayer().getName();
                if (CustomConfig.KICK_SPEEDHACK) {
                    if (!firstPing) {
                        log.info("[AUDIT] possible client timer cheat kicking player: " + pingInterval + " by " + name + ", ip=" + ip);
                        AionConnection connection = getConnection().getActivePlayer().getClientConnection();
                        if (connection != null) connection.close(true);
                        return;
                    }
                    firstPing = false;
                } else {
                    log.info("[AUDIT] possible client timer cheat: " + pingInterval + " by " + name + ", ip=" + ip);
                }
            }
        }
        getConnection().setLastPingTimeMS(System.currentTimeMillis());
        sendPacket(new SM_PONG());
    }
}
