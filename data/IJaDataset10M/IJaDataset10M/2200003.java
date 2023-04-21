package nakayo.gameserver.network.aion.clientpackets;

import nakayo.gameserver.network.aion.AionClientPacket;
import nakayo.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;

/**
 * Send when the client requests the friendlist
 *
 * @author Ben
 */
public class CM_SHOW_FRIENDLIST extends AionClientPacket {

    public CM_SHOW_FRIENDLIST(int opcode) {
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
        if (getConnection().getActivePlayer() != null && getConnection().getActivePlayer().getFriendList() != null) {
            sendPacket(new SM_FRIEND_LIST(getConnection().getActivePlayer().getFriendList()));
        }
    }
}
