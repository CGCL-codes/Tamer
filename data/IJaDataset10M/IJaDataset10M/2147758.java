package java.io;

/**
 * @author "Michael Maaser"
 * 
 */
public class DataInputStream extends InputStream implements DataInput {

    private InputStream in;

    public DataInputStream(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("underlying InputStream must not be null");
        }
        this.in = in;
    }

    public int read() throws IOException {
        return in.read();
    }

    public boolean readBoolean() throws IOException {
        return in.read() > 0;
    }

    public byte readByte() throws IOException {
        return (byte) in.read();
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

    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("cannot read in buffer null");
        }
        for (int i = off; i < len; i++) {
            b[i] = (byte) in.read();
        }
    }

    public int readInt() throws IOException {
        return (((in.read() & 0xff) << 24) | ((in.read() & 0xff) << 16) | ((in.read() & 0xff) << 8) | (in.read() & 0xff));
    }

    public long readLong() throws IOException {
        return (((long) (in.read() & 0xff) << 56) | ((long) (in.read() & 0xff) << 48) | ((long) (in.read() & 0xff) << 40) | ((long) (in.read() & 0xff) << 32) | ((long) (in.read() & 0xff) << 24) | ((long) (in.read() & 0xff) << 16) | ((long) (in.read() & 0xff) << 8) | ((long) (in.read() & 0xff)));
    }

    public short readShort() throws IOException {
        return (short) ((in.read() << 8) | (in.read() & 0xff));
    }

    public String readUTF() throws IOException {
        short len = readShort();
        byte[] buffer = new byte[len];
        StringBuffer sb = new StringBuffer();
        if (read(buffer) != len) {
            throw new UTFDataFormatException("could not read enough bytes from underlying stream");
        }
        for (int i = 0; i < buffer.length; i++) {
            if ((buffer[i] & 0x80) == 0) {
                sb.append(buffer[i] | 0x007f);
            } else if ((buffer[i] & 0xe0) == 0xc0) {
                if ((buffer[i + 1] & 0xc0) == 0x80) {
                    sb.append((char) (((buffer[i] & 0x1F) << 6) | (buffer[++i] & 0x3F)));
                } else {
                    throw new UTFDataFormatException("pattern mismatch in second byte");
                }
            } else if ((buffer[i] & 0xf0) == 0xe0) {
                if ((buffer[i + 1] & 0xc0) == 0x80) {
                    if ((buffer[i + 2] & 0xc0) == 0x80) {
                        sb.append((char) (((buffer[i] & 0x0F) << 12) | ((buffer[++i] & 0x3F) << 6) | (buffer[++i] & 0x3F)));
                    } else {
                        throw new UTFDataFormatException("pattern mismatch in third byte");
                    }
                } else {
                    throw new UTFDataFormatException("pattern mismatch in second byte");
                }
            } else {
                throw new UTFDataFormatException("pattern mismatch");
            }
        }
        return sb.toString();
    }

    public int readUnsignedByte() throws IOException {
        return 0x000000ff & in.read();
    }

    public int readUnsignedShort() throws IOException {
        return (((in.read() & 0xff) << 8) | (in.read() & 0xff));
    }

    public int skipBytes(int n) throws IOException {
        return (int) in.skip(n);
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        in.close();
    }

    public void mark(int readlimit) {
        in.mark(readlimit);
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    public void reset() throws IOException {
        in.reset();
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }
}
