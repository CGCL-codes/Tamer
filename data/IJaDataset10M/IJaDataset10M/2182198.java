package com.go.trove.io;

import java.io.*;

/******************************************************************************
 * A CharToByteBuffer implementation that wraps a ByteBuffer for storage.
 * 
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:--> 01/05/30 <!-- $-->
 */
public class DefaultCharToByteBuffer implements CharToByteBuffer, Serializable {

    private ByteBuffer mBuffer;

    private transient OutputStreamWriter mConvertor;

    private char[] mChars;

    private int mCapacity;

    private int mCursor;

    private String mDefaultEncoding;

    /**
     * @param buffer Buffer that receives the characters converted to bytes.
     */
    public DefaultCharToByteBuffer(ByteBuffer buffer) {
        this(buffer, null);
    }

    /**
     * @param buffer Buffer that receives the characters converted to bytes.
     * @param defaultEncoding Default character encoding to use if setEncoding
     * is not called.
     */
    public DefaultCharToByteBuffer(ByteBuffer buffer, String defaultEncoding) {
        mBuffer = buffer;
        mChars = new char[4000];
        mCapacity = mChars.length;
        mDefaultEncoding = defaultEncoding;
    }

    public void setEncoding(String enc) throws IOException {
        drain(true);
        mConvertor = new OutputStreamWriter(new ByteBufferOutputStream(mBuffer), enc);
    }

    public String getEncoding() {
        return (mConvertor == null) ? mDefaultEncoding : mConvertor.getEncoding();
    }

    public long getBaseByteCount() throws IOException {
        return mBuffer.getBaseByteCount();
    }

    public long getByteCount() throws IOException {
        drain(true);
        return mBuffer.getByteCount();
    }

    public void writeTo(OutputStream out) throws IOException {
        drain(true);
        mBuffer.writeTo(out);
    }

    public void append(byte b) throws IOException {
        drain(true);
        mBuffer.append(b);
    }

    public void append(byte[] bytes) throws IOException {
        append(bytes, 0, bytes.length);
    }

    public void append(byte[] bytes, int offset, int length) throws IOException {
        if (length != 0) {
            drain(true);
            mBuffer.append(bytes, offset, length);
        }
    }

    public void appendSurrogate(ByteData s) throws IOException {
        if (s != null) {
            drain(true);
            mBuffer.appendSurrogate(s);
        }
    }

    public void addCaptureBuffer(ByteBuffer buffer) throws IOException {
        drain(true);
        mBuffer.addCaptureBuffer(buffer);
    }

    public void removeCaptureBuffer(ByteBuffer buffer) throws IOException {
        drain(true);
        mBuffer.removeCaptureBuffer(buffer);
    }

    public void append(char c) throws IOException {
        if (mCursor >= mCapacity) {
            drain(false);
        }
        mChars[mCursor++] = c;
    }

    public void append(char[] chars) throws IOException {
        append(chars, 0, chars.length);
    }

    public void append(char[] chars, int offset, int length) throws IOException {
        if (length == 0) {
            return;
        }
        int capacity = mCapacity;
        if (length < (capacity - mCursor)) {
            System.arraycopy(chars, offset, mChars, mCursor, length);
            mCursor += length;
            return;
        }
        drain(false);
        if (length < capacity) {
            System.arraycopy(chars, offset, mChars, mCursor, length);
            mCursor += length;
            return;
        }
        getConvertor().write(chars, offset, length);
    }

    public void append(String str) throws IOException {
        append(str, 0, str.length());
    }

    public void append(String str, int offset, int length) throws IOException {
        if (length == 0) {
            return;
        }
        int capacity = mCapacity;
        int avail = capacity - mCursor;
        if (length <= avail) {
            str.getChars(offset, offset + length, mChars, mCursor);
            mCursor += length;
            return;
        }
        str.getChars(offset, offset + avail, mChars, mCursor);
        offset += avail;
        length -= avail;
        mCursor = capacity;
        drain(false);
        while (length >= capacity) {
            str.getChars(offset, offset + capacity, mChars, 0);
            offset += capacity;
            length -= capacity;
            mCursor = capacity;
            drain(false);
        }
        if (length > 0) {
            str.getChars(offset, offset + length, mChars, 0);
            mCursor = length;
        }
    }

    public void reset() throws IOException {
        mBuffer.reset();
    }

    public void drain() throws IOException {
        drain(true);
    }

    private OutputStreamWriter getConvertor() throws UnsupportedEncodingException {
        if (mConvertor == null) {
            if (mDefaultEncoding == null) {
                mConvertor = new OutputStreamWriter(new ByteBufferOutputStream(mBuffer));
            } else {
                mConvertor = new OutputStreamWriter(new ByteBufferOutputStream(mBuffer), mDefaultEncoding);
            }
        }
        return mConvertor;
    }

    private void drain(boolean flush) throws IOException {
        if (mCursor != 0) {
            try {
                getConvertor().write(mChars, 0, mCursor);
            } finally {
                mCursor = 0;
            }
        }
        if (flush && mConvertor != null) {
            mConvertor.flush();
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (mConvertor == null) {
            out.writeObject(null);
        } else {
            out.writeObject(mConvertor.getEncoding());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String enc = (String) in.readObject();
        if (enc != null) {
            mConvertor = new OutputStreamWriter(new ByteBufferOutputStream(mBuffer), enc);
        }
    }
}
