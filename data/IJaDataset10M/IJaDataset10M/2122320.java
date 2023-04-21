package net.sourceforge.blogentis.slide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.storage.AbstractFileResource;
import net.sourceforge.blogentis.storage.FileResourceFilter;
import net.sourceforge.blogentis.storage.FileRetrieverService;
import net.sourceforge.blogentis.storage.IStorageAuthToken;
import net.sourceforge.blogentis.storage.IStorageProvider;
import net.sourceforge.blogentis.storage.LocalFileResource;
import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.BlogConstants;
import net.sourceforge.blogentis.utils.LinkFactoryService;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.turbine.om.security.Group;
import org.apache.turbine.om.security.Permission;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.security.AccessControlList;
import org.apache.webdav.lib.Ace;
import org.apache.webdav.lib.Privilege;
import org.apache.webdav.lib.PropertyName;
import org.apache.webdav.lib.WebdavResource;

/**
 * @author abas
 */
public class SlideStorageProvider implements IStorageProvider {

    private static final String BLOG_WEBDAVTOKEN_ATTRIBUTE = "blog.WebDavToken";

    private static final String INITIAL_BLOG_COLLECTIONS = "initialBlogCollections";

    private static Log log = LogFactory.getLog(SlideStorageProvider.class);

    public static final String SERVICE_NAME = "SlideService";

    static BlogDavResource getSlidePath(Credentials credentials, Blog blog) throws HttpException, IOException {
        String link = LinkFactoryService.getInstance().getLink().setBlog(blog).setAbsolute(true).toString();
        HttpURL url = link.startsWith("https") ? new HttpsURL(link) : new HttpURL(link);
        if (credentials != null && credentials instanceof UsernamePasswordCredentials) {
            UsernamePasswordCredentials c = (UsernamePasswordCredentials) credentials;
            url.setUserinfo(c.getUserName(), c.getPassword());
        }
        return new BlogDavResource(url, credentials, WebdavResource.NOACTION, 0);
    }

    public static final String CONFIG_LAST_FILE_HASH = "blog.admin.lastFileHash";

