package utils;

import java.nio.ByteBuffer;

public class NumberUtils {

    public static long decodeLong(byte[] value, int start) {
        ByteBuffer buf = ByteBuffer.wrap(value, start, value.length - start);
        return buf.getLong();
    }

    public static void encodeLong(byte[] value, int start, long number) {
        ByteBuffer buf = ByteBuffer.wrap(value, start, value.length - start);
        buf.putLong(number);
    }

    public static int decodeInt(byte[] value, int start) {
        ByteBuffer buf = ByteBuffer.wrap(value, start, value.length - start);
        return buf.getInt();
    }

    public static void encodeInt(byte[] value, int start, int number) {
        ByteBuffer buf = ByteBuffer.wrap(value, start, value.length - start);
        buf.putInt(number);
    }
}
