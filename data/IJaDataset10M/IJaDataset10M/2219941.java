package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ManagePledgePower;

public final class RequestPledgePower extends L2GameClientPacket {

    static Logger _log = Logger.getLogger(ManagePledgePower.class.getName());

    private static final String _C__C0_REQUESTPLEDGEPOWER = "[C] C0 RequestPledgePower";

    private int _rank;

    private int _action;

    private int _privs;

    @Override
    protected void readImpl() {
        _rank = readD();
        _action = readD();
        if (_action == 2) {
            _privs = readD();
        } else _privs = 0;
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (_action == 2) {
            if (player.getClan() != null && player.isClanLeader()) {
                if (_rank == 9) {
                    _privs = (_privs & L2Clan.CP_CL_VIEW_WAREHOUSE) + (_privs & L2Clan.CP_CH_OPEN_DOOR) + (_privs & L2Clan.CP_CS_OPEN_DOOR);
                }
                player.getClan().setRankPrivs(_rank, _privs);
            }
        } else {
            ManagePledgePower mpp = new ManagePledgePower(getClient().getActiveChar().getClan(), _action, _rank);
            player.sendPacket(mpp);
        }
    }

    @Override
    public String getType() {
        return _C__C0_REQUESTPLEDGEPOWER;
    }
}
