package net.sourceforge.fraglets.codec;

import java.io.UnsupportedEncodingException;

/**
 * An UTF-8 encoder able to encode UCS-4 in addition to UCS-2. See
 * standards ISO/IEC 10646-1:1993 and RFC2279, RFC2781.
 * 
 * @author  marion@users.sourceforge.net
 * @version $Revision: 1.7 $
 */
public class UTF8Encoder {

    /** Holds value of property buffer. */
    private byte[] buffer = new byte[12];

    /** Holds value of property size. */
    private int size = 0;

    /** Creates a new instance of UTF8Encoder */
    public UTF8Encoder() {
    }

    /**
	 * Encode a string of UCS-4 values in UTF-8.
	 * 
	 * @param data UCS-4 encoded int array to encode
	 * @param off where to start in data
	 * @param len how many values to process
	 */
    public void encodeUCS4(int data[], int off, int len) {
        while (--len >= 0) {
            encodeUCS4(data[off++]);
        }
    }

    /**
     * Encode a string  of UCS-2 values in UTF-8.
     * 
	 * @param data character array to encode
	 * @param off where to start in data
	 * @param len how many values to process
	 */
    public void encodeUCS2(char data[], int off, int len) {
        while (--len >= 0) {
            int datum = data[off++];
            if ((datum & 0xf800) == 0xd800) {
                if (datum > 0xdbff) {
                    throw new IllegalArgumentException("invalid surrogate high-half: " + datum);
                } else if (--len < 0) {
                    throw new IllegalArgumentException("truncated surrogate");
                }
                int high = (datum & 0x3ff) << 10;
                datum = data[off++];
                if (datum < 0xdc00 || datum > 0xdfff) {
                    throw new IllegalArgumentException("invalid surrogate low-half: " + datum);
                }
                datum = (high | (datum & 0x3ff)) + 0x10000;
            }
            encodeUCS4(datum);
        }
    }

    /**
	 * Encode a string in UTF-8.
	 * 
	 * @param data string to encode
	 */
    public void encodeString(String data) {
        char copy[] = data.toCharArray();
        encodeUCS2(copy, 0, copy.length);
    }

    /**
	 * Encode a UCS-4 datum in UTF-8.
	 * 
	 * @param datum the datum to encode
	 */
    public final void encodeUCS4(int datum) {
        if (datum < 0) {
            throw new IllegalArgumentException("argument out of range: " + datum);
        } else if (datum <= 0x7f) {
            growBuffer(1);
            buffer[size++] = (byte) (datum & 0xff);
        } else if (datum <= 0x7ff) {
            growBuffer(2);
            buffer[size++] = (byte) (0xc0 | (datum >> 6));
            buffer[size++] = (byte) (0x80 | (datum & 0x3f));
        } else if (datum <= 0xffff) {
            growBuffer(3);
            buffer[size++] = (byte) (0xe0 | (datum >> 12));
            buffer[size++] = (byte) (0x80 | ((datum >> 6) & 0x3f));
            buffer[size++] = (byte) (0x80 | (datum & 0x3f));
        } else if (datum <= 0x1fffff) {
            growBuffer(4);
            buffer[size++] = (byte) (0xf0 | (datum >> 18));
            buffer[size++] = (byte) (0x80 | ((datum >> 12) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 6) & 0x3f));
            buffer[size++] = (byte) (0x80 | (datum & 0x3f));
        } else if (datum <= 0x3ffffff) {
            growBuffer(5);
            buffer[size++] = (byte) (0xf8 | (datum >> 24));
            buffer[size++] = (byte) (0x80 | ((datum >> 18) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 12) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 6) & 0x3f));
            buffer[size++] = (byte) (0x80 | (datum & 0x3f));
        } else {
            growBuffer(6);
            buffer[size++] = (byte) (0xfc | (datum >> 30));
            buffer[size++] = (byte) (0x80 | ((datum >> 24) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 18) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 12) & 0x3f));
            buffer[size++] = (byte) (0x80 | ((datum >> 6) & 0x3f));
            buffer[size++] = (byte) (0x80 | (datum & 0x3f));
        }
    }

    /**
     * Getter for property size.
     * 
     * @return Value of property size.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Clear the encoder.
     */
    public void clear() {
        this.size = 0;
    }

    /**
     * Return a copy of the current buffer,
     * trimmed to the current size.
     * 
     * @return the copied buffer as a byte array
     */
    public byte[] toByteArray() {
        byte result[] = new byte[getSize()];
        if (result.length > 0) {
            System.arraycopy(buffer, 0, result, 0, result.length);
        }
        return result;
    }

    /**
     * Grow the current buffer so that it fits size+amount.
     * 
     * @param amount the amount to grow the buffer
     */
    protected final void growBuffer(int amount) {
        if (size + amount > buffer.length) {
            int more = Math.max(size + amount, (size + size >> 2));
            byte grow[] = new byte[more];
            if (buffer != null && buffer.length > 0) {
                System.arraycopy(buffer, 0, grow, 0, size);
            }
            buffer = grow;
        }
    }
}
