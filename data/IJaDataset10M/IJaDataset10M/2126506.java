package org.subethamail.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;

/**
 * Dumb implementation of <code>java.sql.Blob</code>.  In order to support the
 * fancier methods, it turns the input stream into a byte array instead of the
 * other way 'round.
 * 
 * @author Jeff Schnitzer
 */
public class BlobImpl implements Blob {

    private byte[] bytes;

    public BlobImpl(byte[] bytes) {
        this.bytes = bytes;
    }

    public BlobImpl(InputStream stream, int length) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        try {
            int ch;
            while ((ch = stream.read()) >= 0) out.write(ch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bytes = out.toByteArray();
    }

    @Override
    public long length() throws SQLException {
        return this.bytes.length;
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public void truncate(long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public byte[] getBytes(long pos, int len) throws SQLException {
        long zeroBasedPos = pos - 1;
        if (zeroBasedPos + len > this.bytes.length) len = this.bytes.length - (int) zeroBasedPos;
        return Arrays.copyOfRange(this.bytes, (int) zeroBasedPos, len);
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int i, int j) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long position(byte[] bytes, long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public long position(Blob blob, long pos) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void free() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return new ByteArrayInputStream(this.getBytes(pos, (int) length));
    }
}
