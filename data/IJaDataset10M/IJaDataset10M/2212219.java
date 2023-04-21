package com.zazu.mycl.application.commands;

import com.zazu.mycl.application.AbstractTerminalCommand;
import com.zazu.mycl.application.terminalCommand;
import com.zazu.mycl.application.CommandRunner;

public class HelpCommand extends AbstractTerminalCommand {

    public void run(String[] args, CommandRunner commandRunner, String commandLine, String outputFileName) {
        String CommandName = args[1];
        String className = commandRunner.getCommandClass(CommandName);
        if (className != null) {
            try {
                terminalCommand myRunner;
                myRunner = (terminalCommand) Class.forName(className).newInstance();
                commandRunner.SetLine(myRunner.getSignature());
            } catch (InstantiationException e) {
                commandRunner.SetLine("Error running command.");
            } catch (IllegalAccessException e) {
                commandRunner.SetLine("Error running command.");
            } catch (ClassNotFoundException e) {
                commandRunner.SetLine("Error running command: class not found.");
            }
        } else {
            commandRunner.SetLine("No command found for: " + CommandName);
        }
    }

    public void runSubCommand(String[] args, String commandLine) {
    }

    public String getSignature() {
        return "This command lists help information for MyCl. When entered with a command name it displays help for that command";
    }

    public boolean isThreadable() {
        return isThreadable;
    }

    private boolean isThreadable = true;
}
