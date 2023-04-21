package com.chez.powerteam.jext.console.commands;

import com.chez.powerteam.jext.Utilities;
import com.chez.powerteam.jext.console.Console;

/**
 * This command opens a file in the text area.
 * @author Romain Guy
 */
public class FileCommand extends Command {

    private static final String COMMAND_NAME = "file:";

    public String getCommandName() {
        return COMMAND_NAME + "filename";
    }

    public String getCommandSummary() {
        return "open file `filename'";
    }

    public boolean handleCommand(Console console, String command) {
        if (command.startsWith(COMMAND_NAME)) {
            String argument = command.substring(5);
        }
        return false;
    }
}
