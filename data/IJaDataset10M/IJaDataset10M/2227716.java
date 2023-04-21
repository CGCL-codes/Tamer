package org.hsqldb;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * @author james house jhouse@part.net
 * @version 1.7.2
 * @since 1.7.2
 */
public class jdbcBlob implements Blob {

    private byte[] blobData;

    jdbcBlob(byte[] data) {
        this.blobData = data;
    }

    /**
     * Returns blob data as a stream
     */
    public InputStream getBinaryStream() throws SQLException {
        return new ByteArrayInputStream(blobData);
    }

    /**
     * Returns blob data as an array of bytes
     */
    public byte[] getBytes(long pos, int length) throws SQLException {
        byte[] newData = new byte[length];
        System.arraycopy(blobData, (int) (pos - 1), newData, 0, length);
        return newData;
    }

    /**
     * Returns the length of the blob data
     */
    public long length() throws SQLException {
        return this.blobData.length;
    }

    public long position(Blob pattern, long start) throws SQLException {
        throw jdbcDriver.notSupported;
    }

    public long position(byte[] pattern, long start) throws SQLException {
        throw jdbcDriver.notSupported;
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw jdbcDriver.notSupported;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw jdbcDriver.notSupported;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw jdbcDriver.notSupported;
    }

    public void truncate(long len) throws SQLException {
        throw jdbcDriver.notSupported;
    }
}
