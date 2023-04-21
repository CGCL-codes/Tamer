package org.jmule.ui.sacli.controller;

import java.util.ArrayList;
import org.jmule.resource.Messages;

/** This class manages the known commands, which can be executed on the core. */
public class CommandManager {

    private boolean usingRegExps;

    private static CommandManager singleton = new CommandManager();

    /** Searches for a given command name.
	* The command has only to start with the commandPrefix to be found.
	* The commandPrefix the first token of the line. Has to have a minimal length of currently 3 characters.
	*/
    public Command lookupCommand(String commandPrefix) throws IllegalArgumentException {
        if (commandPrefix.length() < 3) throw new IllegalArgumentException(Messages.getString("Ambiguous command: \"{0}\". You must use at least enough characters to distinguish it from others.", commandPrefix));
        for (int i = 0; i < knownCommands.size(); i++) {
            String cmdName = knownCommands.get(i).getClass().getName().toUpperCase();
            int idx = cmdName.lastIndexOf('.');
            if (idx > -1) cmdName = cmdName.substring(idx + 1, cmdName.length());
            if (cmdName.startsWith(commandPrefix.toUpperCase())) {
                return (Command) knownCommands.get(i);
            }
        }
        throw new IllegalArgumentException(Messages.getString("Invalid command: \"{0}\".", commandPrefix));
    }

    public static CommandManager getInstance() {
        return singleton;
    }

    private CommandManager() {
        knownCommands = new ArrayList();
        knownCommands.add(new org.jmule.ui.sacli.command.HelpCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.CloseCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.CoreInfoCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.Ed2kServerCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.ConfigCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.SearchCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.DownloadsCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.UploadsCLCommand());
        knownCommands.add(new org.jmule.ui.sacli.command.InstallCLCommand());
        usingRegExps = false;
    }

    ArrayList knownCommands;

    public Command[] getRegistredCommands() {
        return (Command[]) knownCommands.toArray(new Command[knownCommands.size()]);
    }

    public void registerCommand(Command command) {
        knownCommands.add(command);
    }
}
