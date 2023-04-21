package edu.upmc.opi.caBIG.caTIES.connector.bridge;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.sql.Date;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import com.sun.rowset.CachedRowSetImpl;

public class CaTIES_ResultSet extends CachedRowSetImpl {

    private static final long serialVersionUID = 962944918241155066L;

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_ResultSet.class);

    private boolean wasNull = false;

    private String remoteObjectUuid = null;

    private final CaTIES_ResultSetMetaData metaData = new CaTIES_ResultSetMetaData();

    public CaTIES_ResultSet(String remoteObjectUuid) throws SQLException {
        this.remoteObjectUuid = remoteObjectUuid;
    }

    public CaTIES_ResultSet() throws SQLException {
        ;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        Object result = super.getObject(columnIndex);
        Class<?> oracleTimeStampCls = oracle.sql.TIMESTAMP.class;
        if (oracleTimeStampCls.isInstance(result)) {
            result = ((oracle.sql.TIMESTAMP) result).timestampValue();
        }
        return (Timestamp) result;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return (getTimestamp(getColIdxByName(columnLabel)));
    }

    public int getColIdxByName(String name) throws SQLException {
        ResultSetMetaData rowSetMD = this.getMetaData();
        int cols = rowSetMD.getColumnCount();
        for (int i = 1; i <= cols; ++i) {
            String colName = rowSetMD.getColumnName(i);
            String colLabel = rowSetMD.getColumnLabel(i);
            if (colName != null && name.equalsIgnoreCase(colName)) return (i); else if (colLabel != null && name.equalsIgnoreCase(colLabel)) return (i); else continue;
        }
        throw new SQLException(resBundle.handleGetObject("cachedrowsetimpl.invalcolnm").toString());
    }

    public Array getArray(int arg0) throws SQLException {
        return super.getArray(arg0);
    }

    public Array getArray(String arg0) throws SQLException {
        return getArray(getColIdxByName(arg0));
    }

    public InputStream getAsciiStream(int arg0) throws SQLException {
        return super.getAsciiStream(arg0);
    }

    public InputStream getAsciiStream(String arg0) throws SQLException {
        return getAsciiStream(getColIdxByName(arg0));
    }

    public BigDecimal getBigDecimal(int arg0) throws SQLException {
        return super.getBigDecimal(arg0);
    }

    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        return getBigDecimal(getColIdxByName(arg0));
    }

    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
        return super.getBigDecimal(arg0, arg1);
    }

    public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
        return getBigDecimal(getColIdxByName(arg0), arg1);
    }

    public InputStream getBinaryStream(int arg0) throws SQLException {
        return super.getBinaryStream(arg0);
    }

    public InputStream getBinaryStream(String arg0) throws SQLException {
        return getBinaryStream(getColIdxByName(arg0));
    }

    public Blob getBlob(int arg0) throws SQLException {
        return super.getBlob(arg0);
    }

    public Blob getBlob(String arg0) throws SQLException {
        return getBlob(getColIdxByName(arg0));
    }

    public boolean getBoolean(int arg0) throws SQLException {
        return super.getBoolean(arg0);
    }

    public boolean getBoolean(String arg0) throws SQLException {
        return getBoolean(getColIdxByName(arg0));
    }

    public byte getByte(int arg0) throws SQLException {
        return super.getByte(arg0);
    }

    public byte getByte(String arg0) throws SQLException {
        return getByte(getColIdxByName(arg0));
    }

    public byte[] getBytes(int arg0) throws SQLException {
        return super.getBytes(arg0);
    }

    public byte[] getBytes(String arg0) throws SQLException {
        return getBytes(getColIdxByName(arg0));
    }

    public Reader getCharacterStream(int arg0) throws SQLException {
        return super.getCharacterStream(arg0);
    }

    public Reader getCharacterStream(String arg0) throws SQLException {
        return getCharacterStream(getColIdxByName(arg0));
    }

    public Clob getClob(int arg0) throws SQLException {
        Clob result = null;
        Object obj = super.getObject(arg0);
        if (obj != null) {
            if (obj.getClass() == String.class) {
                logger.debug("Coverting object of class " + obj.getClass().getSimpleName() + " to a CaTIES_Clob");
                result = new CaTIES_Clob((String) obj);
            }
        }
        return result;
    }

    public Clob getClob(String arg0) throws SQLException {
        int columnIndex = getColIdxByName(arg0);
        Clob result = getClob(columnIndex);
        return result;
    }

    public Date getDate(int arg0) throws SQLException {
        return super.getDate(arg0);
    }

    public Date getDate(String arg0) throws SQLException {
        return getDate(getColIdxByName(arg0));
    }

    public Date getDate(int arg0, Calendar arg1) throws SQLException {
        return super.getDate(arg0, arg1);
    }

    public Date getDate(String arg0, Calendar arg1) throws SQLException {
        return getDate(getColIdxByName(arg0), arg1);
    }

    public double getDouble(int arg0) throws SQLException {
        return super.getDouble(arg0);
    }

    public double getDouble(String arg0) throws SQLException {
        return getDouble(getColIdxByName(arg0));
    }

    public float getFloat(int arg0) throws SQLException {
        return super.getFloat(arg0);
    }

    public float getFloat(String arg0) throws SQLException {
        return getFloat(getColIdxByName(arg0));
    }

    public int getInt(int arg0) throws SQLException {
        return super.getInt(arg0);
    }

    public int getInt(String arg0) throws SQLException {
        return getInt(getColIdxByName(arg0));
    }

    public long getLong(int arg0) throws SQLException {
        return super.getLong(arg0);
    }

    public long getLong(String arg0) throws SQLException {
        return getLong(getColIdxByName(arg0));
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        return super.getNCharacterStream(arg0);
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        return getNCharacterStream(getColIdxByName(arg0));
    }

    public NClob getNClob(int arg0) throws SQLException {
        return super.getNClob(arg0);
    }

    public NClob getNClob(String arg0) throws SQLException {
        return getNClob(getColIdxByName(arg0));
    }

    public String getNString(int arg0) throws SQLException {
        return super.getNString(arg0);
    }

    public String getNString(String arg0) throws SQLException {
        return getNString(getColIdxByName(arg0));
    }

    public Object getObject(int arg0) throws SQLException {
        return super.getObject(arg0);
    }

    public Object getObject(String arg0) throws SQLException {
        return getObject(getColIdxByName(arg0));
    }

    public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
        return super.getObject(arg0, arg1);
    }

    public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {
        return getObject(getColIdxByName(arg0), arg1);
    }

    public Ref getRef(int arg0) throws SQLException {
        return super.getRef(arg0);
    }

    public Ref getRef(String arg0) throws SQLException {
        return getRef(getColIdxByName(arg0));
    }

    public int getRow() throws SQLException {
        return 0;
    }

    public RowId getRowId(int arg0) throws SQLException {
        return super.getRowId(arg0);
    }

    public RowId getRowId(String arg0) throws SQLException {
        return getRowId(getColIdxByName(arg0));
    }

    public SQLXML getSQLXML(int arg0) throws SQLException {
        return super.getSQLXML(arg0);
    }

    public SQLXML getSQLXML(String arg0) throws SQLException {
        return getSQLXML(getColIdxByName(arg0));
    }

    public short getShort(int arg0) throws SQLException {
        return super.getShort(arg0);
    }

    public short getShort(String arg0) throws SQLException {
        return getShort(getColIdxByName(arg0));
    }

    public String getString(int arg0) throws SQLException {
        return super.getString(arg0);
    }

    public String getString(String arg0) throws SQLException {
        return getString(getColIdxByName(arg0));
    }

    public Time getTime(int arg0) throws SQLException {
        return super.getTime(arg0);
    }

    public Time getTime(String arg0) throws SQLException {
        return getTime(getColIdxByName(arg0));
    }

    public Time getTime(int arg0, Calendar arg1) throws SQLException {
        return getTime(arg0, arg1);
    }

    public Time getTime(String arg0, Calendar arg1) throws SQLException {
        return getTime(getColIdxByName(arg0), arg1);
    }

    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
        return getTimestamp(arg0, arg1);
    }

    public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {
        return getTimestamp(getColIdxByName(arg0), arg1);
    }

    public InputStream getUnicodeStream(int arg0) throws SQLException {
        return super.getUnicodeStream(arg0);
    }

    public InputStream getUnicodeStream(String arg0) throws SQLException {
        return getUnicodeStream(getColIdxByName(arg0));
    }
}
