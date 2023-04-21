package com.realtime.crossfire.jxclient.gui.textinput;

import org.jetbrains.annotations.NotNull;

/**
 * Executes commands.
 * @author Andreas Kirschbaum
 */
public interface CommandExecutor {

    /**
     * Executes a command or a list of commands. The commands may be a client-
     * or a server-sided command.
     * @param commandLine the commands to execute
     */
    void executeCommand(@NotNull final CharSequence commandLine);
}
