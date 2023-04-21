package usercommands;

import gameserver.model.gameobjects.player.Player;
import gameserver.services.DredgionInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;

/**
 *
 *
 */
public class Dredgion extends UserCommand {

    public Dredgion() {
        super("dredgion");
    }

    @Override
    public void executeCommand(Player player, String params) {
        String[] args = params.split(" ");
        if (args.length != 1) {
            PacketSendUtility.sendMessage(player, "syntax: .dredgion <register | unregister>");
            return;
        }
        if (args[0].equalsIgnoreCase("register")) {
            DredgionInstanceService.getInstance().registerPlayer(player);
            return;
        }
        if (args[0].equalsIgnoreCase("unregister")) {
            DredgionInstanceService.getInstance().unregisterPlayer(player);
            return;
        }
        PacketSendUtility.sendMessage(player, "syntax: .dredgion <register | unregister>");
    }
}
