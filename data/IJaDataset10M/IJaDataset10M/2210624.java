package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.loginserver.LoginServer;

/**
 * In this packets aion client is asking for fast reconnection to LoginServer.
 *
 * @author -Nemesiss-
 */
public class CM_RECONNECT_AUTH extends AionClientPacket {

    /**
     * Constructs new instance of <tt>CM_RECONNECT_AUTH </tt> packet
     *
     * @param opcode
     */
    public CM_RECONNECT_AUTH(int opcode) {
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
        AionConnection client = getConnection();
        LoginServer.getInstance().requestAuthReconnection(client);
    }
}
