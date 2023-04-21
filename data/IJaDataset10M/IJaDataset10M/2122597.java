package uk.org.ogsadai.converters.webrowset.resultset.types;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import uk.org.ogsadai.common.Base64;
import uk.org.ogsadai.common.BinaryLob;

/**
 * Converts a string value into a <code>java.sql.Blob</code>.
 * 
 * @author The OGSA-DAI Team.
 */
public class BlobStrategy extends TypeStrategy {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2005 - 2007.";

    public InputStream getBinaryStream(String data) throws SQLException {
        if (data == null) {
            return null;
        }
        return getBlob(data).getBinaryStream();
    }

    public Blob getBlob(String data) throws SQLException {
        if (data == null) {
            return null;
        }
        BinaryLob blob = new BinaryLob();
        blob.setBytes(1, Base64.decode(data.getBytes()));
        return blob;
    }

    public byte[] getBytes(String data) throws SQLException {
        if (data == null) {
            return null;
        }
        return Base64.decode(data.getBytes());
    }

    public Object getObject(String data) throws SQLException {
        return getBlob(data);
    }
}
