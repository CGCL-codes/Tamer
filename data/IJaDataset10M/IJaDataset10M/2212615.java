package net.sf.joafip.heapfile.service;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.heapfile.record.entity.HeapHeader;
import net.sf.joafip.heapfile.record.entity.HeapRecord;
import net.sf.joafip.kvstore.record.service.IHeapElementManager;
import net.sf.joafip.kvstore.service.HeapException;

/**
 * heap record iterator<br>
 * make able to browse heap record in their order in heap file<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class HeapRecordIterator {

    private final IHeapElementManager manager;

    private long lastRecordPositionInFile;

    private int recordCount;

    private long expectedPreviousRecordPositionInFile;

    private long positionInFile;

    public HeapRecordIterator(final IHeapElementManager manager) throws HeapException {
        super();
        this.manager = manager;
    }

    public HeapHeader getHeapHeader() throws HeapException {
        final HeapHeader header;
        final long heapSize = manager.getFileSize();
        if (heapSize == 0) {
            header = null;
        } else {
            header = (HeapHeader) manager.getHeapHeader();
            lastRecordPositionInFile = header.getLastRecordPositionInFile();
            recordCount = 0;
            positionInFile = header.getPositionInFile() + HeapHeader.HEAP_HEADER_SIZE;
            assert positionInFile == HeapHeader.HEAP_HEADER_SIZE : "bad position for first record";
            expectedPreviousRecordPositionInFile = -1;
        }
        return header;
    }

    public HeapRecord getNextHeapRecord() throws HeapException {
        final HeapRecord heapRecord;
        if (positionInFile <= lastRecordPositionInFile) {
            heapRecord = (HeapRecord) manager.readHeapFileDataRecord(positionInFile);
            recordCount++;
            final long previousRecordPositionInFile = heapRecord.getPreviousRecordPositionInFile();
            assert previousRecordPositionInFile == expectedPreviousRecordPositionInFile : "bad previous position in file for record #" + recordCount + " position=" + positionInFile + " expected=" + expectedPreviousRecordPositionInFile + " in record=" + previousRecordPositionInFile;
            expectedPreviousRecordPositionInFile = positionInFile;
            final long nextRecordFilePosition = heapRecord.getNextRecordFilePosition();
            positionInFile += heapRecord.getRecordSize();
            assert !(nextRecordFilePosition == -1 && positionInFile <= lastRecordPositionInFile || nextRecordFilePosition != -1 && positionInFile != nextRecordFilePosition) : "bad next record position in file";
        } else {
            heapRecord = null;
        }
        return heapRecord;
    }
}
