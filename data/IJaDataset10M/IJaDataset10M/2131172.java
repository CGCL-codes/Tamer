package org.nakedobjects.plugins.remoting.command.transport.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.remoting.command.server.ServerConnection;
import org.nakedobjects.plugins.remoting.command.shared.marshal.ConnectionException;
import org.nakedobjects.plugins.remoting.command.transport.socket.shared.ProfilingInputStream;
import org.nakedobjects.plugins.remoting.command.transport.socket.shared.ProfilingOutputStream;
import org.nakedobjects.plugins.remoting.command.transport.socket.shared.SocketTransportConstants;
import org.nakedobjects.plugins.remoting.server.ServerFacadeImpl;
import org.nakedobjects.plugins.remoting.shared.ServerFacade;
import org.nakedobjects.plugins.remoting.shared.ServerFacadeLogger;
import org.nakedobjects.plugins.remoting.shared.encoding.object.ObjectEncoderDefault;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.remoting.ServerListener;

public abstract class ServerListenerAbstract implements ServerListener, DebugInfo {

    private static final Logger LOG = Logger.getLogger(ServerListenerAbstract.class);

    /**
     * Injected, see {@link #setEncoder(ObjectEncoderDefault)}.
     */
    private ObjectEncoderDefault encoder;

    /**
     * Injected, see {@link #setAuthenticationManager(AuthenticationManager)}.
     */
    private AuthenticationManager authenticationManager;

    private ServerSocket socket;

    private WorkerPool workerPool;

    private Boolean shutdown = Boolean.FALSE;

    /**
     * TODO: generalize so can listen on multi-homed hosts using new Socket(port, 2, address); 
     */
    public void listen() {
        ensureDependenciesInjected();
        final int port = getConfiguration().getInteger(SocketTransportConstants.SERVER_PORT, SocketTransportConstants.REMOTE_PORT_DEFAULT);
        workerPool = new WorkerPool(5);
        final ServerFacadeImpl sdd = new ServerFacadeImpl(authenticationManager);
        sdd.setEncoder(encoder);
        final ServerFacade sd = new ServerFacadeLogger(encoder, sdd);
        sd.init();
        socket = newServerSocket(port);
        LOG.info("listening for connection on " + socket);
        acceptAndHandleRequests(sd);
        shutdown();
    }

    private ServerSocket newServerSocket(final int port) {
        try {
            ServerSocket socket = new ServerSocket(port);
            LOG.info("nof listener started on " + socket);
            socket.setSoTimeout(1000);
            return socket;
        } catch (final IOException e) {
            throw new NakedObjectException(e);
        } finally {
            shutdown(socket);
        }
    }

    private void acceptAndHandleRequests(final ServerFacade sd) {
        while (!isShutdown()) {
            try {
                final Socket clientSocket = socket.accept();
                LOG.info("connection accepted from " + clientSocket.getInetAddress());
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                if (isDebugging()) {
                    inputStream = new ProfilingInputStream(inputStream);
                    outputStream = new ProfilingOutputStream(outputStream);
                }
                final ServerConnection connection = createClientConnection(inputStream, outputStream, sd);
                final Worker worker = workerPool.getWorker();
                worker.setConnection(connection);
            } catch (final InterruptedIOException ignore) {
                continue;
            } catch (final IOException e) {
                LOG.warn("connection exception", e);
                continue;
            } catch (final ConnectionException e) {
                LOG.warn("connection exception", e);
            }
        }
    }

    public void stop() {
        LOG.info("stopping listener");
        synchronized (shutdown) {
            shutdown = Boolean.TRUE;
        }
    }

    private void shutdown() {
        shutdown(socket);
        socket = null;
        shutdown(workerPool);
        workerPool = null;
    }

    private void shutdown(final ServerSocket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
            LOG.info("socket closed");
        } catch (final IOException e) {
            LOG.error("Failed to close listening socket", e);
        }
    }

    private void shutdown(final WorkerPool workerPool) {
        if (workerPool == null) {
            return;
        }
        workerPool.shutdown();
        LOG.info("worker pool stopped");
    }

    protected boolean isShutdown() {
        synchronized (shutdown) {
            return shutdown.booleanValue();
        }
    }

    /**
     * Hook method.
     */
    protected abstract ServerConnection createClientConnection(InputStream input, OutputStream output, ServerFacade distribution);

    public void debugData(final DebugString debug) {
        debug.appendln("Listener on", socket.toString());
        debug.appendln("Workers", workerPool);
        workerPool.debug(debug);
    }

    public String debugTitle() {
        return "Server Listener";
    }

    private boolean isDebugging() {
        return true;
    }

    private void ensureDependenciesInjected() {
        Assert.assertNotNull("Session manager needed", authenticationManager);
        Assert.assertNotNull("Encoder needed", encoder);
    }

    public void setAuthenticationManager(final AuthenticationManager sessionManager) {
        this.authenticationManager = sessionManager;
    }

    public void setEncoder(final ObjectEncoderDefault encoder) {
        this.encoder = encoder;
    }

    private NakedObjectConfiguration getConfiguration() {
        return NakedObjectsContext.getConfiguration();
    }
}
