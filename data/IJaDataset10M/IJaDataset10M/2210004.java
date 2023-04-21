package com.cell.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import com.cell.CObject;

public class TextSerialize extends IOutput {

    private static void putNext(Writer out, Object src) throws IOException {
        {
            out.write(src + ",");
        }
    }

    public static void putString(Writer ostream, String string) throws IOException {
        if (string == null) {
            ostream.write("0,,");
        } else {
            ostream.write(string.length() + ",");
            ostream.write(string + ",");
        }
    }

    public static void putBytesString(Writer ostream, String string) throws IOException {
        if (string == null) {
            ostream.write("0,,");
        } else {
            byte[] data = string.getBytes(CObject.ENCODING);
            ostream.write(data.length + ",");
            ostream.write(string + ",");
        }
    }

    public static void putByte(Writer ostream, byte value) throws IOException {
        putNext(ostream, value);
    }

    public static void putUnsignedByte(Writer ostream, short value) throws IOException {
        if (value >= 256) {
            try {
                throw new Exception("the max number of a unsigned byte is 255!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        putNext(ostream, value);
    }

    public static void putBoolean(Writer ostream, boolean value) throws IOException {
        putNext(ostream, value);
    }

    public static void putShort(Writer ostream, short value) throws IOException {
        putNext(ostream, value);
    }

    public static void putUnsignedShort(Writer ostream, int value) throws IOException {
        putNext(ostream, value);
    }

    public static void putInt(Writer ostream, int value) throws IOException {
        putNext(ostream, value);
    }

    public static void putUnsignedInt(Writer ostream, long value) throws IOException {
        putNext(ostream, value);
    }

    public static void putLong(Writer ostream, long value) throws IOException {
        putNext(ostream, value);
    }

    public static void putFloat(Writer ostream, float value) throws IOException {
        putNext(ostream, value);
    }

    public static void putDouble(Writer ostream, double value) throws IOException {
        putNext(ostream, value);
    }

    public static void putStrings(Writer ostream, String[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putString(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putBytesStrings(Writer ostream, String[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putBytesString(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putBooleans(Writer ostream, boolean[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putBoolean(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putBytes(Writer ostream, byte[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putByte(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putUnsignedBytes(Writer ostream, short[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putUnsignedByte(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putShorts(Writer ostream, short[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putShort(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putUnsignedShorts(Writer ostream, int[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putUnsignedShort(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putInts(Writer ostream, int[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putInt(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putUnsignedInts(Writer ostream, long[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putUnsignedInt(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putLongs(Writer ostream, long[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putLong(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    public static void putFloats(Writer ostream, float[] value) throws IOException {
        if (value != null) {
            putUnsignedInt(ostream, value.length);
            for (int i = 0; i < value.length; ++i) putFloat(ostream, value[i]);
        } else {
            putUnsignedInt(ostream, 0);
        }
    }

    Writer ostream;

    public TextSerialize(Writer os) {
        ostream = os;
    }

    protected TextSerialize() {
    }

    public void putBoolean(boolean value) throws IOException {
        putBoolean(ostream, value);
    }

    public void putByte(byte value) throws IOException {
        putByte(ostream, value);
    }

    public void putChar(char value) throws IOException {
        putString(ostream, new String(new char[] { value }));
    }

    public void putInt(int value) throws IOException {
        putInt(ostream, value);
    }

    public void putLong(long value) throws IOException {
        putLong(ostream, value);
    }

    public void putShort(short value) throws IOException {
        putShort(ostream, value);
    }

    public void putString(String string, String charset) throws IOException {
        putString(ostream, string);
    }

    public void putString(String string) throws IOException {
        putString(ostream, string);
    }

    public void putUByte(short value) throws IOException {
        putUnsignedByte(ostream, value);
    }

    public void putUInt(long value) throws IOException {
        putUnsignedInt(ostream, value);
    }

    public void putUShort(int value) throws IOException {
        putUnsignedShort(ostream, value);
    }

    public void putFloat(float value) throws IOException {
        putFloat(ostream, value);
    }

    public void putDouble(double value) throws IOException {
        putDouble(ostream, value);
    }
}

;
