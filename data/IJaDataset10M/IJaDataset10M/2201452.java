package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.services.TeleportService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.utils.Util;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.world.World;

/**
 * Admin moveplayertoplayer command.
 *
 * @author Tanelorn
 */
public class MovePlayerToPlayer extends AdminCommand {

    /**
     * Constructor.
     */
    public MovePlayerToPlayer() {
        super("moveplayertoplayer");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVEPLAYERTOPLAYER) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
        if (params == null || params.length < 2) {
            PacketSendUtility.sendMessage(admin, "syntax //moveplayertoplayer <player name 1> <player name 2>");
            return;
        }
        Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));
        if (playerToMove == null) {
            PacketSendUtility.sendMessage(admin, "The specified player is not online.");
            return;
        }
        Player playerDestination = World.getInstance().findPlayer(Util.convertName(params[1]));
        if (playerDestination == null) {
            PacketSendUtility.sendMessage(admin, "The destination player is not online.");
            return;
        }
        if (playerToMove == playerDestination) {
            PacketSendUtility.sendMessage(admin, "Cannot move the specified player to their own position.");
            return;
        }
        TeleportService.teleportTo(playerToMove, playerDestination.getWorldId(), playerDestination.getInstanceId(), playerDestination.getX(), playerDestination.getY(), playerDestination.getZ(), playerDestination.getHeading(), 0);
        PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to the location of player " + playerDestination.getName() + ".");
        PacketSendUtility.sendMessage(playerToMove, "You have been teleported by an administrator.");
    }
}
