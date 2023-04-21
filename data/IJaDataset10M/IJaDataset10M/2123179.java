package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LsClientPacket;

/**
 * This packet is request kicking player.
 */
public class CM_REQUEST_KICK_ACCOUNT extends LsClientPacket {

    /**
	* account id of account that login server request to kick.
	*/
    private int accountId;

    /**
	* Constructs new instance of <tt>CM_REQUEST_KICK_ACCOUNT </tt> packet.
	* @param opcode
	*/
    public CM_REQUEST_KICK_ACCOUNT(int opcode) {
        super(opcode);
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void readImpl() {
        accountId = readD();
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void runImpl() {
        LoginServer.getInstance().kickAccount(accountId);
    }
}
