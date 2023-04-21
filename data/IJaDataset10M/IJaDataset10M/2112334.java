package org.sourceforge.jemm;

import org.sourceforge.jemm.database.remote.client.RemoteDatabase;
import org.sourceforge.jemm.rpc.connection.ConnectionException;
import org.sourceforge.jemm.rpc.connection.socket.SocketClientConnectionFactory;

/**
 * A PersistentStore uses a BDbDatabase to a local persistent database.
 * 
 * The following code shows how to register the PersistentStore such that it
 * will store all objects that have been enhanced:
 * <code>
 * RemoteStore myStore = new RemoteStore();
 * Session.setStore(myStore);
 * </code>
 * 
 * One your program has finished running call the following:
 * <code>
 * Session.shutdown();
 * </code>
 * 
 * @see Session
 * @author Paul Keeble
 * @author Rory Graves
 *
 */
public class RemoteStore extends AbstractStore<RemoteDatabase> {

    protected final String hostname;

    protected final int port;

    public RemoteStore(String hostname, int port) {
        this(hostname, port, false);
    }

    public RemoteStore(String hostname, int port, boolean debug) {
        super(debug);
        this.hostname = hostname;
        this.port = port;
        configure();
    }

    @Override
    protected RemoteDatabase createUnderlyingDatabase() {
        try {
            SocketClientConnectionFactory clientFactory = new SocketClientConnectionFactory(hostname, port);
            return new RemoteDatabase(clientFactory);
        } catch (ConnectionException ce) {
            throw new JEMMInternalException("Unable to initialise connection to remote server", ce);
        }
    }
}
