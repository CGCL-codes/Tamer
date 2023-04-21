package org.torweg.pulse.vfs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.accesscontrol.Role;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.configuration.ConfigurationException;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.invocation.lifecycle.LifecycleException;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.util.HibernateDataSource;
import org.torweg.pulse.webdav.util.DeadProperty;

/**
 * the {@code VirtualFileSystem} is a singleton providing access to
 * {@code VirtualFile}s.
 * <p>
 * The {@code VirtualFileSystem} actually uses {@link VFSProvider}s to access
 * the individual {@link VirtualFile}s. The {@code VirtualFileSystem} consists
 * of two stores: a private and a public store. {@code VirtualFile}s stored in
 * the public store will also be available via HTTP. Moreover, each store may
 * have a different {@code VFSProvider}.
 * </p>
 * <p>
 * A {@code VirtualFile} is identified via its {@code URI}. {@code URI}s for
 * {@code VirtualFile}s must contain a <em>scheme</em> and a <em>path</em>. The
 * scheme may either be {@code public} for public files, or {@code private} for
 * private files. The {@code URI}s must not contain authority, host or port
 * information as well as fragments or queries.
 * </p>
 * <p>
 * Example for a {@code URI} identifying a private file <tt>file.txt</tt> in a
 * top level directory <tt>directory</tt>:<br/>
 * <tt>private:///directory/file.txt</tt>
 * </p>
 * <p>
 * Example for a {@code URI} identifying a public file <tt>something.txt</tt> in
 * the root directory:<br/>
 * <tt>public:///something.txt</tt>
 * </p>
 * 
 * @author Thomas Weber
 * @version $Revision: 2072 $
 */
