package org.gamio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.gamio.conf.ServerProps;
import org.gamio.logging.Log;
import org.gamio.logging.Logger;
import org.gamio.system.Context;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 23 $ $Date: 2008-10-05 21:00:52 -0400 (Sun, 05 Oct 2008) $
 */
public final class Server {

    private static final Log log = Logger.getLogger(Server.class);

    private ServerMqMsgListener serverMqMsgListener = null;

    private ServerState serverState = null;

    private ServerProps serverProps = null;

    private ServerSocketChannel serverSocketChannel = null;

    private ServerStartingListener serverStartingListener = null;

    private Lock lock = null;

    private interface ServerState {

        public void start(Server server);

        public void stop(Server server);
    }

    private static final class ServerStopped implements ServerState {

        private static class ServerStoppedHolder {

            static ServerStopped serverStopped = new ServerStopped();
        }

        private ServerStopped() {
        }

        public static final ServerStopped getInstance() {
            return ServerStoppedHolder.serverStopped;
        }

        public void start(Server server) {
            ServerProps serverProps = server.getServerProps();
            serverProps.initMsglet();
            InetAddress bindAddr = null;
            String host = serverProps.getBindAddr();
            if (host != null) {
                try {
                    bindAddr = InetAddress.getByName(host);
                } catch (Exception e) {
                    log.error(e, "Server[name<", serverProps.getName(), ">, id<", serverProps.getId(), ">]");
                    bindAddr = null;
                }
            }
            ServerSocketChannel serverSocketChannel = null;
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.socket().bind(new InetSocketAddress(bindAddr, serverProps.getPort()), serverProps.getBackLog());
            } catch (IOException e) {
                if (serverSocketChannel != null) {
                    try {
                        serverSocketChannel.close();
                    } catch (IOException e2) {
                    }
                }
                log.error(e, "Failed to start Server[name<", serverProps.getName(), ">, id<", serverProps.getId(), ">]");
                return;
            }
            if (!server.serverStartingListener.onServerStarting(server, serverSocketChannel)) {
                log.error("Failed to start Server[name<", serverProps.getName(), ">, id<", serverProps.getId(), ">]");
                return;
            }
            log.info("Server[name<", serverProps.getName(), ">, id<", serverProps.getId(), ">] is listening on port: ", serverProps.getPort());
            server.setServerSocketChannel(serverSocketChannel);
            server.serverMqMsgListener = new ServerMqMsgListener();
            Context.getInstance().getMessageQueue().registerMessageListener(serverProps.getId(), server.serverMqMsgListener);
            server.changeState(ServerStarted.getInstance());
            log.info("Server[name<", serverProps.getName(), ">, id<", serverProps.getId(), ">] started");
        }

        public void stop(Server server) {
            log.warn("Server[name<", server.getName(), ">, id<", server.getId(), ">] has already stopped");
        }
    }

    private static final class ServerStarted implements ServerState {

        private static class ServerStartedHolder {

            static ServerStarted serverStarted = new ServerStarted();
        }

        private ServerStarted() {
        }

        public static ServerStarted getInstance() {
            return ServerStartedHolder.serverStarted;
        }

        public void start(Server server) {
            log.warn("Server[name<", server.getName(), ">, id<", server.getId(), ">] has already started");
        }

        public void stop(Server server) {
            Context.getInstance().getMessageQueue().deregisterMessageListener(server.getId());
            server.serverMqMsgListener.close();
            server.serverMqMsgListener = null;
            ServerSocketChannel serverSocketChannel = server.getServerSocketChannel();
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    log.error(e, "Failed to close the ServerSocketChannel of Server[name<", server.getName(), ">, id<", server.getId(), ">]");
                }
                server.setServerSocketChannel(null);
            }
            server.changeState(ServerStopped.getInstance());
            log.info("Server[name<", server.getName(), ">, id<", server.getId(), ">] stopped");
        }
    }

    public Server(ServerProps serverProps) {
        this.serverProps = serverProps;
        lock = new ReentrantLock();
        serverState = ServerStopped.getInstance();
    }

    public ServerProps getServerProps() {
        return serverProps;
    }

    public String getName() {
        return serverProps.getName();
    }

    public String getId() {
        return serverProps.getId();
    }

    private void setServerSocketChannel(ServerSocketChannel socketChannel) {
        this.serverSocketChannel = socketChannel;
    }

    private ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    private void changeState(ServerState serverState) {
        this.serverState = serverState;
    }

    public void setServerStartingListener(ServerStartingListener serverStartingListener) {
        this.serverStartingListener = serverStartingListener;
    }

    public void start() {
        lock.lock();
        try {
            serverState.start(this);
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            serverState.stop(this);
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}
