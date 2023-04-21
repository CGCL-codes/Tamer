package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.LsClientPacket;
import com.google.inject.Inject;

/**
 * This packet is request kicking player.
 * 
 * @author -Nemesiss-
 * 
 */
public class CM_REQUEST_KICK_ACCOUNT extends LsClientPacket {

    /**
	 * account id of account that login server request to kick.
	 */
    private int accountId;

    @Inject
    private LoginServer loginServer;

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
        loginServer.kickAccount(accountId);
    }
}
