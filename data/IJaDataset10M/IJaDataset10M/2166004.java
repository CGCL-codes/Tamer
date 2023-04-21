package ParsingFramework.MicroOperations;

import ParsingFramework.IParseOperations;
import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Micro Parser for Parsing and Composing Integers from/to binary
 * data.
 * @author Paul Grace
 */
public class BinaryInteger {

    private static IParseOperations systemParser;

    public BinaryInteger(IParseOperations parent) {
        BinaryInteger.systemParser = parent;
    }

    /**
     * Covert a bit array to a Java int value
     * @param data The array of bits
     * @return a Java int
     */
    public static int inInteger(BitSet data) {
        return inInteger(systemParser.toByteArray((BitSet) data));
    }

    /**
     * Covert a byte array to a Java int value
     * @param bytes The array of bytes
     * @return a Java int
     */
    public static int inInteger(byte[] bytes) {
        int padding = 4 - bytes.length;
        byte[] result = new byte[4];
        for (int i = 0; i < padding; i++) {
            result[i] = 0x00;
        }
        int j = 0;
        for (int i = padding; i < 4; i++) {
            result[i] = bytes[j++];
        }
        ByteBuffer buf = ByteBuffer.wrap(result);
        return buf.getInt();
    }

    public static long inLong(byte[] bytes) {
        int padding = 8 - bytes.length;
        byte[] result = new byte[8];
        for (int i = 0; i < padding; i++) {
            result[i] = 0x00;
        }
        int j = 0;
        for (int i = padding; i < 8; i++) {
            result[i] = bytes[j++];
        }
        ByteBuffer buf = ByteBuffer.wrap(result);
        return buf.getLong();
    }

    /**
     *
     * @param Value
     * @param Size
     * @return
     */
    public BitSet outInteger(Integer Value, int Size) {
        BitSet results = new BitSet(Size);
        byte[] result = null;
        if (Size < 8) {
            result = new byte[1];
            result[0] = (byte) (Value.intValue());
        } else result = new byte[Size / 8];
        int j = 0;
        for (int i = 0; i < Size / 8; i++) {
            result[i] = (byte) (Value.intValue() >>> (i * 8));
        }
        for (int i = 0; i < Size; i++) {
            if ((result[result.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                results.set(i);
            }
        }
        return results;
    }

    public BitSet outLong(Long Value, int Size) {
        BitSet results = new BitSet(Size);
        byte[] result = null;
        if (Size < 8) {
            result = new byte[1];
            result[0] = (byte) (Value.intValue());
        } else result = new byte[Size / 8];
        int j = 0;
        for (int i = 0; i < Size / 8; i++) {
            result[i] = (byte) (Value.intValue() >>> (i * 8));
        }
        for (int i = 0; i < Size; i++) {
            if ((result[result.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                results.set(i);
            }
        }
        return results;
    }

    public BitSet outInteger(String Value, int Size) {
        Integer iVal = new Integer(Value);
        return outInteger(iVal, Size);
    }
}