    private UsernamePasswordCredentials getAdminCredentials(Blog b) throws IOException {
        Group g = b.getGroup();
        try {
            String u = (String) b.getTemp("blog.admin.user");
            if (u != null) {
                User user = TurbineSecurity.getUser(u);
                if (TurbineSecurity.getACL(user).hasPermission(BlogConstants.PERM_ADMIN_BLOG, g)) {
                    return new UsernamePasswordCredentials(user.getName(), user.getPassword());
                }
            }
        } catch (Exception e) {
            log.warn("", e);
        }
        try {
            List list = TurbineSecurity.getUserList(new Criteria());
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                User u = (User) i.next();
                if (TurbineSecurity.getACL(u).hasPermission(BlogConstants.PERM_ADMIN_BLOG, g)) {
                    b.setTemp("blog.admin.user", u.getName());
                    return new UsernamePasswordCredentials(u.getName(), u.getPassword());
                }
            }
        } catch (Exception e) {
            IOException ioe = new IOException("Can't find admin for " + b.getName());
            ioe.initCause(e);
            throw ioe;
        }
        throw new IOException("Can't find admin for " + b.getName());
    }

    private static String lastCredentials = null;

    private static String curCredentials = null;

    private static long lastCredUpdate = 0;

    private static Random rng = new Random();

    private static synchronized void checkCredUpdate() {
        long time = new Date().getTime();
        if (time - lastCredUpdate < 3600000) return;
        lastCredentials = curCredentials;
        curCredentials = String.valueOf(rng.nextLong());
        lastCredUpdate = time;
    }

    static boolean matchRootPassword(String pass) {
        if (pass == null) return false;
        checkCredUpdate();
        return pass.equals(curCredentials) || pass.equals(lastCredentials);
    }

    static UsernamePasswordCredentials getRootCredentials() {
        checkCredUpdate();
        return new UsernamePasswordCredentials(UserAdapterStore.ADMIN_USER_NAME, curCredentials);
    }

    static UsernamePasswordCredentials getCredentials(RunData data) {
        if (data != null) {
            User user = data.getUser();
            if (user != null && user != null && user.hasLoggedIn()) {
                return new UsernamePasswordCredentials(user.getName(), user.getPassword());
            }
        }
        return null;
    }

    /**
     * Delete a file or directory from the slide store.
     * 
     * @param data
     *            the RunData for the current request.
     * @param b
     *            the Blog this file belongs to.
     * @param path
     *            the file to delete
     * @throws Exception
     *             if the deletion failed.
     */
    public Set getAllChildren(IStorageAuthToken auth, String path, FileResourceFilter filter) throws IOException {
        Set s = new HashSet();
        BlogDavResource wdr = ((SlideAuthToken) auth).getResource(false);
        String origPath = wdr.getPath();
        getSubChildren(wdr, s, origPath, path, filter);
        return s;
    }

    private void getSubChildren(BlogDavResource wdr, Set s, String origPath, String subPath, FileResourceFilter filter) {
        try {
            wdr.setPath(wdr.getRelPath(subPath));
            WebdavResource[] resources = wdr.listWebdavResources();
            wdr.setPath(origPath);
            for (int i = 0; i < resources.length; i++) {
                WebdavResource w = resources[i];
                String newPath = w.getPath();
                if (newPath.startsWith(origPath)) newPath = newPath.substring(origPath.length() - (origPath.endsWith("/") ? 1 : 0));
                if (filter != null && filter.isIgnored(newPath)) continue;
                s.add(newPath);
                if (w.isCollection() && (filter == null || filter.descendInto(newPath))) getSubChildren(wdr, s, origPath, newPath, filter);
            }
        } catch (IOException e) {
            log.warn("Could not list path " + subPath, e);
        }
    }

    private void updateMessage(Blog b, String message) {
        b.getConfiguration().setProperty(BlogConstants.CONFIG_DISABLING_REASON, message);
    }

    private BlogDavResource ensureBlogStoreExists(Blog b) throws IOException {
        BlogDavResource bdr = getSlidePath(getRootCredentials(), b);
        bdr.mkcolMethod();
        fixupBlogPermissions(bdr, b);
        return bdr;
    }

    private void fixupBlogStore(Blog b, boolean overwriteOld) throws IOException {
        String originalMessage = b.getConfiguration().getString(BlogConstants.CONFIG_DISABLING_REASON, "");
        try {
            updateMessage(b, "Initializing blog");
            BlogDavResource bdr = ensureBlogStoreExists(b);
            Credentials adminCredentials = getAdminCredentials(b);
            bdr.setCredentials(adminCredentials);
            List allFiles = LocalFileResource.getAllFiles("/", new FileResourceFilter.AllFilesFilter());
            List collections = new ArrayList(50);
            collections.addAll(FileRetrieverService.getInstance().getConfiguration().getList(INITIAL_BLOG_COLLECTIONS));
            for (Iterator i = allFiles.iterator(); i.hasNext(); ) {
                String p = (String) i.next();
                LocalFileResource resource = new LocalFileResource(p);
                if (resource.isDirectory()) collections.add(p);
            }
            int count = 1;
            for (Iterator i = collections.iterator(); i.hasNext(); count++) {
                updateMessage(b, "Initializing blog - Creating folders " + count + "/" + collections.size());
                bdr.mkcolMethod(bdr.getRelPath((String) i.next()));
            }
            collections = null;
            SlideAuthToken slideAuthToken = new SlideAuthToken(adminCredentials, b);
            for (Iterator i = allFiles.iterator(); i.hasNext(); ) {
                String path = (String) i.next();
                LocalFileResource lfr = new LocalFileResource(path);
                if (lfr.isDirectory()) continue;
                updateMessage(b, "Initializing blog - Checking files " + count++ + "/" + allFiles.size());
                AbstractFileResource sfr = getFile(slideAuthToken, path);
                boolean shouldRevert = overwriteOld || !sfr.exists();
                if (!shouldRevert && !sfr.isOriginal()) continue;
                if (shouldRevert || (sfr.isOriginal() && (lfr.getLastModified().equals(sfr.getLastModified()) || lfr.getSize() != sfr.getSize()))) {
                    setOriginal(bdr, path);
                }
            }
            b.getConfiguration().setProperty(CONFIG_LAST_FILE_HASH, LocalFileResource.getLocalFileHash());
            updateMessage(b, originalMessage);
            try {
                b.getConfiguration().save();
            } catch (TorqueException e) {
                log.error("Can't save blog config", e);
                throw new IOException(e.getMessage());
            }
        } finally {
            updateMessage(b, originalMessage);
        }
    }

    private void fixupBlogPermissions(BlogDavResource bdr, Blog b) throws IOException {
        ArrayList acesRoot = new ArrayList(10);
        ArrayList acesMedia = new ArrayList(10);
        try {
            Group g = b.getGroup();
            Permission pBlogAdmin = TurbineSecurity.getPermissionByName(BlogConstants.PERM_ADMIN_BLOG);
            Permission pSiteAdmin = TurbineSecurity.getPermissionByName(BlogConstants.PERM_ADMIN_SITE);
            Permission pMediaAdmin = TurbineSecurity.getPermissionByName(BlogConstants.PERM_EDIT_MEDIA);
            for (Iterator i = TurbineSecurity.getUserList(new Criteria()).iterator(); i.hasNext(); ) {
                User u = (User) i.next();
                AccessControlList acl = TurbineSecurity.getACL(u);
                if (acl.hasPermission(pBlogAdmin, g) || acl.hasPermission(pSiteAdmin)) {
                    Ace ace = new Ace("/users/" + u.getName());
                    ace.addPrivilege(Privilege.ALL);
                    acesRoot.add(ace);
                } else if (acl.hasPermission(pMediaAdmin, g)) {
                    Ace ace = new Ace("/users/" + u.getName());
                    ace.addPrivilege(Privilege.READ);
                    ace.addPrivilege(Privilege.WRITE);
                    acesRoot.add(ace);
                }
            }
        } catch (Exception e) {
            log.error("Could not determine complete ACL set for blog " + b.getName(), e);
            return;
        }
        bdr.aclMethod(bdr.getPath(), (Ace[]) acesRoot.toArray(new Ace[acesRoot.size()]));
        bdr.aclMethod(bdr.getRelPath("/media"), (Ace[]) acesMedia.toArray(new Ace[acesMedia.size()]));
    }

    private void setOriginal(BlogDavResource bdr, String path) throws IOException {
        LocalFileResource lfr = new LocalFileResource(path);
        if (!lfr.exists()) {
            bdr.deleteMethod(bdr.getRelPath(path));
            return;
        }
        if (lfr.isDirectory()) {
            bdr.mkcolMethod(bdr.getRelPath(path));
        } else {
            if (log.isInfoEnabled()) {
                log.info("Updating " + lfr.getPath());
            }
            lfr.setProperty(BlogConstants.PROP_IS_ORIGINAL, "true");
            saveFile(bdr, lfr);
        }
    }

    private boolean saveFile(BlogDavResource bdr, AbstractFileResource afr) throws HttpException, IOException {
        if (!(bdr.putMethod(afr))) {
            return false;
        }
        Hashtable m = new Hashtable(3);
        m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, BlogConstants.PROP_IS_ORIGINAL), "false");
        m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, BlogConstants.PROP_LAST_STAMP), String.valueOf(afr.getLastModified().getTime()));
        for (Iterator i = afr.getProperties(); i.hasNext(); ) {
            String key = (String) i.next();
            m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, key), afr.getProperty(key));
        }
        if (!bdr.proppatchMethod(bdr.getRelPath(afr.getPath()), m, true)) {
            log.warn("Could not set" + " object properties at " + afr.getPath());
        }
        return true;
    }

    public void reinitializeBlogStore(Blog b, FileResourceFilter filter) throws IOException {
        fixupBlogStore(b, true);
        BlogDavResource bdr = ensureBlogStoreExists(b);
        Credentials adminCredentials = getAdminCredentials(b);
        Set files = getAllChildren(new SlideAuthToken(adminCredentials, b), "/", filter);
        bdr.setCredentials(adminCredentials);
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            String path = (String) i.next();
            setOriginal(bdr, path);
        }
    }

    public IStorageAuthToken getAuthToken(BlogRunData data) {
        SlideAuthToken auth = (SlideAuthToken) data.getRequest().getAttribute(BLOG_WEBDAVTOKEN_ATTRIBUTE);
        if (auth != null) {
            Blog blog = auth.getBlog();
            if (blog != data.getBlog()) {
                log.error("Found cached token for blog " + blog.getName() + ", was expecting blog " + data.getBlog().getName());
                auth = null;
            }
        }
        if (auth == null) {
            auth = new SlideAuthToken(data, data.getBlog());
            data.getRequest().setAttribute(BLOG_WEBDAVTOKEN_ATTRIBUTE, auth);
        }
        return auth;
    }

    public IStorageAuthToken getAdminAuthToken(Blog blog) throws IOException {
        return new SlideAuthToken(getAdminCredentials(blog), blog);
    }

    public IStorageAuthToken getSuperUserToken(Blog blog) throws IOException {
        return new SlideAuthToken(getRootCredentials(), blog);
    }

    public AbstractFileResource getFile(IStorageAuthToken auth, String path) throws IOException {
        return new SlideFileResource(((SlideAuthToken) auth).getResource(false), path);
    }

    public void saveFile(IStorageAuthToken auth, AbstractFileResource afr) throws IOException {
        BlogDavResource bdr = ((SlideAuthToken) auth).getResource(true);
        if (!(bdr.putMethod(afr))) {
            throw new IOException("Could not save file at " + afr.getPath());
        }
        Hashtable m = new Hashtable(3);
        m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, BlogConstants.PROP_IS_ORIGINAL), "false");
        m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, BlogConstants.PROP_LAST_STAMP), String.valueOf(afr.getLastModified().getTime()));
        for (Iterator i = afr.getProperties(); i.hasNext(); ) {
            String key = (String) i.next();
            m.put(new PropertyName(BlogConstants.BLOG_NAMESPACE, key), afr.getProperty(key));
        }
        if (!bdr.proppatchMethod(bdr.getRelPath(afr.getPath()), m, true)) {
            throw new IOException("Could not set" + " object properties at " + afr.getPath());
        }
    }

    public void deleteFile(IStorageAuthToken auth, String path) throws IOException {
        BlogDavResource bdr = ((SlideAuthToken) auth).getResource(true);
        setOriginal(bdr, path);
    }

    public void initializeBlogStore(Blog b, boolean overwriteOld) throws Exception {
        fixupBlogStore(b, overwriteOld);
    }

    public void updateBlogPermissions(Blog b) throws IOException {
        fixupBlogPermissions(new SlideAuthToken(getRootCredentials(), b).getResource(false), b);
    }

    public void makeFolder(IStorageAuthToken auth, String path) throws IOException {
        BlogDavResource bdr = ((SlideAuthToken) auth).getResource(false);
        if (!bdr.mkcolMethod(bdr.getRelPath(path))) {
            if (bdr.getStatusCode() != HttpStatus.SC_CONFLICT) throw new IOException("Could not create ");
        }
    }

    public void deleteBlogStore(Blog b) throws IOException {
        ((SlideAuthToken) getSuperUserToken(b)).getResource(true).deleteMethod();
    }

    public boolean isBlogStale(Blog b) {
        String blogH = b.getConfiguration().getString(SlideStorageProvider.CONFIG_LAST_FILE_HASH);
        String fileH = LocalFileResource.getLocalFileHash();
        return !fileH.equals(blogH);
    }

    public boolean matchesStorePassword(String user, String password) {
        return user.equals(UserAdapterStore.ADMIN_USER_NAME) && matchRootPassword(password);
    }
}
