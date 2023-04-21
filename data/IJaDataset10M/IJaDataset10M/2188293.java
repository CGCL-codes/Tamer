package jxl.biff;

/**
 * Class to help handle doubles
 */
public class DoubleHelper {

    /**
   * Private constructor to prevent instantiation
   */
    private DoubleHelper() {
    }

    /**
   * Gets the IEEE value from the byte array passed in
   *
   * @param pos the position in the data block which contains the double value
   * @param data the data block containing the raw bytes
   * @return the double value converted from the raw data
   */
    public static double getIEEEDouble(byte[] data, int pos) {
        int num1 = IntegerHelper.getInt(data[pos], data[pos + 1], data[pos + 2], data[pos + 3]);
        int num2 = IntegerHelper.getInt(data[pos + 4], data[pos + 5], data[pos + 6], data[pos + 7]);
        boolean negative = ((num2 & 0x80000000) != 0);
        long val = ((num2 & 0x7fffffff) * 0x100000000L) + (num1 < 0 ? 0x100000000L + num1 : num1);
        double value = Double.longBitsToDouble(val);
        if (negative) {
            value = -value;
        }
        return value;
    }

    /**
   * Puts the IEEE representation of the double provided into the array
   * at the designated position
   *
   * @param target the data block into which the binary representation is to
   *     be placed
   * @param pos the position in target in which to place the bytes
   * @param d the double value to convert to raw bytes
   */
    public static void getIEEEBytes(double d, byte[] target, int pos) {
        long val = Double.doubleToLongBits(d);
        target[pos] = (byte) (val & 0xff);
        target[pos + 1] = (byte) ((val & 0xff00) >> 8);
        target[pos + 2] = (byte) ((val & 0xff0000) >> 16);
        target[pos + 3] = (byte) ((val & 0xff000000) >> 24);
        target[pos + 4] = (byte) ((val & 0xff00000000L) >> 32);
        target[pos + 5] = (byte) ((val & 0xff0000000000L) >> 40);
        target[pos + 6] = (byte) ((val & 0xff000000000000L) >> 48);
        target[pos + 7] = (byte) ((val & 0xff00000000000000L) >> 56);
    }
}
