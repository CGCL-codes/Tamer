package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.PledgeInfo;

/**
 * This class ...
 *
 * @version $Revision: 1.5.4.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestPledgeInfo extends L2GameClientPacket {

    private static final String _C__66_REQUESTPLEDGEINFO = "[C] 66 RequestPledgeInfo";

    private static Logger _log = Logger.getLogger(RequestPledgeInfo.class.getName());

    private int _clanId;

    @Override
    protected void readImpl() {
        _clanId = readD();
    }

    @Override
    protected void runImpl() {
        if (Config.DEBUG) _log.log(Level.FINE, "Info for clan " + _clanId + " requested");
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2Clan clan = ClanTable.getInstance().getClan(_clanId);
        if (clan == null) {
            if (Config.DEBUG) _log.warning("Clan data for clanId " + _clanId + " is missing for player " + activeChar.getName());
            return;
        }
        PledgeInfo pc = new PledgeInfo(clan);
        activeChar.sendPacket(pc);
    }

    @Override
    public String getType() {
        return _C__66_REQUESTPLEDGEINFO;
    }

    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }
}
