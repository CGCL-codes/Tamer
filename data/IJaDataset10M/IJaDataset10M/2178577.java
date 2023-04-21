package net.sf.joafip.store.service;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;

@NotStorableClass
@StorableAccess
public class TestStoreForPersistenteClassFileLazyNoGarbage extends AbstractTestStoreForPersistenteClass {

    public TestStoreForPersistenteClassFileLazyNoGarbage() throws TestException {
        super();
    }

    public TestStoreForPersistenteClassFileLazyNoGarbage(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new StoreForTest(1, null, path, 1024, 1024, false);
        store.setSubstitutionOfJavaUtilCollection(true);
        store.openAndNewAccessSession(true);
        dataManager = store.getDataManager();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            store.close();
        } catch (Exception e) {
        }
        store = null;
        dataManager = null;
        super.tearDown();
    }
}
