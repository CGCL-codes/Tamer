package eu.dominicum.ra.db4o.outbound;

import java.util.List;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import org.apache.log4j.Logger;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import eu.dominicum.ra.db4o.api.IDb4oConnection;

public class Db4oConnection implements IDb4oConnection {

    private static final Logger LOG = Logger.getLogger(Db4oConnection.class);

    private Db4oManagedConnection managedConnection;

    public Db4oConnection(Db4oManagedConnection mc) {
        this.managedConnection = mc;
    }

    public void associateConnection(Db4oManagedConnection db4oManagedConnection) {
    }

    public void invalidate() {
    }

    /**
	 * Closes the connection.
	 */
    public void close() throws ResourceException {
        LOG.info("Closing a connection " + this.managedConnection);
        if (this.managedConnection == null) {
            return;
        }
        this.managedConnection.removeDb4oConnection(this);
        this.managedConnection.sendEvent(ConnectionEvent.CONNECTION_CLOSED, null, this);
        this.managedConnection = null;
        LOG.info("Connection closed OK");
    }

    public void store(Object object) {
        ObjectContainer store = this.managedConnection.getStore();
        store.store(object);
    }

    public void commit() {
        ObjectContainer store = this.managedConnection.getStore();
        store.commit();
    }

    public List<?> executeNativeQuery(Predicate<?> predicate) {
        List<?> queryResult = null;
        ObjectContainer store = this.managedConnection.getStore();
        queryResult = store.query(predicate);
        return queryResult;
    }
}
