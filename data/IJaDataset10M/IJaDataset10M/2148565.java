package org.datanucleus.jdo.connector;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import org.datanucleus.api.jdo.JDOPersistenceManager;

/**
 * Implementation of ManagedConnection persistence manager
 * Handle is the Object Instance of the API the user application is interacting with.
 */
public class ManagedConnectionImpl implements ManagedConnection {

    private final PasswordCredential credential;

    /**
     * Those instances of {@link PersistenceManagerImpl}, which have been
     * opened for this managed connection.
     */
    private final List<PersistenceManagerImpl> handles = new ArrayList();

    /** event listeners **/
    private final Collection<ConnectionEventListener> cels = new ArrayList();

    private PrintWriter logWriter;

    /** The application server enlists this XAResource into a XA transaction */
    private XAResource xares;

    private ContainerLocalTransaction localTx;

    private JDOPersistenceManager pm;

    private final ManagedConnectionFactoryImpl mcf;

    /**
     * Constructor.
     * @param mcf the ManagedConnectionFactory
     * @param credential the PasswordCredential
     * @throws ResourceException
     */
    public ManagedConnectionImpl(ManagedConnectionFactoryImpl mcf, PasswordCredential credential) throws ResourceException {
        this.credential = credential;
        this.mcf = mcf;
    }

    PasswordCredential getPasswordCredential() {
        return credential;
    }

    ManagedConnectionFactoryImpl getManagedConnectionFactory() {
        return mcf;
    }

    /**
     * Method to start the Transaction
     */
    public void begin() {
        if (getPersistenceManager().getObjectManager().getTransaction() == null) {
            PersistenceManagerImpl.LOGGER.error("Invalid state during begin invoke. Transaction is closed.");
            return;
        }
        PersistenceManagerImpl.LOGGER.debug("Beginning ManagedConnection " + this);
        if (!getPersistenceManager().getObjectManager().getTransaction().isActive()) {
            getPersistenceManager().getObjectManager().getTransaction().begin();
        }
        notifyBegin();
    }

    /**
     * Destroy method
     * @exception javax.resource.ResourceException <description>
     */
    public void destroy() throws ResourceException {
        PersistenceManagerImpl.LOGGER.debug("Destroying ManagedConnection " + this);
        if (!handles.isEmpty()) {
            List handlesToClose = new ArrayList(handles);
            for (Iterator it = handlesToClose.iterator(); it.hasNext(); ) {
                PersistenceManagerImpl om = ((PersistenceManagerImpl) it.next());
                if (!om.isClosed()) {
                    om.close();
                }
            }
        }
        if (pm != null) {
            pm.close();
        }
        localTx = null;
        pm = null;
    }

    /**
     * Cleanup method
     * @exception javax.resource.ResourceException <description>
     */
    public synchronized void cleanup() throws ResourceException {
        PersistenceManagerImpl.LOGGER.debug("Cleaning up ManagedConnection " + this);
        if (pm != null) {
            pm.getObjectManager().disconnectLifecycleListener();
        }
        for (Iterator<PersistenceManagerImpl> i = handles.iterator(); i.hasNext(); ) {
            i.next().setManagedConnection(null);
        }
        handles.clear();
        if (pm != null) {
            pm.close();
        }
        localTx = null;
        pm = null;
    }

    public JDOPersistenceManager getPersistenceManager() {
        if (pm == null) {
            if (getPasswordCredential() == null) {
                pm = (JDOPersistenceManager) mcf.getPersistenceManagerFactory().getPersistenceManager();
            } else {
                pm = (JDOPersistenceManager) mcf.getPersistenceManagerFactory().getPersistenceManager(getPasswordCredential().getUserName(), new String(getPasswordCredential().getPassword()));
            }
        }
        return pm;
    }

    /**
     * Accessor for the connection
     * @param subject <description>
     * @param cri <description>
     * @return <description>
     * @exception javax.resource.ResourceException <description>
     */
    public Object getConnection(Subject subject, ConnectionRequestInfo cri) throws ResourceException {
        PersistenceManagerImpl.LOGGER.debug("Obtaining Connection for this ManagedConnection " + this);
        PasswordCredential pc = getManagedConnectionFactory().getPasswordCredential(subject);
        if (credential != pc && credential != null && pc != null && !credential.equals(pc)) {
            throw new ResourceException("Wrong subject: " + subject + " MCF credentials: " + pc + " MC credentials: " + credential);
        }
        PersistenceManagerImpl pm = new PersistenceManagerImpl(this);
        handles.add(0, pm);
        return pm;
    }

