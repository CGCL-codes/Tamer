package net.sourceforge.jtds.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * This class contains methods to read or write data to and from the TDS data stream.
 * @author Mike Hutchinson.
 *
 */
public final class TdsStream {

    /** The shared network socket. */
    private final TdsSocket socket;

    /** The output packet buffer. */
    private byte[] outBuffer;

    /** The offset of the next byte to write. */
    private int outBufferPtr;

    /** The request packet type. */
    private byte pktType;

    /** True if stream is closed. */
    private boolean isClosed;

    /** The current output buffer size*/
    private int bufferSize;

    /** The Input packet buffer. */
    private byte[] inBuffer;

    /** The offset of the next byte to read. */
    private int inBufferPtr;

    /** The length of current input packet. */
    private int inBufferLen;

    /** A shared byte buffer. */
    private final byte[] byteBuffer = new byte[8000];

    /** A shared char buffer. */
    private final char[] charBuffer = new char[4000];

    /** The parent TdsCore object. */
    private final TdsCore tds;

    /** Cached TdsInputStream instance. */
    private final TdsInputStream tdsInputStream = new TdsInputStream(this);

    /** Cached Tds90InputStream instance. */
    private final Tds90InputStream tds90InputStream = new Tds90InputStream(this);

    /** The byte to char map. */
    private char byteToChar[];

    /** The char to byte map. */
    private byte charToByte[];

    /** default Charset Name. */
    private String charsetName;

    /** High speed character translation mapping table. */
    private static java.util.HashMap<String, Object[]> charsetMap = new java.util.HashMap<String, Object[]>();

    /** Maximum index for byte maps. */
    private static int MAX_BYTE_INDEX = 256;

    /** Maximum index for char maps. */
    private static int MAX_CHAR_INDEX = 65536;

    /** Standard replacement character for unsupported mappings. */
    private static byte REPLACEMENT_CHAR = (byte) 0x3F;

    /**
     * Construct a RequestStream object.
     *
     * @param tds  the TdcCore instance for this stream
     * @param socket the IO socket.
     */
    TdsStream(TdsCore tds, TdsSocket socket) {
        this.tds = tds;
        this.socket = socket;
        this.bufferSize = TdsCore.MIN_PKT_SIZE;
        this.outBuffer = new byte[bufferSize];
        this.outBufferPtr = TdsCore.PKT_HDR_LEN;
        this.inBuffer = new byte[bufferSize];
        this.inBufferLen = bufferSize;
        this.inBufferPtr = bufferSize;
    }

    /**
     * Set the default character set for this connection stream.
     * <p/>For single byte charset a simple lookup table is used to do
     * the conversions. This is roughly 3 times faster than the Charset
     * encoder/decoder. The mapping tables are shared between connections.
     * @param cs the charset instance.
     */
    void setCharset(Charset cs) {
        charsetName = null;
        if (cs.newEncoder().maxBytesPerChar() > 1.0) {
            return;
        }
        String csName = cs.name();
        synchronized (charsetMap) {
            Object maps[] = charsetMap.get(csName);
            if (maps == null) {
                byte page[] = new byte[MAX_BYTE_INDEX];
                for (int i = 0; i < MAX_BYTE_INDEX; i++) {
                    page[i] = (byte) i;
                }
                char charMap[];
                try {
                    charMap = new String(page, cs.name()).toCharArray();
                    if (charMap.length != MAX_BYTE_INDEX) {
                        return;
                    }
                } catch (java.io.UnsupportedEncodingException e) {
                    return;
                }
                byte byteMap[] = new byte[MAX_CHAR_INDEX];
                for (int i = 0; i < MAX_CHAR_INDEX; i++) {
                    byteMap[i] = REPLACEMENT_CHAR;
                }
                for (int i = 0; i < MAX_BYTE_INDEX; i++) {
                    byteMap[charMap[i]] = (byte) i;
                }
                maps = new Object[2];
                maps[0] = charMap;
                maps[1] = byteMap;
                charsetMap.put(csName, maps);
            }
            byteToChar = (char[]) maps[0];
            charToByte = (byte[]) maps[1];
            charsetName = csName;
        }
    }

