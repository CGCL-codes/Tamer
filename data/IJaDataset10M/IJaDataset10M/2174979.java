package br.ufg.integrate.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import br.ufg.integrate.xml.bean.Integration;

/**
 * @author Rogerio
 * @version 0.1
 *
 * Adapted from <a href='https://xlsql.dev.java.net/'>xlSQL</a>.
 */
public class CallableStatementImpl extends PreparedStatementImpl implements CallableStatement {

    private CallableStatement realCstm;

    /**
    * Create a new CallableStatementImpl object.
    *
    */
    public CallableStatementImpl(ConnectionImpl con, CallableStatement clst, Integration i) throws SQLException {
        super(con, clst, i);
        realCstm = clst;
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getArray
    */
    public Array getArray(int i) throws SQLException {
        if (realCstm != null) {
            return realCstm.getArray(i);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getArray
    */
    public Array getArray(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getArray(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBigDecimal
    */
    public java.math.BigDecimal getBigDecimal(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBigDecimal(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBigDecimal
    */
    public java.math.BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBigDecimal(parameterIndex);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBigDecimal
    * @deprecated
    */
    public java.math.BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return realCstm.getBigDecimal(parameterIndex, scale);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBlob
    */
    public Blob getBlob(int i) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBlob(i);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBlob
    */
    public Blob getBlob(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBlob(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBoolean
    */
    public boolean getBoolean(int parameterIndex) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBoolean(parameterIndex);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBoolean
    */
    public boolean getBoolean(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBoolean(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getByte
    */
    public byte getByte(int parameterIndex) throws SQLException {
        if (realCstm != null) {
            return realCstm.getByte(parameterIndex);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getByte
    */
    public byte getByte(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getByte(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBytes
    */
    public byte[] getBytes(int parameterIndex) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBytes(parameterIndex);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getBytes
    */
    public byte[] getBytes(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getBytes(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getClob
    */
    public Clob getClob(int i) throws SQLException {
        if (realCstm != null) {
            return realCstm.getClob(i);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getClob
    */
    public Clob getClob(String parameterName) throws SQLException {
        if (realCstm != null) {
            return realCstm.getClob(parameterName);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDate
    */
    public java.sql.Date getDate(int parameterIndex) throws SQLException {
        if (realCstm != null) {
            return realCstm.getDate(parameterIndex);
        } else {
            throw new SQLException("Not supported");
        }
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDate
    */
    public java.sql.Date getDate(String parameterName) throws SQLException {
        return realCstm.getDate(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDate
    */
    public java.sql.Date getDate(int parameterIndex, java.util.Calendar cal) throws SQLException {
        return realCstm.getDate(parameterIndex, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDate
    */
    public java.sql.Date getDate(String parameterName, java.util.Calendar cal) throws SQLException {
        return realCstm.getDate(parameterName, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDouble
    */
    public double getDouble(int parameterIndex) throws SQLException {
        return realCstm.getDouble(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getDouble
    */
    public double getDouble(String parameterName) throws SQLException {
        return realCstm.getDouble(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getFloat
    */
    public float getFloat(int parameterIndex) throws SQLException {
        return realCstm.getFloat(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getFloat
    */
    public float getFloat(String parameterName) throws SQLException {
        return realCstm.getFloat(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getInt
    */
    public int getInt(String parameterName) throws SQLException {
        return realCstm.getInt(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getInt
    */
    public int getInt(int parameterIndex) throws SQLException {
        return realCstm.getInt(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getLong
    */
    public long getLong(int parameterIndex) throws SQLException {
        return realCstm.getLong(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getLong
    */
    public long getLong(String parameterName) throws SQLException {
        return realCstm.getLong(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getObject
    */
    public Object getObject(int parameterIndex) throws SQLException {
        return realCstm.getObject(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getObject
    */
    public Object getObject(String parameterName) throws SQLException {
        return realCstm.getObject(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getObject
    */
    @SuppressWarnings("unchecked")
    public Object getObject(int i, java.util.Map map) throws SQLException {
        return realCstm.getObject(i, map);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getObject
    */
    @SuppressWarnings("unchecked")
    public Object getObject(String parameterName, java.util.Map map) throws SQLException {
        return realCstm.getObject(parameterName, map);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getRef
    */
    public Ref getRef(int i) throws SQLException {
        return realCstm.getRef(i);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getRef
    */
    public Ref getRef(String parameterName) throws SQLException {
        return realCstm.getRef(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getShort
    */
    public short getShort(String parameterName) throws SQLException {
        return realCstm.getShort(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getShort
    */
    public short getShort(int parameterIndex) throws SQLException {
        return realCstm.getShort(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getString
    */
    public String getString(String parameterName) throws SQLException {
        return realCstm.getString(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getString
    */
    public String getString(int parameterIndex) throws SQLException {
        return realCstm.getString(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTime
    */
    public java.sql.Time getTime(String parameterName) throws SQLException {
        return realCstm.getTime(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTime
    */
    public java.sql.Time getTime(int parameterIndex) throws SQLException {
        return realCstm.getTime(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTime
    */
    public java.sql.Time getTime(String parameterName, java.util.Calendar cal) throws SQLException {
        return realCstm.getTime(parameterName, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTime
    */
    public java.sql.Time getTime(int parameterIndex, java.util.Calendar cal) throws SQLException {
        return realCstm.getTime(parameterIndex, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTimestamp
    */
    public java.sql.Timestamp getTimestamp(String parameterName) throws SQLException {
        return realCstm.getTimestamp(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTimestamp
    */
    public java.sql.Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return realCstm.getTimestamp(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTimestamp
    */
    public java.sql.Timestamp getTimestamp(String parameterName, java.util.Calendar cal) throws SQLException {
        return realCstm.getTimestamp(parameterName, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getTimestamp
    */
    public java.sql.Timestamp getTimestamp(int parameterIndex, java.util.Calendar cal) throws SQLException {
        return realCstm.getTimestamp(parameterIndex, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getURL
    */
    public java.net.URL getURL(int parameterIndex) throws SQLException {
        return realCstm.getURL(parameterIndex);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#getURL
    */
    public java.net.URL getURL(String parameterName) throws SQLException {
        return realCstm.getURL(parameterName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        realCstm.registerOutParameter(parameterIndex, sqlType);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        realCstm.registerOutParameter(parameterName, sqlType);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        realCstm.registerOutParameter(parameterName, sqlType, typeName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(int paramIndex, int sqlType, int scale) throws SQLException {
        realCstm.registerOutParameter(paramIndex, sqlType, scale);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        realCstm.registerOutParameter(parameterName, sqlType, scale);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#registerOutParameter
    */
    public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
        realCstm.registerOutParameter(paramIndex, sqlType, typeName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setAsciiStream
    */
    public void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws SQLException {
        realCstm.setAsciiStream(parameterName, x, length);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setBigDecimal
    */
    public void setBigDecimal(String parameterName, java.math.BigDecimal x) throws SQLException {
        realCstm.setBigDecimal(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setBinaryStream
    */
    public void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws SQLException {
        realCstm.setBinaryStream(parameterName, x, length);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setBoolean
    */
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        realCstm.setBoolean(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setByte
    */
    public void setByte(String parameterName, byte x) throws SQLException {
        realCstm.setByte(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setBytes
    */
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        realCstm.setBytes(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setCharacterStream
    */
    public void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws SQLException {
        realCstm.setCharacterStream(parameterName, reader, length);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setDate
    */
    public void setDate(String parameterName, java.sql.Date x) throws SQLException {
        realCstm.setDate(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setDate
    */
    public void setDate(String parameterName, java.sql.Date x, java.util.Calendar cal) throws SQLException {
        realCstm.setDate(parameterName, x, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setDouble
    */
    public void setDouble(String parameterName, double x) throws SQLException {
        realCstm.setDouble(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setFloat
    */
    public void setFloat(String parameterName, float x) throws SQLException {
        realCstm.setFloat(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setInt
    */
    public void setInt(String parameterName, int x) throws SQLException {
        realCstm.setInt(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setLong
    */
    public void setLong(String parameterName, long x) throws SQLException {
        realCstm.setLong(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setNull
    */
    public void setNull(String parameterName, int sqlType) throws SQLException {
        realCstm.setNull(parameterName, sqlType);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setNull
    */
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        realCstm.setNull(parameterName, sqlType, typeName);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setObject
    */
    public void setObject(String parameterName, Object x) throws SQLException {
        realCstm.setObject(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setObject
    */
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        realCstm.setObject(parameterName, x, targetSqlType);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setObject
    */
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        realCstm.setObject(parameterName, x, targetSqlType);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setShort
    */
    public void setShort(String parameterName, short x) throws SQLException {
        realCstm.setShort(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setString
    */
    public void setString(String parameterName, String x) throws SQLException {
        realCstm.setString(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setTime
    */
    public void setTime(String parameterName, java.sql.Time x) throws SQLException {
        realCstm.setTime(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setTime
    */
    public void setTime(String parameterName, java.sql.Time x, java.util.Calendar cal) throws SQLException {
        realCstm.setTime(parameterName, x, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setTimestamp
    */
    public void setTimestamp(String parameterName, java.sql.Timestamp x) throws SQLException {
        realCstm.setTimestamp(parameterName, x);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setTimestamp
    */
    public void setTimestamp(String parameterName, java.sql.Timestamp x, java.util.Calendar cal) throws SQLException {
        realCstm.setTimestamp(parameterName, x, cal);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#setURL
    */
    public void setURL(String parameterName, java.net.URL val) throws SQLException {
        realCstm.setURL(parameterName, val);
    }

    /**
    * Implements method in interface java.sql.CallableStatement
    * @see java.sql.CallableStatement#wasNull
    */
    public boolean wasNull() throws SQLException {
        return realCstm.wasNull();
    }

    public Reader getCharacterStream(int arg0) throws SQLException {
        return realCstm.getCharacterStream(arg0);
    }

    public Reader getCharacterStream(String arg0) throws SQLException {
        return realCstm.getCharacterStream(arg0);
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        return realCstm.getNCharacterStream(arg0);
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        return realCstm.getNCharacterStream(arg0);
    }

    public NClob getNClob(int arg0) throws SQLException {
        return realCstm.getNClob(arg0);
    }

    public NClob getNClob(String arg0) throws SQLException {
        return realCstm.getNClob(arg0);
    }

    public String getNString(int arg0) throws SQLException {
        return realCstm.getNString(arg0);
    }

    public String getNString(String arg0) throws SQLException {
        return realCstm.getNString(arg0);
    }

    public RowId getRowId(int arg0) throws SQLException {
        return realCstm.getRowId(arg0);
    }

    public RowId getRowId(String arg0) throws SQLException {
        return realCstm.getRowId(arg0);
    }

    public SQLXML getSQLXML(int arg0) throws SQLException {
        return realCstm.getSQLXML(arg0);
    }

    public SQLXML getSQLXML(String arg0) throws SQLException {
        return realCstm.getSQLXML(arg0);
    }

    public boolean isClosed() throws SQLException {
        return realCstm.isClosed();
    }

    public boolean isPoolable() throws SQLException {
        return realCstm.isPoolable();
    }

    @SuppressWarnings("unchecked")
    public boolean isWrapperFor(Class arg0) throws SQLException {
        return realCstm.isWrapperFor(arg0);
    }

    public void setAsciiStream(String arg0, InputStream arg1) throws SQLException {
        realCstm.setAsciiStream(arg0, arg1);
    }

    public void setAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        realCstm.setAsciiStream(arg0, arg1, arg2);
    }

    public void setBinaryStream(String arg0, InputStream arg1) throws SQLException {
    }

    public void setBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
    }

    public void setBlob(String arg0, Blob arg1) throws SQLException {
    }

    public void setBlob(String arg0, InputStream arg1) throws SQLException {
    }

    public void setBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
    }

    public void setCharacterStream(String arg0, Reader arg1) throws SQLException {
    }

    public void setCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setClob(String arg0, Clob arg1) throws SQLException {
    }

    public void setClob(String arg0, Reader arg1) throws SQLException {
    }

    public void setClob(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNCharacterStream(String arg0, Reader arg1) throws SQLException {
    }

    public void setNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNClob(String arg0, NClob arg1) throws SQLException {
    }

    public void setNClob(String arg0, Reader arg1) throws SQLException {
    }

    public void setNClob(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNString(String arg0, String arg1) throws SQLException {
    }

    public void setRowId(String arg0, RowId arg1) throws SQLException {
    }

    public void setSQLXML(String arg0, SQLXML arg1) throws SQLException {
    }

    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
    }

    public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
    }

    public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    public void setBlob(int arg0, InputStream arg1) throws SQLException {
    }

    public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
    }

    public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setClob(int arg0, Reader arg1) throws SQLException {
    }

    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
    }

    public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNClob(int arg0, NClob arg1) throws SQLException {
    }

    public void setNClob(int arg0, Reader arg1) throws SQLException {
    }

    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    public void setNString(int arg0, String arg1) throws SQLException {
    }

    public void setPoolable(boolean arg0) throws SQLException {
    }

    public void setRowId(int arg0, RowId arg1) throws SQLException {
    }

    public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
    }

    @SuppressWarnings("unchecked")
    public Object unwrap(Class arg0) throws SQLException {
        return null;
    }
}
