package eu.dominicum.ra.db4o.outbound;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import org.apache.log4j.Logger;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.diagnostic.Diagnostic;
import com.db4o.diagnostic.DiagnosticListener;
import com.db4o.diagnostic.NativeQueryNotOptimized;
import eu.dominicum.ra.db4o.api.IDb4oConnection;

public class Db4oManagedConnection implements ManagedConnection {

    private static Logger LOG = Logger.getLogger(Db4oManagedConnection.class);

    private static int MC_COUNTER;

    private int mcId;

    private Db4oManagedConnectionFactory mcf;

    private ObjectContainer store;

    private Set<IDb4oConnection> connectionSet;

    private Db4oConnectionEventListener eventListener;

    private boolean destroyed;

    private PrintWriter logWriter;

    public Db4oManagedConnection(Db4oManagedConnectionFactory mcf, Subject subject, ConnectionRequestInfo cxRequestInfo) {
        this.mcId = Db4oManagedConnection.MC_COUNTER++;
        LOG.info("Constructing Managed Connection, ID = " + mcId);
        this.mcf = mcf;
        String username = null;
        String password = null;
        String serverName = null;
        Integer portNumber = null;
        if (cxRequestInfo != null) {
            username = ((Db4oConnectionRequestInfo) cxRequestInfo).getUsername();
            password = ((Db4oConnectionRequestInfo) cxRequestInfo).getPassword();
            serverName = ((Db4oConnectionRequestInfo) cxRequestInfo).getServerName();
            portNumber = ((Db4oConnectionRequestInfo) cxRequestInfo).getPortNumber();
        } else {
            username = mcf.getUsername();
            password = mcf.getPassword();
            serverName = mcf.getServerName();
            portNumber = mcf.getPortNumber();
        }
        LOG.info("Connecting to Db4o : server=" + serverName + ", port=" + portNumber + ", username=" + username + ", password=" + password);
        try {
            Configuration config = Db4o.newConfiguration();
            config.diagnostic().addListener(new DiagnosticListener() {

                @Override
                public void onDiagnostic(Diagnostic diagnostic) {
                    if (diagnostic instanceof NativeQueryNotOptimized) {
                        throw new RuntimeException("Query could not be optimized!!");
                    }
                }
            });
            this.store = Db4o.openClient(config, serverName, portNumber, username, password);
        } catch (Throwable t) {
            LOG.error("Unable to connect to Db4o, is your server running?");
        }
        connectionSet = new HashSet<IDb4oConnection>();
        eventListener = new Db4oConnectionEventListener(this);
    }

    /**
	 * Adds a connection event listener to the ManagedConnection instance. The
	 * registered ConnectionEventListener instances are notified of connection
	 * close and error events as well as local-transaction-related events on the
	 * Managed Connection.
	 * 
	 * @param listener
	 *            a new ConnectionEventListener to be registered
	 */
    public void addConnectionEventListener(ConnectionEventListener listener) {
        LOG.info("Adding a Connection Event Listener");
        this.eventListener.addConnectorListener(listener);
    }

    /**
	 * Used by the container to change the association of an application-level
	 * connection handle with a ManagedConnection instance. The container should
	 * find the right ManagedConnection instance and call the
	 * associateConnection method.
	 * 
	 * @param connection
	 *            application-level connection handle
	 * 
	 * @exception ResourceException
	 *                if the attempt to change the association fails
	 */
    public void associateConnection(Object connection) throws ResourceException {
        LOG.info("Associating a connection");
        if (destroyed) {
            LOG.error("Connection is destroyed");
            throw new IllegalStateException("Connection is destroyed");
        }
        if (connection instanceof IDb4oConnection) {
            Db4oConnection db4oConnection = (Db4oConnection) connection;
            db4oConnection.associateConnection(this);
        } else {
            throw new IllegalStateException("Invalid connection");
        }
    }

    /**
	 * Initiates a cleanup of the client-specific state maintained by a
	 * ManagedConnection instance. The cleanup should invalidate all connection
	 * handles created using this ManagedConnection instance.
	 * 
	 * @exception ResourceException
	 *                if the cleanup fails
	 */
    public void cleanup() throws ResourceException {
        LOG.info("Cleanup for Managed Connection ID " + this.mcId);
        if (destroyed) {
            LOG.error("Managed Connection is destroyed");
            throw new IllegalStateException("Managed Connection is destroyed");
        }
        Iterator it = connectionSet.iterator();
        while (it.hasNext()) {
            Db4oConnection db4oConnection = (Db4oConnection) it.next();
            db4oConnection.invalidate();
        }
        connectionSet.clear();
    }

