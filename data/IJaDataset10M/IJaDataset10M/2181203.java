package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.ChatType;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.serverpackets.SM_MESSAGE;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.chathandlers.AdminCommand;

/**
 * Admin notice command
 *
 * @author Jenose, ZeroSignal
 *         Updated By Darkwolf
 */
public class Notice extends AdminCommand {

    public Notice() {
        super("notice");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_NOTICE) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //notice <message>");
            return;
        }
        String msg;
        if (params.length > 1) {
            msg = "";
            try {
                for (int i = 0; i < params.length; i++) {
                    msg += " " + params[i];
                }
            } catch (NumberFormatException e) {
                PacketSendUtility.sendMessage(admin, "parameters should be text and numbers");
                return;
            }
        } else msg = params[0];
        PacketSendUtility.broadcastPacket(admin, new SM_MESSAGE(0, null, "Information : " + msg, ChatType.SYSTEM_NOTICE), true);
    }
}
