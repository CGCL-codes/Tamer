package handlers.admincommandhandlers;

import java.util.logging.Logger;
import l2.universe.Config;
import l2.universe.gameserver.handler.IAdminCommandHandler;
import l2.universe.gameserver.model.L2ItemInstance;
import l2.universe.gameserver.model.L2Object;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.model.itemcontainer.Inventory;
import l2.universe.gameserver.network.SystemMessageId;
import l2.universe.gameserver.network.serverpackets.CharInfo;
import l2.universe.gameserver.network.serverpackets.ExBrExtraUserInfo;
import l2.universe.gameserver.network.serverpackets.InventoryUpdate;
import l2.universe.gameserver.network.serverpackets.SystemMessage;
import l2.universe.gameserver.network.serverpackets.UserInfo;

/**
 * This class handles following admin commands:
 * - enchant_armor
 *
 * @version $Revision: 1.3.2.1.2.10 $ $Date: 2005/08/24 21:06:06 $
 */
public class AdminEnchant implements IAdminCommandHandler {

    private static Logger _log = Logger.getLogger(AdminEnchant.class.getName());

    private static final String[] ADMIN_COMMANDS = { "admin_seteh", "admin_setec", "admin_seteg", "admin_setel", "admin_seteb", "admin_setew", "admin_setes", "admin_setle", "admin_setre", "admin_setlf", "admin_setrf", "admin_seten", "admin_setun", "admin_setba", "admin_setbe", "admin_enchant" };

    @Override
    public boolean useAdminCommand(final String command, final L2PcInstance activeChar) {
        if (command.equals("admin_enchant")) {
            showMainPage(activeChar);
        } else {
            int armorType = -1;
            if (command.startsWith("admin_seteh")) armorType = Inventory.PAPERDOLL_HEAD; else if (command.startsWith("admin_setec")) armorType = Inventory.PAPERDOLL_CHEST; else if (command.startsWith("admin_seteg")) armorType = Inventory.PAPERDOLL_GLOVES; else if (command.startsWith("admin_seteb")) armorType = Inventory.PAPERDOLL_FEET; else if (command.startsWith("admin_setel")) armorType = Inventory.PAPERDOLL_LEGS; else if (command.startsWith("admin_setew")) armorType = Inventory.PAPERDOLL_RHAND; else if (command.startsWith("admin_setes")) armorType = Inventory.PAPERDOLL_LHAND; else if (command.startsWith("admin_setle")) armorType = Inventory.PAPERDOLL_LEAR; else if (command.startsWith("admin_setre")) armorType = Inventory.PAPERDOLL_REAR; else if (command.startsWith("admin_setlf")) armorType = Inventory.PAPERDOLL_LFINGER; else if (command.startsWith("admin_setrf")) armorType = Inventory.PAPERDOLL_RFINGER; else if (command.startsWith("admin_seten")) armorType = Inventory.PAPERDOLL_NECK; else if (command.startsWith("admin_setun")) armorType = Inventory.PAPERDOLL_UNDER; else if (command.startsWith("admin_setba")) armorType = Inventory.PAPERDOLL_CLOAK; else if (command.startsWith("admin_setbe")) armorType = Inventory.PAPERDOLL_BELT;
            if (armorType != -1) {
                try {
                    final int ench = Integer.parseInt(command.substring(12));
                    if (ench < 0 || ench > 65535) activeChar.sendMessage("You must set the enchant level to be between 0-65535."); else setEnchant(activeChar, ench, armorType);
                } catch (final StringIndexOutOfBoundsException e) {
                    if (Config.DEVELOPER) _log.warning("Set enchant error: " + e);
                    activeChar.sendMessage("Please specify a new enchant value.");
                } catch (final NumberFormatException e) {
                    if (Config.DEVELOPER) _log.warning("Set enchant error: " + e);
                    activeChar.sendMessage("Please specify a valid new enchant value.");
                }
            }
            showMainPage(activeChar);
        }
        return true;
    }

    private void setEnchant(final L2PcInstance activeChar, final int ench, final int armorType) {
        L2Object target = activeChar.getTarget();
        if (target == null) target = activeChar;
        L2PcInstance player = null;
        if (target instanceof L2PcInstance) {
            player = (L2PcInstance) target;
        } else {
            activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INCORRECT_TARGET));
            return;
        }
        int curEnchant = 0;
        L2ItemInstance itemInstance = null;
        L2ItemInstance parmorInstance = player.getInventory().getPaperdollItem(armorType);
        if (parmorInstance != null && parmorInstance.getLocationSlot() == armorType) {
            itemInstance = parmorInstance;
        }
        if (itemInstance == null) return;
        curEnchant = itemInstance.getEnchantLevel();
        player.getInventory().unEquipItemInSlot(armorType);
        itemInstance.setEnchantLevel(ench);
        player.getInventory().equipItem(itemInstance);
        final InventoryUpdate iu = new InventoryUpdate();
        iu.addModifiedItem(itemInstance);
        player.sendPacket(iu);
        player.broadcastPacket(new CharInfo(player));
        player.sendPacket(new UserInfo(player));
        player.broadcastPacket(new ExBrExtraUserInfo(player));
        activeChar.sendMessage("Changed enchantment of " + player.getName() + "'s " + itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench + ".");
        player.sendMessage("Admin has changed the enchantment of your " + itemInstance.getItem().getName() + " from " + curEnchant + " to " + ench + ".");
    }

    private void showMainPage(final L2PcInstance activeChar) {
        AdminHelpPage.showHelpPage(activeChar, "enchant.htm");
    }

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
