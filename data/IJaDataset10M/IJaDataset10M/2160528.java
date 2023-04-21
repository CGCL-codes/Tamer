package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.services.TeleportService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author MrPoke and lord_rex
 */
public class MoveToNpc extends AdminCommand {

    public MoveToNpc() {
        super("movetonpc");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVETONPC) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
            return;
        }
        int npcId = 0;
        try {
            npcId = Integer.valueOf(params[0]);
            TeleportService.teleportToNpc(admin, npcId);
        } catch (ArrayIndexOutOfBoundsException e) {
            PacketSendUtility.sendMessage(admin, "syntax //movetonpc <npc id>");
        } catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "Numbers only!");
        }
    }
}
