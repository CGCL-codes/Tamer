package javasvn;

import junit.framework.TestCase;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Thibaut Fagart
 */
public class JavaSvnTestCase extends TestCase {

    public void testIt() throws SVNException {
        setupLibrary();
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded("svn://swatdc01/ES_JSR168/trunk"));
        String fileUrl = "ES_UIMS_Prototype.ipr";
        ArrayList entries = new ArrayList();
        SVNDirEntry rootEntry = repository.getDir("", -1, false, entries);
        System.out.println("root " + rootEntry.getRelativePath() + ":" + rootEntry.getRevision());
        for (Iterator iterator = entries.iterator(); iterator.hasNext(); ) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            System.out.println("\t" + entry.getRelativePath() + ":" + entry.getRevision());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        repository.getFile(fileUrl, -1, null, baos);
        System.out.println("read " + new String(baos.toByteArray()));
    }

    private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }
}
