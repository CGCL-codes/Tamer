package it.unibz.izock;

import it.unibz.izock.ServerStarter.Starter;
import it.unibz.izock.networking.Authenticator;
import it.unibz.izock.networking.AuthenticatorImpl;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;

public class CLIServerStarter implements Starter {

    /** The log. */
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CLIServerStarter.class);

    public CLIServerStarter() {
        ((ConsoleAppender) Logger.getRootLogger().getAppender("C")).setWriter(new OutputStreamWriter(System.out));
    }

    public void start() {
        try {
            Registry theRegistry = LocateRegistry.createRegistry(5321);
            Authenticator authenticator = new AuthenticatorImpl();
            theRegistry.rebind("iZock", authenticator);
        } catch (RemoteException e) {
            log.fatal("Start Failed", e);
            System.exit(1);
        }
        log.info("Authenticator Started");
    }
}
