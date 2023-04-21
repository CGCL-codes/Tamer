package org.pengyou.webdav.server;

import org.apache.jackrabbit.server.io.*;
import org.apache.jackrabbit.webdav.io.OutputContext;
import org.apache.jackrabbit.webdav.io.InputContext;
import org.apache.jackrabbit.webdav.simple.ResourceConfig;
import org.apache.jackrabbit.webdav.simple.ItemFilter;
import org.apache.jackrabbit.webdav.*;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.apache.jackrabbit.webdav.jcr.JcrDavException;
import org.apache.jackrabbit.webdav.jcr.lock.JcrActiveLock;
import org.apache.jackrabbit.webdav.property.*;
import org.apache.jackrabbit.webdav.lock.*;
import org.apache.jackrabbit.webdav.observation.ObservationConstants;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Text;
import org.apache.jackrabbit.util.ISO9075;
import org.apache.log4j.Logger;
import org.pengyou.webdav.server.io.PYExportContextImpl;
import org.pengyou.webdav.server.io.PYIOManager;
import javax.jcr.*;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.PropertyDefinition;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class PYDavResourceImpl implements DavResource, JcrConstants {

    /**
     * the default logger
     */
    private static final Logger log = Logger.getLogger(PYDavResourceImpl.class);

    private static final HashMap reservedNamespaces = new HashMap();

    static {
        reservedNamespaces.put(DavConstants.NAMESPACE.getPrefix(), DavConstants.NAMESPACE.getURI());
        reservedNamespaces.put(ObservationConstants.NAMESPACE.getPrefix(), ObservationConstants.NAMESPACE.getURI());
    }

    private DavResourceFactory factory;

    private LockManager lockManager;

    private DavSession session;

    private Node node;

    private DavResourceLocator locator;

    private DavPropertySet properties = new DavPropertySet();

    private boolean inited = false;

    private boolean isCollection = true;

    private ItemFilter filter;

    private IOManager ioManager;

    private long modificationTime = IOUtil.UNDEFINED_TIME;

    /**
     * Create a new {@link DavResource}.
     *
     * @param locator
     * @param factory
     * @param session
     */
    public PYDavResourceImpl(DavResourceLocator locator, DavResourceFactory factory, DavSession session, ResourceConfig config) throws RepositoryException, DavException {
        this.session = session;
        this.factory = factory;
        this.locator = locator;
        this.filter = config.getItemFilter();
        this.ioManager = config.getIOManager();
        if (locator != null && locator.getResourcePath() != null) {
            try {
                Item item = session.getRepositorySession().getItem(locator.getJcrPath());
                if (item != null && item.isNode()) {
                    node = (Node) item;
                    isCollection = config.isCollectionResource(node);
                }
            } catch (PathNotFoundException e) {
            }
        } else {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * @return DavResource#COMPLIANCE_CLASS
     * @see org.apache.jackrabbit.webdav.DavResource#getComplianceClass()
     */
    public String getComplianceClass() {
        return DavResource.COMPLIANCE_CLASS;
    }

    /**
     * @return DavResource#METHODS
     * @see org.apache.jackrabbit.webdav.DavResource#getSupportedMethods()
     */
    public String getSupportedMethods() {
        return DavResource.METHODS;
    }

    /**
     * @see DavResource#exists() )
     */
    public boolean exists() {
        return node != null;
    }

    /**
     * @see DavResource#isCollection()
     */
    public boolean isCollection() {
        return isCollection;
    }

    /**
     * Package protected method that allows to define whether this resource
     * represents a collection or not.
     *
     * @param isCollection
     */
    void setIsCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

    /**
     * @see org.apache.jackrabbit.webdav.DavResource#getLocator()
     */
    public DavResourceLocator getLocator() {
        return locator;
    }

    /**
     * @see DavResource#getResourcePath()
     */
    public String getResourcePath() {
        return locator.getResourcePath();
    }

    /**
     * @see DavResource#getHref()
     */
    public String getHref() {
        return locator.getHref(isCollection());
    }

    /**
     * Returns the the last segment of the resource path.<p>
     * Note that this must not correspond to the name of the underlying
     * repository item for two reasons:<ul>
     * <li>SameNameSiblings have an index appended to their item name.</li>
     * <li>the resource path may differ from the item path.</li>
     * </ul>
     * Using the item name as DAV:displayname caused problems with XP built-in
     * client in case of resources representing SameNameSibling nodes.
     *
     * @see DavResource#getDisplayName()
     */
    public String getDisplayName() {
        String resPath = getResourcePath();
        return (resPath != null) ? Text.getName(resPath) : resPath;
    }

    /**
     * @see org.apache.jackrabbit.webdav.DavResource#getModificationTime()
     */
    public long getModificationTime() {
        initProperties();
        return modificationTime;
    }

    /**
     * If this resource exists and the specified context is not <code>null</code>
     * this implementation build a new {@link ExportContext} based on the specified
     * context and forwards the export to its <code>IOManager</code>. If the
     * {@link IOManager#exportContent(ExportContext, DavResource)} fails,
     * an <code>IOException</code> is thrown.
     *
     * @throws IOException if the export fails.
     * @see DavResource#spool(OutputContext)
     * @see ResourceConfig#getIOManager()
     */
    public void spool(OutputContext outputContext) throws IOException {
        if (exists() && outputContext != null) {
            ExportContext exportCtx = getExportContext(outputContext);
            if ((exportCtx instanceof PYExportContextImpl) && ((PYExportContextImpl) exportCtx).getExportType() != null) {
                if (!((PYIOManager) ioManager).exportContent(exportCtx, this, ((PYExportContextImpl) exportCtx).getExportType())) {
                    throw new IOException("Unexpected Error while spooling resource.");
                }
            } else if (!ioManager.exportContent(exportCtx, this)) {
                throw new IOException("Unexpected Error while spooling resource.");
            }
        }
    }

    /**
     * @see DavResource#getProperty(org.apache.jackrabbit.webdav.property.DavPropertyName)
     */
    public DavProperty getProperty(DavPropertyName name) {
        initProperties();
        return properties.get(name);
    }

    /**
     * @see DavResource#getProperties()
     */
    public DavPropertySet getProperties() {
        initProperties();
        return properties;
    }

    /**
     * @see DavResource#getPropertyNames()
     */
    public DavPropertyName[] getPropertyNames() {
        return getProperties().getPropertyNames();
    }

    /**
     * Fill the set of properties
     */
    protected void initProperties() {
        if (!exists() || inited) {
            return;
        }
        try {
            ioManager.exportContent(new PropertyExportCtx(), this);
        } catch (IOException e) {
        }
        if (getDisplayName() != null) {
            properties.add(new DefaultDavProperty(DavPropertyName.DISPLAYNAME, getDisplayName()));
        }
        if (isCollection()) {
            properties.add(new ResourceType(ResourceType.COLLECTION));
            properties.add(new DefaultDavProperty(DavPropertyName.ISCOLLECTION, "1"));
        } else {
            properties.add(new ResourceType(ResourceType.DEFAULT_RESOURCE));
            properties.add(new DefaultDavProperty(DavPropertyName.ISCOLLECTION, "0"));
        }
        properties.add(new LockDiscovery(getLock(Type.WRITE, Scope.EXCLUSIVE)));
        SupportedLock supportedLock = new SupportedLock();
        supportedLock.addEntry(Type.WRITE, Scope.EXCLUSIVE);
        properties.add(supportedLock);
        try {
            PropertyIterator it = node.getProperties();
            while (it.hasNext()) {
                Property p = it.nextProperty();
                String pName = p.getName();
                PropertyDefinition def = p.getDefinition();
                if (def.isMultiple() || isFilteredItem(p)) {
                    log.debug("Property '" + pName + "' not added to webdav property set (either multivalue or filtered).");
                    continue;
                }
                DavPropertyName name = getDavName(pName, node.getSession());
                String value = p.getValue().getString();
                properties.add(new DefaultDavProperty(name, value, def.isProtected()));
            }
        } catch (RepositoryException e) {
            log.error("Unexpected error while retrieving properties: " + e.getMessage());
        }
        inited = true;
    }

    /**
     * @param property
     * @throws DavException
     * @see DavResource#setProperty(org.apache.jackrabbit.webdav.property.DavProperty)
     */
    public void setProperty(DavProperty property) throws DavException {
        if (isLocked(this)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        try {
            setJcrProperty(property);
            node.save();
        } catch (RepositoryException e) {
            JcrDavException je = new JcrDavException(e);
            try {
                node.refresh(false);
            } catch (RepositoryException re) {
            }
            throw je;
        }
    }

    /**
     * @param propertyName
     * @throws DavException
     * @see DavResource#removeProperty(org.apache.jackrabbit.webdav.property.DavPropertyName)
     */
    public void removeProperty(DavPropertyName propertyName) throws DavException {
        if (isLocked(this)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        try {
            removeJcrProperty(propertyName);
            node.save();
        } catch (RepositoryException e) {
            JcrDavException je = new JcrDavException(e);
            try {
                node.refresh(false);
            } catch (RepositoryException re) {
            }
            throw je;
        }
    }

    /**
     * @see DavResource#alterProperties(org.apache.jackrabbit.webdav.property.DavPropertySet, org.apache.jackrabbit.webdav.property.DavPropertyNameSet)
     */
    public MultiStatusResponse alterProperties(DavPropertySet setProperties, DavPropertyNameSet removePropertyNames) throws DavException {
        if (isLocked(this)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        MultiStatusResponse msr = new MultiStatusResponse(getHref(), null);
        boolean success = true;
        List successList = new ArrayList();
        DavPropertyIterator setIter = setProperties.iterator();
        while (setIter.hasNext()) {
            DavProperty prop = setIter.nextProperty();
            try {
                setJcrProperty(prop);
                successList.add(prop);
            } catch (RepositoryException e) {
                msr.add(prop.getName(), new JcrDavException(e).getErrorCode());
                success = false;
            }
        }
        Iterator remNameIter = removePropertyNames.iterator();
        while (remNameIter.hasNext()) {
            DavPropertyName propName = (DavPropertyName) remNameIter.next();
            try {
                removeJcrProperty(propName);
                successList.add(propName);
            } catch (RepositoryException e) {
                msr.add(propName, new JcrDavException(e).getErrorCode());
                success = false;
            }
        }
        try {
            if (success) {
                node.save();
            } else {
                node.refresh(false);
            }
            Iterator it = successList.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                int status = (success) ? DavServletResponse.SC_OK : DavServletResponse.SC_FAILED_DEPENDENCY;
                if (o instanceof DavProperty) {
                    msr.add(((DavProperty) o).getName(), status);
                } else {
                    msr.add((DavPropertyName) o, status);
                }
            }
            return msr;
        } catch (RepositoryException e) {
            try {
                node.refresh(false);
            } catch (RepositoryException re) {
            }
            throw new JcrDavException(e);
        }
    }

    /**
     * @see DavResource#getCollection()
     */
    public DavResource getCollection() {
        DavResource parent = null;
        if (getResourcePath() != null && !getResourcePath().equals("/")) {
            String parentPath = Text.getRelativeParent(getResourcePath(), 1);
            if (parentPath.equals("")) {
                parentPath = "/";
            }
            DavResourceLocator parentloc = locator.getFactory().createResourceLocator(locator.getPrefix(), locator.getWorkspacePath(), parentPath);
            try {
                parent = factory.createResource(parentloc, session);
            } catch (DavException e) {
            }
        }
        return parent;
    }

    /**
     * @see DavResource#getMembers()
     */
    public DavResourceIterator getMembers() {
        ArrayList list = new ArrayList();
        if (exists() && isCollection()) {
            try {
                NodeIterator it = node.getNodes();
                while (it.hasNext()) {
                    Node n = it.nextNode();
                    if (!isFilteredItem(n)) {
                        DavResourceLocator resourceLocator = locator.getFactory().createResourceLocator(locator.getPrefix(), locator.getWorkspacePath(), n.getPath(), false);
                        DavResource childRes = factory.createResource(resourceLocator, session);
                        list.add(childRes);
                    } else {
                        log.debug("Filtered resource '" + n.getName() + "'.");
                    }
                }
            } catch (RepositoryException e) {
            } catch (DavException e) {
            }
        }
        return new DavResourceIteratorImpl(list);
    }

    /**
     * Adds a new member to this resource.
     *
     * @see DavResource#addMember(DavResource, org.apache.jackrabbit.webdav.io.InputContext)
     */
    public void addMember(DavResource member, InputContext inputContext) throws DavException {
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_CONFLICT);
        }
        if (isLocked(this) || isLocked(member)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (isFilteredResource(member)) {
            log.debug("Avoid creation of filtered resource: " + member.getDisplayName());
            throw new DavException(DavServletResponse.SC_FORBIDDEN);
        }
        try {
            String memberName = Text.getName(member.getLocator().getJcrPath());
            ImportContext ctx = getImportContext(inputContext, memberName);
            if (!ioManager.importContent(ctx, member)) {
                throw new DavException(DavServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            }
            node.save();
        } catch (RepositoryException e) {
            log.error("Error while importing resource: " + e.toString());
            throw new JcrDavException(e);
        } catch (IOException e) {
            log.error("Error while importing resource: " + e.toString());
            throw new DavException(DavServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @see DavResource#removeMember(DavResource)
     */
    public void removeMember(DavResource member) throws DavException {
        if (!exists() || !member.exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        if (isLocked(this) || isLocked(member)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (isFilteredResource(member)) {
            log.debug("Avoid removal of filtered resource: " + member.getDisplayName());
            throw new DavException(DavServletResponse.SC_FORBIDDEN);
        }
        try {
            if (!isJsrLockable()) {
                ActiveLock lock = getLock(Type.WRITE, Scope.EXCLUSIVE);
                if (lock != null) {
                    lockManager.releaseLock(lock.getToken(), member);
                }
            }
            ActiveLock lock = getLock(Type.WRITE, Scope.EXCLUSIVE);
            if (lock != null && lockManager.hasLock(lock.getToken(), member)) {
                lockManager.releaseLock(lock.getToken(), member);
            }
            String itemPath = member.getLocator().getJcrPath();
            Item memItem = session.getRepositorySession().getItem(itemPath);
            memItem.remove();
            session.getRepositorySession().save();
        } catch (RepositoryException e) {
            throw new JcrDavException(e);
        }
    }

    /**
     * @see DavResource#move(DavResource)
     */
    public void move(DavResource destination) throws DavException {
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        if (isLocked(this)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (isFilteredResource(destination)) {
            throw new DavException(DavServletResponse.SC_FORBIDDEN);
        }
        try {
            String destItemPath = destination.getLocator().getJcrPath();
            session.getRepositorySession().getWorkspace().move(locator.getJcrPath(), destItemPath);
        } catch (RepositoryException e) {
            throw new JcrDavException(e);
        }
    }

    /**
     * @see DavResource#copy(DavResource, boolean)
     */
    public void copy(DavResource destination, boolean shallow) throws DavException {
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        if (isLocked(destination)) {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
        if (isFilteredResource(destination)) {
            throw new DavException(DavServletResponse.SC_FORBIDDEN);
        }
        if (shallow) {
            throw new DavException(DavServletResponse.SC_FORBIDDEN, "Unable to perform shallow copy.");
        }
        try {
            String destItemPath = destination.getLocator().getJcrPath();
            session.getRepositorySession().getWorkspace().copy(locator.getJcrPath(), destItemPath);
        } catch (PathNotFoundException e) {
            throw new DavException(DavServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RepositoryException e) {
            throw new JcrDavException(e);
        }
    }

    /**
     * @param type
     * @param scope
     * @return true if type is {@link Type#WRITE} and scope is {@link Scope#EXCLUSIVE}
     * @see DavResource#isLockable(org.apache.jackrabbit.webdav.lock.Type, org.apache.jackrabbit.webdav.lock.Scope)
     */
    public boolean isLockable(Type type, Scope scope) {
        return Type.WRITE.equals(type) && Scope.EXCLUSIVE.equals(scope);
    }

    /**
     * @see DavResource#hasLock(org.apache.jackrabbit.webdav.lock.Type, org.apache.jackrabbit.webdav.lock.Scope)
     */
    public boolean hasLock(Type type, Scope scope) {
        return getLock(type, scope) != null;
    }

    /**
     * @see DavResource#getLock(Type, Scope)
     */
    public ActiveLock getLock(Type type, Scope scope) {
        ActiveLock lock = null;
        if (exists() && Type.WRITE.equals(type) && Scope.EXCLUSIVE.equals(scope)) {
            try {
                if (node.isLocked()) {
                    Lock jcrLock = node.getLock();
                    if (jcrLock != null && jcrLock.isLive()) {
                        lock = new JcrActiveLock(jcrLock);
                    }
                }
            } catch (RepositoryException e) {
            }
            if (lock == null) {
                lock = lockManager.getLock(type, scope, this);
            }
        }
        return lock;
    }

    /**
     * @see org.apache.jackrabbit.webdav.DavResource#getLocks()
     */
    public ActiveLock[] getLocks() {
        ActiveLock writeLock = getLock(Type.WRITE, Scope.EXCLUSIVE);
        return (writeLock != null) ? new ActiveLock[] { writeLock } : new ActiveLock[0];
    }

    /**
     * @see DavResource#lock(LockInfo)
     */
    public ActiveLock lock(LockInfo lockInfo) throws DavException {
        ActiveLock lock = null;
        if (isLockable(lockInfo.getType(), lockInfo.getScope())) {
            if (isJsrLockable()) {
                try {
                    Lock jcrLock = node.lock(lockInfo.isDeep(), false);
                    if (jcrLock != null) {
                        lock = new JcrActiveLock(jcrLock);
                    }
                } catch (RepositoryException e) {
                    throw new JcrDavException(e);
                }
            } else {
                lock = lockManager.createLock(lockInfo, this);
            }
        } else {
            throw new DavException(DavServletResponse.SC_PRECONDITION_FAILED, "Unsupported lock type or scope.");
        }
        return lock;
    }

    /**
     * @see DavResource#refreshLock(LockInfo, String)
     */
    public ActiveLock refreshLock(LockInfo lockInfo, String lockToken) throws DavException {
        if (!exists()) {
            throw new DavException(DavServletResponse.SC_NOT_FOUND);
        }
        ActiveLock lock = getLock(lockInfo.getType(), lockInfo.getScope());
        if (lock == null) {
            throw new DavException(DavServletResponse.SC_PRECONDITION_FAILED, "No lock with the given type/scope present on resource " + getResourcePath());
        }
        if (lock instanceof JcrActiveLock) {
            try {
                node.getLock().refresh();
            } catch (RepositoryException e) {
                throw new JcrDavException(e);
            }
        } else {
            lock = lockManager.refreshLock(lockInfo, lockToken, this);
        }
        return lock;
    }

    /**
     * @see DavResource#unlock(String)
     */
    public void unlock(String lockToken) throws DavException {
        ActiveLock lock = getLock(Type.WRITE, Scope.EXCLUSIVE);
        if (lock == null) {
            throw new DavException(DavServletResponse.SC_PRECONDITION_FAILED);
        } else if (lock.isLockedByToken(lockToken)) {
            if (lock instanceof JcrActiveLock) {
                try {
                    node.unlock();
                } catch (RepositoryException e) {
                    throw new JcrDavException(e);
                }
            } else {
                lockManager.releaseLock(lockToken, this);
            }
        } else {
            throw new DavException(DavServletResponse.SC_LOCKED);
        }
    }

    /**
     * @see DavResource#addLockManager(org.apache.jackrabbit.webdav.lock.LockManager)
     */
    public void addLockManager(LockManager lockMgr) {
        this.lockManager = lockMgr;
    }

    /**
     * @see org.apache.jackrabbit.webdav.DavResource#getFactory()
     */
    public DavResourceFactory getFactory() {
        return factory;
    }

    /**
     * Returns the node that is wrapped by this resource.
     *
     * @return the node
     */
    protected Node getNode() {
        return node;
    }

    /**
     * Returns a new <code>ImportContext</code>
     *
     * @param inputCtx
     * @param systemId
     * @return
     * @throws IOException
     */
    protected ImportContext getImportContext(InputContext inputCtx, String systemId) throws IOException {
        return new ImportContextImpl(node, systemId, inputCtx);
    }

    /**
     * Returns true, if the underlying node is nodetype jcr:lockable,
     * without checking its current lock status. If the node is not jcr-lockable
     * an attempt is made to add the mix:lockable mixin type.
     *
     * @return true if this resource is lockable.
     */
    private boolean isJsrLockable() {
        boolean lockable = false;
        if (exists()) {
            try {
                lockable = node.isNodeType(MIX_LOCKABLE);
                if (!lockable && node.canAddMixin(MIX_LOCKABLE)) {
                    node.addMixin(MIX_LOCKABLE);
                    node.save();
                    lockable = true;
                }
            } catch (RepositoryException e) {
            }
        }
        return lockable;
    }

    /**
     * Return true if this resource cannot be modified due to a write lock
     * that is not owned by the given session.
     *
     * @return true if this resource cannot be modified due to a write lock
     */
    private boolean isLocked(DavResource res) {
        ActiveLock lock = res.getLock(Type.WRITE, Scope.EXCLUSIVE);
        if (lock == null) {
            return false;
        } else {
            String[] sLockTokens = session.getLockTokens();
            for (int i = 0; i < sLockTokens.length; i++) {
                if (sLockTokens[i].equals(lock.getToken())) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Builds a webdav property name from the given jcrName. In case the jcrName
     * contains a namespace prefix that would conflict with any of the predefined
     * webdav namespaces a new prefix is assigned.<br>
     * Please note, that the local part of the jcrName is checked for XML
     * compatibility by calling {@link ISO9075#encode(String)}
     *
     * @param jcrName
     * @param session
     * @return a <code>DavPropertyName</code> for the given jcr name.
     */
    private DavPropertyName getDavName(String jcrName, Session session) throws RepositoryException {
        String localName = ISO9075.encode(Text.getLocalName(jcrName));
        String prefix = Text.getNamespacePrefix(jcrName);
        String uri = session.getNamespaceURI(prefix);
        if (reservedNamespaces.containsKey(prefix) && !reservedNamespaces.get(prefix).equals(uri)) {
            prefix = prefix + "0";
        }
        Namespace namespace = Namespace.getNamespace(prefix, uri);
        DavPropertyName name = DavPropertyName.create(localName, namespace);
        return name;
    }

    /**
     * Build jcr property name from dav property name. If the property name
     * defines a namespace uri, that has not been registered yet, an attempt
     * is made to register the uri with the prefix defined. Note, that no
     * extra effort is made to generated a unique prefix.
     *
     * @param propName
     * @return jcr name
     * @throws RepositoryException
     */
    private String getJcrName(DavPropertyName propName) throws RepositoryException {
        String pName = ISO9075.decode(propName.getName());
        String uri = propName.getNamespace().getURI();
        if (uri != null && !"".equals(uri)) {
            Session s = session.getRepositorySession();
            String prefix;
            try {
                prefix = s.getNamespacePrefix(uri);
            } catch (NamespaceException e) {
                NamespaceRegistry nsReg = s.getWorkspace().getNamespaceRegistry();
                prefix = propName.getNamespace().getPrefix();
                if (prefix == null || "".equals(prefix)) {
                    prefix = "_pre" + nsReg.getPrefixes().length + 1;
                }
                nsReg.registerNamespace(prefix, uri);
            }
            if (prefix != null && !"".equals(prefix)) {
                pName = prefix + ":" + pName;
            }
        }
        return pName;
    }

    /**
     * @param property
     * @throws RepositoryException
     */
    private void setJcrProperty(DavProperty property) throws RepositoryException {
        String value = property.getValue().toString();
        node.setProperty(getJcrName(property.getName()), value);
    }

    /**
     * @param propertyName
     * @throws RepositoryException
     */
    private void removeJcrProperty(DavPropertyName propertyName) throws RepositoryException {
        String jcrName = getJcrName(propertyName);
        if (node.hasProperty(jcrName)) {
            node.getProperty(jcrName).remove();
        }
    }

    private boolean isFilteredResource(DavResource resource) {
        return filter != null && filter.isFilteredItem(resource.getDisplayName(), session.getRepositorySession());
    }

    private boolean isFilteredItem(Item item) {
        return filter != null && filter.isFilteredItem(item);
    }

    protected ExportContext getExportContext(OutputContext outputCtx) throws IOException {
        return new PYExportContextImpl(getNode(), outputCtx);
    }

    /**
     * Retrusn the file type for the node.
     * @return File Type
     */
    public String getFileType() throws RepositoryException {
        String filename = node.getName();
        String ext = Text.getName(filename, '.');
        if (ext.equals("")) {
            ext = filename;
        }
        return ext.toLowerCase();
    }

    /**
     * ExportContext that writes the properties of this <code>DavResource</code>
     * and provides not stream.
     */
    private class PropertyExportCtx extends AbstractExportContext {

        private PropertyExportCtx() {
            super(node, false, null);
            setCreationTime(IOUtil.UNDEFINED_TIME);
            setModificationTime(IOUtil.UNDEFINED_TIME);
        }

        public OutputStream getOutputStream() {
            return null;
        }

        public void setContentLanguage(String contentLanguage) {
            if (contentLanguage != null) {
                properties.add(new DefaultDavProperty(DavPropertyName.GETCONTENTLANGUAGE, contentLanguage));
            }
        }

        public void setContentLength(long contentLength) {
            if (contentLength > IOUtil.UNDEFINED_LENGTH) {
                properties.add(new DefaultDavProperty(DavPropertyName.GETCONTENTLENGTH, contentLength + ""));
            }
        }

        public void setContentType(String mimeType, String encoding) {
            String contentType = IOUtil.buildContentType(mimeType, encoding);
            if (contentType != null) {
                properties.add(new DefaultDavProperty(DavPropertyName.GETCONTENTTYPE, contentType));
            }
        }

        public void setCreationTime(long creationTime) {
            String created = IOUtil.getCreated(creationTime);
            properties.add(new DefaultDavProperty(DavPropertyName.CREATIONDATE, created));
        }

        public void setModificationTime(long modTime) {
            if (modTime <= IOUtil.UNDEFINED_TIME) {
                modificationTime = new Date().getTime();
            } else {
                modificationTime = modTime;
            }
            String lastModified = IOUtil.getLastModified(modificationTime);
            properties.add(new DefaultDavProperty(DavPropertyName.GETLASTMODIFIED, lastModified));
        }

        public void setETag(String etag) {
            if (etag != null) {
                properties.add(new DefaultDavProperty(DavPropertyName.GETETAG, etag));
            }
        }

        public void setProperty(Object propertyName, Object propertyValue) {
            if (propertyName instanceof DavPropertyName) {
                DavPropertyName pName = (DavPropertyName) propertyName;
                properties.add(new DefaultDavProperty(pName, propertyValue));
            }
        }
    }
}
