package net.perham.jnap.cmd;

public class PrivateMessageCommand extends BaseCommand {

    public PrivateMessageCommand(String nick, String message) {
        super((short) TX_PRIVATE_MESSAGE, nick + " " + message);
    }
}
