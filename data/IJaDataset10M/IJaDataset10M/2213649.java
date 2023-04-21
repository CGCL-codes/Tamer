package org.wcb.autohome;

import java.rmi.Naming;
import org.wcb.autohome.factories.HAGateway;
import org.wcb.autohome.interfaces.IHAGateway;
import java.util.Properties;
import java.rmi.RMISecurityManager;

/**
 * This is old, but is used to start the server for a client server application.
 * This is now more of a web application.
 */
public class JHomeServer {

    /**
     * This class is used to start an rmi version of the jhome server.
     * @param loc location of the properties file
     * @param host the address of the server.
     */
    public JHomeServer(String loc, String host) {
        Properties prop = System.getProperties();
        prop.put("java.rmi.server.codebase", "file://" + loc);
        System.setProperties(prop);
        if (System.getSecurityManager() == null) {
            System.out.print("Setting up security manager . . .");
            System.setSecurityManager(new RMISecurityManager());
            System.out.println("Security Enabled!");
        }
        try {
            IHAGateway gw = new HAGateway();
            Naming.rebind("rmi://" + host + ":1099/GatewayServer", gw);
        } catch (Exception e) {
            System.out.println("Error trouble: " + e);
        }
        System.out.println("Server started");
    }

    /**
     * Main method to start the application
     * @param args Two arguments should be passed file location and hostname
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: JHomeServer [file location] [hostname] ");
            System.exit(0);
        }
        new JHomeServer(args[0], args[1]);
    }
}
