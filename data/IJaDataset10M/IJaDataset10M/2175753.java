package es.juanrak.svn;

import java.io.File;
import java.util.ArrayList;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNChangedFiles {

    private SVNURL branchURL;

    private String username;

    private String password;

    private SVNRevision startingRevision;

    private SVNRevision endingRevision;

    private String destinationDirectory;

    private ISVNAuthenticationManager authManager;

    /**
     * <p>
     * Tested with SVNKit 1.1.4
     * </p>
     * <p>
     * Use to export added or modified files from a Subversion repository at
     * branchURL between startingRevision and endingRevision revisions to the specified
     * destinationDirectory.
     * </p>
     *
     * @param branchURL fully qualified URL of the SVN repository (i.e. "http://[domain]/[path-to-svn]/[branch]")
     * @param username  SVN username
     * @param password  SVN password
     * @param startingRevision  Starting revision number for the branch specified
     * @param endingRevision    Ending revision number for the branch specified
     * @param destinationDirectory  Root directory where to export files. <i>Files with same name will be overwritten without warning.</i>
     */
    public SVNChangedFiles(String branchURL, String username, String password, long startingRevision, long endingRevision, String destinationDirectory) {
        super();
        DAVRepositoryFactory.setup();
        try {
            this.branchURL = SVNURL.parseURIEncoded(branchURL);
            this.username = username;
            this.password = password;
            this.startingRevision = SVNRevision.create(startingRevision);
            this.endingRevision = SVNRevision.create(endingRevision);
            this.destinationDirectory = destinationDirectory;
            this.authManager = SVNWCUtil.createDefaultAuthenticationManager(this.username, this.password);
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Calls {@link SVNChangedFiles#export(ArrayList)} passing in a null.
     */
    public void export() {
        export(null);
    }

    /**
     * <p>
     * Exports files added or modified between the startingRevision and endingRevision revision numbers,
     * including creating sub-directories, relative to the destinationDirectory.</p>
     * </p>
     * <p>
     * <i>Files of the same name will be overwritten without warning.</i>
     * </p>
     *
     * @param changes ArrayList of SVNDiffStatus objects for export, passing null will populate with the results of {@link SVNChangedFiles#list()}
     */
    public void export(ArrayList changes) {
        if (changes == null) changes = list();
        SVNUpdateClient updateClient = new SVNUpdateClient(authManager, SVNWCUtil.createDefaultOptions(true));
        try {
            for (int idx = 0; idx < changes.size(); idx++) {
                SVNDiffStatus change = (SVNDiffStatus) changes.get(idx);
                File destination = new File(destinationDirectory + "\\" + change.getPath());
                updateClient.doExport(change.getURL(), destination, this.endingRevision, this.endingRevision, null, true, false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * Returns an {@link java.util.ArrayList} of {@link org.tmatesoft.svn.core.wc.SVNDiffStatus} objects
     * representing all files exported or modified on the given branchURL between the startingRevision and endingRevision
     * revisions.
     * </p>
     * @return {@link java.util.ArrayList} of {@link org.tmatesoft.svn.core.wc.SVNDiffStatus}
     */
    public ArrayList list() {
        try {
            SVNDiffClient diffClient = new SVNDiffClient(authManager, SVNWCUtil.createDefaultOptions(true));
            ArrayList changes = new ArrayList();
            ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler(changes);
            diffClient.doDiffStatus(this.branchURL, this.startingRevision, this.branchURL, this.endingRevision, true, false, handler);
            return changes;
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ImplISVNDiffStatusHandler implements ISVNDiffStatusHandler {

        private ArrayList changes;

        public ImplISVNDiffStatusHandler(ArrayList changes) {
            this.changes = changes;
        }

        public void handleDiffStatus(SVNDiffStatus status) throws SVNException {
            if (status.getKind() == SVNNodeKind.FILE && (status.getModificationType() == SVNStatusType.STATUS_ADDED || status.getModificationType() == SVNStatusType.STATUS_MODIFIED)) changes.add(status);
        }
    }
}
