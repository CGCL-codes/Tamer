package uk.ac.warwick.dcs.cokefolk.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.*;
import uk.ac.warwick.dcs.cokefolk.connection.ClientConnection;
import uk.ac.warwick.dcs.cokefolk.connection.xmlstream.XMLStreamClient;

/**
 * An abstract class to hold the client logic. It aims to facilitate the
 * construction of clients, so be independent of the choice of user interface
 * and server connection method.
 * 
 * @author Tim Retout
 * 
 */
public abstract class AbstractClient implements ClientInteraction {

    protected static final String HOST = "host";

    protected static final String PORT = "port";

    protected final ClientConnection server;

    protected final Preferences prefs;

    /**
	 * Constructs a client.
	 */
    public AbstractClient() {
        prefs = Preferences.userNodeForPackage(this.getClass());
        server = new XMLStreamClient(this);
    }

    /**
	 * Runs the client.
	 * 
	 * @throws IOException
	 */
    public void run() throws IOException {
        boolean running = true;
        while (running) {
            String result = null;
            String userInput = getInput();
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(userInput.split("\\s")));
            Command command = Command.toCommand(args.remove(0));
            switch(command) {
                case CONNECT:
                    String host = args.get(0);
                    int port = Integer.valueOf(args.get(1));
                    server.connect(host, port);
                    break;
                case DISCONNECT:
                    server.disconnect();
                    break;
                case LOGIN:
                    String username = args.get(0);
                    String password = args.get(1);
                    server.login(username, password);
                    break;
                case LOGOUT:
                    server.logout();
                    break;
                case EXIT:
                    output("Bye");
                    running = false;
                    break;
                case SET:
                    break;
                case HELP:
                    help();
                    break;
                case TD:
                    result = server.query(userInput);
                    break;
                default:
                    throw new AssertionError(command);
            }
            if (result != null) output(result);
        }
        server.logout();
        server.disconnect();
    }
}

/**
 * Encapsulates the available client commands.
 * 
 * @author Tim Retout
 * 
 */
enum Command {

    CONNECT, DISCONNECT, LOGIN, LOGOUT, EXIT, SET, HELP, SQL, TD;

    /**
	 * Gets the Command corresponding to the given String.
	 * 
	 * @param str
	 *            the String to look up. May be null. Case-insensitive.
	 * @return a Command, defaults to <code>TD</code>; returns
	 *         <code>EXIT</code> on null input.
	 */
    public static Command toCommand(String str) {
        try {
            return Command.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return TD;
        } catch (NullPointerException ex2) {
            return EXIT;
        }
    }
}

;
