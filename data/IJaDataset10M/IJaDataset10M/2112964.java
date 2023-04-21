package handlers.chathandlers;

import java.util.Collection;
import l2.universe.Config;
import l2.universe.ExternalConfig;
import l2.universe.gameserver.datatables.MapRegionTable;
import l2.universe.gameserver.handler.IChatHandler;
import l2.universe.gameserver.model.BlockList;
import l2.universe.gameserver.model.L2World;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.network.serverpackets.CreatureSay;
import l2.universe.gameserver.instancemanager.IrcManager;

/**
 * Shout chat handler.
 *
 * @author  durgus
 */
public class ChatShout implements IChatHandler {

    private static final int[] COMMAND_IDS = { 1 };

    /**
	 * Handle chat type 'shout'
	 */
    public void handleChat(int type, L2PcInstance activeChar, String target, String text) {
        if (!activeChar.isGM() && !activeChar.getFloodProtectors().getShoutChat().tryPerformAction("shout chat")) return;
        if (ExternalConfig.IRC_ENABLED) IrcManager.getInstance().sendChan("07!" + activeChar.getName() + ": " + text);
        final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
        final Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();
        if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("on") || (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("gm") && activeChar.isGM())) {
            final int region = MapRegionTable.getInstance().getMapRegion(activeChar.getX(), activeChar.getY());
            for (L2PcInstance player : pls) {
                if (region == MapRegionTable.getInstance().getMapRegion(player.getX(), player.getY()) && !BlockList.isBlocked(player, activeChar) && player.getInstanceId() == activeChar.getInstanceId()) player.sendPacket(cs);
            }
        } else if (Config.DEFAULT_GLOBAL_CHAT.equalsIgnoreCase("global")) {
            if (!activeChar.isGM() && !activeChar.getFloodProtectors().getGlobalChat().tryPerformAction("global chat")) {
                activeChar.sendMessage("Do not spam shout channel.");
                return;
            }
            for (L2PcInstance player : pls) {
                if (!BlockList.isBlocked(player, activeChar)) player.sendPacket(cs);
            }
        }
    }

    /**
	 * Returns the chat types registered to this handler
	 */
    public int[] getChatTypeList() {
        return COMMAND_IDS;
    }
}
