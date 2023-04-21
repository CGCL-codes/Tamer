package net.sf.joafip.store.service.garbage;

import java.util.Set;
import java.util.TreeSet;
import net.sf.joafip.AbstractJoafipTestCase;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.HeapMemoryDataManagerMock;
import net.sf.joafip.kvstore.service.IHeapDataManager;
import net.sf.joafip.store.entity.StoreHeader;
import net.sf.joafip.store.entity.garbage.ReferenceLinkGarbageException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.garbage.recordmgr.LinkRecordManager;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestLinkManager extends AbstractJoafipTestCase {

    private LinkManager linkManager;

    private IHeapDataManager dataManager;

    private Set<DataRecordIdentifier> attachedToRoot;

    public TestLinkManager() throws TestException {
        super();
    }

    public TestLinkManager(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dataManager = new HeapMemoryDataManagerMock();
        final HeapRecordableManager heapRecordableManager = new HeapRecordableManager(dataManager);
        final HelperBinaryConversion helperBinaryConversion = new HelperBinaryConversion();
        final StoreHeader storeHeader = new StoreHeader(heapRecordableManager, helperBinaryConversion);
        final LinkRecordManager linkRecordManager = new LinkRecordManager(storeHeader, heapRecordableManager, helperBinaryConversion);
        final GarbageManagerForLinkUpdateMock garbageManager = new GarbageManagerForLinkUpdateMock(linkRecordManager);
        linkManager = new LinkManager(garbageManager);
        dataManager.startService(true);
        attachedToRoot = new TreeSet<DataRecordIdentifier>();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            dataManager.stopService();
        } catch (Exception e) {
        }
        linkManager = null;
        dataManager = null;
        attachedToRoot = null;
        super.tearDown();
    }

    public void testRefencedState() throws GarbageException, ReferenceLinkGarbageException {
        attachedToRoot.clear();
        linkManager.initializeLinkUpdate(attachedToRoot);
        addLink(1, 2);
        linkManager.linkUpdate();
        assertTrue("must be referenced", isReferenced(2));
        assertFalse("must not be referenced", isReferenced(1));
        linkManager.initializeLinkUpdate(attachedToRoot);
        removeLink(1, 2);
        linkManager.linkUpdate();
        assertFalse("must not be referenced", isReferenced(1));
        assertFalse("must not be referenced", isReferenced(2));
    }

    private void addLink(final long referencing, final long referenced) {
        final DataRecordIdentifier referencingDataRecordIdentifier = new DataRecordIdentifier(referencing);
        final DataRecordIdentifier referencedDataRecordIdentifier = new DataRecordIdentifier(referenced);
        linkManager.addLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
    }

    private void removeLink(final long referencing, final long referenced) {
        final DataRecordIdentifier referencingDataRecordIdentifier = new DataRecordIdentifier(referencing);
        final DataRecordIdentifier referencedDataRecordIdentifier = new DataRecordIdentifier(referenced);
        linkManager.removeLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
    }

    private boolean isReferenced(final long identifier) throws GarbageException, ReferenceLinkGarbageException {
        final DataRecordIdentifier dataRecordIdentifier = new DataRecordIdentifier(identifier);
        return linkManager.isReferenced(dataRecordIdentifier);
    }
}
