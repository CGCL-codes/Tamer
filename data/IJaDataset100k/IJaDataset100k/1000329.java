package org.mineground.commands.irc;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.User;
import org.mineground.Main;
import org.mineground.handlers.irc.CommandExecutor;
import org.mineground.handlers.irc.UserLevel;

/**
 * @file SetLevel.java (21.02.2012)
 * @author Daniel Koenen
 *
 */
public class SetLevel implements CommandExecutor {

    @Override
    public void onCommand(User sender, UserLevel level, String channel, String command, String[] args) {
        if (level.compareTo(UserLevel.IRC_OP) < 0) {
            return;
        }
        if (args.length < 2) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage:" + Colors.NORMAL + " !setlevel [exact player name] [level]");
            return;
        }
        String playerName = args[0];
        String playerLevel = args[1];
        if (playerLevel.equalsIgnoreCase("admin") && level == UserLevel.IRC_OP) {
            Main.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Error: OPs are not allowed to set admins, sorry.");
            return;
        }
        Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), "pex user " + playerName + " delete");
        Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), "pex user " + playerName + " group set " + playerLevel);
        Main.getInstance().getIRCHandler().sendMessage(channel, Colors.MAGENTA + playerName + Colors.DARK_GREEN + "'s userlevel has been set to " + Colors.MAGENTA + playerLevel + Colors.DARK_GREEN + ".");
    }
}
