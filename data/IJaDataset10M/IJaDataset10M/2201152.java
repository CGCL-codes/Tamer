package org.tas.fileparser;

import java.util.EventObject;

/**
 * Event object to be used with when a RecordParsedEvent is fired from an 
 * AbstractRecordParser
 * @author Shane Steidley
 * @version $Id: RecordParsedEvent.java 58 2008-12-13 19:53:31Z ssteidl $
 */
public class RecordParsedEvent extends EventObject {

    private Record record = null;

    private int recordNumber = -1;

    /**
     * Create a RecordParsedEvent
     * @param parser The AbstractRecordParser
     * @param record The Record
     */
    public RecordParsedEvent(AbstractRecordParser parser, Record record, int recordNumber) {
        super(parser);
        setRecord(record);
        setRecordNumber(recordNumber);
    }

    /**
     * Return the Record
     * @return The Record
     */
    public Record getRecord() {
        return this.record;
    }

    /**
     * Set the Record
     * @param record The Record
     */
    protected void setRecord(Record record) {
        this.record = record;
    }

    /**
     * Get the record number for this record.  The record number is the absolute
     * index of the record starting at 1.  
     * @return The record number
     */
    public int getRecordNumber() {
        return recordNumber;
    }

    /**
     * Set the record number for this record.  The record number is the position
     * of the record compared to other records.
     */
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
}
