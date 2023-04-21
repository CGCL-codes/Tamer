package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.Util;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.world.World;

/**
 * @author Elusive
 */
public class Kick extends AdminCommand {

    public Kick() {
        super("kick");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_KICK) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
        if (params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //kick <player name>");
            return;
        }
        Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "The specified player is not online.");
            return;
        }
        player.getClientConnection().close(new SM_QUIT_RESPONSE(), true);
        PacketSendUtility.sendMessage(admin, "You kicked player " + player.getName() + " from the game");
    }
}
