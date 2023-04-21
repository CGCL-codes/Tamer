package com.sitescape.team.dao.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.support.AbstractLobType;
import com.sitescape.team.domain.SSBlobSerializable;

/**
 * @author Janet McCann
 *
 * This class handles lazy loading of blobs which contain serialized objects.
 * We don't retrieve the blob data until needed.
 * Wrap blob in SSBlob. 
 */
public class SSBlobSerializableType extends AbstractLobType {

    /**
	 * Initial size for ByteArrayOutputStreams used for serialization output.
	 * <p>If a serialized object is larger than these 1024 bytes, the size of
	 * the byte array used by the output stream will be doubled each time the
	 * limit is reached.
	 */
    public static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 1024;

    public SSBlobSerializableType() {
        super();
    }

    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

    public Class returnedClass() {
        return SSBlobSerializable.class;
    }

    protected Object nullSafeGetInternal(ResultSet rs, String[] names, Object owner, LobHandler lobHandler) throws SQLException, IOException, HibernateException {
        return new SSBlobSerializable(rs.getBlob(names[0]));
    }

    protected void nullSafeSetInternal(PreparedStatement ps, int index, Object value, LobCreator lobCreator) throws SQLException, IOException {
        Object obj = null;
        if (value != null) obj = ((SSBlobSerializable) value).getValue();
        if (obj != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(obj);
                oos.flush();
                lobCreator.setBlobAsBytes(ps, index, baos.toByteArray());
            } finally {
                oos.close();
            }
        } else {
            lobCreator.setBlobAsBytes(ps, index, null);
        }
    }

    public Serializable disassemble(Object value) throws HibernateException {
        if (value != null) {
            ((SSBlobSerializable) value).getValue();
        }
        return (Serializable) value;
    }
}
