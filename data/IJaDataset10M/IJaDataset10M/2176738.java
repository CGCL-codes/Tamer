package org.avaje.ebean.server.plugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.PersistenceException;
import org.avaje.ebean.util.DataTypes;
import org.avaje.ebean.util.Message;

/**
 * The generic ResultSetReader that is used to read objects out of a
 * <code>ResultSet</code>. This also can take into account a
 * BooleanConverter.
 */
public class ResultSetReader {

    private Calendar calendar;

    private int stringInitialSize = 512;

    private int clobBufferSize = 512;

    private int blobBufferSize = 1024;

    /**
	 * Create with a BooleanConverter. If BooleanConverter is null it is assumed
	 * the jdbc driver or database handles booleans.
	 */
    public ResultSetReader() {
        calendar = new GregorianCalendar();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
	 * Return the Object value for a given column.
	 * 
	 * @param rset
	 *            the ResultSet that has the row column data
	 * @param rsetColumn
	 *            the column index in the resultSet
	 * @param dataTypeDB
	 *            the java.sql.Types dataType according to the JDBC Driver
	 */
    public Object read(ResultSet rset, int rsetColumn, int dataTypeDB) throws SQLException {
        switch(dataTypeDB) {
            case java.sql.Types.LONGVARCHAR:
                return getLongVarChar(rset, rsetColumn);
            case java.sql.Types.CLOB:
                return getClob(rset, rsetColumn);
            case java.sql.Types.LONGVARBINARY:
                return getLongVarBinary(rset, rsetColumn);
            case java.sql.Types.BLOB:
                return getBlob(rset, rsetColumn);
            default:
                return getSimpleType(rset, rsetColumn, dataTypeDB);
        }
    }

    /**
	 * Get Object value for the simple data types. That is, Integer, BigDecimal,
	 * Varchar, Date, Timestamp etc but <b>NOT</b> CLOB, BLOB, LongVarChar,
	 * LongVarBinary etc.
	 */
    protected Object getSimpleType(ResultSet rset, int colIndex, int dataType) throws SQLException {
        try {
            switch(dataType) {
                case DataTypes.UTIL_DATE:
                    {
                        Timestamp ts = rset.getTimestamp(colIndex, calendar);
                        if (ts != null) {
                            return new java.util.Date(ts.getTime());
                        } else {
                            return null;
                        }
                    }
                case java.sql.Types.BOOLEAN:
                    boolean b = rset.getBoolean(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Boolean.valueOf(b);
                case java.sql.Types.VARCHAR:
                    return rset.getString(colIndex);
                case java.sql.Types.CHAR:
                    return rset.getString(colIndex);
                case java.sql.Types.TINYINT:
                    byte t = rset.getByte(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Byte.valueOf(t);
                case java.sql.Types.SMALLINT:
                    short s = rset.getShort(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Short.valueOf(s);
                case java.sql.Types.INTEGER:
                    {
                        int i = rset.getInt(colIndex);
                        if (rset.wasNull()) {
                            return null;
                        }
                        return Integer.valueOf(i);
                    }
                case java.sql.Types.BIGINT:
                    long l = rset.getLong(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Long.valueOf(l);
                case java.sql.Types.REAL:
                    float f = rset.getFloat(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Float.valueOf(f);
                case java.sql.Types.FLOAT:
                    double d1 = rset.getDouble(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Double.valueOf(d1);
                case java.sql.Types.DOUBLE:
                    double d2 = rset.getDouble(colIndex);
                    if (rset.wasNull()) {
                        return null;
                    }
                    return Double.valueOf(d2);
                case java.sql.Types.NUMERIC:
                    return rset.getBigDecimal(colIndex);
                case java.sql.Types.DECIMAL:
                    return rset.getBigDecimal(colIndex);
                case java.sql.Types.DATE:
                    return rset.getDate(colIndex);
                case java.sql.Types.TIMESTAMP:
                    return rset.getTimestamp(colIndex);
                case java.sql.Types.TIME:
                    return rset.getTime(colIndex);
                case java.sql.Types.BINARY:
                    return rset.getBytes(colIndex);
                case java.sql.Types.VARBINARY:
                    return rset.getBytes(colIndex);
                case java.sql.Types.OTHER:
                    return rset.getObject(colIndex);
                case java.sql.Types.JAVA_OBJECT:
                    return rset.getObject(colIndex);
                default:
                    String m = Message.msg("persist.bind.datatype", "" + dataType, "" + colIndex);
                    throw new SQLException(m);
            }
        } catch (Exception e) {
            String msg = "dataType[" + dataType + "] bind[" + colIndex + "]";
            throw new PersistenceException(msg, e);
        }
    }

    /**
	 * Get a LONGVARCHAR dataType and return it as a String.
	 */
    protected Object getLongVarChar(ResultSet rset, int rsetColumn) throws SQLException {
        return getStringLob(rset, rsetColumn, Types.LONGVARCHAR);
    }

    /**
	 * Get a CLOB dataType and return it as a String.
	 */
    protected Object getClob(ResultSet rset, int rsetColumn) throws SQLException {
        return getStringLob(rset, rsetColumn, Types.CLOB);
    }

    /**
	 * Get a LONGVARBINARY dataType and return it as a byte[].
	 */
    protected Object getLongVarBinary(ResultSet rset, int rsetColumn) throws SQLException {
        return getBinaryLob(rset, rsetColumn, Types.LONGVARBINARY);
    }

    /**
	 * Get a BLOB dataType and return it as a byte[].
	 */
    protected Object getBlob(ResultSet rset, int rsetColumn) throws SQLException {
        return getBinaryLob(rset, rsetColumn, Types.BLOB);
    }

    protected byte[] getBinaryLob(ResultSet rset, int rsetColumn, int fromDataType) throws SQLException {
        InputStream in = null;
        if (fromDataType == java.sql.Types.BLOB) {
            Blob blob = rset.getBlob(rsetColumn);
            if (blob == null) {
                return null;
            }
            in = blob.getBinaryStream();
        } else if (fromDataType == java.sql.Types.LONGVARBINARY) {
            in = rset.getBinaryStream(rsetColumn);
        } else {
            throw new SQLException(Message.msg("invalid.argument", "" + fromDataType));
        }
        try {
            if (in == null) {
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[blobBufferSize];
            int len;
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, len);
            }
            byte[] data = out.toByteArray();
            if (data.length == 0) {
                data = null;
            }
            in.close();
            out.close();
            return data;
        } catch (IOException e) {
            throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
        }
    }

    protected String getStringLob(ResultSet rset, int rsetColumn, int fromDataType) throws SQLException {
        Reader reader = null;
        if (fromDataType == java.sql.Types.CLOB) {
            Clob clob = rset.getClob(rsetColumn);
            if (clob == null) {
                return null;
            }
            reader = clob.getCharacterStream();
        } else if (fromDataType == java.sql.Types.LONGVARCHAR) {
            reader = rset.getCharacterStream(rsetColumn);
        } else {
            throw new SQLException(Message.msg("invalid.argument", "" + fromDataType));
        }
        if (reader == null) {
            return null;
        }
        char[] buffer = new char[clobBufferSize];
        int readLength = 0;
        StringBuilder out = new StringBuilder(stringInitialSize);
        try {
            while ((readLength = reader.read(buffer)) != -1) {
                out.append(buffer, 0, readLength);
            }
            reader.close();
        } catch (IOException e) {
            throw new SQLException(Message.msg("persist.clob.io", e.getMessage()));
        }
        return out.toString();
    }
}
