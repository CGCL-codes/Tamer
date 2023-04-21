package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.AskJoinAlly;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 *
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinAlly extends L2GameClientPacket {

    private static final String _C__82_REQUESTJOINALLY = "[C] 82 RequestJoinAlly";

    private int _id;

    @Override
    protected void readImpl() {
        _id = readD();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) {
            return;
        }
        L2Object ob = L2World.getInstance().findObject(_id);
        if (!(ob instanceof L2PcInstance)) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET));
            return;
        }
        if (activeChar.getClan() == null) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER));
            return;
        }
        L2PcInstance target = (L2PcInstance) ob;
        L2Clan clan = activeChar.getClan();
        if (!clan.checkAllyJoinCondition(activeChar, target)) {
            return;
        }
        if (!activeChar.getRequest().setRequest(target, this)) {
            return;
        }
        SystemMessage sm = new SystemMessage(SystemMessageId.S2_ALLIANCE_LEADER_OF_S1_REQUESTED_ALLIANCE);
        sm.addString(activeChar.getClan().getAllyName());
        sm.addString(activeChar.getName());
        target.sendPacket(sm);
        sm = null;
        AskJoinAlly aja = new AskJoinAlly(activeChar.getObjectId(), activeChar.getClan().getAllyName());
        target.sendPacket(aja);
    }

    @Override
    public String getType() {
        return _C__82_REQUESTJOINALLY;
    }
}
