package org.sulweb.infumon.common;

import java.io.*;
import java.sql.*;

/**
 *
 * @author lucio
 */
public class InfuBlob implements Blob {

    private byte[] data;

    private boolean freed;

    /** Creates a new instance of InfuBlob */
    public InfuBlob() {
    }

    public InfuBlob(byte[] data) {
        this.data = data;
    }

    public long position(Blob pattern, long start) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        byte[] bpattern = pattern.getBytes(0, (int) pattern.length());
        return position(bpattern, start);
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        if (len + pos > data.length) grow(len + (int) pos);
        System.arraycopy(bytes, offset, data, (int) pos, len);
        return len;
    }

    public byte[] getBytes(long pos, int length) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        if (pos == 1 && length == data.length) return data;
        int index = (int) pos - 1;
        if (index + length > data.length) length = data.length - index;
        byte[] result = new byte[length];
        System.arraycopy(data, (int) (pos - 1), result, 0, result.length);
        return result;
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        return setBytes(pos, bytes, 0, bytes.length);
    }

    public long position(byte[] pattern, long start) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        long result = -1;
        for (int index = 0; index < data.length - pattern.length + 1 && result == -1; index++) {
            boolean differ = false;
            for (int subIndex = 0; subIndex < pattern.length && (subIndex + index) < data.length && !differ; subIndex++) if (data[index + subIndex] != pattern[subIndex]) differ = true;
            if (!differ) result = index + 1;
        }
        return result;
    }

    public void truncate(long len) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        if (len <= data.length) {
            byte[] olddata = data;
            data = new byte[(int) len];
            System.arraycopy(olddata, 0, data, 0, (int) len);
        }
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        return new BlobOutputStream((int) pos);
    }

    public long length() throws SQLException {
        if (freed) throw new SQLException("Free already called");
        return data.length;
    }

    public InputStream getBinaryStream() throws SQLException {
        if (freed) throw new SQLException("Free already called");
        return new ByteArrayInputStream(data);
    }

    private void grow(int newsize) {
        byte[] olddata = data;
        data = new byte[newsize];
        System.arraycopy(olddata, 0, data, 0, olddata.length);
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        if (freed) throw new SQLException("Free already called");
        return new ByteArrayInputStream(data, (int) pos, (int) length);
    }

    public void free() throws SQLException {
        data = null;
        freed = true;
    }

    private class BlobOutputStream extends OutputStream {

        private int base, seek;

        public BlobOutputStream(int pos) {
            base = pos;
            seek = 0;
        }

        public void close() {
            seek = -1;
        }

        public void write(int b) throws IOException {
            if (seek < 0) throw new IOException("Stream already closed");
            byte bb = (byte) (b & 0xff);
            if (base + seek >= data.length) grow(data.length + 1);
            data[base + seek] = bb;
            seek++;
        }
    }
}
