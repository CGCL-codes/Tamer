package ucm;

import model.FlightsClient;
import model.Server;

/**
 * Starts the server
 * Precondition: The program is closed
 * Postcondition:  The program is started
 * 1. User starts the program via .bat file.
 */
public class UCServerStart {

    public static void main(String args[]) {
        Server serv = new Server();
        serv.startServer();
    }
}
