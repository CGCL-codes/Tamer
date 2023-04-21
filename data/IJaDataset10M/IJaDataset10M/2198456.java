package net.oesterholt.jndbm.streams;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NDbmDataInputStream implements DataInput {

    private InputStream _in = null;

    public void reset(InputStream i) {
        _in = i;
    }

    public NDbmDataInputStream(InputStream in) {
        _in = in;
    }

    public String readLine() throws IOException {
        throw new IOException("This method is not implemented");
    }

    public int readUnsignedByte() throws IOException {
        throw new IOException("This method is not implemented");
    }

    public int readUnsignedShort() throws IOException {
        throw new IOException("This method is not implemented");
    }

    public void readFully(byte[] arg0) throws IOException {
        throw new IOException("This method is not implemented");
    }

    public void readFully(byte[] arg0, int arg1, int arg2) throws IOException {
        throw new IOException("This method is not implemented");
    }

    public boolean readBoolean() throws IOException {
        int b = _in.read();
        if (b == 'T') {
            return true;
        } else {
            return false;
        }
    }

    public byte readByte() throws IOException {
        return (byte) _in.read();
    }

    public char readChar() throws IOException {
        return (char) readShort();
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public static int getInt(byte[] b) {
        return ((((int) b[5]) - ' ') << 30) | ((((int) b[4]) - ' ') << 24) | ((((int) b[3]) - ' ') << 18) | ((((int) b[2]) - ' ') << 12) | ((((int) b[1]) - ' ') << 6) | (((int) b[0]) - ' ');
    }

    public int readInt() throws IOException {
        byte[] b = { 0, 0, 0, 0, 0, 0 };
        _in.read(b);
        return getInt(b);
    }

    public long readLong() throws IOException {
        byte[] b = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        _in.read(b);
        long i = ((((long) b[10]) - ' ') << 60) | ((((long) b[9]) - ' ') << 54) | ((((long) b[8]) - ' ') << 48) | ((((long) b[7]) - ' ') << 42) | ((((long) b[6]) - ' ') << 36) | ((((long) b[5]) - ' ') << 30) | ((((long) b[4]) - ' ') << 24) | ((((long) b[3]) - ' ') << 18) | ((((long) b[2]) - ' ') << 12) | ((((long) b[1]) - ' ') << 6) | (((long) b[0]) - ' ');
        return i;
    }

    public short readShort() throws IOException {
        byte[] b = { 0, 0, 0 };
        _in.read(b);
        int c = ((((int) b[2]) - ' ') << 12) | ((((int) b[1]) - ' ') << 6) | (((int) b[0]) - ' ');
        return (short) c;
    }

    public static int sizeOfShort() {
        return 3;
    }

    public static int sizeOfInt() {
        return 6;
    }

    public static int sizeOfLong() {
        return 11;
    }

    public String readUTF() throws IOException {
        int l = readInt();
        byte[] b = new byte[l];
        _in.read(b);
        return new String(b, "UTF-8");
    }

    public int skipBytes(int b) throws IOException {
        return (int) _in.skip(b);
    }
}
