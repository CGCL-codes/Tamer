package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.GmListTable;

/**
 * This class handles RequestGmLista packet triggered by /gmlist command
 *
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestGmList extends L2GameClientPacket {

    private static final String _C__81_REQUESTGMLIST = "[C] 81 RequestGmList";

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        if (getClient().getActiveChar() == null) return;
        GmListTable.getInstance().sendListToPlayer(getClient().getActiveChar());
    }

    @Override
    public String getType() {
        return _C__81_REQUESTGMLIST;
    }
}
