package com.mysql.jdbc;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.SQLException;

/**
 * Buffer contains code to read and write packets from/to the MySQL server.
 * 
 * @version $Id: ByteArrayBuffer.java,v 1.1.2.1 2005/05/13 18:58:38 mmatthews
 *          Exp $
 * @author Mark Matthews
 */
class ByteArrayBuffer extends Buffer {

    private int bufLength = 0;

    private byte[] byteBuffer;

    private int position = 0;

    ByteArrayBuffer(byte[] buf) {
        this.byteBuffer = buf;
        setBufLength(buf.length);
    }

    ByteArrayBuffer(int size) {
        this.byteBuffer = new byte[size];
        setBufLength(this.byteBuffer.length);
        this.position = MysqlIO.HEADER_LENGTH;
    }

    final void clear() {
        this.position = MysqlIO.HEADER_LENGTH;
    }

    final void ensureCapacity(int additionalData) throws SQLException {
        if ((this.position + additionalData) > getBufLength()) {
            if ((this.position + additionalData) < this.byteBuffer.length) {
                setBufLength(this.byteBuffer.length);
            } else {
                int newLength = (int) (this.byteBuffer.length * 1.25);
                if (newLength < (this.byteBuffer.length + additionalData)) {
                    newLength = this.byteBuffer.length + (int) (additionalData * 1.25);
                }
                if (newLength < this.byteBuffer.length) {
                    newLength = this.byteBuffer.length + additionalData;
                }
                byte[] newBytes = new byte[newLength];
                System.arraycopy(this.byteBuffer, 0, newBytes, 0, this.byteBuffer.length);
                this.byteBuffer = newBytes;
                setBufLength(this.byteBuffer.length);
            }
        }
    }

    /**
	 * Skip over a length-encoded string
	 * 
	 * @return The position past the end of the string
	 */
    public int fastSkipLenString() {
        long len = this.readFieldLength();
        this.position += len;
        return (int) len;
    }

    protected final byte[] getBufferSource() {
        return this.byteBuffer;
    }

    int getBufLength() {
        return this.bufLength;
    }

    /**
	 * Returns the array of bytes this Buffer is using to read from.
	 * 
	 * @return byte array being read from
	 */
    public byte[] getByteBuffer() {
        return this.byteBuffer;
    }

    final byte[] getBytes(int len) {
        byte[] b = new byte[len];
        System.arraycopy(this.byteBuffer, this.position, b, 0, len);
        this.position += len;
        return b;
    }

    byte[] getBytes(int offset, int len) {
        byte[] dest = new byte[len];
        System.arraycopy(this.byteBuffer, offset, dest, 0, len);
        return dest;
    }

    int getCapacity() {
        return this.byteBuffer.length;
    }

    public ByteBuffer getNioBuffer() {
        throw new IllegalArgumentException(Messages.getString("ByteArrayBuffer.0"));
    }

    /**
	 * Returns the current position to write to/ read from
	 * 
	 * @return the current position to write to/ read from
	 */
    public int getPosition() {
        return this.position;
    }

    final boolean isLastDataPacket() {
        return ((getBufLength() < 9) && ((this.byteBuffer[0] & 0xff) == 254));
    }

    final long newReadLength() {
        int sw = this.byteBuffer[this.position++] & 0xff;
        switch(sw) {
            case 251:
                return 0;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLongLong();
            default:
                return sw;
        }
    }

    final byte readByte() {
        return this.byteBuffer[this.position++];
    }

    final byte readByte(int readAt) {
        return this.byteBuffer[readAt];
    }

    final long readFieldLength() {
        int sw = this.byteBuffer[this.position++] & 0xff;
        switch(sw) {
            case 251:
                return NULL_LENGTH;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLongLong();
            default:
                return sw;
        }
    }

