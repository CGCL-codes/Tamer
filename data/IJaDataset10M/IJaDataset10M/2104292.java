package net.sf.joafip.service;

import net.sf.joafip.heapfile.service.FileStateRestoredException;
import net.sf.joafip.heapfile.service.FileStateUnstableException;
import net.sf.joafip.store.service.AbstractStoreTestCase;
import net.sf.joafip.store.service.objectfortest.Bob1;

public class TestFilePersistence extends AbstractStoreTestCase {

    private FilePersistence filePersistence;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filePersistence = new FilePersistence(path);
    }

    @Override
    protected void tearDown() throws Exception {
        filePersistence.close();
        super.tearDown();
    }

    public void testNotOpen() throws FileStateRestoredException, FileStateUnstableException {
        final DataAccessSession dataAccessSession = filePersistence.createDataAccessSession();
        try {
            dataAccessSession.getObject("bob1");
            failNotOpened();
        } catch (FilePersistenceException e) {
        }
        try {
            dataAccessSession.setObject("bob1", new Object());
            failNotOpened();
        } catch (FilePersistenceException e) {
        }
        try {
            dataAccessSession.removeObject("bob1");
            failNotOpened();
        } catch (FilePersistenceException e) {
        }
        try {
            dataAccessSession.close();
            failNotOpened();
        } catch (FilePersistenceException e) {
        }
        try {
            dataAccessSession.close(EnumFilePersistenceCloseAction.SAVE);
            failNotOpened();
        } catch (FilePersistenceException e) {
        }
    }

    /**
	 * 
	 */
    private void failNotOpened() {
        fail("must fail because do not open data session");
    }

    public void testAdd() throws FilePersistenceException, FileStateRestoredException, FileStateUnstableException {
        addBob1();
        final DataAccessSession session = filePersistence.createDataAccessSession();
        session.open();
        final Bob1 bob1 = (Bob1) session.getObject("bob1");
        assertNotNull("must found an object", bob1);
        assertEquals("value must be 0", 0, bob1.getVal());
        session.close();
        assertNotUsed();
    }

    /**
	 * vérifie que la persistance n'est plus utilisée
	 */
    private void assertNotUsed() {
        if (filePersistence.isUsed()) {
            fail("must not have opened session");
        }
    }

    /**
	 * @throws FilePersistenceException
	 * @throws FileStateUnstableException
	 * @throws FileStateRestoredException
	 * @throws FileCorruptedException
	 */
    private void addBob1() throws FilePersistenceException, FileStateRestoredException, FileStateUnstableException {
        Bob1 bob1 = new Bob1();
        bob1.setVal(0);
        final DataAccessSession session = filePersistence.createDataAccessSession();
        session.open();
        assertNull("must not have bob1 object", filePersistence.getObject("bob1"));
        assertNull("must not replace an other object", filePersistence.setObject("bob1", bob1));
        assertEquals("save must be done", EnumFilePersistenceCloseAction.SAVE, session.close(EnumFilePersistenceCloseAction.SAVE));
    }

    public void testModification() {
    }

    public void testDeletion() throws FilePersistenceException, FileStateRestoredException, FileStateUnstableException {
        addBob1();
        final DataAccessSession session = filePersistence.createDataAccessSession();
        session.open();
        session.removeObject("bob1");
        assertEquals("save must be done", EnumFilePersistenceCloseAction.SAVE, session.close(EnumFilePersistenceCloseAction.SAVE));
        session.open();
        final Bob1 bob1;
        bob1 = (Bob1) filePersistence.getObject("bob1");
        assertNull("must not have object for 'bob1' key", bob1);
        assertEquals("save must be done", EnumFilePersistenceCloseAction.SAVE, session.close(EnumFilePersistenceCloseAction.SAVE));
        assertNotUsed();
    }
}
