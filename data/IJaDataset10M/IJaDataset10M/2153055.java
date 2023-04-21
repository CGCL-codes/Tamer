package net.sf.joafip.service;

import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.export_import.Container;

public class TestImport222 extends AbstractDeleteFileTestCase {

    protected FilePersistence filePersistence;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        filePersistence = new FilePersistence(path, true, false);
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            filePersistence.close();
        } catch (Throwable throwable) {
        }
        super.tearDown();
    }

    public void testImport222() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException {
        filePersistence.xmlImport("export222");
        final DataAccessSession dataAccessSession = filePersistence.createDataAccessSession();
        dataAccessSession.open();
        final Container container = (Container) dataAccessSession.getObject("container");
        assertTrue("bad state", container.checkState());
        dataAccessSession.close(EnumFilePersistenceCloseAction.SAVE);
    }
}