    /**
     * Set the output buffer size
     *
     * @param size The new buffer size (>= {@link TdsCore#MIN_PKT_SIZE} <= {@link TdsCore#MAX_PKT_SIZE}).
     */
    void setBufferSize(int size) {
        if (size < outBufferPtr || size == bufferSize) {
            return;
        }
        if (size < TdsCore.MIN_PKT_SIZE || size > TdsCore.MAX_PKT_SIZE) {
            throw new IllegalArgumentException("Invalid buffer size parameter " + size);
        }
        byte[] tmp = new byte[size];
        System.arraycopy(outBuffer, 0, tmp, 0, outBufferPtr);
        outBuffer = tmp;
    }

    /**
     * Set the current output packet type.
     *
     * @param pktType The packet type eg TdsCore.QUERY_PKT.
     */
    void setPacketType(byte pktType) {
        this.pktType = pktType;
    }

    /**
     * Write a byte to the output stream.
     *
     * @param b The byte value to write.
     * @throws IOException
     */
    void write(byte b) throws IOException {
        if (outBufferPtr == outBuffer.length) {
            putPacket(0);
        }
        outBuffer[outBufferPtr++] = b;
    }

    /**
     * Write an array of bytes to the output stream.
     *
     * @param b The byte array to write.
     * @throws IOException
     */
    void write(byte[] b) throws IOException {
        int bytesToWrite = b.length;
        int off = 0;
        while (bytesToWrite > 0) {
            int available = outBuffer.length - outBufferPtr;
            if (available == 0) {
                putPacket(0);
                continue;
            }
            int bc = (available > bytesToWrite) ? bytesToWrite : available;
            System.arraycopy(b, off, outBuffer, outBufferPtr, bc);
            off += bc;
            outBufferPtr += bc;
            bytesToWrite -= bc;
        }
    }

    /**
     * Write a ByteBuffer to the output stream.
     *
     * @param bb The ByteBuffer to write.
     * @throws IOException
     */
    void write(ByteBuffer bb) throws IOException {
        int bytesToWrite = bb.remaining();
        while (bytesToWrite > 0) {
            int available = outBuffer.length - outBufferPtr;
            if (available == 0) {
                putPacket(0);
                continue;
            }
            int bc = (available > bytesToWrite) ? bytesToWrite : available;
            bb.get(outBuffer, outBufferPtr, bc);
            outBufferPtr += bc;
            bytesToWrite -= bc;
        }
    }

    /**
     * Write a partial byte buffer to the output stream.
     *
     * @param b The byte array buffer.
     * @param off The offset into the byte array.
     * @param len The number of bytes to write.
     * @throws IOException
     */
    void write(byte[] b, int off, int len) throws IOException {
        int limit = (off + len) > b.length ? b.length : off + len;
        int bytesToWrite = limit - off;
        int i = len - bytesToWrite;
        while (bytesToWrite > 0) {
            int available = outBuffer.length - outBufferPtr;
            if (available == 0) {
                putPacket(0);
                continue;
            }
            int bc = (available > bytesToWrite) ? bytesToWrite : available;
            System.arraycopy(b, off, outBuffer, outBufferPtr, bc);
            off += bc;
            outBufferPtr += bc;
            bytesToWrite -= bc;
        }
        for (; i > 0; i--) {
            write((byte) 0);
        }
    }

    /**
     * Write an int value to the output stream.
     *
     * @param i The int value to write.
     * @throws IOException
     */
    void write(int i) throws IOException {
        write((byte) i);
        write((byte) (i >> 8));
        write((byte) (i >> 16));
        write((byte) (i >> 24));
    }

    /**
     * Write a short value to the output stream.
     *
     * @param s The short value to write.
     * @throws IOException
     */
    void write(short s) throws IOException {
        write((byte) s);
        write((byte) (s >> 8));
    }

