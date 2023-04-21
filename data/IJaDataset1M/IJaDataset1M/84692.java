package securus.services;

import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.util.Strings;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.svnkit.SvnKitClientAdapterFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import securus.entity.SharedFile;
import securus.entity.User;
import securus.services.FileSendingService.FileSystemItem;

/**
 * Server-side svn operations
 * 
 * @author m.kanel
 * 
 */
@Name("svnService")
@Scope(ScopeType.SESSION)
@AutoCreate
public class SVNService implements Serializable {

    private static final long serialVersionUID = 4924177977835224175L;

    public static final String SVN_CLIENT_TYPE = "svnkit";

    public static final long SYNCH_DEVICE_ID = -1L;

    private final Map<User, ISVNClientAdapter> adapters = new HashMap<User, ISVNClientAdapter>();

    private static Map<String, SVNRepository> repositories = new HashMap<String, SVNRepository>();

    @Logger
    private Log log;

    @In(required = false)
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    static {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    @Create
    public void init() {
        if (!SvnKitClientAdapterFactory.isSVNClientAvailable(SVN_CLIENT_TYPE)) {
            try {
                SvnKitClientAdapterFactory.setup();
            } catch (SVNClientException e) {
                log.error("Cannot create an SVN client adapter of type: " + SVN_CLIENT_TYPE, e);
                throw new IllegalStateException(e);
            }
        }
    }

    @Destroy
    public void destroy() {
        Collection<ISVNClientAdapter> values = adapters.values();
        for (ISVNClientAdapter svn : values) {
            svn.dispose();
        }
        adapters.clear();
    }

    /**
	 * Do not call dispose() for SVNAdapter after use(!). It will be disposed
	 * lately by @Destroy method of this class
	 * 
	 * @param user
	 * @return adapter
	 */
    public ISVNClientAdapter getSVNAdapter(User user) {
        ISVNClientAdapter userSVN = adapters.get(user);
        if (userSVN == null) {
            userSVN = SVNClientAdapterFactory.createSVNClient(SVN_CLIENT_TYPE);
            userSVN.setUsername(user.getUserId().toString());
            userSVN.setPassword(Encryptor.decrypt(user.getPassword2(), user.getUserName()));
            adapters.put(user, userSVN);
        }
        return userSVN;
    }

    public List<ISVNDirEntry> getSVNTree(SVNUrl root, User user) {
        return getSVNTree(root, user, SVNRevision.HEAD, true);
    }

    public List<ISVNDirEntry> getSVNTree(SVNUrl root, User user, boolean recursive) {
        return getSVNTree(root, user, SVNRevision.HEAD, recursive);
    }

    public List<ISVNDirEntry> getSVNTree(SVNUrl root, User user, SVNRevision revision, boolean recursive) {
        ISVNClientAdapter adapter = getSVNAdapter(user);
        ISVNDirEntry[] entries;
        try {
            entries = adapter.getList(root, revision, recursive);
        } catch (SVNClientException e) {
            throw new SecurusException(e);
        }
        return Arrays.asList(entries);
    }

    public static SVNRepository getRepository(User user, String root) throws SVNException {
        SVNRepository repository = repositories.get(root);
        if (repository == null) {
            SVNURL url = SVNURL.parseURIEncoded(root);
            repository = SVNRepositoryFactory.create(url);
            SVNSSHAuthentication authManager = new SVNSSHAuthentication(user.getUserId().toString(), Encryptor.decrypt(user.getPassword2(), user.getUserName()), 22, false);
            SVNSSHAuthentication[] auth = { authManager };
            repository.setAuthenticationManager(new BasicAuthenticationManager(auth));
            repositories.put(root, repository);
        }
        return repository;
    }

    public static void closeRepository(String root) throws SVNException {
        SVNRepository repository = repositories.get(root);
        if (repository != null) {
            repository.closeSession();
            repositories.remove(root);
        }
    }

    /**
	 * Do not call dispose() for SVNAdapter after use(!). It will be disposed
	 * lately by @Destroy method of this class
	 * 
	 * @return adapter
	 */
    public ISVNClientAdapter getSVNAdapter() {
        return getSVNAdapter(currentUser);
    }

    public InputStream getContent(String path, Long deviceId) throws MalformedURLException, SVNClientException {
        return getContent(SVNRevision.HEAD, buildURL(currentUser, deviceId, path));
    }

    public InputStream getContent(SVNRevision rev, String path) throws MalformedURLException, SVNClientException {
        return getSVNAdapter(currentUser).getContent(new SVNUrl(path), rev, rev);
    }

    public InputStream getContent(User user, String path, Long deviceId) throws MalformedURLException, SVNClientException {
        return getSVNAdapter(user).getContent(new SVNUrl(buildURL(user, deviceId, path)), SVNRevision.HEAD, SVNRevision.HEAD);
    }

    public static SVNUrl buildSVNURL(User user, long deviceId, String path) {
        String url = buildURL(user, deviceId, path);
        try {
            return new SVNUrl(url);
        } catch (MalformedURLException e) {
            throw new SecurusException(e);
        }
    }

    public static String buildURL(User user, long deviceId, String path) {
        String url = "svn+ssh://" + user.getAccount().getStorageServer().getInternalAddress() + "/home/" + user.getUserId() + "/svn/";
        if (deviceId > 0) {
            url += deviceId;
        } else {
            url += "public";
        }
        url += "/root";
        if (!(path == null || path.isEmpty())) {
            url += "/" + path;
        }
        return url;
    }

    public static String getFileName(String path) {
        String[] split = Strings.split(path, "/");
        String name = "";
        for (int i = split.length - 1; i >= 0 && name.isEmpty(); i--) {
            name = split[i];
        }
        return name;
    }

    public static class SrvFileSystemItem extends FileSystemItem {

        private final SVNService svn;

        private final Long deviceId;

        private final User user;

        public SrvFileSystemItem(FileItem file, SVNService svn, Long deviceId, User user) {
            super(file.getFile(), file.isFolder());
            this.svn = svn;
            this.deviceId = deviceId;
            this.user = user;
        }

        @Override
        public InputStream getContent() {
            try {
                return svn.getContent(user, getFile(), deviceId);
            } catch (Exception e) {
                throw new SecurusException(e);
            }
        }
    }

    public static String buildURL(SharedFile f) {
        Long deviceId = (f.getDevice().isPublic() ? SYNCH_DEVICE_ID : f.getDevice().getDeviceId());
        return buildURL(f.getFrom(), deviceId, f.getPath());
    }

    public static SVNUrl buildSVNURL(SharedFile f) {
        Long deviceId = (f.getDevice().isPublic() ? SYNCH_DEVICE_ID : f.getDevice().getDeviceId());
        return buildSVNURL(f.getFrom(), deviceId, f.getPath());
    }
}