    /**
     * Accessor for the Log
     * @return The Log writer
     * @exception javax.resource.ResourceException <description>
     */
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    /**
     * Mutator for the Log
     * @param writer PrintWriter to use for Log
     * @exception javax.resource.ResourceException <description>
     */
    public void setLogWriter(PrintWriter writer) throws ResourceException {
        this.logWriter = writer;
    }

    /**
     * Mutator to add a connection listener
     * @param cel <description>
     */
    public void addConnectionEventListener(ConnectionEventListener cel) {
        synchronized (cels) {
            cels.add(cel);
        }
    }

    /**
     * Mutator to remove a connection listener
     * @param cel <description>
     */
    public void removeConnectionEventListener(ConnectionEventListener cel) {
        synchronized (cels) {
            cels.remove(cel);
        }
    }

    /**
     * Mutator to associate a connection
     * @param c <description>
     * @exception javax.resource.ResourceException <description>
     */
    public void associateConnection(Object c) throws ResourceException {
        if (!(c instanceof PersistenceManagerImpl)) {
            throw new ResourceException("wrong Connection type!");
        }
        PersistenceManagerImpl.LOGGER.debug("Associating " + c + " to this ManagedConnection " + this);
        ((PersistenceManagerImpl) c).setManagedConnection(this);
        if (!handles.contains(c)) {
            handles.add(0, (PersistenceManagerImpl) c);
        }
    }

    /**
     * Accessor for the local transaction
     * @return <description>
     * @exception javax.resource.ResourceException <description>
     */
    public LocalTransaction getLocalTransaction() throws ResourceException {
        PersistenceManagerImpl.LOGGER.debug("ManagedConnectionImpl.getLocalTransaction() invoked");
        if (localTx == null) {
            localTx = new ContainerLocalTransaction(this);
        }
        return localTx;
    }

    /**
     * Accessor for the connection MetaData
     * @return <description>
     * @exception javax.resource.ResourceException <description>
     */
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        throw new ResourceException("Not Yet Implemented");
    }

    /**
     * Accessor for the XA resource. The application server enlists
     * this XAResource into a XA transaction
     * This is invoked only once per instance.
     * @return <description>
     * @exception javax.resource.ResourceException <description>
     */
    public XAResource getXAResource() throws ResourceException {
        PersistenceManagerImpl.LOGGER.debug("ManagedConnectionImpl.getXAResource() invoked");
        if (xares == null) {
            xares = new ConnectionXAResource(this);
        }
        return xares;
    }

    /**
     * Called by the PM handle, whenever it gets closed
     */
    void notifyClosed(PersistenceManagerImpl handle) {
        ConnectionEvent ce = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED, null);
        ce.setConnectionHandle(handle);
        Collection<ConnectionEventListener> localCels = null;
        synchronized (cels) {
            localCels = new ArrayList(cels);
        }
        for (Iterator<ConnectionEventListener> i = localCels.iterator(); i.hasNext(); ) {
            i.next().connectionClosed(ce);
        }
    }

    void notifyBegin() {
        for (Iterator<PersistenceManagerImpl> it = handles.iterator(); it.hasNext(); ) {
            notifyTxBegin(it.next());
        }
    }

    void notifyCommit() {
        for (Iterator<PersistenceManagerImpl> it = handles.iterator(); it.hasNext(); ) {
            notifyTxCommit(it.next());
        }
    }

    void notifyRollback() {
        List h = new ArrayList(handles);
        for (Iterator<PersistenceManagerImpl> it = h.iterator(); it.hasNext(); ) {
            notifyTxRollback(it.next());
        }
    }

    void clearHandles() {
        handles.clear();
    }

    void notifyTxBegin(PersistenceManagerImpl handle) {
    }

    void notifyTxCommit(PersistenceManagerImpl handle) {
    }

    void notifyTxRollback(PersistenceManagerImpl handle) {
    }
}
