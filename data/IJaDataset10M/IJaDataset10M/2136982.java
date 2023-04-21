package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Record which contains the size of the row and column gutters.  These are
 * all set to zero by default
 */
class GuttersRecord extends WritableRecordData {

    /**
   * The binary data
   */
    private byte[] data;

    /**
   * The rowGutter
   */
    private int rowGutter;

    /**
   * The column gutter
   */
    private int colGutter;

    /**
   * The maximum outline level for the row gutter
   */
    private int maxRowOutline;

    /**
   * The maximum row outline level for the column gutter
   */
    private int maxColumnOutline;

    /**
   * Constructor
   */
    public GuttersRecord() {
        super(Type.GUTS);
    }

    /**
   * Gets the binary data for output
   * 
   * @return the binary data
   */
    public byte[] getData() {
        data = new byte[8];
        IntegerHelper.getTwoBytes(rowGutter, data, 0);
        IntegerHelper.getTwoBytes(colGutter, data, 2);
        IntegerHelper.getTwoBytes(maxRowOutline, data, 4);
        IntegerHelper.getTwoBytes(maxColumnOutline, data, 6);
        return data;
    }

    /** 
   * Accessor for the maximum row outline
   *
   * @return the maximum row outline
   */
    public int getMaxRowOutline() {
        return maxRowOutline;
    }

    /** 
   * Sets the maximum row outline
   *
   * @param value the maximum row outline
   */
    public void setMaxRowOutline(int value) {
        maxRowOutline = value;
        rowGutter = 1 + 14 * value;
    }

    /** 
   * Accessor for the maximum column outline
   *
   * @return the maximum column outline
   */
    public int getMaxColumnOutline() {
        return maxColumnOutline;
    }

    /** 
   * Sets the maximum column outline
   *
   * @param value the maximum row outline
   */
    public void setMaxColumnOutline(int value) {
        maxColumnOutline = value;
        colGutter = 1 + 14 * value;
    }
}
