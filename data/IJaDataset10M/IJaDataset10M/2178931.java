package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.TaskId;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.Util;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.world.World;
import java.util.concurrent.Future;

/**
 * @author Watson
 */
public class Ungag extends AdminCommand {

    public Ungag() {
        super("ungag");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_GAG) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
            return;
        }
        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "Syntax: //ungag <player name>");
            return;
        }
        String name = Util.convertName(params[0]);
        Player player = World.getInstance().findPlayer(name);
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
            PacketSendUtility.sendMessage(admin, "Syntax: //ungag <player name>");
            return;
        }
        player.setGagged(false);
        Future<?> task = player.getController().getTask(TaskId.GAG);
        if (task != null) player.getController().cancelTask(TaskId.GAG);
        PacketSendUtility.sendMessage(player, "Your chat ban punishment is over");
        PacketSendUtility.sendMessage(admin, "Player " + name + " chat ban has been lifted");
    }
}
