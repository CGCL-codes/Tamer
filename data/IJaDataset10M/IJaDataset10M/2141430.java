package org.tmatesoft.svn.examples.wc;

import gate.versioning.svnkit.InfoHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.util.SVNPathUtil;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class WorkingCopy {

    static Logger lgr = Logger.getLogger(WorkingCopy.class);

    private static SVNClientManager ourClientManager;

    private static ISVNEventHandler myCommitEventHandler;

    private static ISVNEventHandler myUpdateEventHandler;

    private static ISVNEventHandler myWCEventHandler;

    public static void main(String[] args) throws SVNException {
        setupLibrary();
        SVNURL repositoryURL = null;
        String testDir = "test/scratch/svn-kit-test";
        File testDirFile = new File(testDir);
        if (testDirFile.exists()) gate.util.Files.rmdir(testDirFile);
        repositoryURL = SVNRepositoryFactory.createLocalRepository(new File(testDir + "/testRep"), null, false, true);
        String name = "userName";
        String password = "userPassword";
        String myWorkingCopyPath = testDir + "/MyWorkingCopy";
        String importDir = testDir + "/importDir";
        String importFile = importDir + "/importFile.txt";
        String importFileText = "This unversioned file is imported into a repository";
        String newDir = "/newDir";
        String newFile = newDir + "/newFile.txt";
        String fileText = "This is a new file added to the working copy";
        if (args != null) {
            try {
                repositoryURL = (args.length >= 1) ? SVNURL.parseURIEncoded(args[0]) : repositoryURL;
            } catch (SVNException e) {
                System.err.println("'" + args[0] + "' is not a valid URL");
                System.exit(1);
            }
            myWorkingCopyPath = (args.length >= 2) ? args[1] : myWorkingCopyPath;
            name = (args.length >= 3) ? args[2] : name;
            password = (args.length >= 4) ? args[3] : password;
        }
        SVNURL url = repositoryURL.appendPath("MyRepos", false);
        SVNURL copyURL = repositoryURL.appendPath("MyReposCopy", false);
        SVNURL importToURL = url.appendPath(importDir, false);
        myCommitEventHandler = new CommitEventHandler();
        myUpdateEventHandler = new UpdateEventHandler();
        myWCEventHandler = new WCEventHandler();
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, name, password);
        ourClientManager.getCommitClient().setEventHandler(myCommitEventHandler);
        ourClientManager.getUpdateClient().setEventHandler(myUpdateEventHandler);
        ourClientManager.getWCClient().setEventHandler(myWCEventHandler);
        long committedRevision = -1;
        lgr.info("Making a new directory at '" + url + "'...");
        try {
            committedRevision = makeDirectory(url, "making a new directory at '" + url + "'").getNewRevision();
        } catch (SVNException svne) {
            error("error while making a new directory at '" + url + "'", svne);
        }
        lgr.info("Committed to revision " + committedRevision);
        lgr.info("");
        File anImportDir = new File(importDir);
        File anImportFile = new File(anImportDir, SVNPathUtil.tail(importFile));
        createLocalDir(anImportDir, new File[] { anImportFile }, new String[] { importFileText });
        lgr.info("Importing a new directory into '" + importToURL + "'...");
        try {
            boolean isRecursive = true;
            committedRevision = importDirectory(anImportDir, importToURL, "importing a new directory '" + anImportDir.getAbsolutePath() + "'", isRecursive).getNewRevision();
        } catch (SVNException svne) {
            error("error while importing a new directory '" + anImportDir.getAbsolutePath() + "' into '" + importToURL + "'", svne);
        }
        lgr.info("Committed to revision " + committedRevision);
        lgr.info("");
        File wcDir = new File(myWorkingCopyPath);
        if (wcDir.exists()) {
            error("the destination directory '" + wcDir.getAbsolutePath() + "' already exists!", null);
        }
        wcDir.mkdirs();
        lgr.info("Checking out a working copy from '" + url + "'...");
        try {
            checkout(url, SVNRevision.HEAD, wcDir, true);
        } catch (SVNException svne) {
            error("error while checking out a working copy for the location '" + url + "'", svne);
        }
        lgr.info("");
        try {
            showInfo(wcDir, SVNRevision.WORKING, true);
        } catch (SVNException svne) {
            error("error while recursively getting info for the working copy at'" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        File aNewDir = new File(wcDir, newDir);
        File aNewFile = new File(aNewDir, SVNPathUtil.tail(newFile));
        createLocalDir(aNewDir, new File[] { aNewFile }, new String[] { fileText });
        lgr.info("Recursively scheduling a new directory '" + aNewDir.getAbsolutePath() + "' for addition...");
        try {
            addEntry(aNewDir);
        } catch (SVNException svne) {
            error("error while recursively adding the directory '" + aNewDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        boolean isRecursive = true;
        boolean isRemote = true;
        boolean isReportAll = false;
        boolean isIncludeIgnored = true;
        boolean isCollectParentExternals = false;
        lgr.info("Status for '" + wcDir.getAbsolutePath() + "':");
        try {
            showStatus(wcDir, isRecursive, isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals);
        } catch (SVNException svne) {
            error("error while recursively performing status for '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Updating '" + wcDir.getAbsolutePath() + "'...");
        try {
            update(wcDir, SVNRevision.HEAD, true);
        } catch (SVNException svne) {
            error("error while recursively updating the working copy at '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Committing changes for '" + wcDir.getAbsolutePath() + "'...");
        try {
            committedRevision = commit(wcDir, false, "'/newDir' with '/newDir/newFile.txt' were added").getNewRevision();
        } catch (SVNException svne) {
            error("error while committing changes to the working copy at '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("Committed to revision " + committedRevision);
        lgr.info("");
        System.out.println("Locking (with stealing if the entry is already locked) '" + aNewFile.getAbsolutePath() + "'.");
        try {
            lock(aNewFile, true, "locking '/newDir/newFile.txt'");
        } catch (SVNException svne) {
            error("error while locking the working copy file '" + aNewFile.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Status for '" + wcDir.getAbsolutePath() + "':");
        try {
            showStatus(wcDir, isRecursive, isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals);
        } catch (SVNException svne) {
            error("error while recursively performing status for '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Copying '" + url + "' to '" + copyURL + "'...");
        try {
            committedRevision = copy(url, copyURL, false, "remotely copying '" + url + "' to '" + copyURL + "'").getNewRevision();
        } catch (SVNException svne) {
            error("error while copying '" + url + "' to '" + copyURL + "'", svne);
        }
        lgr.info("Committed to revision " + committedRevision);
        lgr.info("");
        lgr.info("Switching '" + wcDir.getAbsolutePath() + "' to '" + copyURL + "'...");
        try {
            switchToURL(wcDir, copyURL, SVNRevision.HEAD, true);
        } catch (SVNException svne) {
            error("error while switching '" + wcDir.getAbsolutePath() + "' to '" + copyURL + "'", svne);
        }
        lgr.info("");
        try {
            showInfo(wcDir, SVNRevision.WORKING, true);
        } catch (SVNException svne) {
            error("error while recursively getting info for the working copy at'" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Scheduling '" + aNewDir.getAbsolutePath() + "' for deletion ...");
        try {
            delete(aNewDir, true);
        } catch (SVNException svne) {
            error("error while schediling '" + wcDir.getAbsolutePath() + "' for deletion", svne);
        }
        lgr.info("");
        lgr.info("Status for '" + wcDir.getAbsolutePath() + "':");
        try {
            showStatus(wcDir, isRecursive, isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals);
        } catch (SVNException svne) {
            error("error while recursively performing status for '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("");
        lgr.info("Committing changes for '" + wcDir.getAbsolutePath() + "'...");
        try {
            committedRevision = commit(wcDir, false, "deleting '" + aNewDir.getAbsolutePath() + "' from the filesystem as well as from the repository").getNewRevision();
        } catch (SVNException svne) {
            error("error while committing changes to the working copy '" + wcDir.getAbsolutePath() + "'", svne);
        }
        lgr.info("Committed to revision " + committedRevision);
    }

    private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    private static SVNCommitInfo makeDirectory(SVNURL url, String commitMessage) throws SVNException {
        return ourClientManager.getCommitClient().doMkDir(new SVNURL[] { url }, commitMessage);
    }

    private static SVNCommitInfo importDirectory(File localPath, SVNURL dstURL, String commitMessage, boolean isRecursive) throws SVNException {
        return ourClientManager.getCommitClient().doImport(localPath, dstURL, commitMessage, null, true, false, SVNDepth.fromRecurse(isRecursive));
    }

    private static SVNCommitInfo commit(File wcPath, boolean keepLocks, String commitMessage) throws SVNException {
        return ourClientManager.getCommitClient().doCommit(new File[] { wcPath }, keepLocks, commitMessage, null, null, false, false, SVNDepth.fromRecurse(true));
    }

    private static long checkout(SVNURL url, SVNRevision revision, File destPath, boolean isRecursive) throws SVNException {
        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        return updateClient.doCheckout(url, destPath, revision, revision, isRecursive ? SVNDepth.INFINITY : SVNDepth.EMPTY, false);
    }

    private static long update(File wcPath, SVNRevision updateToRevision, boolean isRecursive) throws SVNException {
        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        return updateClient.doUpdate(wcPath, updateToRevision, SVNDepth.fromRecurse(isRecursive), false, false);
    }

    private static long switchToURL(File wcPath, SVNURL url, SVNRevision updateToRevision, boolean isRecursive) throws SVNException {
        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        return updateClient.doSwitch(wcPath, url, SVNRevision.UNDEFINED, updateToRevision, SVNDepth.fromRecurse(isRecursive), false, false);
    }

    private static void showStatus(File wcPath, boolean isRecursive, boolean isRemote, boolean isReportAll, boolean isIncludeIgnored, boolean isCollectParentExternals) throws SVNException {
        ourClientManager.getStatusClient().doStatus(wcPath, SVNRevision.HEAD, SVNDepth.fromRecurse(isRecursive), isRemote, isReportAll, isIncludeIgnored, isCollectParentExternals, new StatusHandler(isRemote), null);
    }

    private static void showInfo(File wcPath, SVNRevision revision, boolean isRecursive) throws SVNException {
        ourClientManager.getWCClient().doInfo(wcPath, SVNRevision.UNDEFINED, revision, SVNDepth.fromRecurse(isRecursive), null, new InfoHandler());
    }

    private static void addEntry(File wcPath) throws SVNException {
        ourClientManager.getWCClient().doAdd(wcPath, false, false, false, SVNDepth.fromRecurse(true), false, false);
    }

    private static void lock(File wcPath, boolean isStealLock, String lockComment) throws SVNException {
        ourClientManager.getWCClient().doLock(new File[] { wcPath }, isStealLock, lockComment);
    }

    private static void delete(File wcPath, boolean force) throws SVNException {
        ourClientManager.getWCClient().doDelete(wcPath, force, false);
    }

    private static SVNCommitInfo copy(SVNURL srcURL, SVNURL dstURL, boolean isMove, String commitMessage) throws SVNException {
        SVNCopySource source = new SVNCopySource(SVNRevision.UNDEFINED, SVNRevision.HEAD, srcURL);
        return ourClientManager.getCopyClient().doCopy(new SVNCopySource[] { source }, dstURL, isMove, false, true, commitMessage, null);
    }

    private static void error(String message, Exception e) {
        System.err.println(message + (e != null ? ": " + e.getMessage() : ""));
        System.exit(1);
    }

    public static final void createLocalDir(File aNewDir, File[] localFiles, String[] fileContents) {
        if (!aNewDir.mkdirs()) {
            error("failed to create a new directory '" + aNewDir.getAbsolutePath() + "'.", null);
        }
        for (int i = 0; i < localFiles.length; i++) {
            File aNewFile = localFiles[i];
            try {
                if (!aNewFile.createNewFile()) {
                    error("failed to create a new file '" + aNewFile.getAbsolutePath() + "'.", null);
                }
            } catch (IOException ioe) {
                aNewFile.delete();
                error("error while creating a new file '" + aNewFile.getAbsolutePath() + "'", ioe);
            }
            String contents = null;
            if (i > fileContents.length - 1) {
                continue;
            }
            contents = fileContents[i];
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(aNewFile);
                fos.write(contents.getBytes());
            } catch (FileNotFoundException fnfe) {
                error("the file '" + aNewFile.getAbsolutePath() + "' is not found", fnfe);
            } catch (IOException ioe) {
                error("error while writing into the file '" + aNewFile.getAbsolutePath() + "'", ioe);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                    }
                }
            }
        }
    }
}
