package cz.cvut.kbss.owldiff.protege;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNManager {

    private static final Logger LOG = Logger.getLogger(SVNManager.class.getName());

    private static SVNManager INSTANCE;

    private static SVNClientManager clientManager;

    private File managedFile = null;

    private SVNManager() {
    }

    public static synchronized SVNManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SVNManager();
        }
        return INSTANCE;
    }

    public boolean connect(final String login, final String pass, final File file) {
        try {
            final ISVNAuthenticationManager m = SVNWCUtil.createDefaultAuthenticationManager(login, pass);
            clientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true), m);
            SVNRepositoryFactoryImpl.setup();
            DAVRepositoryFactory.setup();
            FSRepositoryFactory.setup();
            final SVNRepository r = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(clientManager.getWCClient().doInfo(file, SVNRevision.BASE).getURL().toString()));
            r.setAuthenticationManager(m);
            r.testConnection();
            managedFile = file;
            return true;
        } catch (SVNException e) {
            LOG.log(Level.SEVERE, "An error occured during login.", e);
            return false;
        }
    }

    public List<Long> getRevisionNumbers() {
        final List<Long> fileRevisions = new ArrayList<Long>();
        try {
            clientManager.getLogClient().doLog(new File[] { managedFile }, SVNRevision.UNDEFINED, SVNRevision.UNDEFINED, SVNRevision.UNDEFINED, false, false, false, -1, null, new ISVNLogEntryHandler() {

                public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
                    fileRevisions.add(logEntry.getRevision());
                }
            });
        } catch (SVNException e) {
            LOG.severe("An error occured during fetching revision numbers.");
        }
        return fileRevisions;
    }

    public boolean isUnderSVN() {
        try {
            return clientManager.getStatusClient().doStatus(managedFile, false).getContentsStatus().equals(SVNStatusType.STATUS_UNVERSIONED);
        } catch (SVNException e) {
            LOG.log(Level.SEVERE, "An error occured while getting status for '" + managedFile + "'", e);
            return false;
        }
    }

    public File getFileForRevision(long revision) {
        return getFile(SVNRevision.create(revision));
    }

    public File getBaseFile() {
        return getFile(SVNRevision.BASE);
    }

    public File getHeadFile() {
        return getFile(SVNRevision.HEAD);
    }

    private File getFile(SVNRevision revision) {
        File tmp;
        try {
            tmp = File.createTempFile("tmpOntologyFile", "OWLDiffPlugin");
            tmp.deleteOnExit();
            clientManager.getWCClient().doGetFileContents(managedFile, SVNRevision.UNDEFINED, revision, false, new FileOutputStream(tmp));
            return tmp;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "An error occured during fetching file " + managedFile + " in revision " + revision, e);
            return null;
        } catch (SVNException e) {
            LOG.log(Level.SEVERE, "An error occured during fetching file " + managedFile + " in revision " + revision, e);
            return null;
        }
    }
}
