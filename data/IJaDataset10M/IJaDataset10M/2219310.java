package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ... cS
 *
 * @version $Revision: 1.1.2.2.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestRecipeShopMessageSet extends L2GameClientPacket {

    private static final String _C__B1_RequestRecipeShopMessageSet = "[C] b1 RequestRecipeShopMessageSet";

    private String _name;

    @Override
    protected void readImpl() {
        _name = readS();
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (player.getCreateList() != null) player.getCreateList().setStoreName(_name);
    }

    @Override
    public String getType() {
        return _C__B1_RequestRecipeShopMessageSet;
    }
}
