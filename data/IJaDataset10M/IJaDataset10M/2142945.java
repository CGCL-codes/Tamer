package mud;

import mud.core.MessageQueue;
import mud.core.User;
import mud.data.Configuration;
import mud.data.ConfigurationException;
import mud.data.Database;
import mud.data.DummyDB;
import mud.server.ConnectionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: Michael Keller
 * Date: Nov 30, 2004
 * Copyright by Continuum.
 * Do not use for nuclear reactors and time machines.
 */
public class Control {

    public static final String DEFAULT_CONFIG_FILE = "config.xml";

    private static Database database = null;

    private static Configuration config = null;

    public static void main(String[] args) throws IOException {
        String configFile = DEFAULT_CONFIG_FILE;
        if (args.length > 1) {
            usage();
            System.exit(1);
        } else if (args.length == 1) {
            configFile = args[0];
        }
        try {
            config = new Configuration(configFile);
        } catch (ConfigurationException e) {
            System.out.println("Error initializing server! There is a problem with the configuration file:");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        database = new DummyDB();
        MessageQueue.init();
        MessageQueue.start();
        new ConnectionListener().start();
        help();
        String input = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        loop: while ((input = br.readLine()) != null) {
            input = input.trim();
            char command = input.charAt(0);
            switch(command) {
                case 'u':
                    int count = User.getConnectedUsers().size();
                    System.out.println(String.format("Currently connected: %d user%s", count, count != 1 ? "s" : ""));
                    break;
                case 'b':
                    if (input.length() > 2) {
                        broadcast(input.substring(2).trim());
                    } else {
                        System.out.println("usage: b [message]");
                    }
                    break;
                case 't':
                    System.out.println("Terminating program...");
                    break loop;
                case 'h':
                    help();
                    break;
                default:
                    System.out.println("Unknown command. Enter 'h' for help.");
            }
        }
    }

    public static Configuration getConfiguration() {
        return config;
    }

    public static Database getDatabase() {
        return database;
    }

    public static void infiniteLoopAlert(StackTraceElement[] ste) {
        broadcast("Infinite loop alert!");
    }

    private static void usage() {
        System.out.println("Usage: ");
        System.out.println("  java " + Control.class.getName() + " [config-file]");
    }

    private static void help() {
        System.out.println("Commands are:");
        System.out.println("  u             Show number of connected users");
        System.out.println("  b [message]   Broadcast a message to all users");
        System.out.println("  t             Terminate program");
        System.out.println("  h             Show this help text");
    }

    private static void broadcast(String msg) {
        Log.info("System broadcast: %s", msg);
        msg = AnsiColor.BRIGHT + AnsiColor.F_RED + "System broadcast: " + msg + "\n" + AnsiColor.RESET;
        synchronized (User.class) {
            for (User u : User.getConnectedUsers()) {
                u.tell(msg);
            }
        }
    }
}