    /**
     * Write a long value to the output stream.
     *
     * @param l The long value to write.
     * @throws IOException
     */
    void write(long l) throws IOException {
        write((byte) l);
        write((byte) (l >> 8));
        write((byte) (l >> 16));
        write((byte) (l >> 24));
        write((byte) (l >> 32));
        write((byte) (l >> 40));
        write((byte) (l >> 48));
        write((byte) (l >> 56));
    }

    /**
     * Write a double value to the output stream.
     *
     * @param f The double value to write.
     * @throws IOException
     */
    void write(double f) throws IOException {
        long l = Double.doubleToLongBits(f);
        write((byte) l);
        write((byte) (l >> 8));
        write((byte) (l >> 16));
        write((byte) (l >> 24));
        write((byte) (l >> 32));
        write((byte) (l >> 40));
        write((byte) (l >> 48));
        write((byte) (l >> 56));
    }

    /**
     * Write a float value to the output stream.
     *
     * @param f The float value to write.
     * @throws IOException
     */
    void write(float f) throws IOException {
        int l = Float.floatToIntBits(f);
        write((byte) l);
        write((byte) (l >> 8));
        write((byte) (l >> 16));
        write((byte) (l >> 24));
    }

    /**
     * Write a String to the output stream as translated bytes.
     *
     * @param s The String to write.
     * @param info The CharsetInfo instance defining the charset.
     * @throws IOException
     */
    void write(String s, Charset info) throws IOException {
        int len = s.length();
        char chars[] = (len > charBuffer.length) ? new char[len] : charBuffer;
        s.getChars(0, len, chars, 0);
        write(chars, len, info);
    }

    /**
     * Write a String to the output stream as translated bytes.
     *
     * @param chars the char[] to write.
     * @param len the length of the string.
     * @param info the CharsetInfo instance defining the charset.
     * @throws IOException
     */
    void write(char chars[], int len, Charset info) throws IOException {
        if (info.name().equals(charsetName)) {
            byte bytes[] = (len > byteBuffer.length) ? new byte[len] : byteBuffer;
            for (int i = 0; i < len; i++) {
                bytes[i] = charToByte[chars[i]];
            }
            write(bytes, 0, len);
        } else {
            CharBuffer cb = CharBuffer.wrap(chars, 0, len);
            ByteBuffer bb = info.encode(cb);
            len = bb.remaining();
            byte bytes[] = (len > byteBuffer.length) ? new byte[len] : byteBuffer;
            bb.get(bytes, 0, len);
            write(bytes, 0, len);
        }
    }

    /**
     * Write a String object to the output stream as unicode.
     *
     * @param chars the char[] to write.
     * @param len the length of the array to write.
     * @throws IOException
     */
    void writeUnicode(char chars[], int len) throws IOException {
        int src = 0;
        while (src < len) {
            int available = outBuffer.length - outBufferPtr;
            if (available == 0) {
                putPacket(0);
                available = outBuffer.length - outBufferPtr;
            }
            if (available == 1) {
                write((byte) chars[src]);
                putPacket(0);
                write((byte) (chars[src++] >> 8));
                available = outBuffer.length - outBufferPtr;
                if (src == len) {
                    break;
                }
            }
            available /= 2;
            byte buf[] = outBuffer;
            int ptr = outBufferPtr;
            int limit = src + ((len - src > available) ? available : len - src);
            while (src < limit) {
                int c = chars[src++];
                buf[ptr] = (byte) c;
                buf[ptr + 1] = (byte) (c >> 8);
                ptr += 2;
            }
            outBufferPtr = ptr;
        }
    }

    /**
     * Write a String object to the output stream as unicode.
     *
     * @param s The String to write.
     * @throws IOException
     */
    void writeUnicode(String s) throws IOException {
        int len = s.length();
        char chars[] = (len > charBuffer.length) ? new char[len] : charBuffer;
        s.getChars(0, len, chars, 0);
        writeUnicode(chars, len);
    }

    /**
     * Output a java.sql.Date/Time/Timestamp value to the server
     * as a Sybase datetime value.
     *
     * @param value the date value to write
     */
    void write(DateTime value) throws IOException {
        if (value == null) {
            write((byte) 0);
            return;
        }
        write((byte) 8);
        write((int) value.getDate());
        write((int) value.getTime());
    }

