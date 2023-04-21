package net.sf.joafip.heapfile.service;

import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.kvstore.entity.HeapFileSetup;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.kvstore.service.AbstractTestHeapDataManagerRecord;
import net.sf.joafip.kvstore.service.HeapException;
import net.sf.joafip.kvstore.service.IHeapDataManager;

@NotStorableClass
@DoNotTransform
public class TestHeapFileDataManagerFreeing extends AbstractTestHeapDataManagerRecord {

    public TestHeapFileDataManagerFreeing() throws TestException {
        super();
    }

    public TestHeapFileDataManagerFreeing(final String name) throws TestException {
        super(name);
    }

    @Override
    protected IHeapDataManager createHeapDataManager(final HeapFileSetup setup) throws HeapException {
        return new HeapFileDataManager(setup, false);
    }

    @Override
    protected void createHeap(final boolean removeFile) throws HeapException {
        createFileHeapDataManager(removeFile);
    }

    @Override
    protected boolean manageFreeRecord() {
        return true;
    }

    @Override
    protected void checkIntegrity() throws HeapException {
        HeapFileDataManagerIntegrityChecker.getInstance().checkIntegrity(heapDataManager);
    }

    /**
	 * create record #0,#1, and #2<br>
	 * then delete #0, then #1, then #2<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree1() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(0);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }

    /**
	 * create record #0,#1, and #2<br>
	 * then delete #2, then #1, then #0<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree2() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(0);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }

    /**
	 * create record #0,#1, and #2<br>
	 * then delete #0, then #2, then #1<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree3() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(0);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }

    /**
	 * create record #0,#1,#2,#3 and #4<br>
	 * then delete #1, then #2, then #3<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree4() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        createRecord(40, 3);
        createRecord(40, 4);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(3);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }

    /**
	 * create record #0,#1,#2,#3 and #4<br>
	 * then delete #3, then #2, then #1<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree5() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        createRecord(40, 3);
        createRecord(40, 4);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(3);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }

    /**
	 * create record #0,#1,#2,#3 and #4<br>
	 * then delete #1, then #3, then #2<br>
	 * 
	 * @throws HeapException
	 */
    public void testFree6() throws HeapException {
        createHeap(true);
        createRecord(20, 0);
        createRecord(30, 1);
        createRecord(40, 2);
        createRecord(40, 3);
        createRecord(40, 4);
        closeHeap();
        createHeap(false);
        checkIntegrity();
        DataRecordIdentifier identifier = new DataRecordIdentifier(1);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(3);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        identifier = new DataRecordIdentifier(2);
        heapDataManager.deleteDataRecord(identifier);
        checkIntegrity();
        closeHeap();
        createHeap(false);
        checkIntegrity();
    }
}
