package net.sf.joafip.store.service.collection.subs;

import java.util.List;
import java.util.Vector;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.java.util.PVector;
import net.sf.joafip.store.service.StoreForTest;
import net.sf.joafip.store.service.collection.AbstractTestStoreCollectionList;

/**
 * test Vector substituted by PVector, with garbage
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@StorableAccess
public class TestStoreCollectionVectorSubsWithG extends AbstractTestStoreCollectionList {

    public TestStoreCollectionVectorSubsWithG() throws TestException {
        super();
    }

    public TestStoreCollectionVectorSubsWithG(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        store = new StoreForTest(1, null, path, true);
        store.setSubstitutionOfJavaUtilCollection(true);
        store.openAndNewAccessSession(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void assertListType(final List<Integer> list) {
        assertTrue("must be PVector instance", list instanceof PVector);
    }

    @Override
    protected List<Integer> createList() {
        return new Vector<Integer>();
    }
}
