package org.one.stone.soup.xapp.application.subversion.build.agent;

import java.io.File;
import org.one.stone.soup.process.monitor.Grabber;
import org.one.stone.soup.process.monitor.GrabberWait;
import org.one.stone.soup.process.monitor.exec.Executable;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class JavaSVNTest {

    static {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
    }

    private SVNRepository repository;

    private ISVNAuthenticationManager authManager;

    private SVNURL url;

    private long latestRevision = -1;

    public static void main(String[] args) {
        try {
            new JavaSVNTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JavaSVNTest() throws Exception {
        String repositoryPath = "C:/Documents and Settings/ncross/subversion-repository";
        url = SVNURL.parseURIEncoded("svn://127.0.0.1");
        repository = SVNRepositoryFactory.create(url);
        authManager = SVNWCUtil.createDefaultAuthenticationManager("admin", "admin");
        repository.setAuthenticationManager(authManager);
        SVNURL rootUrl = repository.getRepositoryRoot(true);
        rootUrl.getHost();
        long revision = repository.getLatestRevision();
        String log = repository.getRevisionPropertyValue(revision, "svn:log");
        String date = repository.getRevisionPropertyValue(revision, "svn:date");
        String author = repository.getRevisionPropertyValue(revision, "svn:author");
        SVNDirEntry entry = repository.info("/", revision);
        String path = entry.getRelativePath();
        Executable look = new Executable(new String[] { "cmd.exe /C svnlook changed \"" + repositoryPath + "\"" });
        GrabberWait waiter = new GrabberWait();
        Grabber grabber = new Grabber(waiter, "look", ".*", "\n", look);
        look.process();
        waiter.waitForGrab(1000);
        if (waiter.isGrabbed()) {
            System.out.println(waiter.getData().toString());
        } else {
            System.out.println("grab failed");
        }
        SVNRevision svnRevision = SVNRevision.create(revision);
        SVNUpdateClient client = new SVNUpdateClient(authManager, null);
        client.doExport(url, new File("C:/Documents and Settings/ncross/test"), svnRevision, svnRevision, null, true, true);
    }
}
