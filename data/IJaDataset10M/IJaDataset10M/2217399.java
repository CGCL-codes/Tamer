package org.cilogon.service.servlet;

import edu.uiuc.ncsa.security.core.cache.CachedObject;
import edu.uiuc.ncsa.security.core.cache.Cleanup;
import edu.uiuc.ncsa.security.core.cache.MaxAgePolicy;
import edu.uiuc.ncsa.security.core.cache.ValidTimestampPolicy;
import edu.uiuc.ncsa.security.core.ipc.IPCBean;
import edu.uiuc.ncsa.security.core.ipc.IPCEvent;
import edu.uiuc.ncsa.security.core.ipc.IPCEventListener;
import edu.uiuc.ncsa.security.delegation.storage.TransactionStore;
import edu.uiuc.ncsa.security.delegation.storage.impl.ServiceTransaction;
import edu.uiuc.ncsa.security.delegation.token.AuthorizationGrant;
import edu.uiuc.ncsa.security.servlet.AbstractServlet;
import org.cilogon.rdf.CILogonConfiguration;
import org.cilogon.service.ServiceEnvironment;
import org.cilogon.service.config.cli.ServiceConfigurationDepot;
import org.cilogon.service.servlet.config.ServletConfigurationDepot;
import org.cilogon.service.servlet.util.DBServiceException;
import org.cilogon.service.servlet.util.TransactionCleanup;
import org.cilogon.service.storage.ArchivedUserStore;
import org.cilogon.service.storage.UserStore;
import org.cilogon.service.util.CILogonServiceTransaction;
import org.cilogon.service.util.ServiceTokenFactory;
import org.cilogon.service.util.X509Cache;
import org.cilogon.util.CILogon;
import org.cilogon.util.exceptions.CILogonException;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import static org.cilogon.service.servlet.config.ServletConfigurationDepot.CONFIG_FILE_PROPERTY;

/**
 * This adds the inter-process communication monitoring for exceptions.
 * <p>Created by Jeff Gaynor<br>
 * on May 28, 2010 at  4:19:17 PM
 */
public abstract class AbstractServiceServlet extends AbstractServlet implements CILogon {

    public HashMap<String, IPCEventListener> getIpcListeners() {
        if (ipcListeners == null) {
            ipcListeners = new HashMap<String, IPCEventListener>();
        }
        return ipcListeners;
    }

    static HashMap<String, IPCEventListener> ipcListeners;

    public void addIPCListener(IPCEventListener ipcEventListener) {
        getIpcListeners().put(ipcEventListener.getIdentifier(), ipcEventListener);
    }

    public void removeIPCListener(IPCEventListener ipcEventListener) {
        getIpcListeners().remove(ipcEventListener.getIdentifier());
    }

    protected void fireIPCEvent(IPCEvent ipcEvent, String identifier) {
        debug("fireIPCEvent: firing event for " + identifier);
        if (getIpcListeners().containsKey(identifier)) {
            getIpcListeners().get(identifier).fireEventHappened(ipcEvent);
        } else {
            warn("No listener for transaction with id = " + identifier);
        }
    }

    protected void ipcUpdate(ServiceTransaction serviceTransaction, int status) {
        ipcUpdate(serviceTransaction.getIdentifier(), status);
    }

    /**
     * Update the current status of the IPC bean. This is done throughout the process to leave
     * and audit trail.
     *
     * @param identifier
     */
    protected void ipcUpdate(String identifier, int status) {
        debug("ipcOk: Firing ok event for " + identifier);
        if (getIpcListeners().containsKey(identifier)) {
            IPCEvent ipcEvent = new IPCEvent(this);
            ipcEvent.setExitStatus(status);
            fireIPCEvent(ipcEvent, identifier);
        } else {
            throw new CILogonException("Error: no IPC bean for id " + identifier);
        }
    }

    /**
     * Sets the status ok and fires the event. Invoke this at the very end of the entire
     * process to indicate a normal exit. Remember that as soon as you call this, the
     * listener will exit!
     *
     * @param identifier
     */
    protected void ipcOk(String identifier) {
        debug("ipcOk: Firing ok event for " + identifier);
        if (getIpcListeners().containsKey(identifier)) {
            IPCEvent ipcEvent = new IPCEvent(this);
            ipcEvent.setExitStatus(IPCBean.STATUS_DONE);
            fireIPCEvent(ipcEvent, identifier);
        } else {
            throw new CILogonException("Error: no IPC bean for id " + identifier);
        }
    }

    protected void handleException(Throwable t, String identifier) throws IOException, ServletException {
        IPCEvent ipcEvent = new IPCEvent(this);
        ipcEvent.setThrowable(t);
        ipcEvent.setExitStatus(IPCBean.STATUS_EXCEPTION);
        fireIPCEvent(ipcEvent, identifier);
        if (t instanceof DBServiceException) {
            throw (DBServiceException) t;
        }
        super.handleException(t, null, null);
    }

    protected String infoID(ServiceTransaction trans) {
        return infoID(trans.getCallback(), trans.getIdentifier());
    }

    protected String infoID(URI callback, String idToken) {
        return infoID(callback.toString(), idToken);
    }

