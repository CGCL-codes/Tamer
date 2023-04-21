package handlers.chathandlers;

import l2.universe.Config;
import l2.universe.gameserver.handler.IChatHandler;
import l2.universe.gameserver.model.BlockList;
import l2.universe.gameserver.model.L2World;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.CreatureSay;
import l2.universe.gameserver.network.serverpackets.SystemMessage;

/**
 * Tell chat handler.
 *
 * @author  durgus
 */
public class ChatTell implements IChatHandler {

    private static final int[] COMMAND_IDS = { 2 };

    /**
	 * Handle chat type 'tell'
	 */
    public void handleChat(int type, L2PcInstance activeChar, String target, String text) {
        if (target == null) return;
        if (!activeChar.isGM() && !activeChar.getFloodProtectors().getGlobalChat().tryPerformAction("global chat")) return;
        if (activeChar.isChatBanned()) {
            activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CHATTING_PROHIBITED));
            return;
        }
        if (Config.JAIL_DISABLE_CHAT && activeChar.isInJail() && !activeChar.isGM()) {
            activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CHATTING_PROHIBITED));
            return;
        }
        final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
        final L2PcInstance receiver = L2World.getInstance().getPlayer(target);
        if (receiver != null && !receiver.isSilenceMode()) {
            if (Config.JAIL_DISABLE_CHAT && receiver.isInJail() && !activeChar.isGM()) {
                activeChar.sendMessage("Player is in jail.");
                return;
            }
            if (receiver.isChatBanned()) {
                activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE));
                return;
            }
            if (receiver.getClient() == null || receiver.getClient().isDetached()) {
                activeChar.sendMessage("Player is in offline mode.");
                return;
            }
            if (!BlockList.isBlocked(receiver, activeChar)) {
                receiver.sendPacket(cs);
                activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), type, "->" + receiver.getName(), text));
            } else activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE));
        } else activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME));
    }

    /**
	 * Returns the chat types registered to this handler
	 */
    public int[] getChatTypeList() {
        return COMMAND_IDS;
    }
}