    /**
     * Write a BigDecimal value to the output stream.
     *
     * @param value The BigDecimal value to write.
     * @param serverType the server type (sqlserver, sybase).
     * @throws IOException
     */
    void write(BigDecimal value, int serverType) throws IOException {
        if (value == null) {
            write((byte) 0);
        } else {
            byte signum = (byte) (value.signum() < 0 ? 0 : 1);
            BigInteger bi = value.unscaledValue();
            byte mantisse[] = bi.abs().toByteArray();
            byte len = (byte) (mantisse.length + 1);
            if (len > 17) {
                throw new IOException("BigDecimal to big to send");
            }
            if (serverType == TdsCore.SYBASE) {
                write((byte) len);
                write((byte) ((signum == 0) ? 1 : 0));
                for (int i = 0; i < mantisse.length; i++) {
                    write((byte) mantisse[i]);
                }
            } else {
                write((byte) len);
                write((byte) signum);
                for (int i = mantisse.length - 1; i >= 0; i--) {
                    write((byte) mantisse[i]);
                }
            }
        }
    }

    /**
     * Flush the packet to the output stream setting the last packet flag.
     *
     * @throws IOException
     */
    void flush() throws IOException {
        putPacket(1);
    }

    /**
     * Write the TDS packet to the network.
     *
     * @param last Set to 1 if this is the last packet else 0.
     * @throws IOException
     */
    private void putPacket(int last) throws IOException {
        if (isClosed) {
            throw new IOException(Messages.get("error.io.outclosed"));
        }
        outBuffer[0] = pktType;
        outBuffer[1] = (byte) last;
        outBuffer[2] = (byte) (outBufferPtr >> 8);
        outBuffer[3] = (byte) outBufferPtr;
        outBuffer[4] = 0;
        outBuffer[5] = 0;
        outBuffer[6] = (byte) ((tds.getTdsVersion() >= TdsCore.TDS70) ? 1 : 0);
        outBuffer[7] = 0;
        socket.sendBytes(outBuffer);
        outBufferPtr = TdsCore.PKT_HDR_LEN;
    }

    /**
     * Retrieves the next input byte without reading forward.
     *
     * @return the next byte in the input stream as an <code>int</code>
     * @throws IOException if an I/O error occurs
     */
    int peek() throws IOException {
        if (inBufferPtr >= inBufferLen) {
            getPacket();
        }
        return (int) inBuffer[inBufferPtr] & 0xFF;
    }

    /**
     * Reads the next input byte from the server response stream.
     *
     * @return the next byte in the input stream as an <code>int</code>
     * @throws IOException if an I/O error occurs
     */
    int read() throws IOException {
        if (inBufferPtr >= inBufferLen) {
            getPacket();
        }
        return (int) inBuffer[inBufferPtr++] & 0xFF;
    }

    /**
     * Reads a byte array from the server response stream.
     *
     * @param b the byte array to read into
     * @return the number of bytes read as an <code>int</code>
     * @throws IOException if an I/O error occurs
     */
    int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads a byte array from the server response stream, specifying a start
     * offset and length.
     *
     * @param b   the byte array
     * @param off the starting offset in the array
     * @param len the number of bytes to read
     * @return the number of bytes read as an <code>int</code>
     * @throws IOException if an I/O error occurs
     */
    int read(byte[] b, int off, int len) throws IOException {
        int bytesToRead = len;
        while (bytesToRead > 0) {
            if (inBufferPtr >= inBufferLen) {
                getPacket();
            }
            int available = inBufferLen - inBufferPtr;
            int bc = (available > bytesToRead) ? bytesToRead : available;
            System.arraycopy(inBuffer, inBufferPtr, b, off, bc);
            off += bc;
            bytesToRead -= bc;
            inBufferPtr += bc;
        }
        return len;
    }

