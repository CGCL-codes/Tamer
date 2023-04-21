package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;

/**
 * This class trades Gold Bars for Adena and vice versa.
 *
 * @author Ahmed
 */
public class Banking implements IVoicedCommandHandler {

    private static final String[] _voicedCommands = { "bank", "withdraw", "deposit" };

    /**
	 * @see net.sf.l2j.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target) {
        if (!activeChar.getFloodProtectors().getBankingSystem().tryPerformAction("BankingSystem")) {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return false;
        }
        if (command.equalsIgnoreCase("bank")) activeChar.sendMessage(".deposit (" + Config.BANKING_SYSTEM_ADENA + " Adena = " + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar) / .withdraw (" + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar = " + Config.BANKING_SYSTEM_ADENA + " Adena)"); else if (command.equalsIgnoreCase("deposit")) {
            if (activeChar.getInventory().getInventoryItemCount(57, 0) >= Config.BANKING_SYSTEM_ADENA) {
                InventoryUpdate iu = new InventoryUpdate();
                activeChar.getInventory().reduceAdena("Goldbar", Config.BANKING_SYSTEM_ADENA, activeChar, null);
                activeChar.getInventory().addItem("Goldbar", 3470, Config.BANKING_SYSTEM_GOLDBARS, activeChar, null);
                activeChar.getInventory().updateDatabase();
                activeChar.sendPacket(iu);
                activeChar.sendMessage("Thank you, you now have " + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar(s), and " + Config.BANKING_SYSTEM_ADENA + " less adena.");
            } else activeChar.sendMessage("You do not have enough Adena to convert to Goldbar(s), you need " + Config.BANKING_SYSTEM_ADENA + " Adena.");
        } else if (command.equalsIgnoreCase("withdraw")) if (activeChar.getInventory().getInventoryItemCount(3470, 0) >= Config.BANKING_SYSTEM_GOLDBARS) {
            InventoryUpdate iu = new InventoryUpdate();
            activeChar.getInventory().destroyItemByItemId("Adena", 3470, Config.BANKING_SYSTEM_GOLDBARS, activeChar, null);
            activeChar.getInventory().addAdena("Adena", Config.BANKING_SYSTEM_ADENA, activeChar, null);
            activeChar.getInventory().updateDatabase();
            activeChar.sendPacket(iu);
            activeChar.sendMessage("Thank you, you now have " + Config.BANKING_SYSTEM_ADENA + " Adena, and " + Config.BANKING_SYSTEM_GOLDBARS + " less Goldbar(s).");
        } else activeChar.sendMessage("You do not have any Goldbars to turn into " + Config.BANKING_SYSTEM_ADENA + " Adena.");
        return true;
    }

    /**
	 * @see net.sf.l2j.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
    public String[] getVoicedCommandList() {
        return _voicedCommands;
    }
}
