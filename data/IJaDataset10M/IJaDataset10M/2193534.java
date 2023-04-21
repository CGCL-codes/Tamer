package net.galacticwar.lua;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * Provides methods for reading data streams in Little Endian format
 * @author Yoann Meste (aka Mancer)
 *
 */
public class LittleEndianInputStream extends FilterInputStream {

    public LittleEndianInputStream(InputStream in) {
        super(in);
    }

    public boolean readBoolean() throws IOException {
        int bool = in.read();
        if (bool == -1) throw new EOFException();
        return (bool != 0);
    }

    public byte readByte() throws IOException {
        int temp = in.read();
        return (byte) temp;
    }

    public int readUnsignedByte() throws IOException {
        int temp = in.read();
        if (temp == -1) throw new EOFException();
        return temp;
    }

    public short readShort() throws IOException {
        int byte1 = in.read();
        int byte2 = in.read();
        if (byte2 == -1) throw new EOFException();
        return (short) (((byte2 << 24) >>> 16) + (byte1 << 24) >>> 24);
    }

    public int readUnsignedShort() throws IOException {
        int byte1 = in.read();
        int byte2 = in.read();
        if (byte2 == -1) throw new EOFException();
        return ((byte2 << 24) >> 16) + ((byte1 << 24) >> 24);
    }

    public char readChar() throws IOException {
        int byte1 = in.read();
        int byte2 = in.read();
        if (byte2 == -1) throw new EOFException();
        return (char) (((byte2 << 24) >>> 16) + ((byte1 << 24) >>> 24));
    }

    public int readInt16() throws IOException {
        int v = in.read();
        v += (in.read() << 8);
        return v;
    }

    public int readInt32() throws IOException {
        int v = in.read();
        v += (in.read() << 8);
        v += (in.read() << 16);
        v += (in.read() << 24);
        return v;
    }

    public long readLong() throws IOException {
        long byte1 = in.read();
        long byte2 = in.read();
        long byte3 = in.read();
        long byte4 = in.read();
        long byte5 = in.read();
        long byte6 = in.read();
        long byte7 = in.read();
        long byte8 = in.read();
        if (byte8 == -1) {
            throw new EOFException();
        }
        return (byte8 << 56) + ((byte7 << 56) >>> 8) + ((byte6 << 56) >>> 16) + ((byte5 << 56) >>> 24) + ((byte4 << 56) >>> 32) + ((byte3 << 56) >>> 40) + ((byte2 << 56) >>> 48) + ((byte1 << 56) >>> 56);
    }

    public float[] readVector2() throws IOException {
        float[] vector = new float[2];
        vector[0] = readFloat();
        vector[1] = readFloat();
        return vector;
    }

    public float[] readVector3() throws IOException {
        float[] vector = new float[3];
        vector[0] = readFloat();
        vector[1] = readFloat();
        vector[2] = readFloat();
        return vector;
    }

    public float[] readVector4() throws IOException {
        float[] vector = new float[4];
        vector[0] = readFloat();
        vector[1] = readFloat();
        vector[2] = readFloat();
        vector[3] = readFloat();
        return vector;
    }

    public String readString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b;
        while ((b = readByte()) != 0) {
            baos.write(b);
        }
        return new String(baos.toByteArray());
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt32());
    }

    public final int skipBytes(int n) throws IOException {
        for (int i = 0; i < n; i += (int) skip(n - i)) ;
        return n;
    }
}