    protected String infoID(URI callback, URI idToken) {
        return infoID(callback.toString(), idToken.toString());
    }

    protected String infoID(String callback, AuthorizationGrant ag) {
        return infoID(callback, ag.getToken());
    }

    protected String getTCToken(String tempCred) {
        String x = "(none)";
        if (tempCred != null) {
            int lastSlashIndex = tempCred.lastIndexOf("/");
            int nextLastSlashIndex = tempCred.lastIndexOf("/", lastSlashIndex - 1);
            x = tempCred.substring(nextLastSlashIndex + 1, lastSlashIndex);
        }
        return x;
    }

    protected String infoID(String callback, String tempCred) {
        return "(cb=" + callback + ", tc=" + getTCToken(tempCred) + ")";
    }

    static ServiceConfigurationDepot serviceConfigurationDepot;

    public ServiceEnvironment getServiceEnvironment() throws IOException {
        return (ServiceEnvironment) getEnvironment();
    }

    protected CILogonConfiguration getConfiguration() throws IOException {
        return getConfigurationDepot().getCurrentConfiguration();
    }

    protected ServiceConfigurationDepot getConfigurationDepot() throws IOException {
        if (serviceConfigurationDepot == null) {
            serviceConfigurationDepot = new ServletConfigurationDepot();
            debug("got server file = " + getServletContext().getInitParameter(CONFIG_FILE_PROPERTY));
            File f = new File(getServletContext().getInitParameter(CONFIG_FILE_PROPERTY));
            debug("file = " + f.getCanonicalPath());
            debug("file exists? " + f.exists());
            if (!f.exists()) {
                throw new CILogonException("Error bootstrapping system: The configuration file \"" + f.getCanonicalPath() + "\" does not exist.");
            }
            serviceConfigurationDepot.deserialize(getServletContext().getInitParameter(CONFIG_FILE_PROPERTY));
            if (null == serviceConfigurationDepot.getCurrentConfiguration()) {
                throw new CILogonException("Error bootstrapping system: there is no root for the configuration in the file \"" + f.getCanonicalPath() + "\"");
            }
            debug("servlet config found, root null? " + (null == serviceConfigurationDepot.getCurrentConfiguration()));
        }
        return serviceConfigurationDepot;
    }

    @Override
    public void loadEnvironment() throws IOException {
        setEnvironment(new ServiceEnvironment(getConfiguration()));
    }

    public ServiceTokenFactory getTokenFactory() throws IOException, ServletException {
        return getServiceEnvironment().getTokenFactory();
    }

    public TransactionStore getTransactionStore() throws IOException, ServletException {
        return getServiceEnvironment().getTransactionStore();
    }

    protected CILogonServiceTransaction newTransaction() throws IOException, ServletException {
        return (CILogonServiceTransaction) getTransactionStore().create();
    }

    static Cleanup<String, CILogonServiceTransaction> transactionCleanup = null;

    static Cleanup<String, CachedObject> x509CacheCleanup = null;

    @Override
    public void init() throws ServletException {
        super.init();
        TransactionStore transactionStore = null;
        try {
            transactionStore = getTransactionStore();
        } catch (IOException e) {
            throw new ServletException("Could not get the transaction store", e);
        }
        if (transactionCleanup == null) {
            transactionCleanup = new TransactionCleanup();
            transactionCleanup.setStopThread(false);
            transactionCleanup.setMap(transactionStore);
            transactionCleanup.addRetentionPolicy(new ValidTimestampPolicy());
            transactionCleanup.start();
            info("Starting transaction store cleanup thread");
        }
        if (x509CacheCleanup == null) {
            x509CacheCleanup = new Cleanup<String, CachedObject>() {

                @Override
                public void log(List<CachedObject> removed) {
                    if (removed.isEmpty()) {
                        return;
                    }
                    StringBuffer sb = new StringBuffer();
                    String indent = "\t";
                    sb.append("\nX509 cache cleanup:\n");
                    sb.append(indent + getMap().size() + " remaining\n");
                    sb.append(indent + removed.size() + " removed:\n");
                    log(sb.toString());
                }
            };
            x509CacheCleanup.setMap(X509Cache.getInstance());
            x509CacheCleanup.addRetentionPolicy(new MaxAgePolicy(X509Cache.getInstance(), 2 * TOKEN_LIFETIME));
            info("Starting X509 cache cleanup thread");
            x509CacheCleanup.start();
        }
    }

    protected void shutdownCleanup(Cleanup c) {
        if (c != null && !c.isStopThread()) {
            c.setStopThread(true);
            c.interrupt();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        shutdownCleanup(transactionCleanup);
        shutdownCleanup(x509CacheCleanup);
    }

    protected UserStore getUserStore() throws IOException {
        return getServiceEnvironment().getUserStore();
    }

    protected ArchivedUserStore getArchivedUserStore() throws IOException {
        return getServiceEnvironment().getArchivedUserStore();
    }

    protected CILogonServiceTransaction getTransaction(AuthorizationGrant ag) throws IOException, ServletException {
        return (CILogonServiceTransaction) getTransactionStore().get(ag);
    }
}
