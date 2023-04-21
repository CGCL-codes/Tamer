package jxl.write.biff;

import java.util.List;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.write.Number;

/**
 * Contains an array of RK numbers
 */
class MulRKRecord extends WritableRecordData {

    /**
   * The row  containing these numbers
   */
    private int row;

    /**
   * The first column these rk number occur on
   */
    private int colFirst;

    /**
   * The last column these rk number occur on
   */
    private int colLast;

    /**
   * The array of rk numbers
   */
    private int[] rknumbers;

    /**
   * The array of xf indices
   */
    private int[] xfIndices;

    /**
   * Constructs the rk numbers from the integer cells
   * 
   * @param numbers A list of jxl.write.Number objects
   */
    public MulRKRecord(List numbers) {
        super(Type.MULRK);
        row = ((Number) numbers.get(0)).getRow();
        colFirst = ((Number) numbers.get(0)).getColumn();
        colLast = colFirst + numbers.size() - 1;
        rknumbers = new int[numbers.size()];
        xfIndices = new int[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            rknumbers[i] = (int) ((Number) numbers.get(i)).getValue();
            xfIndices[i] = ((CellValue) numbers.get(i)).getXFIndex();
        }
    }

    /**
   * Gets the binary data for output to file
   * 
   * @return the binary data
   */
    public byte[] getData() {
        byte[] data = new byte[rknumbers.length * 6 + 6];
        IntegerHelper.getTwoBytes(row, data, 0);
        IntegerHelper.getTwoBytes(colFirst, data, 2);
        int pos = 4;
        int rkValue = 0;
        byte[] rkBytes = new byte[4];
        for (int i = 0; i < rknumbers.length; i++) {
            IntegerHelper.getTwoBytes(xfIndices[i], data, pos);
            rkValue = rknumbers[i] << 2;
            rkValue |= 0x2;
            IntegerHelper.getFourBytes(rkValue, data, pos + 2);
            pos += 6;
        }
        IntegerHelper.getTwoBytes(colLast, data, pos);
        return data;
    }
}
