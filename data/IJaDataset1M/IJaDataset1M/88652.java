package org.vmaster.client.cmdline;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vmaster.client.Main;
import org.vmaster.client.VMasterClient;
import org.vmaster.client.commands.Commands;
import org.vmaster.client.commands.Usage;
import biz.xsoftware.impl.remote.adapters.TransportException;
import biz.xsoftware.impl.thread.ExceptionHandler;

/**
 * This is the entry point for the Command Line, instantiate
 * this class and call VMasterCommandLine.start(String args[])
 * to start the CommandLine
 */
public class VMasterCommandLine implements VMasterClient {

    private static final Logger log = Logger.getLogger(VMasterCommandLine.class.getName());

    public VMasterCommandLine() {
    }

    public void start(String[] args) {
        String method = "params( ";
        String function = null;
        try {
            Vector v = new Vector();
            for (int i = 0; i < args.length; i++) {
                method += "," + args[i];
                v.add(args[i]);
            }
            method += ") ";
            log.fine(method + "started");
            function = (String) args[0];
            v.remove(0);
            executeCommand(function, v);
        } catch (Exception e) {
            log.log(Level.WARNING, "Unexpected Exception caught: ", e);
            ExceptionHandler.handle(e);
            System.exit(Main.UNEXPECTED_ERROR);
        }
        log.fine(method + "succeeded");
    }

    public void executeCommand(String function, Vector args) throws TransportException {
        if (Commands.equals(Commands.createRepository, function)) new CreateRepositoryView().onCreateRepositoryView(args); else if (Commands.equals(Commands.createProject, function)) new CreateProjectView().onCreateProject(args); else if (Commands.equals(Commands.createNewView, function)) new CreateNewViewView().onCreateView(args); else if (Commands.equals(Commands.shutDown, function)) new ShutDownView().onShutDown(args); else Usage.printUsage();
    }
}
