package org.jeuron.jlightning.container;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.jeuron.jlightning.connection.commander.CommanderException;
import org.jeuron.jlightning.connection.commander.DefaultCommander;
import org.jeuron.jlightning.connection.commander.Request;
import org.jeuron.jlightning.connection.commander.RequestAction;
import org.jeuron.jlightning.connection.commander.Response;
import org.jeuron.jlightning.connection.commander.ResponseAction;
import org.jeuron.jlightning.connection.manager.ListenerDatagramConnectionManager;
import org.jeuron.jlightning.connection.protocol.factory.DefaultByteBufferProtocolFactory;
import org.jeuron.jlightning.dispatcher.factory.DefaultDispatcherFactory;

/**
 * <p>Extends {@link AbstractListenerContainer} to implements basic datagram
 * listener container operations.
 *
 * @author Mike Karrys
 * @since 1.0
 * @see Container
 * @see AbstractContainer
 * @see AbstractListenerContainer
 */
public class DatagramListenerContainer extends AbstractListenerContainer {

    /**
     * Starts Listener.
     *
     */
    public Response start() throws ContainerException {
        Response response = null;
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing Container");
            logger.debug("autostart=" + isAutostart());
            logger.debug("buffersize=" + getBufferSize());
            logger.debug("remoteHost=" + getRemoteHost());
            logger.debug("keepalive=" + this.keepalive);
            logger.debug("remotePort=" + this.remotePort);
            logger.debug("localPort=" + this.localPort);
            logger.debug("poolSize=" + this.poolSize);
            logger.debug("timeout=" + this.timeout);
        }
        initialize();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("Starting JLightning Datagram Listener Container.");
            }
            if (remotePort == 0) {
                throw new ContainerException("Remote Port must be specified.");
            }
            if (dispatcherFactory == null) {
                dispatcherFactory = new DefaultDispatcherFactory();
            }
            if (processorFactory == null) {
                throw new ContainerException("ProcessorFactory must be specified.");
            }
            if (protocolFactory == null) {
                protocolFactory = new DefaultByteBufferProtocolFactory();
            }
            if (commander == null) {
                commander = new DefaultCommander();
            }
            SocketAddress remoteSocketAddress = new InetSocketAddress(remoteHost, remotePort);
            SocketAddress localSocketAddress = new InetSocketAddress(localPort);
            connectionManager = new ListenerDatagramConnectionManager();
            connectionManager.setContainer(this);
            connectionManager.setRemoteSocketAddress(remoteSocketAddress);
            connectionManager.setLocalSocketAddress(localSocketAddress);
            connectionManager.setProcessorFactory(processorFactory);
            connectionManager.setProtocolFactory(protocolFactory);
            connectionManager.setSocketBufferSize(bufferSize);
            connectionManager.setCommander(commander);
            connectionManager.setDispatcherFactory(dispatcherFactory);
            commander.setConnectionManager(connectionManager);
            connectionManager.init();
            threadPool.execute(connectionManager);
            connectionManager.waitForThreadStart();
            if (autostart) {
                if (logger.isDebugEnabled()) {
                    logger.debug("In Autostart");
                }
                Request request = new Request();
                request.setAction(RequestAction.BIND);
                response = commander.sendReceive(request);
                if (response.getAction() != ResponseAction.EXCEPTION) {
                    setStarted(true);
                }
            } else {
                response = new Response();
                response.setAction(ResponseAction.SUCCESS);
            }
            if (logger.isDebugEnabled()) {
                logger.info("JLightning Datagram Listener Container Started.");
            }
        } catch (IOException ex) {
            throw new ContainerException(ex);
        }
        return response;
    }

    /**
     * Stops Listener.
     *
     */
    public Response stop() throws ContainerException {
        Response response = null;
        if (logger.isDebugEnabled()) {
            logger.debug("JLightning Datagram Listener Container Stopping.");
        }
        if (isStarted()) {
            response = shutdown();
            setStarted(false);
        } else {
            response = new Response();
            response.setAction(ResponseAction.TRUE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("JLightning Datagram Listener Container Stopped.");
        }
        return response;
    }
}
