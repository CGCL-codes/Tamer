package org.mortbay.io;

import java.io.IOException;

/** ByteArrayEndPoint.
 * @author gregw
 *
 */
public class ByteArrayEndPoint implements EndPoint {

    byte[] _inBytes;

    ByteArrayBuffer _in;

    ByteArrayBuffer _out;

    boolean _closed;

    boolean _nonBlocking;

    /**
     * 
     */
    public ByteArrayEndPoint() {
    }

    /**
     * @return the nonBlocking
     */
    public boolean isNonBlocking() {
        return _nonBlocking;
    }

    /**
     * @param nonBlocking the nonBlocking to set
     */
    public void setNonBlocking(boolean nonBlocking) {
        _nonBlocking = nonBlocking;
    }

    /**
     * 
     */
    public ByteArrayEndPoint(byte[] input, int outputSize) {
        _inBytes = input;
        _in = new ByteArrayBuffer(input);
        _out = new ByteArrayBuffer(outputSize);
    }

    /**
     * @return Returns the in.
     */
    public ByteArrayBuffer getIn() {
        return _in;
    }

    /**
     * @param in The in to set.
     */
    public void setIn(ByteArrayBuffer in) {
        _in = in;
    }

    /**
     * @return Returns the out.
     */
    public ByteArrayBuffer getOut() {
        return _out;
    }

    /**
     * @param out The out to set.
     */
    public void setOut(ByteArrayBuffer out) {
        _out = out;
    }

    public boolean isOpen() {
        return !_closed;
    }

    public boolean isBlocking() {
        return !_nonBlocking;
    }

    public boolean blockReadable(long millisecs) {
        return true;
    }

    public boolean blockWritable(long millisecs) {
        return true;
    }

    public void close() throws IOException {
        _closed = true;
    }

    public int fill(Buffer buffer) throws IOException {
        if (_closed) throw new IOException("CLOSED");
        if (_in == null) return -1;
        if (_in.length() <= 0) return _nonBlocking ? 0 : -1;
        int len = buffer.put(_in);
        _in.skip(len);
        return len;
    }

    public int flush(Buffer buffer) throws IOException {
        if (_closed) throw new IOException("CLOSED");
        int len = _out.put(buffer);
        buffer.skip(len);
        return len;
    }

    public int flush(Buffer header, Buffer buffer, Buffer trailer) throws IOException {
        if (_closed) throw new IOException("CLOSED");
        int flushed = 0;
        if (header != null && header.length() > 0) {
            int len = _out.put(header);
            header.skip(len);
            flushed += len;
        }
        if (header == null || header.length() == 0) {
            if (buffer != null && buffer.length() > 0) {
                int len = _out.put(buffer);
                buffer.skip(len);
                flushed += len;
            }
            if (buffer == null || buffer.length() == 0) {
                if (trailer != null && trailer.length() > 0) {
                    int len = _out.put(trailer);
                    trailer.skip(len);
                    flushed += len;
                }
            }
        }
        return flushed;
    }

    /**
     * 
     */
    public void reset() {
        _closed = false;
        _in.clear();
        _out.clear();
        if (_inBytes != null) _in.setPutIndex(_inBytes.length);
    }

    public String getLocalAddr() {
        return null;
    }

    public String getLocalHost() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public Object getTransport() {
        return _inBytes;
    }

    public void flush() throws IOException {
    }

    public boolean isBufferingInput() {
        return false;
    }

    public boolean isBufferingOutput() {
        return false;
    }

    public boolean isBufferred() {
        return false;
    }
}
