package net.sf.joafip.store.service;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.service.HeapMemoryDataManagerMock;

@NotStorableClass
@StorableAccess
public class TestStoreForPersistenteClassMemoryLazy extends AbstractTestStoreForPersistenteClass {

    public TestStoreForPersistenteClassMemoryLazy() throws TestException {
        super();
    }

    public TestStoreForPersistenteClassMemoryLazy(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dataManager = new HeapMemoryDataManagerMock();
        store = new StoreForTest(1, null, dataManager, true, true);
        store.setSubstitutionOfJavaUtilCollection(true);
        store.openAndNewAccessSession(true);
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
