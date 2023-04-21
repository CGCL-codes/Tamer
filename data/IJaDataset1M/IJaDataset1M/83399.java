package com.l2jserver.gameserver.network.clientpackets;

import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.AdminCommandAccessRights;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.util.GMAudit;

/**
 * This class handles all GM commands triggered by //command
 *
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:29 $
 */
public final class SendBypassBuildCmd extends L2GameClientPacket {

    private static Logger _log = Logger.getLogger(SendBypassBuildCmd.class.getName());

    private static final String _C__5B_SENDBYPASSBUILDCMD = "[C] 5b SendBypassBuildCmd";

    public static final int GM_MESSAGE = 9;

    public static final int ANNOUNCEMENT = 10;

    private String _command;

    @Override
    protected void readImpl() {
        _command = readS();
        if (_command != null) _command = _command.trim();
    }

    @Override
    protected void runImpl() {
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null) return;
        String command = "admin_" + _command.split(" ")[0];
        IAdminCommandHandler ach = AdminCommandHandler.getInstance().getAdminCommandHandler(command);
        if (ach == null) {
            if (activeChar.isGM()) activeChar.sendMessage("The command " + command.substring(6) + " does not exists!");
            _log.warning("No handler registered for admin command '" + command + "'");
            return;
        }
        if (!AdminCommandAccessRights.getInstance().hasAccess(command, activeChar.getAccessLevel())) {
            activeChar.sendMessage("You don't have the access right to use this command!");
            _log.warning("Character " + activeChar.getName() + " tryed to use admin command " + command + ", but have no access to it!");
            return;
        }
        if (Config.GMAUDIT) GMAudit.auditGMAction(activeChar.getName() + " [" + activeChar.getObjectId() + "]", _command, (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target"));
        ach.useAdminCommand("admin_" + _command, activeChar);
    }

    @Override
    public String getType() {
        return _C__5B_SENDBYPASSBUILDCMD;
    }
}
