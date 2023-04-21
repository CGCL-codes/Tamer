package ParsingFramework.MicroOperations;

import ParsingFramework.IParseOperations;
import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Micro Parser for Parsing and Composing Integers from/to binary
 * data.
 * @author Paul Grace
 */
public class BinaryDouble {

    private static IParseOperations systemParser;

    public BinaryDouble(IParseOperations parent) {
        BinaryDouble.systemParser = parent;
    }

    public double inDouble(byte[] bytes) {
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
        return Double.longBitsToDouble(buf.getLong());
    }

    public BitSet outDouble(Double val, int Size) {
        Long Value = Double.doubleToLongBits(val);
        BitSet results = new BitSet(Size);
        byte[] result = null;
        if (Size < 8) {
            result = new byte[1];
            result[0] = (byte) (Value.longValue());
        } else result = new byte[Size / 8];
        int j = 0;
        for (int i = 0; i < Size / 8; i++) {
            result[i] = (byte) (Value.longValue() >>> (i * 8));
        }
        for (int i = 0; i < Size; i++) {
            if ((result[result.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                results.set(i);
            }
        }
        return results;
    }
}
