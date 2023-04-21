package mnemosyne.archiver;

import mnemosyne.archiver.nonPersistentTestObjects.MockPersistentObject;
import mnemosyne.archiver.nonPersistentTestObjects.MockPersistentObjectFactory;
import mnemosyne.archiver.testObjects.SimpleObject;
import mnemosyne.core.*;
import mnemosyne.guid.GuidGenerator;
import mnemosyne.guid.RmiUidGuidGenerator;
import java.io.File;

/**
 * @version $Id: FileSystemArchiverTest.java,v 1.2 2004/10/09 07:52:16 charlesblaxland Exp $
 */
public class FileSystemArchiverTest extends ArchiverTestBase {

    public static final String ROOT_NAME = "obj";

    private GuidGenerator guidGenerator;

    private FileSystemArchiver archiver;

    private VersionManager versionMgr = new VersionManager(new LongVersion(0));

    protected void setUp() throws Exception {
        super.setUp();
        archiver = new FileSystemArchiver(versionMgr, new MockPersistentObjectFactory(), archiveDirName);
        guidGenerator = new RmiUidGuidGenerator();
    }

    public void testLoadLatestWhenNoArchiveFilesReturnsNewPersistentRoot() throws Exception {
        assertNumArchiveFilesEquals(0);
        PersistentRoot root = archiver.loadLatest();
        assertNumArchiveFilesEquals(1);
        assertNotNull(root);
    }

    public void testSaveSnapshot() throws Exception {
        Version startVersion = PersistentContext.get(versionMgr).getVersion();
        versionMgr.setLatestVersion(versionMgr.getNextVersion());
        writeInitialSnapshot();
        Version endVersion = PersistentContext.get(versionMgr).getVersion();
        assertNotSame(startVersion, endVersion);
        assertNumArchiveFilesEquals(1);
    }

    public void testSaveTransaction() throws Exception {
        PersistentRoot root = writeInitialSnapshot();
        Transaction transaction = new StandardTransaction();
        SimpleObject obj = new SimpleObject(2);
        makePersistent(obj);
        root.setPersistentRoot(ROOT_NAME, obj);
        transaction.addModifiedObject(((Persistable) root).MN__getPersistentObject());
        transaction.prepareForCommit();
        archiver.saveTransaction(transaction);
        assertNumArchiveFilesEquals(2);
    }

    public void assertNumArchiveFilesEquals(int expected) throws Exception {
        File archiveDir = new File(archiveDirName);
        File[] files = archiveDir.listFiles();
        assertEquals(expected, files.length);
    }

    private PersistentRoot writeInitialSnapshot() throws ArchiverException {
        PersistentRoot root = new PersistentRootImpl();
        makePersistent(root);
        SimpleObject obj = new SimpleObject(1);
        makePersistent(obj);
        root.setPersistentRoot(ROOT_NAME, obj);
        archiver.saveSnapshot(root);
        return root;
    }

    private void makePersistent(Object obj) {
        PersistentObject persistentObject = new MockPersistentObject(guidGenerator.generateGUID());
        persistentObject.initialize(obj);
    }
}
