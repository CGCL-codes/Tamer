package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.AskJoinPledge;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 *
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinPledge extends L2GameClientPacket {

    private static final String _C__24_REQUESTJOINPLEDGE = "[C] 24 RequestJoinPledge";

    private int _target;

    private int _pledgeType;

    @Override
    protected void readImpl() {
        _target = readD();
        _pledgeType = readD();
    }

    @Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        final L2Clan clan = activeChar.getClan();
        if (clan == null) return;
        final L2PcInstance target = L2World.getInstance().getPlayer(_target);
        if (target == null) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET));
            return;
        }
        if (!clan.checkClanJoinCondition(activeChar, target, _pledgeType)) return;
        if (!activeChar.getRequest().setRequest(target, this)) return;
        final String pledgeName = activeChar.getClan().getName();
        final String subPledgeName = (activeChar.getClan().getSubPledge(_pledgeType) != null ? activeChar.getClan().getSubPledge(_pledgeType).getName() : null);
        target.sendPacket(new AskJoinPledge(activeChar.getObjectId(), subPledgeName, _pledgeType, pledgeName));
    }

    public int getPledgeType() {
        return _pledgeType;
    }

    @Override
    public String getType() {
        return _C__24_REQUESTJOINPLEDGE;
    }
}
