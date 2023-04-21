package net.sf.beatrix.lang;

public class Bytes {

    /**
   * System.arraycopy doesn't work :/ -> ArrayStoreException
   */
    public static byte[] toPrimitiveByteArray(Byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    public static Byte[] toObjectByteArray(byte[] data) {
        Byte[] result = new Byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    public static short toUnsigned(byte b) {
        return (short) (b < 0 ? Math.pow(2, Byte.SIZE) + b : b);
    }
}
