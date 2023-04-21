package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Shutdown;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ChooseInventoryItem;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class EnchantScrolls implements IItemHandler {

    private static final int[] ITEM_IDS = { 729, 730, 731, 732, 6569, 6570, 947, 948, 949, 950, 6571, 6572, 951, 952, 953, 954, 6573, 6574, 955, 956, 957, 958, 6575, 6576, 959, 960, 961, 962, 6577, 6578 };

    public void useItem(L2PlayableInstance playable, L2ItemInstance item) {
        if (!(playable instanceof L2PcInstance)) return;
        L2PcInstance activeChar = (L2PcInstance) playable;
        if (activeChar.isCastingNow()) return;
        if (Config.SAFE_SIGTERM && Shutdown.getCounterInstance() != null) {
            activeChar.sendMessage("You are not allowed to Enchant during server restart/shutdown!");
            return;
        }
        activeChar.setActiveEnchantItem(item);
        activeChar.sendPacket(new SystemMessage(SystemMessageId.SELECT_ITEM_TO_ENCHANT));
        activeChar.sendPacket(new ChooseInventoryItem(item.getItemId()));
        return;
    }

    public int[] getItemIds() {
        return ITEM_IDS;
    }
}
