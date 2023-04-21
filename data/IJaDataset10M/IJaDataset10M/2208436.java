package affd.hfst;

/**
 * A way of handling unsigned little-endian data
 */
public class ByteArray {

    public byte[] bytes;

    public int index;

    public ByteArray(int size) {
        bytes = new byte[size];
        index = 0;
    }

    public int getUShort() throws java.io.IOException {
        int result = 0;
        result |= (bytes[index + 1] & 0xFF);
        result <<= 8;
        result |= (bytes[index] & 0xFF);
        index += 2;
        return result;
    }

    public long getUInt() throws java.io.IOException {
        long result = 0;
        result |= (bytes[index + 3] & 0xFF);
        result <<= 8;
        result |= (bytes[index + 2] & 0xFF);
        result <<= 8;
        result |= (bytes[index + 1] & 0xFF);
        result <<= 8;
        result |= (bytes[index] & 0xFF);
        index += 4;
        return result;
    }

    public Boolean getBool() throws java.io.IOException {
        if (this.getUInt() == 0) {
            return false;
        }
        return true;
    }

    public float getFloat() throws java.io.IOException {
        int bits = 0;
        bits |= (bytes[index + 3] & 0xFF);
        bits <<= 8;
        bits |= (bytes[index + 2] & 0xFF);
        bits <<= 8;
        bits |= (bytes[index + 1] & 0xFF);
        bits <<= 8;
        bits |= (bytes[index] & 0xFF);
        index += 4;
        return Float.intBitsToFloat(bits);
    }
}