    final int readInt() {
        byte[] b = this.byteBuffer;
        return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8);
    }

    final int readIntAsLong() {
        byte[] b = this.byteBuffer;
        return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8) | ((b[this.position++] & 0xff) << 16) | ((b[this.position++] & 0xff) << 24);
    }

    final byte[] readLenByteArray(int offset) {
        long len = this.readFieldLength();
        if (len == NULL_LENGTH) {
            return null;
        }
        if (len == 0) {
            return Constants.EMPTY_BYTE_ARRAY;
        }
        this.position += offset;
        return getBytes((int) len);
    }

    final long readLength() {
        int sw = this.byteBuffer[this.position++] & 0xff;
        switch(sw) {
            case 251:
                return 0;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLong();
            default:
                return sw;
        }
    }

    final long readLong() {
        byte[] b = this.byteBuffer;
        return ((long) b[this.position++] & 0xff) | (((long) b[this.position++] & 0xff) << 8) | ((long) (b[this.position++] & 0xff) << 16) | ((long) (b[this.position++] & 0xff) << 24);
    }

    final int readLongInt() {
        byte[] b = this.byteBuffer;
        return (b[this.position++] & 0xff) | ((b[this.position++] & 0xff) << 8) | ((b[this.position++] & 0xff) << 16);
    }

    final long readLongLong() {
        byte[] b = this.byteBuffer;
        return (b[this.position++] & 0xff) | ((long) (b[this.position++] & 0xff) << 8) | ((long) (b[this.position++] & 0xff) << 16) | ((long) (b[this.position++] & 0xff) << 24) | ((long) (b[this.position++] & 0xff) << 32) | ((long) (b[this.position++] & 0xff) << 40) | ((long) (b[this.position++] & 0xff) << 48) | ((long) (b[this.position++] & 0xff) << 56);
    }

    final int readnBytes() {
        int sw = this.byteBuffer[this.position++] & 0xff;
        switch(sw) {
            case 1:
                return this.byteBuffer[this.position++] & 0xff;
            case 2:
                return this.readInt();
            case 3:
                return this.readLongInt();
            case 4:
                return (int) this.readLong();
            default:
                return 255;
        }
    }

    final String readString() {
        int i = this.position;
        int len = 0;
        int maxLen = getBufLength();
        while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
            len++;
            i++;
        }
        String s = new String(this.byteBuffer, this.position, len);
        this.position += (len + 1);
        return s;
    }

    final String readString(String encoding) throws SQLException {
        int i = this.position;
        int len = 0;
        int maxLen = getBufLength();
        while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
            len++;
            i++;
        }
        try {
            return new String(this.byteBuffer, this.position, len, encoding);
        } catch (UnsupportedEncodingException uEE) {
            throw new SQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        } finally {
            this.position += (len + 1);
        }
    }

    void setBufLength(int bufLengthToSet) {
        this.bufLength = bufLengthToSet;
    }

    /**
	 * Sets the array of bytes to use as a buffer to read from.
	 * 
	 * @param byteBuffer
	 *            the array of bytes to use as a buffer
	 */
    public void setByteBuffer(byte[] byteBufferToSet) {
        this.byteBuffer = byteBufferToSet;
    }

    /**
	 * Set the current position to write to/ read from
	 * 
	 * @param position
	 *            the position (0-based index)
	 */
    public void setPosition(int positionToSet) {
        this.position = positionToSet;
    }

    final void writeByte(byte b) throws SQLException {
        ensureCapacity(1);
        this.byteBuffer[this.position++] = b;
    }

    final void writeBytesNoNull(byte[] bytes) throws SQLException {
        int len = bytes.length;
        ensureCapacity(len);
        System.arraycopy(bytes, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeBytesNoNull(byte[] bytes, int offset, int length) throws SQLException {
        ensureCapacity(length);
        System.arraycopy(bytes, offset, this.byteBuffer, this.position, length);
        this.position += length;
    }

    final void writeDouble(double d) throws SQLException {
        long l = Double.doubleToLongBits(d);
        writeLongLong(l);
    }

    final void writeFieldLength(long length) throws SQLException {
        if (length < 251) {
            writeByte((byte) length);
        } else if (length < 65536L) {
            ensureCapacity(3);
            writeByte((byte) 252);
            writeInt((int) length);
        } else if (length < 16777216L) {
            ensureCapacity(4);
            writeByte((byte) 253);
            writeLongInt((int) length);
        } else {
            ensureCapacity(9);
            writeByte((byte) 254);
            writeLongLong(length);
        }
    }

    final void writeFloat(float f) throws SQLException {
        ensureCapacity(4);
        int i = Float.floatToIntBits(f);
        byte[] b = this.byteBuffer;
        b[this.position++] = (byte) (i & 0xff);
        b[this.position++] = (byte) (i >>> 8);
        b[this.position++] = (byte) (i >>> 16);
        b[this.position++] = (byte) (i >>> 24);
    }

    final void writeInt(int i) throws SQLException {
        ensureCapacity(2);
        byte[] b = this.byteBuffer;
        b[this.position++] = (byte) (i & 0xff);
        b[this.position++] = (byte) (i >>> 8);
    }

    final void writeLenBytes(byte[] b) throws SQLException {
        int len = b.length;
        ensureCapacity(len + 9);
        writeFieldLength(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeLenString(String s, String encoding, String serverEncoding, SingleByteCharsetConverter converter, boolean parserKnowsUnicode) throws UnsupportedEncodingException, SQLException {
        byte[] b = null;
        if (converter != null) {
            b = converter.toBytes(s);
        } else {
            b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode);
        }
        int len = b.length;
        ensureCapacity(len + 9);
        writeFieldLength(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeLong(long i) throws SQLException {
        ensureCapacity(4);
        byte[] b = this.byteBuffer;
        b[this.position++] = (byte) (i & 0xff);
        b[this.position++] = (byte) (i >>> 8);
        b[this.position++] = (byte) (i >>> 16);
        b[this.position++] = (byte) (i >>> 24);
    }

    final void writeLongInt(int i) throws SQLException {
        ensureCapacity(3);
        byte[] b = this.byteBuffer;
        b[this.position++] = (byte) (i & 0xff);
        b[this.position++] = (byte) (i >>> 8);
        b[this.position++] = (byte) (i >>> 16);
    }

    final void writeLongLong(long i) throws SQLException {
        ensureCapacity(8);
        byte[] b = this.byteBuffer;
        b[this.position++] = (byte) (i & 0xff);
        b[this.position++] = (byte) (i >>> 8);
        b[this.position++] = (byte) (i >>> 16);
        b[this.position++] = (byte) (i >>> 24);
        b[this.position++] = (byte) (i >>> 32);
        b[this.position++] = (byte) (i >>> 40);
        b[this.position++] = (byte) (i >>> 48);
        b[this.position++] = (byte) (i >>> 56);
    }

    final void writeString(String s) throws SQLException {
        ensureCapacity((s.length() * 2) + 1);
        writeStringNoNull(s);
        this.byteBuffer[this.position++] = 0;
    }

    final void writeStringNoNull(String s) throws SQLException {
        int len = s.length();
        ensureCapacity(len * 2);
        System.arraycopy(s.getBytes(), 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeStringNoNull(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode) throws UnsupportedEncodingException, SQLException {
        byte[] b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode);
        int len = b.length;
        ensureCapacity(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }
}
