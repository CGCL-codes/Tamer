package org.objectstyle.cayenne.access.types;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.objectstyle.cayenne.CayenneException;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.util.MemoryBlob;
import org.objectstyle.cayenne.validation.BeanValidationFailure;
import org.objectstyle.cayenne.validation.ValidationResult;

/**
 * Handles <code>byte[]</code>, mapping it as either of JDBC types - BLOB or
 * (VAR)BINARY. Can be configured to trim trailing zero bytes.
 * 
 * @author Andrus Adamchik
 */
public class ByteArrayType extends AbstractType {

    private static final int BUF_SIZE = 8 * 1024;

    protected boolean trimmingBytes;

    protected boolean usingBlobs;

    /**
     * Strips null bytes from the byte array, returning a potentially smaller array that
     * contains no trailing zero bytes.
     */
    public static byte[] trimBytes(byte[] bytes) {
        int bytesToTrim = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] != 0) {
                bytesToTrim = bytes.length - 1 - i;
                break;
            }
        }
        if (bytesToTrim == 0) {
            return bytes;
        }
        byte[] dest = new byte[bytes.length - bytesToTrim];
        System.arraycopy(bytes, 0, dest, 0, dest.length);
        return dest;
    }

    public ByteArrayType(boolean trimmingBytes, boolean usingBlobs) {
        this.usingBlobs = usingBlobs;
        this.trimmingBytes = trimmingBytes;
    }

    public String getClassName() {
        return "byte[]";
    }

    /**
     * Validates byte[] property.
     * 
     * @since 1.1
     */
    public boolean validateProperty(Object source, String property, Object value, DbAttribute dbAttribute, ValidationResult validationResult) {
        if (!(value instanceof byte[])) {
            return true;
        }
        if (dbAttribute.getMaxLength() <= 0) {
            return true;
        }
        byte[] bytes = (byte[]) value;
        if (bytes.length > dbAttribute.getMaxLength()) {
            String message = "\"" + property + "\" exceeds maximum allowed length (" + dbAttribute.getMaxLength() + " bytes): " + bytes.length;
            validationResult.addFailure(new BeanValidationFailure(source, property, message));
            return false;
        }
        return true;
    }

    public Object materializeObject(ResultSet rs, int index, int type) throws Exception {
        byte[] bytes = null;
        if (type == Types.BLOB) {
            bytes = (isUsingBlobs()) ? readBlob(rs.getBlob(index)) : readBinaryStream(rs, index);
        } else {
            bytes = rs.getBytes(index);
            if (bytes != null && type == Types.BINARY && isTrimmingBytes()) {
                bytes = trimBytes(bytes);
            }
        }
        return bytes;
    }

    public Object materializeObject(CallableStatement cs, int index, int type) throws Exception {
        byte[] bytes = null;
        if (type == Types.BLOB) {
            if (!isUsingBlobs()) {
                throw new CayenneException("Binary streams are not supported in stored procedure parameters.");
            }
            bytes = readBlob(cs.getBlob(index));
        } else {
            bytes = cs.getBytes(index);
            if (bytes != null && type == Types.BINARY && isTrimmingBytes()) {
                bytes = trimBytes(bytes);
            }
        }
        return bytes;
    }

    public void setJdbcObject(PreparedStatement st, Object val, int pos, int type, int precision) throws Exception {
        if (type == Types.BLOB) {
            if (isUsingBlobs()) {
                st.setBlob(pos, writeBlob((byte[]) val));
            } else {
                st.setBytes(pos, (byte[]) val);
            }
        } else {
            super.setJdbcObject(st, val, pos, type, precision);
        }
    }

    protected Blob writeBlob(byte[] bytes) {
        return bytes != null ? new MemoryBlob(bytes) : null;
    }

    protected byte[] readBlob(Blob blob) throws IOException, SQLException {
        if (blob == null) {
            return null;
        }
        if (blob.length() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("BLOB is too big to be read as byte[] in memory: " + blob.length());
        }
        int size = (int) blob.length();
        if (size == 0) {
            return new byte[0];
        }
        int bufSize = (size < BUF_SIZE) ? size : BUF_SIZE;
        InputStream in = blob.getBinaryStream();
        return (in != null) ? readValueStream(new BufferedInputStream(in, bufSize), size, bufSize) : null;
    }

    protected byte[] readBinaryStream(ResultSet rs, int index) throws IOException, SQLException {
        InputStream in = rs.getBinaryStream(index);
        return (in != null) ? readValueStream(in, -1, BUF_SIZE) : null;
    }

    protected byte[] readValueStream(InputStream in, int streamSize, int bufSize) throws IOException {
        byte[] buf = new byte[bufSize];
        int read;
        ByteArrayOutputStream out = (streamSize > 0) ? new ByteArrayOutputStream(streamSize) : new ByteArrayOutputStream();
        try {
            while ((read = in.read(buf, 0, bufSize)) >= 0) {
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        } finally {
            in.close();
        }
    }

    /**
     * Returns <code>true</code> if byte columns are handled as BLOBs internally.
     */
    public boolean isUsingBlobs() {
        return usingBlobs;
    }

    public void setUsingBlobs(boolean usingBlobs) {
        this.usingBlobs = usingBlobs;
    }

    public boolean isTrimmingBytes() {
        return trimmingBytes;
    }

    public void setTrimmingBytes(boolean trimingBytes) {
        this.trimmingBytes = trimingBytes;
    }
}
