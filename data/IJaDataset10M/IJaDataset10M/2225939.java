package nakayo.gameserver.network.loginserver.clientpackets;

import nakayo.gameserver.network.loginserver.LoginServer;
import nakayo.gameserver.network.loginserver.LsClientPacket;

/**
 * In this packet LoginServer is sending response for SM_ACCOUNT_RECONNECT_KEY with account name and reconnectionKey.
 *
 * @author -Nemesiss-
 */
public class CM_ACCOUNT_RECONNECT_KEY extends LsClientPacket {

    /**
     * accountId of account that will be reconnecting.
     */
    private int accountId;

    /**
     * ReconnectKey that will be used for authentication.
     */
    private int reconnectKey;

    /**
     * Constructs new instance of <tt>CM_ACCOUNT_RECONNECT_KEY </tt> packet
     *
     * @param opcode
     */
    public CM_ACCOUNT_RECONNECT_KEY(int opcode) {
        super(opcode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl() {
        accountId = readD();
        reconnectKey = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl() {
        LoginServer.getInstance().authReconnectionResponse(accountId, reconnectKey);
    }
}
