package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestWithdrawalPledge extends L2GameClientPacket {

    private static final String _C__26_REQUESTWITHDRAWALPLEDGE = "[C] 26 RequestWithdrawalPledge";

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        if (activeChar.getClan() == null) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER));
            return;
        }
        if (activeChar.isClanLeader()) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.CLAN_LEADER_CANNOT_WITHDRAW));
            return;
        }
        if (activeChar.isInCombat()) {
            activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_CANNOT_LEAVE_DURING_COMBAT));
            return;
        }
        L2Clan clan = activeChar.getClan();
        clan.removeClanMember(activeChar.getName(), System.currentTimeMillis() + Config.ALT_CLAN_JOIN_DAYS * 86400000L);
        SystemMessage sm = new SystemMessage(SystemMessageId.S1_HAS_WITHDRAWN_FROM_THE_CLAN);
        sm.addString(activeChar.getName());
        clan.broadcastToOnlineMembers(sm);
        sm = null;
        clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(activeChar.getName()));
        activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_CLAN));
        activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_MUST_WAIT_BEFORE_JOINING_ANOTHER_CLAN));
    }

    @Override
    public String getType() {
        return _C__26_REQUESTWITHDRAWALPLEDGE;
    }
}
