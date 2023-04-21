package net.sf.archimede.model.folder;

import java.util.GregorianCalendar;
import java.util.List;
import junit.framework.TestCase;
import net.sf.archimede.model.AllTests;
import net.sf.archimede.model.CredentialsWrapper;
import net.sf.archimede.model.DatabaseUtil;
import net.sf.archimede.model.ObjectExistsException;
import net.sf.archimede.model.ObjectLockedException;
import net.sf.archimede.model.TransactionException;
import net.sf.archimede.model.collection.Collection;
import net.sf.archimede.model.collection.CollectionDao;
import net.sf.archimede.model.collection.CollectionImpl;
import net.sf.archimede.model.folder.Folder;
import net.sf.archimede.model.folder.FolderDao;
import net.sf.archimede.model.folder.FolderImpl;

public class FolderImplTest extends TestCase {

    private Collection aCollection;

    private FolderDao folderDao = FolderDao.createInstance();

    private CollectionDao collectionDao = CollectionDao.createInstance();

    public FolderImplTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (this.aCollection == null) {
            DatabaseUtil.getSingleton().beginTransaction(new CredentialsWrapper(AllTests.ADMIN_USERNAME, AllTests.ADMIN_PASSWORD));
            this.aCollection = new CollectionImpl();
            this.aCollection.setName("" + Math.random());
            this.aCollection.setDescription("A fantastic collection of documents");
            this.aCollection.setParent(this.collectionDao.getRootCollection());
            this.collectionDao.save(this.aCollection);
            DatabaseUtil.getSingleton().commitTransaction();
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        DatabaseUtil.getSingleton().beginTransaction(new CredentialsWrapper(AllTests.ADMIN_USERNAME, AllTests.ADMIN_PASSWORD));
        this.collectionDao.remove(this.aCollection);
        DatabaseUtil.getSingleton().commitTransaction();
        System.out.println("tear down------------------------------------------");
    }

    public void testSave() throws TransactionException {
        DatabaseUtil.getSingleton().beginTransaction(new CredentialsWrapper(AllTests.ADMIN_USERNAME, AllTests.ADMIN_PASSWORD));
        Folder folder3 = new FolderImpl();
        folder3.setName("folder_3");
        folder3.setParentCollection(this.aCollection);
        folder3.setDatestamp(new GregorianCalendar());
        this.folderDao.save(folder3);
        DatabaseUtil.getSingleton().commitTransaction();
    }

    public void testRetrieve() throws TransactionException {
        this.testSave();
        DatabaseUtil.getSingleton().beginTransaction(new CredentialsWrapper(AllTests.ADMIN_USERNAME, AllTests.ADMIN_PASSWORD));
        List folders = this.collectionDao.retrieve(this.aCollection.getId()).getFolders();
        Folder folderFromCollection = (Folder) folders.get(0);
        String id = folderFromCollection.getId();
        Folder folder = this.folderDao.retrieve(id);
        assertEquals(folder.getName(), "folder_3");
        assertEquals(folder.getParentCollection(), this.aCollection);
        DatabaseUtil.getSingleton().commitTransaction();
    }

    public void testDelete() throws TransactionException, ObjectLockedException, ObjectExistsException {
        this.testSave();
        DatabaseUtil.getSingleton().beginTransaction(new CredentialsWrapper(AllTests.ADMIN_USERNAME, AllTests.ADMIN_PASSWORD));
        List folders = collectionDao.retrieve(aCollection.getId()).getFolders();
        Folder folderFromCollection = (Folder) folders.get(0);
        folderDao.remove(folderFromCollection);
        assertEquals(null, folderDao.retrieve(folderFromCollection.getId()));
        DatabaseUtil.getSingleton().commitTransaction();
    }
}