    /**
	 * Destroys the physical connection.
	 * 
	 * @exception ResourceException
	 *                if the method fails to destroy the connection
	 */
    public void destroy() throws ResourceException {
        if (destroyed) return;
        LOG.info("Destroying Managed Connection ID " + this.mcId);
        destroyed = true;
        Db4oManagedConnection.MC_COUNTER--;
        Iterator it = connectionSet.iterator();
        while (it.hasNext()) {
            Db4oConnection db4oConnection = (Db4oConnection) it.next();
            db4oConnection.invalidate();
        }
        connectionSet.clear();
        if (this.store != null) {
            try {
                this.store.close();
            } catch (Exception e) {
                LOG.error("Error whilst destroying Managed Connection ID " + mcId);
                throw new ResourceException(e.getMessage());
            }
        }
        LOG.info("Managed Connection ID " + mcId + " destroyed OK");
    }

    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        LOG.info("Getting the Connection for Managed Connection ID " + this.mcId);
        if (destroyed) {
            LOG.error("Connection is destroyed");
            throw new IllegalStateException("Connection is destroyed");
        }
        IDb4oConnection db4oConn = new Db4oConnection(this);
        this.connectionSet.add(db4oConn);
        return db4oConn;
    }

    /**
	 * Returns a javax.resource.spi.LocalTransaction instance. The
	 * LocalTransaction interface is used by the container to manage local
	 * transactions for a RM instance.
	 * 
	 * Because this implementation does not support transactions, the method
	 * throws an exception.
	 * 
	 * @return javax.resource.spi.LocalTransaction instance
	 * 
	 * @exception ResourceException
	 *                if transactions are not supported
	 */
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("This connector does not support transactions");
    }

    /**
	 * Gets the log writer for this ManagedConnection instance.
	 * 
	 * @return the character output stream associated with this
	 *         ManagedConnection instance
	 * 
	 * @exception ResourceException
	 *                if the method fails
	 */
    public PrintWriter getLogWriter() throws ResourceException {
        return this.logWriter;
    }

    /**
	 * Gets the metadata information for this connection's underlying EIS
	 * resource manager instance. The ManagedConnectionMetaData interface
	 * provides information about the underlying EIS instance associated with
	 * the ManagedConnection instance.
	 * 
	 * @return ManagedConnectionMetaData ManagedConnectionMetaData instance
	 * 
	 * @exception ResourceException
	 *                if the metadata cannot be retrieved
	 */
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new Db4oManagedConnectionMetaData(this);
    }

    /**
	 * Returns a javax.transaction.xa.XAresource instance. An application server
	 * enlists this XAResource instance with the Transaction Manager if the
	 * ManagedConnection instance is being used in a JTA transaction that is
	 * being coordinated by the Transaction Manager.
	 * 
	 * Because this implementation does not support transactions, the method
	 * throws an exception.
	 * 
	 * @return the XAResource instance
	 * 
	 * @exception ResourceException
	 *                if transactions are not supported
	 */
    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException("This connector does not support transactions");
    }

    /**
	 * Removes an already registered connection event listener from the
	 * ManagedConnection instance.
	 * 
	 * @param listener
	 *            already registered connection event listener to be removed
	 */
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        this.eventListener.removeConnectorListener(listener);
    }

    /**
	 * Sets the log writer for this ManagedConnection instance. The log writer
	 * is a character output stream to which all logging and tracing messages
	 * for this ManagedConnection instance will be printed.
	 * 
	 * @param out
	 *            character output stream to be associated
	 * 
	 * @exception ResourceException
	 *                if the method fails
	 */
    public void setLogWriter(PrintWriter logWriter) throws ResourceException {
        this.logWriter = logWriter;
    }

    /**
	 * Removes the associated connection handle from the connections set to the
	 * physical connection.
	 * 
	 * @param db4oCon
	 *            the connection handle
	 */
    public void removeDb4oConnection(IDb4oConnection db4oCon) {
        connectionSet.remove(db4oCon);
    }

    /**
	 * Sends a connection event to the application server.
	 * 
	 * @param eventType
	 *            the ConnectionEvent type
	 * @param ex
	 *            exception indicating a connection-related error
	 * @param connectionHandle
	 *            the connection handle associated with the ManagedConnection
	 *            instance
	 */
    public void sendEvent(int eventType, Exception ex, Object connectionHandle) {
        this.eventListener.sendEvent(eventType, ex, connectionHandle);
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

    public ObjectContainer getStore() {
        return store;
    }
}