    /**
     * Reads a <code>String</code> from the server response stream, creating
     * it from a translated <code>byte</code> array.
     * @param len  the length of the string to read <b>in bytes</b>
     * @param info descriptor of the charset to use
     * @return the result as a <code>String</code>
     * @throws IOException if an I/O error occurs
     */
    String readString(int len, Charset info) throws IOException {
        byte[] bytes = (len > byteBuffer.length) ? new byte[len] : byteBuffer;
        read(bytes, 0, len);
        if (info.name().equals(charsetName)) {
            char[] chars = (len > charBuffer.length) ? new char[len] : charBuffer;
            for (int i = 0; i < len; i++) {
                chars[i] = byteToChar[bytes[i] & 0xFF];
            }
            return new String(chars, 0, len);
        }
        return info.decode(ByteBuffer.wrap(bytes, 0, len)).toString();
    }

    /**
     * Reads a <code>String</code> object from the server response stream.
     * @param len the length of the string to read <b>in characters</b>
     * @return the result as a <code>String</code>
     * @throws IOException if an I/O error occurs
     */
    String readUnicode(int len) throws IOException {
        char[] chars = (len > charBuffer.length) ? new char[len] : charBuffer;
        int dest = 0;
        while (dest < len) {
            int available = inBufferLen - inBufferPtr;
            if (available == 0) {
                getPacket();
                available = inBufferLen - inBufferPtr;
            }
            if (available == 1) {
                int b1 = read();
                chars[dest++] = (char) (b1 | (read() << 8));
                available = inBufferLen - inBufferPtr;
                if (dest == len) {
                    break;
                }
            }
            available /= 2;
            byte buf[] = inBuffer;
            int ptr = inBufferPtr;
            int limit = dest + ((len - dest > available) ? available : len - dest);
            while (dest < limit) {
                chars[dest++] = (char) ((buf[ptr] & 0xFF) | (buf[ptr + 1] << 8));
                ptr += 2;
            }
            inBufferPtr = ptr;
        }
        return new String(chars, 0, len);
    }

    /**
     * Reads a <code>short</code> value from the server response stream.
     *
     * @return the result as a <code>short</code>
     * @throws IOException if an I/O error occurs
     */
    short readShort() throws IOException {
        if (inBufferPtr >= inBufferLen) {
            getPacket();
        }
        int b1 = inBuffer[inBufferPtr++] & 0xFF;
        if (inBufferPtr >= inBufferLen) {
            getPacket();
        }
        int b2 = inBuffer[inBufferPtr++] << 8;
        return (short) (b1 | b2);
    }

    /**
     * Reads an <code>int</code> value from the server response stream.
     *
     * @return the result as a <code>int</code>
     * @throws IOException if an I/O error occurs
     */
    int readInt() throws IOException {
        if (inBufferLen - inBufferPtr > 3) {
            int b1 = inBuffer[inBufferPtr++] & 0xFF;
            int b2 = inBuffer[inBufferPtr++] & 0xFF;
            int b3 = inBuffer[inBufferPtr++] & 0xFF;
            return (inBuffer[inBufferPtr++] << 24) | (b3 << 16) | (b2 << 8) | b1;
        }
        int b1 = read();
        int b2 = read() << 8;
        int b3 = read() << 16;
        int b4 = read() << 24;
        return b4 | b3 | b2 | b1;
    }

    /**
     * Reads a <code>long</code> value from the server response stream.
     *
     * @return the result as a <code>long</code>
     * @throws IOException if an I/O error occurs
     */
    long readLong() throws IOException {
        if (inBufferLen - inBufferPtr > 7) {
            long b1 = inBuffer[inBufferPtr++] & 0xFFL;
            long b2 = (inBuffer[inBufferPtr++] & 0xFFL) << 8;
            long b3 = (inBuffer[inBufferPtr++] & 0xFFL) << 16;
            long b4 = (inBuffer[inBufferPtr++] & 0xFFL) << 24;
            long b5 = (inBuffer[inBufferPtr++] & 0xFFL) << 32;
            long b6 = (inBuffer[inBufferPtr++] & 0xFFL) << 40;
            long b7 = (inBuffer[inBufferPtr++] & 0xFFL) << 48;
            long b8 = (long) inBuffer[inBufferPtr++] << 56;
            return b1 | b2 | b3 | b4 | b5 | b6 | b7 | b8;
        }
        long b1 = ((long) read());
        long b2 = ((long) read()) << 8;
        long b3 = ((long) read()) << 16;
        long b4 = ((long) read()) << 24;
        long b5 = ((long) read()) << 32;
        long b6 = ((long) read()) << 40;
        long b7 = ((long) read()) << 48;
        long b8 = ((long) read()) << 56;
        return b1 | b2 | b3 | b4 | b5 | b6 | b7 | b8;
    }

