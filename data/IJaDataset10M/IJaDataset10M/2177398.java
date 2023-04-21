package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.PledgeReceivePowerInfo;

/**
 * Format: (ch) dS
 * @author  -Wooden-
 *
 */
public final class RequestPledgeMemberPowerInfo extends L2GameClientPacket {

    protected static final Logger _log = Logger.getLogger(RequestPledgeMemberPowerInfo.class.getName());

    private static final String _C__D0_14_REQUESTPLEDGEMEMBERPOWERINFO = "[C] D0:14 RequestPledgeMemberPowerInfo";

    @SuppressWarnings("unused")
    private int _unk1;

    private String _player;

    @Override
    protected void readImpl() {
        _unk1 = readD();
        _player = readS();
    }

    /**
     * @see com.l2jserver.util.network.BaseRecievePacket.ClientBasePacket#runImpl()
     */
    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        L2Clan clan = activeChar.getClan();
        if (clan == null) return;
        L2ClanMember member = clan.getClanMember(_player);
        if (member == null) return;
        activeChar.sendPacket(new PledgeReceivePowerInfo(member));
    }

    /**
     * @see com.l2jserver.gameserver.BasePacket#getType()
     */
    @Override
    public String getType() {
        return _C__D0_14_REQUESTPLEDGEMEMBERPOWERINFO;
    }
}
