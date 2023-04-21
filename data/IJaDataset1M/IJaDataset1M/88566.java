package org.seamantics.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessControlException;
import java.util.Properties;
import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NamespaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;
import org.apache.commons.lang.time.StopWatch;
import org.apache.jackrabbit.rmi.client.ClientXASession;
import org.hibernate.transaction.JBossTransactionManagerLookup;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jcrom.Jcrom;
import org.seamantics.rmi.RepositoryClient;
import org.seamthebits.measure.MeasureCalls;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Name("jcrSession")
@AutoCreate
@MeasureCalls
public class JCRSession implements Session {

    Session session;

    @Logger
    Log log;

    public Session getJcrSession() {
        if (session == null) createSession();
        return session;
    }

    @Create
    public void create() {
        Jcrom.setCurrentSession(this);
    }

    private void createSession() {
        try {
            Repository repository = RepositoryClient.instance();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            final Session session = repository.login(new SimpleCredentials("superuser", new char[] {}));
            stopWatch.stop();
            if (session != null && session.isLive()) {
                log.debug("established jcr-session to #0 #1 with #2 (needed #3 milliseconds)", session.getRepository().getDescriptor(Repository.REP_NAME_DESC), session.getRepository().getDescriptor(Repository.REP_VERSION_DESC), session.getUserID(), stopWatch.getTime());
            } else {
                throw new RuntimeException("Could not initialize CMSService. The current jcr-session is not active.");
            }
            if (!CmsController.instance().isReadOnly()) {
                if (session instanceof ClientXASession) {
                    ClientXASession xasession = (ClientXASession) session;
                    XAResource xaResource = xasession.getXAResource();
                    TransactionManager transactionManager = new JBossTransactionManagerLookup().getTransactionManager(new Properties());
                    if (transactionManager != null) {
                        if (transactionManager.getTransaction() != null) {
                            transactionManager.getTransaction().enlistResource(xaResource);
                            log.debug("Enlisted jcr xa session in container transaction");
                        } else {
                            log.debug("Did not enlist jcr xa session to container transaction, due to missing transaction");
                        }
                    } else {
                        log.error("Could not enlist jcr xa session to container transaction, due to missing transaction manager");
                    }
                }
            }
            this.session = session;
        } catch (Exception e) {
            destroy();
            throw new RuntimeException(e);
        }
    }

    @Destroy
    public void destroy() {
        if (session != null) session.logout();
    }

    public void addLockToken(String lt) {
        session.addLockToken(lt);
    }

    public void checkPermission(String absPath, String actions) throws AccessControlException, RepositoryException {
        getJcrSession().checkPermission(absPath, actions);
    }

    public void exportDocumentView(String absPath, ContentHandler contentHandler, boolean skipBinary, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException {
        getJcrSession().exportDocumentView(absPath, contentHandler, skipBinary, noRecurse);
    }

    public void exportDocumentView(String absPath, OutputStream out, boolean skipBinary, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException {
        getJcrSession().exportDocumentView(absPath, out, skipBinary, noRecurse);
    }

    public void exportSystemView(String absPath, ContentHandler contentHandler, boolean skipBinary, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException {
        getJcrSession().exportSystemView(absPath, contentHandler, skipBinary, noRecurse);
    }

    public void exportSystemView(String absPath, OutputStream out, boolean skipBinary, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException {
        getJcrSession().exportSystemView(absPath, out, skipBinary, noRecurse);
    }

    public Object getAttribute(String name) {
        return getJcrSession().getAttribute(name);
    }

    public String[] getAttributeNames() {
        return getJcrSession().getAttributeNames();
    }

    public ContentHandler getImportContentHandler(String parentAbsPath, int uuidBehavior) throws PathNotFoundException, ConstraintViolationException, VersionException, LockException, RepositoryException {
        return getJcrSession().getImportContentHandler(parentAbsPath, uuidBehavior);
    }

    public Item getItem(String absPath) throws PathNotFoundException, RepositoryException {
        return getJcrSession().getItem(absPath);
    }

    public String[] getLockTokens() {
        return getJcrSession().getLockTokens();
    }

    public String getNamespacePrefix(String uri) throws NamespaceException, RepositoryException {
        return getJcrSession().getNamespacePrefix(uri);
    }

    public String[] getNamespacePrefixes() throws RepositoryException {
        return getJcrSession().getNamespacePrefixes();
    }

    public String getNamespaceURI(String prefix) throws NamespaceException, RepositoryException {
        return getJcrSession().getNamespaceURI(prefix);
    }

    public Node getNodeByUUID(String uuid) throws ItemNotFoundException, RepositoryException {
        return getJcrSession().getNodeByUUID(uuid);
    }

    public Repository getRepository() {
        return getJcrSession().getRepository();
    }

    public Node getRootNode() throws RepositoryException {
        return getJcrSession().getRootNode();
    }

    public String getUserID() {
        return getJcrSession().getUserID();
    }

    public ValueFactory getValueFactory() throws UnsupportedRepositoryOperationException, RepositoryException {
        return getJcrSession().getValueFactory();
    }

    public Workspace getWorkspace() {
        return getJcrSession().getWorkspace();
    }

    public boolean hasPendingChanges() throws RepositoryException {
        return getJcrSession().hasPendingChanges();
    }

    public Session impersonate(Credentials credentials) throws LoginException, RepositoryException {
        return getJcrSession().impersonate(credentials);
    }

    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior) throws IOException, PathNotFoundException, ItemExistsException, ConstraintViolationException, VersionException, InvalidSerializedDataException, LockException, RepositoryException {
        getJcrSession().importXML(parentAbsPath, in, uuidBehavior);
    }

    public boolean isLive() {
        return getJcrSession().isLive();
    }

    public boolean itemExists(String absPath) throws RepositoryException {
        return getJcrSession().itemExists(absPath);
    }

    public void logout() {
        getJcrSession().logout();
    }

    public void move(String srcAbsPath, String destAbsPath) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        getJcrSession().move(srcAbsPath, destAbsPath);
    }

    public void refresh(boolean keepChanges) throws RepositoryException {
        getJcrSession().refresh(keepChanges);
    }

    public void removeLockToken(String lt) {
        getJcrSession().removeLockToken(lt);
    }

    public void save() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {
        getJcrSession().save();
    }

    public void setNamespacePrefix(String prefix, String uri) throws NamespaceException, RepositoryException {
        getJcrSession().setNamespacePrefix(prefix, uri);
    }
}
