package org.nuclearbunny.icybee.commands;

import org.nuclearbunny.icybee.Client;
import tcl.lang.*;

public class RegisterCommand implements Command {

    private static final String USAGE = "usage: s_register [password]";

    private Client client;

    public RegisterCommand(Client client) {
        this.client = client;
    }

    public void cmdProc(Interp interp, TclObject[] args) throws TclException {
        if (args.length != 2) {
            throw new TclNumArgsException(interp, 1, args, RegisterCommand.USAGE);
        } else {
            client.sendPersonalMessage("server", "p " + args[1].toString());
        }
    }
}
