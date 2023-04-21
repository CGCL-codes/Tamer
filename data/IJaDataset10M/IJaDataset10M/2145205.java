package webirc.client.commands;

/**
 * @author Ayzen
 * @version 1.0 12.07.2006 22:35:35
 */
public class UnknownCommand extends IRCCommand {

    private String message;

    public UnknownCommand(String message) {
        this.message = message;
    }

    public UnknownCommand(String prefix, String command, String params) {
        super(prefix, command, params);
        message = ':' + prefix + ' ' + command + params;
        name = command;
    }

    public String getName() {
        return name;
    }

    public String getFullMessage() {
        return message;
    }
}