    /**
     * Reads an <code>unsigned long</code> value from the server response stream.
     *
     * @return the result as a <code>BigDecimal</code>
     * @throws IOException if an I/O error occurs
     */
    BigDecimal readUnsignedLong() throws IOException {
        int b1 = read();
        long b2 = read();
        long b3 = ((long) read()) << 8;
        long b4 = ((long) read()) << 16;
        long b5 = ((long) read()) << 24;
        long b6 = ((long) read()) << 32;
        long b7 = ((long) read()) << 40;
        long b8 = ((long) read()) << 48;
        return new BigDecimal(Long.toString(b2 | b3 | b4 | b5 | b6 | b7 | b8)).multiply(new BigDecimal(256)).add(new BigDecimal(b1));
    }

    /**
     * Read a MONEY value from the server response stream.
     *
     * @param len the length of the money type.
     * @return The java.math.BigDecimal value or null.
     * @throws IOException
     */
    BigDecimal readMoney(int len) throws IOException {
        if (len == 4) {
            return BigDecimal.valueOf(readInt(), 4);
        } else if (len == 8) {
            long msw = (long) readInt() << 32;
            long lsw = readInt() & 0xFFFFFFFFL;
            return BigDecimal.valueOf((lsw | msw), 4);
        } else if (len != 0) {
            throw new IOException("Invalid money value.");
        }
        return null;
    }

    /**
     * Get a DATETIME value from the server response stream.
     *
     * @param len the length of the datatime data type.
     * @return The java.sql.Timestamp value or null.
     * @throws java.io.IOException
     */
    DateTime readDatetime(final int len) throws IOException {
        int daysSince1900;
        int time;
        int minutes;
        switch(len) {
            case 0:
                return null;
            case 8:
                daysSince1900 = readInt();
                time = readInt();
                return new DateTime(daysSince1900, time);
            case 4:
                daysSince1900 = readShort() & 0xFFFF;
                minutes = readShort();
                return new DateTime((short) daysSince1900, (short) minutes);
            default:
                throw new IOException("Invalid DATETIME value with size of " + len + " bytes.");
        }
    }

    /**
     * Discards bytes from the server response stream.
     *
     * @param skip the number of bytes to discard
     * @return the number of bytes skipped
     */
    int skip(int skip) throws IOException {
        int tmp = skip;
        while (skip > 0) {
            if (inBufferPtr >= inBufferLen) {
                getPacket();
            }
            int available = inBufferLen - inBufferPtr;
            if (skip > available) {
                skip -= available;
                inBufferPtr = inBufferLen;
            } else {
                inBufferPtr += skip;
                skip = 0;
            }
        }
        return tmp;
    }

    /**
     * Consumes the rest of the server response, without parsing it.
     * <p/>
     * <b>Note:</b> Use only in extreme cases, packets will not be parsed and
     * could leave the connection in an inconsistent state.
     */
    void skipToEnd() {
        try {
            inBufferPtr = inBufferLen;
            while (inBuffer[1] != 1) {
                inBuffer = socket.getNetPacket(inBuffer);
            }
        } catch (IOException ex) {
        }
    }

    /**
     * Closes this response stream. The stream id is unlinked from the
     * underlying shared socket as well.
     */
    void close() {
        isClosed = true;
    }

