package net.sf.joafip.store.service.collection.subs;

import java.util.Hashtable;
import java.util.Map;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.java.util.PHashMap;
import net.sf.joafip.store.service.StoreForTest;
import net.sf.joafip.store.service.collection.AbstractTestStoreCollectionMap;

/**
 * test HashTable substituted by PHashMap, no garbage
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@StorableAccess
public class TestStoreCollectionHashTableSubsNoG extends AbstractTestStoreCollectionMap {

    public TestStoreCollectionHashTableSubsNoG() throws TestException {
        super();
    }

    public TestStoreCollectionHashTableSubsNoG(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new StoreForTest(1, null, path, false);
        store.setSubstitutionOfJavaUtilCollection(true);
        store.openAndNewAccessSession(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void assertMapType(final Map<String, String> map) {
        assertTrue("must be PHashMap instance", map instanceof PHashMap);
    }

    @Override
    protected Map<String, String> createMap() {
        return new Hashtable<String, String>();
    }
}
