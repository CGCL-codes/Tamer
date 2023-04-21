package sevs.network;

/**
 * Network layer for the server application.
 * 
 * <p>
 * Calling the run method will start an infinite loop
 * to accept incoming connections from client.
 * 
 * <p>
 * Once the request is received, it will be dispatched to
 * the RequestHandler, which was specified as a parameter
 * when you created a NetworkServer object.
 * 
 * @author  Kohsuke Kawaguchi
 */
public interface NetworkServer extends Runnable {
}
