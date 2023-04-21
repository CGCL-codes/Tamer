package org.bluesock.bluemud.driver;

import org.bluesock.bluemud.lib.MudEvent;

/**
 * Bootstrap stub. 
 * Fires up the driver with a world creation event.
 */
public class BootStrap {

    public static void main(String[] args) {
        int listeningPort = 3000;
        if (args.length == 0 || args[0].equals("-?")) {
            System.out.println("Syntax: BootStrap <listening-port>");
            System.exit(0);
        } else {
            try {
                listeningPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid listening port. Halting.");
                System.exit(-1);
            }
        }
        try {
            System.out.println();
            System.out.println("Bootstrapping");
            Driver.execute("code/mudlib/libinit.py");
            System.out.println("Mudlib initialized.");
            Server s = new Server(listeningPort);
            System.out.println("Server initialized.");
            s.start();
            System.out.println("Finished bootstrapping.");
        } catch (Throwable e) {
            haltWithError(e);
        }
    }

    private static void haltWithError(Throwable error) {
        if (error instanceof ExceptionInInitializerError) {
            error = ((ExceptionInInitializerError) error).getException();
        }
        if (error instanceof FatalBluemudException) {
            System.err.println("Fatal bluemud error: " + error.getMessage());
        } else {
            System.err.println("Unexpected fatal error. Stacktrace follows. ");
            error.printStackTrace();
        }
        System.out.println();
        System.out.println("Halting due to fatal error. " + "See error log for details.");
        System.out.println();
        System.exit(-1);
    }
}
