package net.sf.joafip.store.service.collection.nosubs;

import gnu.trove.THashMap;
import java.util.Map;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.store.service.StoreClassNotFoundException;
import net.sf.joafip.store.service.StoreDataCorruptedException;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.StoreForTest;
import net.sf.joafip.store.service.StoreInvalidClassException;
import net.sf.joafip.store.service.StoreNotSerializableException;
import net.sf.joafip.store.service.StoreTooBigForSerializationException;
import net.sf.joafip.store.service.collection.AbstractTestStoreCollectionMap;
import net.sf.joafip.store.service.proxy.ProxyException;

/**
 * test Trove HashMap not substituted, with garbage
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@StorableAccess
public class TestStoreCollectionTroveHashMapNosubsWithG extends AbstractTestStoreCollectionMap {

    public TestStoreCollectionTroveHashMapNosubsWithG() throws TestException {
        super();
    }

    public TestStoreCollectionTroveHashMapNosubsWithG(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new StoreForTest(1, null, path, true);
        store.openAndNewAccessSession(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void assertMapType(final Map<String, String> map) {
        assertTrue("must be THashMap instance", map instanceof THashMap);
    }

    @Override
    protected Map<String, String> createMap() {
        return new THashMap<String, String>();
    }

    @SuppressWarnings("PMD")
    @Override
    public void testMap() throws StoreException, StoreInvalidClassException, StoreNotSerializableException, StoreClassNotFoundException, StoreDataCorruptedException, ProxyException, StoreTooBigForSerializationException {
        super.testMap();
    }
}
