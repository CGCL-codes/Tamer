package org.nuclearbunny.icybee.commands;

import org.nuclearbunny.icybee.Client;
import org.nuclearbunny.icybee.protocol.PrintPacket;
import tcl.lang.Command;
import tcl.lang.Interp;
import tcl.lang.TclException;
import tcl.lang.TclObject;
import java.io.IOException;

public class ClientLogCommand implements Command {

    private static final String USAGE = "usage: c_log [filename]";

    private Client client;

    public ClientLogCommand(Client client) {
        this.client = client;
    }

    public void cmdProc(Interp interp, TclObject[] args) throws TclException {
        try {
            if (args.length == 1) {
                if (client.isLoggingEnabled()) {
                    client.stopLogging();
                } else {
                    client.startLogging();
                }
            } else {
                client.startLogging(args[1].toString());
            }
        } catch (IllegalStateException e) {
            client.printMessage(PrintPacket.MSG_TYPE_ERROR, "unable to toggle logging");
            e.printStackTrace();
        } catch (IOException e) {
            client.printMessage(PrintPacket.MSG_TYPE_ERROR, "unable to open specified log file");
            e.printStackTrace();
        }
    }
}
