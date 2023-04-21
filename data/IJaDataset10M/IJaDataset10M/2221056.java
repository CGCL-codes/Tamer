package oracle.toplink.essentials.internal.sequencing;

import oracle.toplink.essentials.internal.databaseaccess.Accessor;
import oracle.toplink.essentials.threetier.ConnectionPool;

class ServerSessionConnectionHandler implements SequencingConnectionHandler {

    ServerSessionConnectionHandler(ConnectionPool pool) {
        this.pool = pool;
    }

    ConnectionPool pool;

    public void onConnect() {
        if (!isConnected()) {
            pool.startUp();
        }
    }

    public boolean isConnected() {
        return pool.isConnected();
    }

    public Accessor acquireAccessor() {
        return pool.acquireConnection();
    }

    public void releaseAccessor(Accessor accessor) {
        pool.releaseConnection(accessor);
    }

    public void onDisconnect() {
        if (isConnected()) {
            pool.shutDown();
        }
    }

    protected void finalize() throws Throwable {
        onDisconnect();
    }
}
