package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ItemList;

public class RequestBuySellUIClose extends L2GameClientPacket {

    private static final String _C__D0_76_REQUESTBUYSELLUICLOSE = "[C] D0:76 RequestBuySellUIClose";

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null || activeChar.isInventoryDisabled()) return;
        activeChar.sendPacket(new ItemList(activeChar, true));
    }

    @Override
    public String getType() {
        return _C__D0_76_REQUESTBUYSELLUICLOSE;
    }
}
