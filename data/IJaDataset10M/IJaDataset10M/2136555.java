package net.sf.joafip.store.service.garbage.recordmgr;

import java.util.List;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.HeapException;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.store.entity.garbage.ReferenceLink;
import net.sf.joafip.store.entity.garbage.ReferenceLinkGarbageException;
import net.sf.joafip.store.service.garbage.GarbageException;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableException;

/**
 * test {@link LinkRecordManager}
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestLinkRecordManager extends AbstractGarbageManagerTestCase {

    private static final String MUST_CONTAINS_2 = "must contains 2";

    private static final String BAD_SIZE = "bad size";

    private final int[][] referenceList = new int[][] { { 0, 1 }, { 2, 3 }, { 2, 4 }, { 5, 6 }, { 7, 6 } };

    private final int[][] numberOfReferencingList = new int[][] { { 1, 1 }, { 3, 1 }, { 4, 1 }, { 6, 2 } };

    public TestLinkRecordManager() throws TestException {
        super();
    }

    public TestLinkRecordManager(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAdd() throws HeapException, HeapRecordableException, RBTException, GarbageException, ReferenceLinkGarbageException {
        storeHeaderSetup();
        addReferenceLink();
        checkNumberOfLink();
    }

    /**
	 * to check number of link after add
	 * 
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    private void checkNumberOfLink() throws GarbageException, ReferenceLinkGarbageException {
        for (int[] numberOfRefDef : numberOfReferencingList) {
            final DataRecordIdentifier referencedDataRecordIdentifier = newDataRecordIdentifier(numberOfRefDef[0]);
            final int expectedNumberOfReferencing = numberOfRefDef[1];
            final ReferenceLink referenceLink = linkRecordManager.searchByReferenced(referencedDataRecordIdentifier);
            assertNotNull("reference link must exist for " + referencedDataRecordIdentifier, referenceLink);
            final int numberOfReferencing = referenceLink.getNumberOfReferencing();
            assertEquals("bad number of referencing", expectedNumberOfReferencing, numberOfReferencing);
        }
    }

    public void testDuplicate() throws HeapException, HeapRecordableException, RBTException, GarbageException, ReferenceLinkGarbageException {
        storeHeaderSetup();
        addReferenceLink();
        final DataRecordIdentifier referencingDataRecordIdentifier = newDataRecordIdentifier(referenceList[0][0]);
        final DataRecordIdentifier referencedDataRecordIdentifier = newDataRecordIdentifier(referenceList[0][1]);
        appendOrUpdateReferenceLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        List<DataRecordIdentifier> list = linkRecordManager.getReferencingForReferenced(referencedDataRecordIdentifier);
        assertEquals(BAD_SIZE, 1, list.size());
        assertEquals("bad value", referencingDataRecordIdentifier, list.get(0));
        ReferenceLink referenceLink = linkRecordManager.searchByReferenced(referencedDataRecordIdentifier);
        int linkCount = referenceLink.getLinkCount(referencingDataRecordIdentifier);
        assertEquals("bad link count", 2, linkCount);
        removeReferenceLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        list = linkRecordManager.getReferencingForReferenced(referencedDataRecordIdentifier);
        assertEquals(BAD_SIZE, 1, list.size());
        referenceLink = linkRecordManager.searchByReferenced(referencedDataRecordIdentifier);
        linkCount = referenceLink.getLinkCount(referencingDataRecordIdentifier);
        assertEquals("bad link count", 1, linkCount);
    }

    public void testRemoveReferenced() throws HeapException, HeapRecordableException, RBTException, GarbageException, ReferenceLinkGarbageException {
        storeHeaderSetup();
        addReferenceLink();
        List<DataRecordIdentifier> list;
        linkRecordManager.removeLink(newDataRecordIdentifier(1));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(1));
        assertEquals(BAD_SIZE, 0, list.size());
        linkRecordManager.removeLink(newDataRecordIdentifier(6));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(6));
        assertEquals(BAD_SIZE, 0, list.size());
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(3));
        assertEquals(BAD_SIZE, 1, list.size());
        assertTrue(MUST_CONTAINS_2, list.contains(newDataRecordIdentifier(2)));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(4));
        assertEquals(BAD_SIZE, 1, list.size());
        assertTrue(MUST_CONTAINS_2, list.contains(newDataRecordIdentifier(2)));
    }

    public void testGetReference() throws GarbageException, HeapException, HeapRecordableException, RBTException, ReferenceLinkGarbageException {
        storeHeaderSetup();
        addReferenceLink();
        List<DataRecordIdentifier> list;
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(1));
        assertEquals(BAD_SIZE, 1, list.size());
        assertTrue("must contains 0", list.contains(newDataRecordIdentifier(0)));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(3));
        assertEquals(BAD_SIZE, 1, list.size());
        assertTrue(MUST_CONTAINS_2, list.contains(newDataRecordIdentifier(2)));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(4));
        assertEquals(BAD_SIZE, 1, list.size());
        assertTrue(MUST_CONTAINS_2, list.contains(newDataRecordIdentifier(2)));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(6));
        assertEquals(BAD_SIZE, 2, list.size());
        assertTrue("must conatins 5", list.contains(newDataRecordIdentifier(5)));
        assertTrue("must conatins 7", list.contains(newDataRecordIdentifier(7)));
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(0));
        assertEquals(BAD_SIZE, 0, list.size());
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(5));
        assertEquals(BAD_SIZE, 0, list.size());
        list = linkRecordManager.getReferencingForReferenced(newDataRecordIdentifier(7));
        assertEquals(BAD_SIZE, 0, list.size());
    }

    public void testAddRemoved() throws HeapException, HeapRecordableException, RBTException, GarbageException, ReferenceLinkGarbageException {
        storeHeaderSetup();
        addReferenceLink();
        final DataRecordIdentifier referencingDataRecordIdentifier = newDataRecordIdentifier(referenceList[0][0]);
        final DataRecordIdentifier referencedDataRecordIdentifier = newDataRecordIdentifier(referenceList[0][1]);
        linkManager.removeLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        linkManager.addLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        heapRecordableManager.save();
        dataManager.flush();
    }

    /**
	 * append reference link defined in {@link #referenceList}
	 * 
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    private void addReferenceLink() throws GarbageException, ReferenceLinkGarbageException {
        for (int[] reference : referenceList) {
            final DataRecordIdentifier referencingDataRecordIdentifier = newDataRecordIdentifier(reference[0]);
            final DataRecordIdentifier referencedDataRecordIdentifier = newDataRecordIdentifier(reference[1]);
            appendOrUpdateReferenceLink(referencingDataRecordIdentifier, referencedDataRecordIdentifier);
        }
    }

    /**
	 * append or update reference link
	 * 
	 * @param referencingDataRecordIdentifier
	 * @param referencedDataRecordIdentifier
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    private void appendOrUpdateReferenceLink(final DataRecordIdentifier referencingDataRecordIdentifier, final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final ReferenceLink referenceLink = linkRecordManager.searchByReferenced(referencedDataRecordIdentifier);
        if (referenceLink == null) {
            final ReferenceLink newReferenceLink = new ReferenceLink(referencedDataRecordIdentifier);
            newReferenceLink.updateLinkCount(referencingDataRecordIdentifier, 1);
            linkRecordManager.appendNewReferenceLink(newReferenceLink);
        } else {
            referenceLink.updateLinkCount(referencingDataRecordIdentifier, 1);
            linkRecordManager.updateReferenceLink(referenceLink);
        }
    }

    /**
	 * remove reference link
	 * 
	 * @param referencingDataRecordIdentifier
	 * @param referencedDataRecordIdentifier
	 * @return true if removed, false if link for referenced not exist
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    private boolean removeReferenceLink(final DataRecordIdentifier referencingDataRecordIdentifier, final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final boolean removed;
        final ReferenceLink referenceLink = linkRecordManager.searchByReferenced(referencedDataRecordIdentifier);
        if (referenceLink.getNode() == null) {
            removed = false;
        } else {
            referenceLink.updateLinkCount(referencingDataRecordIdentifier, -1);
            if (referenceLink.getNumberOfReferencing() == 0) {
                linkRecordManager.deleteReferenceLink(referenceLink);
            }
            removed = true;
        }
        return removed;
    }
}
