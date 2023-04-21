package net.sf.pando.fipa_console;

import java.util.HashMap;
import net.sf.pando.fipa_console.ConsCommand.CommandType;
import org.apache.commons.cli.Options;

public class ConsCommandCollection {

    private static HashMap<String, ConsCommand> commands;

    public ConsCommandCollection() {
        commands = new HashMap<String, ConsCommand>();
    }

    public void initCollection() {
        try {
            addCommand("help", CommandType.DEFAULT, null, "-- display help content");
            addCommand("message", CommandType.DEFAULT, getDefaultOptions(), "-- display message content that could be sent to message transport protocol (MTP)");
            addCommand("send", CommandType.DEFAULT, getDefaultOptions(), "-- send message to message transport protocol (MTS)");
            addCommand("acl", CommandType.DEFAULT, getDefaultOptions(), "-- switch to acl mode. In this mode user can input the acl content. ");
            addCommand("to", CommandType.DEFAULT, getDefaultOptions(), "-- switch to to mode. In this mode user can input ids of the receiving agents . ");
            addCommand("exit", CommandType.DEFAULT, getDefaultOptions(), "-- exit a mode");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCommand(String name, CommandType commandType, Options options, String commandDescription) throws Exception {
        ConsCommand consoleCommand = new ConsCommand();
        consoleCommand.setName(name);
        consoleCommand.setCommandType(commandType);
        consoleCommand.setOptions(options);
        consoleCommand.setCommandDescription(commandDescription);
        commands.put(name, consoleCommand);
    }

    private Options getDefaultOptions() {
        Options options = new Options();
        options.addOption("h", false, "display help (short form)");
        options.addOption("help", false, "display help");
        return options;
    }

    public ConsCommand getConsoleCommand(String name) {
        return commands.get(name);
    }

    public static HashMap<String, ConsCommand> getCommands() {
        return commands;
    }
}
