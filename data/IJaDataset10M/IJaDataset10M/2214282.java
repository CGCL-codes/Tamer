package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Stores the default character set in operation when the workbook was
 * saved
 */
class CodepageRecord extends WritableRecordData {

    /**
   * The binary data
   */
    private byte[] data;

    /**
   * Constructor
   */
    public CodepageRecord() {
        super(Type.CODEPAGE);
        data = new byte[] { (byte) 0xe4, (byte) 0x4 };
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
