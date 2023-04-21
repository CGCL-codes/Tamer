package org.mineground.handlers.irc;

import java.util.HashMap;
import org.jibble.pircbot.User;
import org.mineground.Main;

/**
 * @file CommandHandler.java (18.02.2012)
 * @author Daniel Koenen
 *
 */
public class CommandHandler {

    private HashMap<String, CommandExecutor> commandMap = new HashMap<String, CommandExecutor>();

    private Handler ircHandler;

    public CommandHandler(Handler ircHandler) {
        this.ircHandler = ircHandler;
    }

    public void addCommand(String commandName, CommandExecutor executor) {
        commandMap.put(commandName, executor);
    }

    public void removeCommand(String commandName) {
        commandMap.remove(commandName);
    }

    public void triggerCommand(String command, String user, String channel, String[] args) {
        if (!commandMap.containsKey(command)) {
            return;
        }
        User ircUser = ircHandler.getUser(user, channel);
        UserLevel ircUserLevel;
        if (Main.getInstance().getIRCHandler().isOwner(user, channel)) ircUserLevel = UserLevel.IRC_OWNER; else if (Main.getInstance().getIRCHandler().isSop(user, channel)) ircUserLevel = UserLevel.IRC_SOP; else if (Main.getInstance().getIRCHandler().isOp(user, channel)) ircUserLevel = UserLevel.IRC_OP; else if (Main.getInstance().getIRCHandler().isHop(user, channel)) ircUserLevel = UserLevel.IRC_HOP; else if (Main.getInstance().getIRCHandler().isVoice(user, channel)) ircUserLevel = UserLevel.IRC_VOP; else ircUserLevel = UserLevel.IRC_NONE;
        commandMap.get(command).onCommand(ircUser, ircUserLevel, channel, command, args);
    }

    public void triggerPrivateCommand(String command, String user, String[] args) {
        if (!commandMap.containsKey(command)) {
            return;
        }
        CommandExecutor commandHandler = commandMap.get(command);
        if (commandHandler instanceof PrivateCommandExecutor) {
            ((PrivateCommandExecutor) commandHandler).onPrivateCommand(user, command, args);
        }
    }

    public HashMap<String, CommandExecutor> getCommands() {
        return commandMap;
    }
}