public final class VirtualFileSystem {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 8653361606165674231L;

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualFileSystem.class);

    /**
	 * the singleton.
	 */
    private static VirtualFileSystem instance;

    /**
	 * the {@code VFSProvider} for the public store.
	 */
    private VFSProvider publicProvider;

    /**
	 * the {@code VFSProvider} for the private store.
	 */
    private VFSProvider privateProvider;

    /**
	 * private constructor.
	 */
    private VirtualFileSystem() {
        super();
    }

    /**
	 * initialises the root directories of each realm.
	 */
    private static void initRootDirectories() {
        HibernateDataSource datasource = Lifecycle.getHibernateDataSource();
        User superuser = User.getSuperUser(datasource);
        ArrayList<VirtualFile> missingRootDirs = new ArrayList<VirtualFile>();
        try {
            VirtualFile f = instance.getVirtualFile("private:///", superuser);
            if (!f.exists()) {
                LOGGER.debug("Initialising root directory " + "for the private provider.");
                f.setDirectory(true);
                missingRootDirs.add(f);
            }
            f = instance.getVirtualFile("public:///", superuser);
            if (!f.exists()) {
                LOGGER.debug("Initialising root directory " + "for the public provider.");
                f.setDirectory(true);
                missingRootDirs.add(f);
            }
            if (!missingRootDirs.isEmpty()) {
                Session s = datasource.createNewSession();
                Transaction tx = s.beginTransaction();
                try {
                    for (VirtualFile root : missingRootDirs) {
                        s.save(root);
                    }
                    tx.commit();
                } finally {
                    s.close();
                }
            }
        } catch (Exception e) {
            throw new PulseException("Unexpected error initialising the virtual file system.", e);
        }
    }

    /**
	 * internally used by {@link VirtualFileSystem#getVirtualFile(URI, User)} to
	 * give access to a file that does not exist already.
	 * 
	 * @param path
	 *            the path locating the {@code VirtualFile}
	 * @param user
	 *            the {@code User} to be used for access checks
	 * @throws IOException
	 *             on errors accessing the {@code VirtualFile}
	 * @return a {@code VirtualFile} for the given path.
	 * 
	 */
    private VirtualFile getNewVirtualFile(final URI path, final User user) throws IOException {
        VirtualFile file = new VirtualFile(user, path);
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            VirtualFile parent = null;
            if (!path.getPath().equals("/")) {
                try {
                    String parentPath = path.toString().substring(0, path.toString().lastIndexOf("/"));
                    if (parentPath.endsWith("://")) {
                        parentPath = new StringBuilder(parentPath).append('/').toString();
                    }
                    parent = loadVirtualFile(new URI(parentPath), s);
                } catch (URISyntaxException e) {
                    LOGGER.debug("{} while trying to find parent.", e.getLocalizedMessage());
                }
            }
            if ((parent != null) && parent.canRead(user) && parent.canWrite(user)) {
                if (parent.isFile()) {
                    throw new IOException("Parent is not a directory.");
                }
                file.setParent(parent);
                s.saveOrUpdate(parent);
                inheritParentRoles(file, parent);
                s.saveOrUpdate(file);
            }
            tx.commit();
        } catch (LifecycleException e) {
            tx.rollback();
            throw new VFIOException(e);
        } finally {
            s.close();
        }
        return file;
    }

    /**
	 * utility method to inherit the roles of the parent.
	 * 
	 * @param roleReceiver
	 *            the {@code VirtualFile} to receive the {@code Role}s
	 * @param roleProvider
	 *            the {@code VirtualFile} which provides the {@code Role}s
	 */
    private void inheritParentRoles(final VirtualFile roleReceiver, final VirtualFile roleProvider) {
        for (Role r : roleProvider.getReadRoles()) {
            roleReceiver.addReadRole(r);
        }
        for (Role r : roleProvider.getWriteRoles()) {
            roleReceiver.addWriteRole(r);
        }
    }

    /**
	 * tokenizes a given URI.
	 * 
	 * @param uri
	 *            the URI to tokenize
	 * @return the tokens
	 */
    private List<String> tokenizeURI(final URI uri) {
        StringTokenizer pe = new StringTokenizer(uri.getPath(), "/");
        List<String> pathElements = new ArrayList<String>(pe.countTokens());
        while (pe.hasMoreTokens()) {
            try {
                pathElements.add(URLDecoder.decode(pe.nextToken(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                pathElements.add(pe.nextToken());
            }
        }
        return pathElements;
    }

    /**
	 * tries to load a {@code VirtualFile} for the given path.
	 * <p>
	 * Utility method for an atomic load.
	 * </p>
	 * 
	 * @param path
	 *            the path
	 * @return the {@code VirtualFile} or {@code null}, if no such file exists.
	 * @throws IOException
	 *             on errors loading the file
	 */
    protected VirtualFile loadVirtualFile(final URI path) throws IOException {
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            VirtualFile file = loadVirtualFile(path, s);
            tx.commit();
            return file;
        } catch (LifecycleException e) {
            tx.rollback();
            throw new VFIOException(e);
        } finally {
            s.close();
        }
    }

    /**
	 * tries to load a {@code VirtualFile} for the given path.
	 * 
	 * @param path
	 *            the path
	 * @param s
	 *            the session with current transaction
	 * @return the {@code VirtualFile} or {@code null}, if no such file exists.
	 * @throws IOException
	 *             on errors loading the file
	 */
    public VirtualFile loadVirtualFile(final URI path, final Session s) throws IOException {
        boolean isPublic;
        String pathName;
        if (path.getScheme().equals("private")) {
            isPublic = false;
            pathName = "private:///";
        } else {
            isPublic = true;
            pathName = "public:///";
        }
        List<String> pathElements = tokenizeURI(path);
        VirtualFile file = (VirtualFile) s.createCriteria(VirtualFile.class).add(Restrictions.and(Restrictions.and(Restrictions.isNull("parent"), Restrictions.eq("isPublic", isPublic)), Restrictions.eq("path", pathName))).uniqueResult();
        for (String name : pathElements) {
            boolean found = false;
            for (VirtualFile child : file.getChildren()) {
                if (child.getName().equals(name)) {
                    file = child;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return null;
            }
        }
        if (file != null) {
            file.getReadRoles().size();
            file.getWriteRoles().size();
        }
        return file;
    }

    /**
	 * returns the private provider.
	 * 
	 * @return the private provider
	 */
    protected VFSProvider getPrivateProvider() {
        return this.privateProvider;
    }

    /**
	 * @param p
	 *            the private provider to set
	 */
    protected void setPrivateProvider(final VFSProvider p) {
        this.privateProvider = p;
    }

    /**
	 * returns the public provider.
	 * 
	 * @return the public provider
	 */
    protected VFSProvider getPublicProvider() {
        return this.publicProvider;
    }

    /**
	 * @param p
	 *            the public provider to set
	 */
    protected void setPublicProvider(final VFSProvider p) {
        this.publicProvider = p;
    }

    /**
	 * returns the {@code VFSProvider} for the given {@code URI}.
	 * 
	 * @param uri
	 *            the URI
	 * @return the provider or {@code null}, if no such provider exists
	 */
    protected VFSProvider getProvider(final URI uri) {
        if (uri.getScheme().equals("private")) {
            return this.privateProvider;
        } else if (uri.getScheme().equals("public")) {
            return this.publicProvider;
        }
        return null;
    }

    /**
	 * returns the {@code VFSProvider} for the given {@code VirtualFile}.
	 * 
	 * @param f
	 *            the file
	 * @return the provider
	 */
    public VFSProvider getProvider(final VirtualFile f) {
        return getProvider(f.getURI());
    }

    /**
	 * updates the modification tokens for the file.
	 * 
	 * @param file
	 *            the file
	 * @param user
	 *            the user
	 */
    protected void markModified(final VirtualFile file, final User user) {
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            file.setLastModified(System.currentTimeMillis());
            file.setLastModifier(user);
            updateFileSize(file);
            s.saveOrUpdate(file);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
    }

    /**
	 * distributes the {@code VirtualFile}.
	 * 
	 * @param file
	 *            the {@code VirtualFile} to be distributed
	 * @param user
	 *            the user who modified the file
	 * @return {@code true} if the distribution has been successful; otherwise
	 *         {@code false}
	 */
    protected boolean distribute(final VirtualFile file, final User user) {
        boolean success;
        if (file.isPublic()) {
            success = this.publicProvider.distribute(file);
        } else {
            success = this.privateProvider.distribute(file);
        }
        if (success) {
            markModified(file, user);
        }
        return success;
    }

    /**
	 * indexes the given {@code VirtualFile} for full text search.
	 * <p>
	 * This method is called upon {@link VFOutputStream#close()} to start the
	 * indexing.
	 * </p>
	 * <p>
	 * TODO: has to be implemented yet
	 * </P>
	 * 
	 * @param file
	 *            the file to be indexed
	 */
    protected void index(final VirtualFile file) {
        LOGGER.debug("TODO index: " + file.toString());
    }

    /**
	 * provides an {@code InputStream} to read the {@code VirtualFile} .
	 * 
	 * @param file
	 *            the {@code VirtualFile} to be read from the
	 *            {@code InputStream}
	 * @return an {@code InputStream} to read from the {@code VirtualFile}
	 * @throws VFIOException
	 *             on errors aquiring the {@code InputStream}
	 */
    public VFInputStream getInputStream(final VirtualFile file) throws VFIOException {
        try {
            if (file.isPublic()) {
                return new VFInputStream(this.publicProvider.getInputStream(file));
            } else {
                return new VFInputStream(this.privateProvider.getInputStream(file));
            }
        } catch (IOException e) {
            throw new VFIOException(e);
        }
    }

    /**
	 * provides an {@code OutputStream} to write the {@code VirtualFile}.
	 * 
	 * @param file
	 *            the {@code VirtualFile} to be written to by the
	 *            {@code OutputStream}
	 * @param user
	 *            the user used to access the file
	 * @return an {@code OutputStream} to read from the {@code VirtualFile}
	 * @throws VFIOException
	 *             on errors aquiring the {@code OutputStream}
	 */
    public VFOutputStream getOutputStream(final VirtualFile file, final User user) throws VFIOException {
        if (!file.canWrite(user) || !file.canRead(user)) {
            throw new InsufficientPermissionsException("Insufficient permissions to write to " + file.getURI());
        }
        try {
            if (file.isPublic()) {
                return new VFOutputStream(this.publicProvider.getOutputStream(file), file, user);
            } else {
                return new VFOutputStream(this.privateProvider.getOutputStream(file), file, user);
            }
        } catch (IOException e) {
            throw new VFIOException(e);
        }
    }

    /**
	 * updates the file size for the given file
	 * <em>but does not save the changes in the database</em>.
	 * 
	 * @param file
	 *            the file
	 */
    public void updateFileSize(final VirtualFile file) {
        if (file.isPublic()) {
            file.setFilesize(this.publicProvider.getFileSize(file));
        } else {
            file.setFilesize(this.privateProvider.getFileSize(file));
        }
    }

    /**
	 * deletes the given {@code VirtualFile} and purges it from the
	 * {@code VirtualFileSystem}s full text search index.
	 * 
	 * @param file
	 *            the {@code VirtualFile} to be deleted
	 * @return {@code true}, if successful. Otherwise {@code false}.
	 */
    public boolean delete(final VirtualFile file) {
        FullTextSession s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            VirtualFile vfile = (VirtualFile) s.get(VirtualFile.class, file.getId());
            VirtualFile parent = vfile.getParent();
            if (parent != null) {
                parent.removeChild(vfile);
                s.saveOrUpdate(parent);
            }
            s.delete(vfile);
            tx.commit();
            boolean deleted = false;
            if (vfile.isPublic()) {
                deleted = this.publicProvider.delete(vfile);
            } else {
                deleted = this.privateProvider.delete(vfile);
            }
            if (!deleted) {
                tx = s.beginTransaction();
                s.saveOrUpdate(new VFSDeferredDelete(vfile));
                tx.commit();
            }
            return deleted;
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
    }

    /**
	 * provides access to the {@code VirtualFileSystem}.
	 * 
	 * @return the {@code VirtualFileSystem} singleton
	 */
    public static VirtualFileSystem getInstance() {
        return VirtualFileSystem.instance;
    }

    /**
	 * returns a {@code VirtualFile} for the given path.
	 * 
	 * @param path
	 *            the path locating the {@code VirtualFile}
	 * @param user
	 *            the {@code User} to be used for access checks
	 * @throws IOException
	 *             on errors accessing the {@code VirtualFile}
	 * @throws URISyntaxException
	 *             on errors parsing the URI
	 * @return a {@code VirtualFile} for the given path.
	 */
    public VirtualFile getVirtualFile(final String path, final User user) throws IOException, URISyntaxException {
        return getVirtualFile(new URI(path), user);
    }

    /**
	 * returns a {@code VirtualFile} for the given path.
	 * 
	 * @param uri
	 *            the path locating the {@code VirtualFile}
	 * @param user
	 *            the {@code User} to be used for access checks
	 * @throws IOException
	 *             on errors accessing the {@code VirtualFile}
	 * @return a {@code VirtualFile} for the given path.
	 */
    public VirtualFile getVirtualFile(final URI uri, final User user) throws IOException {
        VirtualFile f = loadVirtualFile(uri);
        if (f != null) {
            return f;
        }
        return getNewVirtualFile(uri, user);
    }

    /**
	 * returns prototypes of all known dead properties.
	 * 
	 * @return prototypes of all known dead properties
	 */
    public Collection<DeadProperty> getAllDeadProperties() {
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            Collection<DeadProperty> props = getAllDeadProperties(s);
            tx.commit();
            return props;
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
    }

    /**
	 * returns prototypes of all known dead properties using the given session.
	 * <p>
	 * It is assumed that the transaction is controlled by the caller.
	 * </p>
	 * 
	 * @param s
	 *            the Hibernate<sup>TM</sup> session
	 * @return prototypes of all known dead properties
	 */
    public Collection<DeadProperty> getAllDeadProperties(final Session s) {
        @SuppressWarnings("unchecked") List<Object[]> props = (List<Object[]>) s.createCriteria(DeadProperty.class, "dp").setProjection(Projections.projectionList().add(Projections.groupProperty("dp.namespaceURI")).add(Projections.groupProperty("dp.propName"))).setCacheable(true).list();
        Set<DeadProperty> prototypes = new HashSet<DeadProperty>();
        for (Object[] propertyDefinition : props) {
            prototypes.add(new DeadProperty(propertyDefinition[1].toString(), propertyDefinition[0].toString()));
        }
        return prototypes;
    }

    /**
	 * resolves the HTTP path to the given {@code VirtualFile}.
	 * 
	 * @param file
	 *            the {@code VirtualFile} to be resolved
	 * @return the {@code URI} to the {@code VirtualFile} or {@code null}, if
	 *         the {@code VirtualFile} is private
	 */
    public URI getHttpURI(final VirtualFile file) {
        if (file.isPublic()) {
            return this.publicProvider.resolveHTTPPath(file);
        }
        return this.privateProvider.resolveHTTPPath(file);
    }

    /**
	 * checks if the given HTTP {@code URI} denotes a {@code VirtualFile}.
	 * 
	 * @param uri
	 *            the HTTP uri
	 * @return the {@code VirtualFile} or {@code null}, if the {@code URI} does
	 *         not denote a {@code VirtualFile}
	 */
    public VirtualFile fromHttpURI(final URI uri) {
        if (uri == null) {
            return null;
        }
        String uriPath = uri.getPath();
        if (uriPath == null) {
            return null;
        }
        if ((this.publicProvider.getHttpBasePath() != null) && (uriPath.startsWith(this.publicProvider.getHttpBasePathString()))) {
            URI relativeURI = this.publicProvider.getHttpBasePath().relativize(uri);
            try {
                return loadVirtualFile(new URI("public:///" + relativeURI));
            } catch (Exception e) {
                LOGGER.warn("Could not get VirtualFile from HTTP-URI:" + e.getLocalizedMessage(), e);
            }
        }
        if ((this.privateProvider.getHttpBasePath() != null) && (uriPath.startsWith(this.privateProvider.getHttpBasePathString()))) {
            URI relativeURI = this.privateProvider.getHttpBasePath().relativize(uri);
            try {
                return loadVirtualFile(new URI("private:///" + relativeURI));
            } catch (Exception e) {
                LOGGER.warn("Could not get VirtualFile from HTTP-URI:" + e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    /**
	 * returns the size of the {@code VirtualFile} identified by the given
	 * {@code URI}.
	 * 
	 * @param uri
	 *            the uri
	 * @param user
	 *            the user
	 * @return the size
	 * @throws IOException
	 *             on errors
	 */
    public long getFileSize(final URI uri, final User user) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null && file.canRead(user)) {
            file.getFilesize();
        }
        return 0;
    }

    /**
	 * returns the creation date of the {@code VirtualFile} identified by the
	 * given {@code URI}.
	 * 
	 * @param uri
	 *            the uri
	 * @param user
	 *            the user
	 * @return the creation date
	 * @throws IOException
	 *             on errors
	 */
    public long getCreated(final URI uri, final User user) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null && file.canRead(user)) {
            return file.getCreated();
        }
        return 0;
    }

    /**
	 * returns the last modification date of the {@code VirtualFile} identified
	 * by the given {@code URI}.
	 * 
	 * @param uri
	 *            the uri
	 * @param user
	 *            the user
	 * @return the last modification date
	 * @throws IOException
	 *             on errors
	 */
    public long getLastModified(final URI uri, final User user) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null && file.canRead(user)) {
            return file.lastModified();
        }
        return 0;
    }

    /**
	 * returns whether the {@code URI} identifies a collection.
	 * 
	 * @param uri
	 *            the URI
	 * @return {@code true}, if and only if the URI identifies a collection.
	 *         Otherwise {@code false}.
	 * @throws IOException
	 *             on errors
	 */
    public boolean isCollection(final URI uri) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null) {
            return file.isDirectory();
        }
        return false;
    }

    /**
	 * returns whether the {@code URI} identifies a resource.
	 * 
	 * @param uri
	 *            the URI
	 * @return {@code true}, if and only if the URI identifies a resource.
	 *         Otherwise {@code false}.
	 * @throws IOException
	 *             on errors
	 */
    public boolean isResource(final URI uri) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null) {
            return file.isFile();
        }
        return false;
    }

    /**
	 * returns whether the {@code URI} identifies an existing object.
	 * 
	 * @param uri
	 *            the URI
	 * @return {@code true}, if and only if the URI identifies an existing
	 *         object. Otherwise {@code false}.
	 * @throws IOException
	 *             on errors
	 */
    public boolean exists(final URI uri) throws IOException {
        VirtualFile file = loadVirtualFile(uri);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    /**
	 * creates a new directory.
	 * 
	 * @param dir
	 *            the directory to be created
	 * @param user
	 *            the user who creates the directory
	 * @return {@code true}, if and only if the directory has been created
	 * @throws VFIOException
	 *             on errors creating the directory
	 */
    public boolean mkdir(final VirtualFile dir, final User user) throws VFIOException {
        VFSProvider provider;
        if (dir.isPublic()) {
            provider = this.publicProvider;
        } else {
            provider = this.privateProvider;
        }
        if (dir.getParent() == null) {
            throw new VFIOException("The new directory must have an existing parent.");
        }
        boolean success = provider.mkdir(dir);
        dir.setDirectory(true);
        if (success) {
            markModified(dir, user);
        }
        return success;
    }

    /**
	 * return the public HTTP base URI.
	 * 
	 * @return the public HTTP base URI
	 */
    public String getPublicHTTPBase() {
        return this.publicProvider.getHttpBasePathString();
    }

    /**
	 * return the private HTTP base URI.
	 * 
	 * @return the private HTTP base URI
	 */
    public String getPrivateHTTPBase() {
        return this.privateProvider.getHttpBasePathString();
    }

    /**
	 * initialises and configures the {@code VirtualFileSystem}.
	 * 
	 * @param conf
	 *            the configuration element
	 * @return the configured singleton
	 */
    public static synchronized VirtualFileSystem init(final Element conf) {
        if (instance == null) {
            instance = new VirtualFileSystem();
            for (Object providerConfObj : conf.getChildren("provider")) {
                Element providerConfig = (Element) providerConfObj;
                try {
                    if (providerConfig.getAttributeValue("realm").equals("public")) {
                        LOGGER.debug("initialising public VFSProvider...");
                        VFSProvider provider = (VFSProvider) Class.forName(providerConfig.getAttributeValue("class")).newInstance();
                        provider.init(providerConfig);
                        instance.setPublicProvider(provider);
                    } else if (providerConfig.getAttributeValue("realm").equals("private")) {
                        LOGGER.debug("initialising private VFSProvider...");
                        VFSProvider provider = (VFSProvider) Class.forName(providerConfig.getAttributeValue("class")).newInstance();
                        provider.init(providerConfig);
                        instance.setPrivateProvider(provider);
                    }
                } catch (Exception e) {
                    throw new ConfigurationException("Error while setting up the virtual file system: " + e.getLocalizedMessage(), e);
                }
            }
            initRootDirectories();
        }
        return instance;
    }

    /**
	 * a deferred delete for the {@code VirtualFileSystem}.
	 */
    @Entity(name = "VFSDeferredDelete")
    static final class VFSDeferredDelete {

        /**
		 * the id of the deferred delete.
		 */
        @Id
        @Basic
        private final long id;

        /**
		 * the creation timestamp.
		 */
        @Basic
        private final long timestamp = new Date().getTime();

        /**
		 * the VFS URI.
		 */
        @Lob
        @Column(length = 131072, nullable = false)
        private final String uri;

        /**
		 * used by Hibernate<sup>TM</sup>.
		 */
        @Deprecated
        protected VFSDeferredDelete() {
            super();
            this.id = -1;
            this.uri = null;
        }

        /**
		 * creates a new {@code VFSDeferredDelete} for the given URI.
		 * 
		 * @param f
		 *            the {@code VirtualFile} for the deferred deletion
		 */
        public VFSDeferredDelete(final VirtualFile f) {
            super();
            this.id = f.getId();
            this.uri = f.getURI().toString();
        }

        /**
		 * @return Returns the id.
		 */
        public long getId() {
            return this.id;
        }

        /**
		 * @return Returns the timestamp.
		 */
        public long getTimestamp() {
            return this.timestamp;
        }

        /**
		 * returns the URI as a string.
		 * 
		 * @return the URI
		 */
        public String getURI() {
            return this.uri;
        }

        /**
		 * tries to delete the file using the corresponding {@code VFSProvider}.
		 * 
		 * @return {@code true}, if the {@code VFSDeferredDelete} can be removed
		 *         from the queue. Otherwise {@code false}.
		 */
        public boolean delete() {
            try {
                URI realURI = new URI(this.uri);
                if (VirtualFileSystem.getInstance().exists(realURI)) {
                    LOGGER.info("Skipping deferred delete at '{}' because a " + "VirtualFile exists for this location.", this.uri);
                    return true;
                }
                return VirtualFileSystem.getInstance().getProvider(realURI).delete(realURI);
            } catch (URISyntaxException e) {
                LOGGER.warn("Cannot execute deferred delete for URI {}: {}", this.uri, e.getLocalizedMessage());
                return true;
            } catch (IOException e) {
                LOGGER.error("Cannot execute deferred delete for URI {}: {}", this.uri, e.getLocalizedMessage());
                return true;
            }
        }

        /**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#hashCode()
		 */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (this.id ^ (this.id >>> 32));
            result = prime * result + ((this.uri == null) ? 0 : this.uri.hashCode());
            return result;
        }

        /**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            VFSDeferredDelete other = (VFSDeferredDelete) obj;
            if (this.id != other.id) {
                return false;
            }
            if (this.uri == null) {
                if (other.uri != null) {
                    return false;
                }
            } else if (!this.uri.equals(other.uri)) {
                return false;
            }
            return true;
        }
    }
}
