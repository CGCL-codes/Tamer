package com.simontuffs.soapstone;

import java.net.InetAddress;
import com.simontuffs.soapstone.api.Commands;
import com.simontuffs.soapstone.api.OptionSpace.Options;

/**
 * @author simon@simontuffs.com
 */
public class Actor {

    protected String name;

    protected InetAddress serverAddress;

    protected int port = 8000;

    protected boolean verbose = false, showOptions = false;

    protected String protocol;

    protected Options options;

    /**
     * Server uses two ports: one for control and one for data.  The
     * control port is used to switch modes between raw, http, and jax-rpc.
     * Once mode is switched, data is streamed through the data-port.
     */
    protected int dataPort = port;

    protected int controlPort = port + 1;

    protected static class HelpOptions extends RuntimeException {
    }

    public String options() {
        String nl = "\n";
        String help = "  -port {port}       Specify base port number of server [" + dataPort + "].  " + nl + "                     Data and control ports are derived from this" + nl + "                     (data-port=port, control-port = port+1)" + nl + "  -verbose           Issue diagnostic messages [" + verbose + "]" + nl + "  -options           Show current option values [" + showOptions + "]";
        return help;
    }

    public Actor(Options args) throws Exception {
        options = args;
        options.setDefault(Commands.HELP, false);
        options.setDefault(Commands.SERVER, "localhost");
        options.setDefault(Commands.PORT, port);
        options.setDefault(Commands.VERBOSE, verbose);
        options.setDefault("-options", showOptions);
        serverAddress = InetAddress.getByName(options.getString(Commands.SERVER));
        dataPort = options.getInt(Commands.PORT);
        options.setDefault(Commands.CONTROL, dataPort + 1);
        controlPort = options.getInt(Commands.CONTROL);
    }
}
