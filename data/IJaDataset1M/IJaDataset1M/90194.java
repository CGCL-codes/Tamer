package ch.enterag.utils.zip;

/** Implements a number of often used byte-buffer utilities.
 @author Hartwig Thomas
 */
public class BU {

    /** long bit mask for single byte */
    private static final long lBYTE_MASK = 0x00000000000000FFL;

    /** int bit mask for single byte */
    private static final int iBYTE_MASK = 0x000000FF;

    /** converts int to byte (unsigned)
   * @param i int to be converted.
   * @return resulting byte.
   */
    private static byte lowByte(int i) {
        i = i & iBYTE_MASK;
        if (i >= 0x00000080) i = -0x00000100 + i;
        return (byte) i;
    }

    /** extracts an short from 2 bytes of a byte buffer in little-endian
   * order.
   @param bufShort little-endian byte buffer of size => 2.
   @param iPos position of short
   @return extracted short.
   */
    public static short toShort(byte[] bufShort, int iPos) {
        short wShort = 0;
        int iByte = 0;
        if (bufShort.length < iPos + 2) throw new IllegalArgumentException("byte buffer for short must have length >= 2!");
        for (int i = iPos + 1; i >= iPos; i--) {
            wShort <<= 8;
            iByte = bufShort[i];
            iByte = iByte & iBYTE_MASK;
            wShort = (short) (wShort | iByte);
        }
        return wShort;
    }

    /** extracts an short from first 2 bytes of a byte buffer in little-endian
   * order.
   @param bufShort little-endian byte buffer of size => 2.
   @return extracted short.
   */
    public static short toShort(byte[] bufShort) {
        return toShort(bufShort, 0);
    }

    /** converts a short into a 2-byte little-endian byte buffer. 
   @param wShort short to be converted.
   @return little-endian byte buffer.
   */
    public static byte[] fromShort(short wShort) {
        byte[] bufShort = new byte[2];
        for (int i = 1; i >= 0; i--) {
            bufShort[1 - i] = lowByte(wShort);
            wShort >>= 8;
        }
        return bufShort;
    }

    /** extracts an int from 4 bytes of a byte buffer in little-endian
   * order.
   @param bufInt little-endian byte buffer of size => 4.
   @param iPos starting position.
   @return extracted int.
   */
    public static int toInt(byte[] bufInt, int iPos) {
        int iInt = 0;
        int iByte = 0;
        if (bufInt.length < iPos + 4) throw new IllegalArgumentException("byte buffer for int must have length >= 4!");
        for (int i = iPos + 3; i >= iPos; i--) {
            iInt <<= 8;
            iByte = bufInt[i];
            iByte = iByte & iBYTE_MASK;
            iInt |= iByte;
        }
        return iInt;
    }

    /** extracts an int from first 4 bytes of a byte buffer in little-endian
   * order.
   @param bufInt little-endian byte buffer of size => 4.
   @return extracted int.
   */
    public static int toInt(byte[] bufInt) {
        return toInt(bufInt, 0);
    }

    /** converts an int into a 4-byte little-endian byte buffer. 
   @param iInt int to be converted.
   @return little-endian byte buffer.
   */
    public static byte[] fromInt(int iInt) {
        byte[] bufInt = new byte[4];
        for (int i = 3; i >= 0; i--) {
            bufInt[3 - i] = lowByte(iInt);
            iInt >>= 8;
        }
        return bufInt;
    }

    /** extracts a long from 8 bytes of a byte buffer in little-endian
   * order.
   @param bufLong little-endian byte buffer of size => 8.
   @param iPos starting position.
   @return extracted long.
   */
    public static long toLong(byte[] bufLong, int iPos) {
        long lLong = 0;
        long lByte = 0;
        if (bufLong.length < iPos + 8) throw new IllegalArgumentException("byte buffer for long must have length >= 8!");
        for (int i = iPos + 7; i >= iPos; i--) {
            lLong <<= 8;
            lByte = bufLong[i];
            lByte = lByte & iBYTE_MASK;
            lLong |= lByte;
        }
        return lLong;
    }

    /** extracts a long from first 8 bytes of a byte buffer in little-endian
   * order.
   @param bufLong little-endian byte buffer of size => 8.
   @return extracted long.
   */
    public static long toLong(byte[] bufLong) {
        return toLong(bufLong, 0);
    }

    /** converts a long into a 8-byte little-endian byte buffer. 
   @param lLong long to be converted.
   @return little-endian byte buffer.
   */
    public static byte[] fromLong(long lLong) {
        byte[] bufLong = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bufLong[7 - i] = lowByte((int) (lLong & lBYTE_MASK));
            lLong >>= 8;
        }
        return bufLong;
    }
}
