package de.hpi.eworld.eworldeventserver;

import de.hpi.eworld.util.ServerConstants;

/**
 * The EWorldServer class handles command line parameters and starts the server core.
 * If you want to run the eWorldEventServer with custom port values, simply provide command line parameters.
 * "eWorldEventServer.java 1234 5678" would assign 1234 as data port and 5678 as control port.
 * 
 * @author Sebastian Enderlein
 * @version 0.1
 * 
 */
public class EWorldEventServer {

    public static void main(String[] args) {
        int dataPort = ServerConstants.Ports.DEFAULT_EWORLDSERVER_DATA_PORT;
        int controlPort = ServerConstants.Ports.DEFAULT_EWORLDSERVER_CONTROL_PORT;
        String configFile = "configuration.xml";
        if (args.length == 3) {
            try {
                dataPort = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Port number of data port [Parameter 1] is not an integer " + dataPort);
                System.exit(-1);
            }
            try {
                controlPort = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println("Port number of control port [Parameter 2] is not an integer " + controlPort);
                System.exit(-1);
            }
            configFile = args[2];
        } else if (args.length == 1) {
            configFile = args[0];
        } else {
            System.out.println("\nRunning eWorldEventServer with standard ports! If you want to choose your ports manually, use: \"" + "\"eWorldEventServer.java [dataport] [controlport] [configfile]\n\n");
            System.out.println("Note: If you manually assign port values, you have to apply these values also to eWorld.\n\n");
        }
        Core application = Core.getInstance(configFile);
        application.startListening(dataPort, controlPort);
    }
}
