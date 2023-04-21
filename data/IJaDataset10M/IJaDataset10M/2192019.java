package org.portmap.server.admin.shell;

import org.portmap.server.core.SocketTransmitter;
import org.portmap.server.core.SocketTransmitterRegistry;

public class Start extends Command {

    public String getName() {
        return "START";
    }

    public String getHelp() {
        return "Starts transmitter" + "\n" + "Usage: START <port>";
    }

    public String execute(String[] args) throws CommandException {
        if (args.length != 1) throw new CommandException("Invalid syntax. Try 'help " + getName() + "'");
        try {
            int port = Integer.parseInt(args[0]);
            SocketTransmitter transmitter = SocketTransmitterRegistry.getInstance().getTransmitter(port);
            if (transmitter == null) throw new CommandException("No such transmitter");
            transmitter.start();
            return "OK." + "\n";
        } catch (Exception e) {
            throw new CommandException("Cannot perform START command", e);
        }
    }
}
