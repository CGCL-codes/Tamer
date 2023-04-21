package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.FriendList.Status;
import org.openaion.gameserver.network.aion.AionClientPacket;

/**
 * Packet received when a user changes his buddylist status
 * @author Ben
 *
 */
public class CM_FRIEND_STATUS extends AionClientPacket {

    private int status;

    public CM_FRIEND_STATUS(int opcode) {
        super(opcode);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void readImpl() {
        status = readC();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();
        Status statusEnum = Status.getByValue(status);
        if (statusEnum == null) statusEnum = Status.ONLINE;
        activePlayer.getFriendList().setStatus(statusEnum);
    }
}
