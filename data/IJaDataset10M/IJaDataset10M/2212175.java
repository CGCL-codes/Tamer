package ti.io;

import java.io.InputStream;

/**
 * This class adds support for reading unsigned numeric types.
 * 
 * @author Rob Clark
 */
public class UDataInputStream extends java.io.DataInputStream {

    /**
   * Class Constructor.
   * 
   * @param in          the input stream to read from
   */
    public UDataInputStream(java.io.InputStream in) {
        super(in);
    }

    /**
   * Read an unsigned byte.
   * 
   * @return an unsigned byte cast as an int
   * @exception java.io.IOException if an I/O error occurs
   */
    public short readUByte() throws java.io.IOException {
        return (short) (readByte() & 0xff);
    }

    /**
   * Read an unsigned short.  (2 bytes)
   * 
   * @return an unsigned two byte value cast as an int
   * @exception java.io.IOException if an I/O error occurs
   */
    public int readUShort() throws java.io.IOException {
        int b0 = (int) (readByte());
        int b1 = (int) (readByte());
        return (int) ((b1 & 0xff) | ((b0 & 0xff) << 8));
    }

    /**
   * Read an unsigned long.  (4 bytes)
   * 
   * @return an unsigned int value cast as a long
   * @exception java.io.IOException if an I/O error occurs
   */
    public long readUInt() throws java.io.IOException {
        int b0 = (int) (readByte());
        int b1 = (int) (readByte());
        int b2 = (int) (readByte());
        int b3 = (int) (readByte());
        return (((b3 & 0xff) | ((b2 & 0xff) << 8) | ((b1 & 0xff) << 16) | ((b0 & 0xff) << 24)) & 0x00000000ffffffffL);
    }

    public void setIn(InputStream in) {
        this.in = in;
    }
}
