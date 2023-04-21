package com.sun.tools.jdi;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.spi.*;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketAttachingConnector extends GenericAttachingConnector {

    static final String ARG_PORT = "port";

    static final String ARG_HOST = "hostname";

    public SocketAttachingConnector() {
        super(new SocketTransportService());
        String defaultHostName;
        try {
            defaultHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            defaultHostName = "";
        }
        addStringArgument(ARG_HOST, getString("socket_attaching.host.label"), getString("socket_attaching.host"), defaultHostName, false);
        addIntegerArgument(ARG_PORT, getString("socket_attaching.port.label"), getString("socket_attaching.port"), "", true, 0, Integer.MAX_VALUE);
        transport = new Transport() {

            public String name() {
                return "dt_socket";
            }
        };
    }

    public VirtualMachine attach(Map<String, ? extends Connector.Argument> arguments) throws IOException, IllegalConnectorArgumentsException {
        String host = argument(ARG_HOST, arguments).value();
        if (host.length() > 0) {
            host = host + ":";
        }
        String address = host + argument(ARG_PORT, arguments).value();
        return super.attach(address, arguments);
    }

    public String name() {
        return "com.sun.jdi.SocketAttach";
    }

    public String description() {
        return getString("socket_attaching.description");
    }
}
