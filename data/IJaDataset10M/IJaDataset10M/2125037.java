package handlers.admincommandhandlers;

import java.util.logging.Logger;
import l2.universe.gameserver.handler.IAdminCommandHandler;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands:
 * <ul>
 * 	<li>admin_unblockip</li>
 * </ul>
 *
 * @version $Revision: 1.3.2.6.2.4 $ $Date: 2005/04/11 10:06:06 $
 */
public class AdminUnblockIp implements IAdminCommandHandler {

    private static final Logger _log = Logger.getLogger(AdminTeleport.class.getName());

    private static final String[] ADMIN_COMMANDS = { "admin_unblockip" };

    @Override
    public boolean useAdminCommand(final String command, final L2PcInstance activeChar) {
        if (command.startsWith("admin_unblockip ")) {
            try {
                final String ipAddress = command.substring(16);
                if (unblockIp(ipAddress, activeChar)) activeChar.sendMessage("Removed IP " + ipAddress + " from blocklist!");
            } catch (final StringIndexOutOfBoundsException e) {
                activeChar.sendMessage("Usage mode: //unblockip <ip>");
            }
        }
        return true;
    }

    private boolean unblockIp(final String ipAddress, final L2PcInstance activeChar) {
        _log.warning("IP removed by GM " + activeChar.getName());
        return true;
    }

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
