package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Stores the flag which indicates whether the version of excel can
 * understand natural language input for formulae
 */
class UsesElfsRecord extends WritableRecordData {

    /**
   * The binary data for output to file
   */
    private byte[] data;

    /**
   * The uses ELFs flag
   */
    private boolean usesElfs;

    /**
   * Constructor
   */
    public UsesElfsRecord() {
        super(Type.USESELFS);
        usesElfs = true;
        data = new byte[2];
        if (usesElfs) {
            data[0] = 1;
        }
    }

    /**
   * Gets the binary data for output to file
   * 
   * @return the binary data
   */
    public byte[] getData() {
        return data;
    }
}
