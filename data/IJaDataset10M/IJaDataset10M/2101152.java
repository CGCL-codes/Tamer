package org.exist.versioning.svn;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.exist.EXistException;
import org.exist.xmldb.XmldbURI;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class VersioningRepositoryImpl {

    public static final Logger LOG = Logger.getLogger(VersioningRepositoryImpl.class);

    private String name = "anonymous";

    private String password = "anonymous";

    private boolean connected = false;

    private long latestRevision = -1;

    XmldbURI collection = null;

    org.tmatesoft.svn.core.io.SVNRepository repository = null;

    protected VersioningRepositoryImpl() {
    }

    public boolean connect(XmldbURI collection, String url) {
        if (connected) return true;
        setupLibrary();
        this.collection = collection;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            LOG.error("error while creating an SVNRepository for location '" + url + "': " + svne.getMessage());
            return false;
        }
        ISVNAuthenticationManager authManager = new BasicAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
        try {
            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                LOG.error("There is no entry at '" + url + "'.");
                return false;
            } else if (nodeKind == SVNNodeKind.FILE) {
                LOG.error("The entry at '" + url + "' is a file while a directory was expected.");
                return false;
            }
            System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
            System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
            System.out.println("");
        } catch (SVNException svne) {
            LOG.error("error while listing entries: " + svne.getMessage());
            return false;
        }
        try {
            latestRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            LOG.error("error while fetching the latest repository revision: " + svne.getMessage());
            return false;
        }
        System.out.println("");
        System.out.println("---------------------------------------------");
        System.out.println("Repository latest revision: " + latestRevision);
        connected = true;
        return true;
    }

    private void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    protected boolean update() {
        return update(latestRevision);
    }

    protected boolean update(long toRevision) {
        ISVNReporterBaton reporterBaton = new ExportReporterBaton(toRevision);
        ISVNEditor exportEditor;
        try {
            exportEditor = new ExportEditor(collection);
            repository.update(toRevision, null, true, reporterBaton, exportEditor);
        } catch (EXistException e) {
            e.printStackTrace();
            return false;
        } catch (SVNException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Exported revision: " + toRevision);
        return true;
    }

    @SuppressWarnings("unchecked")
    public Collection<SVNLogEntry> log(String[] targetPaths, Collection<SVNLogEntry> entries, long startRevision, long endRevision, boolean changedPath, boolean strictNode) throws SVNException {
        return repository.log(new String[] { "" }, entries, startRevision, endRevision, changedPath, strictNode);
    }
}
