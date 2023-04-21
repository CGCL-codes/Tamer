package bunny.x.nio.http;

import bunny.x.nio.NioServer;

/**
 * The Non-blocking IO HTTP Server.
 *
 * @author Christopher Ottley.
 */
public class HTTPServer {

    /** The NIO server. */
    private NioServer server;

    /**
   * Create the HTTP server on the specified port with the processing class.
   * @param listenPort The port the server is to listen on.
   * @param processorClassName The class that is responsible for the processing.
   * @throws Exception An exception if the server can't bind to the port.
   *         Or load the processor class.
   */
    public HTTPServer(final int listenPort, final String processorClassName) throws Exception {
        server = new NioServer(new HTTPServerAcceptor(listenPort, processorClassName), new HTTPServerProcessor());
    }

    /**
   * Start the server.
   */
    public final void start() {
        server.start();
    }

    /**
   * NIO specific method.
   * An explicit call to the selector so that events trigger.
   */
    public final void wakeupSelector() {
        server.wakeupSelector();
    }
}
