package org.amse.bomberman.server.net.tcpimpl.servers;

import java.io.IOException;
import org.amse.bomberman.server.net.Session;

/**
 *
 * @author Kirilchuk V.E.
 */
class StartedState implements ServerState {

    private static final ServerState INSTANCE = new StartedState();

    private StartedState() {
    }

    static ServerState getInstance() {
        return INSTANCE;
    }

    @Override
    public void start(TcpServer server, int port) throws IOException, IllegalStateException {
        System.err.println("Server: start error. Already in started state.");
        throw new IllegalStateException("Server: start error. " + "Already in started state.");
    }

    @Override
    public void stop(TcpServer server) {
        if (server.getServerSocket() != null) {
            try {
                server.getServerSocket().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        for (Session session : server.getSessions()) {
            System.out.println("Server: interrupting session " + session.getId() + "...");
            session.terminateSession();
        }
        server.setLastId(0);
        if (server.getServiceContext() != null) {
            server.getServiceContext().getGameStorage().clearGames();
        }
        server.setServerState(StoppedState.getInstance());
        System.out.println("Server: shutdowned.");
    }
}
