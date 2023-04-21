package com.l2jserver.gameserver.network.clientpackets;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExSendManorList;
import javolution.util.FastList;

/**
 * Format: ch
 * c (id) 0xD0
 * h (subid) 0x01
 * @author l3x
 */
public class RequestManorList extends L2GameClientPacket {

    private static final String _C__D0_01_REQUESTMANORLIST = "[C] D0:01 RequestManorList";

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        FastList<String> manorsName = new FastList<String>();
        manorsName.add("gludio");
        manorsName.add("dion");
        manorsName.add("giran");
        manorsName.add("oren");
        manorsName.add("aden");
        manorsName.add("innadril");
        manorsName.add("goddard");
        manorsName.add("rune");
        manorsName.add("schuttgart");
        ExSendManorList manorlist = new ExSendManorList(manorsName);
        player.sendPacket(manorlist);
    }

    @Override
    public String getType() {
        return _C__D0_01_REQUESTMANORLIST;
    }

    @Override
    protected boolean triggersOnActionRequest() {
        return false;
    }
}
