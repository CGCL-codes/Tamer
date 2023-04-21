package handlers.admincommandhandlers;

import java.util.StringTokenizer;
import l2.universe.Config;
import l2.universe.gameserver.geoeditorcon.GeoEditorListener;
import l2.universe.gameserver.handler.IAdminCommandHandler;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author  Luno, Dezmond
 */
public class AdminGeoEditor implements IAdminCommandHandler {

    private static final String[] ADMIN_COMMANDS = { "admin_ge_status", "admin_ge_mode", "admin_ge_join", "admin_ge_leave" };

    @Override
    public boolean useAdminCommand(final String command, final L2PcInstance activeChar) {
        if (!Config.ACCEPT_GEOEDITOR_CONN) {
            activeChar.sendMessage("Server do not accepts geoeditor connections now.");
            return true;
        }
        if (command.startsWith("admin_ge_status")) activeChar.sendMessage(GeoEditorListener.getInstance().getStatus()); else if (command.startsWith("admin_ge_mode")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            try {
                final String val = command.substring("admin_ge_mode".length());
                final StringTokenizer st = new StringTokenizer(val);
                if (st.countTokens() < 1) {
                    activeChar.sendMessage("Usage: //ge_mode X");
                    activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                    activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                    activeChar.sendMessage("Mode 2: Send coordinates each second.");
                    return true;
                }
                final int m = Integer.parseInt(st.nextToken());
                GeoEditorListener.getInstance().getThread().setMode(m);
                activeChar.sendMessage("Geoeditor connection mode set to " + m + ".");
            } catch (final Exception e) {
                activeChar.sendMessage("Usage: //ge_mode X");
                activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
                activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
                activeChar.sendMessage("Mode 2: Send coordinates each second.");
                e.printStackTrace();
            }
            return true;
        } else if (command.equals("admin_ge_join")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().addGM(activeChar);
            activeChar.sendMessage("You added to list for geoeditor.");
        } else if (command.equals("admin_ge_leave")) {
            if (GeoEditorListener.getInstance().getThread() == null) {
                activeChar.sendMessage("Geoeditor not connected.");
                return true;
            }
            GeoEditorListener.getInstance().getThread().removeGM(activeChar);
            activeChar.sendMessage("You removed from list for geoeditor.");
        }
        return true;
    }

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }
}
