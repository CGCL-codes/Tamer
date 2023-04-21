package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Contains the list of explicit horizontal page breaks on the current sheet
 */
class HorizontalPageBreaksRecord extends WritableRecordData {

    /**
   * The row breaks
   */
    private int[] rowBreaks;

    /**
   * Constructor
   * 
   * @param break the row breaks
   */
    public HorizontalPageBreaksRecord(int[] breaks) {
        super(Type.HORIZONTALPAGEBREAKS);
        rowBreaks = breaks;
    }

    /**
   * Gets the binary data to write to the output file
   * 
   * @return the binary data
   */
    public byte[] getData() {
        byte[] data = new byte[rowBreaks.length * 6 + 2];
        IntegerHelper.getTwoBytes(rowBreaks.length, data, 0);
        int pos = 2;
        for (int i = 0; i < rowBreaks.length; i++) {
            IntegerHelper.getTwoBytes(rowBreaks[i], data, pos);
            IntegerHelper.getTwoBytes(0xff, data, pos + 4);
            pos += 6;
        }
        return data;
    }
}
