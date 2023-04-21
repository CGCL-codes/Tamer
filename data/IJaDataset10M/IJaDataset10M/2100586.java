package jhomenet.ws;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import jhomenet.commons.net.proxy.IServerProxy;
import jhomenet.ws.system.ClientConfiguration;
import jhomenet.ws.auth.AuthTask;

/**
 * The main client class.
 * <br>
 * Id: $Id: $
 *
 * @author dirwin
 */
public class JHomeNetClient {

    /**
	 * Logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(JHomeNetClient.class.getName());

    /**
     * Define the client configuration.
     */
    private ClientConfiguration configuration;

    /**
	 * Constructor.
	 */
    public JHomeNetClient() {
    }

    /**
	 * Start the client application. In particular, there are two tasks that the client
	 * executes in order to get the client application up and running. They include:
	 * 	1) Authentication: the client attempts to authenticate against the server. If the
	 * 	   authentication fails, then the remaining tasks cannot be completed and the
	 * 	   application quits.
	 * 	2) Main: after successfully authenticating the client against the server, this
	 * 	   task is responsible for setting up and initializing the main client application.
	 */
    private void startClient() {
        configuration = ClientConfiguration.getDefaultConfiguration();
        AuthTask task = new AuthTask();
        try {
            if (task.isAuthenticated()) {
                IServerProxy serverProxy = task.getServerProxy();
                logger.debug("Client authentication succeeded");
            } else {
                logger.debug("Client authentication failed");
                shutdown();
            }
        } catch (Throwable t) {
            shutdown();
        }
    }

    /**
	 * Shut the client application down.
	 */
    public final void shutdown() {
        System.exit(0);
    }

    /**
	 * Main application start point.
	 * 
	 * @param args Command line arguments
	 */
    public static void main(String[] args) {
        new JHomeNetClient().startClient();
    }
}
