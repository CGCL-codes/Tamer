package net.sf.japi.net.rest.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.japi.net.rest.Authenticator;
import net.sf.japi.net.rest.Http11Header;
import net.sf.japi.net.rest.HttpException;
import net.sf.japi.net.rest.RequestURI;
import net.sf.japi.net.rest.RestInputStream;
import net.sf.japi.net.rest.RestOutputStream;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class AbstractSession implements Session {

    /** Default size for the I/O buffer. */
    protected static final int BUFFER_SIZE = 4096;

    /** The Logger. */
    private static final Logger LOG = Logger.getLogger("net.sf.japi.net.rest");

    /** Indicates whether or not the thread is currently running.
     * <code>true</code> means the thread is running, <code>false</code> means the thread is not
     * running.
     */
    private volatile boolean running;

    /** The client with which the server shall communicate. */
    @NotNull
    private final Socket client;

    /** The server. */
    private final RestServer server;

    /** The RestProcessor. */
    private final RestProcessor restProcessor;

    /** The Authenticator to use for authentication. */
    private final Authenticator authenticator;

    /** Creates an AbstractSyncServerSession.
     * @param client Client with which to communicate.
     * @param server Server which communicates.
     * @param authenticator Authenticator which is used for authentication.
     */
    public AbstractSession(@NotNull final Socket client, @NotNull final RestServer server, @NotNull final Authenticator authenticator) {
        this.client = client;
        this.server = server;
        this.authenticator = authenticator;
        restProcessor = new RestProcessor(this);
    }

    /** {@inheritDoc} */
    public void run() {
        try {
            final OutputStream clientOut = client.getOutputStream();
            @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed" }) final RestOutputStream response = new RestOutputStream(clientOut);
            RequestURI requestURI = null;
            try {
                response.setHeader(Http11Header.SERVER, "Karatasi SyncServer");
                LOG.info("connected with client " + client.getRemoteSocketAddress());
                @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed" }) final RestInputStream request = new RestInputStream(client);
                requestURI = request.getRequestURI();
                LOG.info("client " + client.getRemoteSocketAddress() + " requests " + requestURI);
                authenticator.checkAuthentication(request);
                restProcessor.invoke(request, response);
            } catch (final HttpException e) {
                final byte[] content = e.getHtmlMessage().getBytes("UTF-8");
                response.setStatus(e.getStatus());
                response.setHeader(Http11Header.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.setHeader(Http11Header.CONTENT_LENGTH, Integer.toString(content.length));
                for (final Map.Entry<String, String> additionalHeader : e.getAdditionalHeaders().entrySet()) {
                    response.setHeader(additionalHeader.getKey(), additionalHeader.getValue());
                }
                response.write(content);
                response.flush();
                LOG.info(e.getStatus() + ": by client " + client.getRemoteSocketAddress() + ": " + e.getDetailMessage() + " (requested URI: " + requestURI + ")");
            } finally {
                client.shutdownOutput();
                client.shutdownInput();
                client.close();
                LOG.info("session closed");
            }
        } catch (final IOException e) {
            LOG.log(Level.WARNING, null, e);
        } finally {
            server.fireRestServerClientDisconnected();
        }
    }

    /** {@inheritDoc} */
    public synchronized void start() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }
}
