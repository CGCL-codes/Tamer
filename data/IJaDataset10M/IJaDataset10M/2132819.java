package endUser;

import CSApi.ComServer;
import java.io.PrintStream;
import tools.APILogger;

/**
 *
 * @author TecHunter
 */
public class ServerTestMain {

    public static void main(String args[]) {
        ComServer server;
        try {
            server = new ComServer(TestComServerThread.class, ComServer.DEFAULT_PORT, true, APILogger.LOG_DEBUG, new PrintStream("debug.log"), new PrintStream("error.log"));
            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
