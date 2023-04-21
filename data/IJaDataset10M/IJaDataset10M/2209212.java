package jxl.biff;

import jxl.common.Logger;
import jxl.read.biff.Record;

/**
 * Range information for conditional formatting
 */
public class AutoFilterInfoRecord extends WritableRecordData {

    private static Logger logger = Logger.getLogger(AutoFilterInfoRecord.class);

    /**
   * The data
   */
    private byte[] data;

    /**
   * Constructor
   */
    public AutoFilterInfoRecord(Record t) {
        super(t);
        data = getRecord().getData();
    }

    /**
   * Retrieves the data for output to binary file
   * 
   * @return the data to be written
   */
    public byte[] getData() {
        return data;
    }
}
