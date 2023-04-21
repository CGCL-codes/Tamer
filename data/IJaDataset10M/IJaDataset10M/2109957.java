package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2ManufactureItem;
import net.sf.l2j.gameserver.model.L2ManufactureList;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.RecipeShopMsg;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ... cd(dd)
 *
 * @version $Revision: 1.1.2.3.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestRecipeShopListSet extends L2GameClientPacket {

    private static final String _C__B2_RequestRecipeShopListSet = "[C] b2 RequestRecipeShopListSet";

    private int _count;

    private int[] _items;

    @Override
    protected void readImpl() {
        _count = readD();
        if (_count < 0 || _count * 8 > _buf.remaining() || _count > Config.MAX_ITEM_IN_PACKET) _count = 0;
        _items = new int[_count * 2];
        for (int x = 0; x < _count; x++) {
            int recipeID = readD();
            _items[x * 2 + 0] = recipeID;
            int cost = readD();
            _items[x * 2 + 1] = cost;
        }
    }

    @Override
    protected void runImpl() {
        L2PcInstance player = getClient().getActiveChar();
        if (player == null) return;
        if (player.isInDuel()) {
            player.sendPacket(new SystemMessage(SystemMessageId.CANT_CRAFT_DURING_COMBAT));
            return;
        }
        if (_count == 0) {
            player.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_NONE);
            player.broadcastUserInfo();
            player.standUp();
        } else {
            L2ManufactureList createList = new L2ManufactureList();
            for (int x = 0; x < _count; x++) {
                int recipeID = _items[x * 2 + 0];
                int cost = _items[x * 2 + 1];
                createList.add(new L2ManufactureItem(recipeID, cost));
            }
            createList.setStoreName(player.getCreateList() != null ? player.getCreateList().getStoreName() : "");
            player.setCreateList(createList);
            player.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_MANUFACTURE);
            player.sitDown();
            player.broadcastUserInfo();
            player.sendPacket(new RecipeShopMsg(player));
            player.broadcastPacket(new RecipeShopMsg(player));
        }
    }

    @Override
    public String getType() {
        return _C__B2_RequestRecipeShopListSet;
    }
}
