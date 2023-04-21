package com.go.trove.io;

import java.io.Writer;
import java.io.IOException;

/******************************************************************************
 * A Writer that writes into a CharToByteBuffer.
 * 
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:--> 00/12/05 <!-- $-->
 */
public class CharToByteBufferWriter extends Writer {

    private CharToByteBuffer mBuffer;

    private boolean mClosed;

    public CharToByteBufferWriter(CharToByteBuffer buffer) {
        mBuffer = buffer;
    }

    public void write(int c) throws IOException {
        checkIfClosed();
        mBuffer.append((char) c);
    }

    public void write(char[] chars) throws IOException {
        checkIfClosed();
        mBuffer.append(chars);
    }

    public void write(char[] chars, int offset, int length) throws IOException {
        checkIfClosed();
        mBuffer.append(chars, offset, length);
    }

    public void write(String str) throws IOException {
        checkIfClosed();
        mBuffer.append(str);
    }

    public void write(String str, int offset, int length) throws IOException {
        checkIfClosed();
        mBuffer.append(str, offset, length);
    }

    public void flush() throws IOException {
        checkIfClosed();
    }

    public void close() {
        mClosed = true;
    }

    private void checkIfClosed() throws IOException {
        if (mClosed) {
            throw new IOException("Writer closed");
        }
    }
}
