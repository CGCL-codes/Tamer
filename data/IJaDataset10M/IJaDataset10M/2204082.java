package uk.azdev.openfire.relay;

import java.util.HashMap;
import java.util.Map;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;

public class CommandProcessor {

    private Map<String, Command> commandMap;

    public CommandProcessor() {
        initMap();
    }

    private void initMap() {
        commandMap = new HashMap<String, Command>();
        addCommandToMap(new HelpCommand());
        addCommandToMap(new InviteCommand());
    }

    private void addCommandToMap(Command command) {
        commandMap.put(command.getName(), command);
    }

    public void processCommand(String commandString, Friend originator, XFireConnection connection) {
        String[] components = commandString.split("\\s");
        String commandName = components[0];
        String[] arguments = new String[components.length - 1];
        System.arraycopy(components, 1, arguments, 0, arguments.length);
        Command command = commandMap.get(commandName);
        if (command == null) {
            OpenFireRelay.relayLog.warning("User \"" + originator.getUserName() + "\" attempted to invoke unknown command");
            connection.getConversation(originator.getSessionId()).sendMessage("Unknown command: " + commandName);
        } else {
            OpenFireRelay.relayLog.info("Executing command \"" + commandName + "\" for user \"" + originator.getUserName() + "\"");
            command.execute(arguments, originator, connection);
        }
    }

    private class HelpCommand implements Command {

        public void execute(String[] args, Friend executor, XFireConnection connection) {
            String response;
            if (args.length > 0) {
                response = getSpecificCommandHelp(args[0], executor, connection);
            } else {
                response = getCommandList(executor, connection);
            }
            Conversation conversation = connection.getConversation(executor.getSessionId());
            conversation.sendMessage(response);
        }

        private String getCommandList(Friend executor, XFireConnection connection) {
            StringBuffer helpText = new StringBuffer();
            helpText.append("Available commands: \n");
            for (Command command : commandMap.values()) {
                helpText.append("\t- ");
                helpText.append(command.getName());
                helpText.append("\n");
            }
            helpText.append("\nIf you require more information on a particular command, run help again with the command name as an argument");
            return helpText.toString();
        }

        private String getSpecificCommandHelp(String commandName, Friend executor, XFireConnection connection) {
            if (!commandMap.containsKey(commandName)) {
                return "Command \"" + commandName + "\" is unknown";
            }
            return commandMap.get(commandName).getHelpText();
        }

        public String getName() {
            return "help";
        }

        public String getHelpText() {
            return "help [command]\nprints a short description of how to use the specified command. If no command is specified " + "then the list of available commands for the current user is printed";
        }
    }
}
