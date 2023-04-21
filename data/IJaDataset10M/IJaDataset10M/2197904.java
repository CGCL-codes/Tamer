package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * cd(dd)
 * @version $Revision: 1.1.2.2.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestRecipeShopManageQuit extends L2GameClientPacket {

    private static final String _C__B3_RequestRecipeShopManageQuit = "[C] b2 RequestRecipeShopManageQuit";

    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        player.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_NONE);
        player.broadcastUserInfo();
        player.standUp();
    }

    @Override
    public String getType() {
        return _C__B3_RequestRecipeShopManageQuit;
    }
}
