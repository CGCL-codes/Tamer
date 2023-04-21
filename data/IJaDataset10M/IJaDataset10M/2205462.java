package org.mineground.commands.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.Main;
import org.mineground.Utilities;

/**
 *
 * @file Report.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Report implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws CommandException {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "* Usage: '/report <Player> <Message>'");
            return true;
        }
        String playerName = args[0];
        String reportMessage;
        StringBuilder reportMessageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reportMessageBuilder.append(args[i]);
            reportMessageBuilder.append(" ");
        }
        reportMessage = reportMessageBuilder.toString().substring(0, reportMessageBuilder.toString().length() - 1);
        if (Utilities.countOnlineAdmins() > 0) {
            Utilities.sendAdminMessage(ChatColor.RED + "Report from " + player.getName() + " (" + playerName + "): " + ChatColor.GOLD + reportMessage);
            player.sendMessage(ChatColor.GREEN + "* Your report has been sent.");
            return true;
        }
        Main.getInstance().getIRCHandler().sendMessage(Main.getInstance().getConfigHandler().ircDevChannel, Colors.RED + "Ingame report from " + player.getName() + " (" + playerName + "): " + Colors.NORMAL + reportMessage);
        return true;
    }
}
