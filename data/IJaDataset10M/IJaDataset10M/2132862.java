package net.sf.joafip.store.service;

import java.util.Map;
import net.sf.joafip.AbstractDeleteFileTestCase;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestConstant;
import net.sf.joafip.TestException;
import net.sf.joafip.java.util.PTreeMap;

@NotStorableClass
@StorableAccess
public class TestStoreOpenCloseNoG extends AbstractDeleteFileTestCase {

    private StoreForTest store;

    public TestStoreOpenCloseNoG() throws TestException {
        super();
    }

    public TestStoreOpenCloseNoG(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        if (store != null) {
            try {
                store.close();
            } catch (final Exception exception) {
            }
        }
        store = null;
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void testOpenClose() throws StoreException, StoreClassNotFoundException, StoreInvalidClassException, StoreDataCorruptedException, StoreNotSerializableException, StoreTooBigForSerializationException, TestException {
        logger.info("first open ( creation )");
        Map<String, Object> rootObjectMap;
        store = new StoreForTest(1, null, TestConstant.getWinRamDiskRuntimeDir(), false);
        store.openAndNewAccessSession(true);
        rootObjectMap = (Map<String, Object>) store.readAndGetRoot();
        assertNull("null expected for empty store", rootObjectMap);
        rootObjectMap = new PTreeMap<String, Object>();
        store.setRoot(rootObjectMap);
        store.save(true, false);
        store.close();
        logger.info("second open ( creation ) do not clear files");
        store = new StoreForTest(1, null, TestConstant.getWinRamDiskRuntimeDir(), false);
        store.openAndNewAccessSession(false);
        rootObjectMap = (Map<String, Object>) store.readAndGetRoot();
        assertNotNull("not null expected for non-empty store", rootObjectMap);
        store.close();
    }

    @SuppressWarnings("unchecked")
    public void testOpenCloseClear() throws StoreException, StoreClassNotFoundException, StoreInvalidClassException, StoreDataCorruptedException, StoreNotSerializableException, StoreTooBigForSerializationException, TestException {
        Map<String, Object> rootObjectMap;
        store = new StoreForTest(1, null, TestConstant.getWinRamDiskRuntimeDir(), false);
        store.openAndNewAccessSession(true);
        rootObjectMap = (Map<String, Object>) store.readAndGetRoot();
        assertNull("null expected for empty store", rootObjectMap);
        rootObjectMap = new PTreeMap<String, Object>();
        store.setRoot(rootObjectMap);
        store.save(true, false);
        store.close();
        store.openAndNewAccessSession(true);
        rootObjectMap = (Map<String, Object>) store.readAndGetRoot();
        assertNull("null expected for empty store", rootObjectMap);
        rootObjectMap = new PTreeMap<String, Object>();
        store.setRoot(rootObjectMap);
        store.save(true, false);
        store.close();
    }
}
