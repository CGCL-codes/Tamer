package org.mineground.handlers.irc;

import org.jibble.pircbot.User;

/**
 * @file CommandExecutor.java (18.02.2012)
 * @author Daniel Koenen
 *
 */
public interface CommandExecutor {

    public void onCommand(User sender, UserLevel level, String channel, String command, String args[]);
}