    /**
     * Creates a simple <code>InputStream</code> over the server response.
     * <p/>
     * This method can be used to obtain a stream which can be passed to
     * <code>InputStreamReader</code>s to assist in reading multi byte
     * character sets.
     *
     * @param len the number of bytes available in the server response
     * @return the <code>InputStream</code> built over the server response
     */
    InputStream getInputStream(int len) {
        tdsInputStream.init(len);
        return tdsInputStream;
    }

    /**
     * Creates a simple <code>InputStream</code> over the server response.
     * <p/>
     * This method can be used to obtain a stream which can be passed to
     * <code>InputStreamReader</code>s to assist in reading multi byte
     * character sets.
     *
     * @return the <code>InputStream</code> built over the server response
     */
    InputStream getTDS90InputStream() throws IOException {
        tds90InputStream.init();
        return tds90InputStream;
    }

    /**
     * Read the next TDS packet from the network.
     *
     * @throws IOException if an I/O error occurs
     */
    private void getPacket() throws IOException {
        while (inBufferPtr >= inBufferLen) {
            if (isClosed) {
                throw new IOException(Messages.get("error.io.inclosed"));
            }
            inBuffer = socket.getNetPacket(inBuffer);
            inBufferLen = socket.getPktLen(inBuffer);
            inBufferPtr = TdsCore.PKT_HDR_LEN;
        }
    }

    /**
     * Simple inner class implementing an <code>InputStream</code> over the
     * server response.
     */
    private static class TdsInputStream extends InputStream {

        /** The underlying <code>ResponseStream</code>. */
        private TdsStream tds;

        /** The maximum amount of data to make available. */
        private int maxLen;

        /**
         * Creates a <code>TdsInputStream</code> instance.
         *
         * @param tds    the underlying <code>ResponseStream</code>
         */
        public TdsInputStream(TdsStream tds) {
            this.tds = tds;
        }

        /**
         * initialize the stream and set the length.
         * @param len the length of byte data available.
         */
        public void init(int len) {
            this.maxLen = len;
        }

        public int read() throws IOException {
            return (maxLen-- > 0) ? tds.read() : -1;
        }

        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (maxLen < 1) {
                return -1;
            }
            len = (len > this.maxLen) ? this.maxLen : len;
            this.maxLen -= len;
            return tds.read(b, off, len);
        }

        public int available() throws IOException {
            return maxLen;
        }

        public void close() throws IOException {
            while (this.maxLen-- > 0) {
                tds.read();
            }
        }
    }

    /**
     * Simple inner class implementing an <code>InputStream</code> over the
     * server response.
     * <p/>Used to read SQL 2005 streamed data.
     */
    private static class Tds90InputStream extends InputStream {

        /** The underlying <code>ResponseStream</code>. */
        private TdsStream tds;

        /** End of file flag. */
        private boolean eof;

        /** The TDS fragement size or 0 for EOF. */
        private int fragSize;

        /**
         * Creates a <code>Tds90InputStream</code> instance.
         *
         * @param tds    the underlying <code>ResponseStream</code>
         */
        public Tds90InputStream(TdsStream tds) {
            this.tds = tds;
        }

        /**
         * Initialise the stream.
         * 
         * @throws IOException
         */
        public void init() throws IOException {
            this.fragSize = tds.readInt();
            this.eof = this.fragSize < 1;
        }

        public int read() throws IOException {
            while (!this.eof) {
                if (this.fragSize-- > 0) {
                    return tds.read();
                }
                this.fragSize = tds.readInt();
                this.eof = this.fragSize < 1;
            }
            return -1;
        }

        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (this.eof) {
                return -1;
            }
            int saveLen = len;
            while (len > 0 && !this.eof) {
                int bc = (len > this.fragSize) ? this.fragSize : len;
                tds.read(b, off, bc);
                this.fragSize -= bc;
                if (this.fragSize < 1) {
                    this.fragSize = tds.readInt();
                    this.eof = this.fragSize < 1;
                }
                len -= bc;
                off += bc;
            }
            return saveLen - len;
        }

        public void close() throws IOException {
            while (!this.eof) {
                read();
            }
        }
    }
}
