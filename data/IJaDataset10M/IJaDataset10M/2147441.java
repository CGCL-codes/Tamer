package jdbm.recman;

/**
 *  This class contains some test utilities.
 */
public class TestUtil {

    /**
     *  Creates a "record" containing "length" repetitions of the
     *  indicated byte.
     */
    public static byte[] makeRecord(int length, byte b) {
        byte[] retval = new byte[length];
        for (int i = 0; i < length; i++) retval[i] = b;
        return retval;
    }

    /**
     *  Checks whether the record has the indicated length and data
     */
    public static boolean checkRecord(byte[] data, int length, byte b) {
        if (data.length != length) {
            System.err.println("length doesn't match: expected " + length + ", got " + data.length);
            return false;
        }
        for (int i = 0; i < length; i++) if (data[i] != b) {
            System.err.println("byte " + i + " wrong: expected " + b + ", got " + data[i]);
            return false;
        }
        return true;
    }
}
