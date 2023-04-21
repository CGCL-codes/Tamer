package net.funtip.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class FTCallableStatement extends FTPreparedStatement implements CallableStatement {

    CallableStatement realCallableStatement;

    public FTCallableStatement(CallableStatement statement, FTConnection parent, String sql) {
        super(statement, parent, sql);
        realCallableStatement = statement;
    }

    public Array getArray(int i) throws SQLException {
        return new FTSQLArray(realCallableStatement.getArray(i), this, lastSql);
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return realCallableStatement.getBigDecimal(parameterIndex);
    }

    /**	 * @deprecated	 */
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return realCallableStatement.getBigDecimal(parameterIndex, scale);
    }

    public Blob getBlob(int i) throws SQLException {
        return realCallableStatement.getBlob(i);
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        return realCallableStatement.getBoolean(parameterIndex);
    }

    public byte getByte(int parameterIndex) throws SQLException {
        return realCallableStatement.getByte(parameterIndex);
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        return realCallableStatement.getBytes(parameterIndex);
    }

    public Clob getClob(int i) throws SQLException {
        return realCallableStatement.getClob(i);
    }

    public Date getDate(int parameterIndex) throws SQLException {
        return realCallableStatement.getDate(parameterIndex);
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return realCallableStatement.getDate(parameterIndex, cal);
    }

    public double getDouble(int parameterIndex) throws SQLException {
        return realCallableStatement.getDouble(parameterIndex);
    }

    public float getFloat(int parameterIndex) throws SQLException {
        return realCallableStatement.getFloat(parameterIndex);
    }

    public int getInt(int parameterIndex) throws SQLException {
        return realCallableStatement.getInt(parameterIndex);
    }

    public long getLong(int parameterIndex) throws SQLException {
        return realCallableStatement.getLong(parameterIndex);
    }

    public Object getObject(int parameterIndex) throws SQLException {
        return realCallableStatement.getObject(parameterIndex);
    }

    public Ref getRef(int i) throws SQLException {
        return realCallableStatement.getRef(i);
    }

    public short getShort(int parameterIndex) throws SQLException {
        return realCallableStatement.getShort(parameterIndex);
    }

    public String getString(int parameterIndex) throws SQLException {
        return realCallableStatement.getString(parameterIndex);
    }

    public Time getTime(int parameterIndex) throws SQLException {
        return realCallableStatement.getTime(parameterIndex);
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return realCallableStatement.getTime(parameterIndex, cal);
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return realCallableStatement.getTimestamp(parameterIndex);
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return realCallableStatement.getTimestamp(parameterIndex, cal);
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        realCallableStatement.registerOutParameter(parameterIndex, sqlType);
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        realCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
    }

    public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
        realCallableStatement.registerOutParameter(paramIndex, sqlType, typeName);
    }

    public boolean wasNull() throws SQLException {
        return realCallableStatement.wasNull();
    }

    /**	 * @see java.sql.CallableStatement#getArray(String)	 */
    public Array getArray(String arg0) throws SQLException {
        return realCallableStatement.getArray(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getBigDecimal(String)	 */
    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        return realCallableStatement.getBigDecimal(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getBlob(String)	 */
    public Blob getBlob(String arg0) throws SQLException {
        return realCallableStatement.getBlob(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getBoolean(String)	 */
    public boolean getBoolean(String arg0) throws SQLException {
        return realCallableStatement.getBoolean(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getByte(String)	 */
    public byte getByte(String arg0) throws SQLException {
        return realCallableStatement.getByte(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getBytes(String)	 */
    public byte[] getBytes(String arg0) throws SQLException {
        return realCallableStatement.getBytes(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getClob(String)	 */
    public Clob getClob(String arg0) throws SQLException {
        return realCallableStatement.getClob(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getDate(String, Calendar)	 */
    public Date getDate(String arg0, Calendar arg1) throws SQLException {
        return realCallableStatement.getDate(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#getDate(String)	 */
    public Date getDate(String arg0) throws SQLException {
        return realCallableStatement.getDate(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getDouble(String)	 */
    public double getDouble(String arg0) throws SQLException {
        return realCallableStatement.getDouble(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getFloat(String)	 */
    public float getFloat(String arg0) throws SQLException {
        return realCallableStatement.getFloat(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getInt(String)	 */
    public int getInt(String arg0) throws SQLException {
        return realCallableStatement.getInt(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getLong(String)	 */
    public long getLong(String arg0) throws SQLException {
        return realCallableStatement.getLong(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getObject(String)	 */
    public Object getObject(String arg0) throws SQLException {
        return realCallableStatement.getObject(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getRef(String)	 */
    public Ref getRef(String arg0) throws SQLException {
        return realCallableStatement.getRef(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getShort(String)	 */
    public short getShort(String arg0) throws SQLException {
        return realCallableStatement.getShort(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getString(String)	 */
    public String getString(String arg0) throws SQLException {
        return realCallableStatement.getString(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getTime(String, Calendar)	 */
    public Time getTime(String arg0, Calendar arg1) throws SQLException {
        return realCallableStatement.getTime(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#getTime(String)	 */
    public Time getTime(String arg0) throws SQLException {
        return realCallableStatement.getTime(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getTimestamp(String, Calendar)	 */
    public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {
        return realCallableStatement.getTimestamp(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#getTimestamp(String)	 */
    public Timestamp getTimestamp(String arg0) throws SQLException {
        return realCallableStatement.getTimestamp(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getURL(int)	 */
    public URL getURL(int arg0) throws SQLException {
        return realCallableStatement.getURL(arg0);
    }

    /**	 * @see java.sql.CallableStatement#getURL(String)	 */
    public URL getURL(String arg0) throws SQLException {
        return realCallableStatement.getURL(arg0);
    }

    /**	 * @see java.sql.CallableStatement#registerOutParameter(String, int, int)	 */
    public void registerOutParameter(String arg0, int arg1, int arg2) throws SQLException {
        realCallableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#registerOutParameter(String, int, String)	 */
    public void registerOutParameter(String arg0, int arg1, String arg2) throws SQLException {
        realCallableStatement.registerOutParameter(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#registerOutParameter(String, int)	 */
    public void registerOutParameter(String arg0, int arg1) throws SQLException {
        realCallableStatement.registerOutParameter(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setAsciiStream(String, InputStream, int)	 */
    public void setAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        realCallableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setBigDecimal(String, BigDecimal)	 */
    public void setBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
        realCallableStatement.setBigDecimal(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setBinaryStream(String, InputStream, int)	 */
    public void setBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        realCallableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setBoolean(String, boolean)	 */
    public void setBoolean(String arg0, boolean arg1) throws SQLException {
        realCallableStatement.setBoolean(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setByte(String, byte)	 */
    public void setByte(String arg0, byte arg1) throws SQLException {
        realCallableStatement.setByte(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setBytes(String, byte[])	 */
    public void setBytes(String arg0, byte[] arg1) throws SQLException {
        realCallableStatement.setBytes(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setCharacterStream(String, Reader, int)	 */
    public void setCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
        realCallableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setDate(String, Date, Calendar)	 */
    public void setDate(String arg0, Date arg1, Calendar arg2) throws SQLException {
        realCallableStatement.setDate(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setDate(String, Date)	 */
    public void setDate(String arg0, Date arg1) throws SQLException {
        realCallableStatement.setDate(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setDouble(String, double)	 */
    public void setDouble(String arg0, double arg1) throws SQLException {
        realCallableStatement.setDouble(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setFloat(String, float)	 */
    public void setFloat(String arg0, float arg1) throws SQLException {
        realCallableStatement.setFloat(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setInt(String, int)	 */
    public void setInt(String arg0, int arg1) throws SQLException {
        realCallableStatement.setInt(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setLong(String, long)	 */
    public void setLong(String arg0, long arg1) throws SQLException {
        realCallableStatement.setLong(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setNull(String, int, String)	 */
    public void setNull(String arg0, int arg1, String arg2) throws SQLException {
        realCallableStatement.setNull(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setNull(String, int)	 */
    public void setNull(String arg0, int arg1) throws SQLException {
        realCallableStatement.setNull(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setObject(String, Object, int, int)	 */
    public void setObject(String arg0, Object arg1, int arg2, int arg3) throws SQLException {
        realCallableStatement.setObject(arg0, arg1, arg2, arg3);
    }

    /**	 * @see java.sql.CallableStatement#setObject(String, Object, int)	 */
    public void setObject(String arg0, Object arg1, int arg2) throws SQLException {
        realCallableStatement.setObject(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setObject(String, Object)	 */
    public void setObject(String arg0, Object arg1) throws SQLException {
        realCallableStatement.setObject(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setShort(String, short)	 */
    public void setShort(String arg0, short arg1) throws SQLException {
        realCallableStatement.setShort(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setString(String, String)	 */
    public void setString(String arg0, String arg1) throws SQLException {
        realCallableStatement.setString(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setTime(String, Time, Calendar)	 */
    public void setTime(String arg0, Time arg1, Calendar arg2) throws SQLException {
        realCallableStatement.setTime(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setTime(String, Time)	 */
    public void setTime(String arg0, Time arg1) throws SQLException {
        realCallableStatement.setTime(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setTimestamp(String, Timestamp, Calendar)	 */
    public void setTimestamp(String arg0, Timestamp arg1, Calendar arg2) throws SQLException {
        realCallableStatement.setTimestamp(arg0, arg1, arg2);
    }

    /**	 * @see java.sql.CallableStatement#setTimestamp(String, Timestamp)	 */
    public void setTimestamp(String arg0, Timestamp arg1) throws SQLException {
        realCallableStatement.setTimestamp(arg0, arg1);
    }

    /**	 * @see java.sql.CallableStatement#setURL(String, URL)	 */
    public void setURL(String arg0, URL arg1) throws SQLException {
        realCallableStatement.setURL(arg0, arg1);
    }

    public Reader getCharacterStream(int arg0) throws SQLException {
        return realCallableStatement.getCharacterStream(arg0);
    }

    public Reader getCharacterStream(String arg0) throws SQLException {
        return realCallableStatement.getCharacterStream(arg0);
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        return realCallableStatement.getNCharacterStream(arg0);
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        return realCallableStatement.getNCharacterStream(arg0);
    }

    public String getNString(int arg0) throws SQLException {
        return realCallableStatement.getNString(arg0);
    }

    public String getNString(String arg0) throws SQLException {
        return realCallableStatement.getNString(arg0);
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return realCallableStatement.getObject(i, map);
    }

    public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
        return realCallableStatement.getObject(s, map);
    }

    public void setAsciiStream(String arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setAsciiStream(arg0, arg1);
    }

    public void setAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    public void setBinaryStream(String arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setBinaryStream(arg0, arg1);
    }

    public void setBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    public void setBlob(String arg0, Blob arg1) throws SQLException {
        realCallableStatement.setBlob(arg0, arg1);
    }

    public void setBlob(String arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setBlob(arg0, arg1);
    }

    public void setBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setBlob(arg0, arg1, arg2);
    }

    public void setCharacterStream(String arg0, Reader arg1) throws SQLException {
        realCallableStatement.setCharacterStream(arg0, arg1);
    }

    public void setCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    public void setClob(String arg0, Clob arg1) throws SQLException {
        realCallableStatement.setClob(arg0, arg1);
    }

    public void setClob(String arg0, Reader arg1) throws SQLException {
        realCallableStatement.setClob(arg0, arg1);
    }

    public void setClob(String arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setClob(arg0, arg1, arg2);
    }

    public void setNCharacterStream(String arg0, Reader arg1) throws SQLException {
        realCallableStatement.setNCharacterStream(arg0, arg1);
    }

    public void setNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setNCharacterStream(arg0, arg1, arg2);
    }

    public void setNClob(String arg0, Reader arg1) throws SQLException {
        realCallableStatement.setNClob(arg0, arg1);
    }

    public void setNClob(String arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setNClob(arg0, arg1, arg2);
    }

    public void setNString(String arg0, String arg1) throws SQLException {
        realCallableStatement.setNString(arg0, arg1);
    }

    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setAsciiStream(arg0, arg1);
    }

    public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setBinaryStream(arg0, arg1);
    }

    public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    public void setBlob(int arg0, InputStream arg1) throws SQLException {
        realCallableStatement.setBlob(arg0, arg1);
    }

    public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
        realCallableStatement.setBlob(arg0, arg1, arg2);
    }

    public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
        realCallableStatement.setCharacterStream(arg0, arg1);
    }

    public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    public void setClob(int arg0, Reader arg1) throws SQLException {
        realCallableStatement.setClob(arg0, arg1);
    }

    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setClob(arg0, arg1, arg2);
    }

    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
        realCallableStatement.setNCharacterStream(arg0, arg1);
    }

    public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setNCharacterStream(arg0, arg1, arg2);
    }

    public void setNClob(int arg0, Reader arg1) throws SQLException {
        realCallableStatement.setNClob(arg0, arg1);
    }

    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
        realCallableStatement.setNClob(arg0, arg1, arg2);
    }

    public void setNString(int arg0, String arg1) throws SQLException {
        realCallableStatement.setNString(arg0, arg1);
    }

    public boolean isClosed() throws SQLException {
        return realCallableStatement.isClosed();
    }

    public boolean isPoolable() throws SQLException {
        return realCallableStatement.isPoolable();
    }

    public void setPoolable(boolean arg0) throws SQLException {
        realCallableStatement.setPoolable(arg0);
    }

    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return realCallableStatement.isWrapperFor(arg0);
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return realCallableStatement.unwrap(arg0);
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
        return realCallableStatement.getNClob(parameterIndex);
    }

    public NClob getNClob(String parameterName) throws SQLException {
        return realCallableStatement.getNClob(parameterName);
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
        return realCallableStatement.getRowId(parameterIndex);
    }

    public RowId getRowId(String parameterName) throws SQLException {
        return realCallableStatement.getRowId(parameterName);
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return realCallableStatement.getSQLXML(parameterIndex);
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return realCallableStatement.getSQLXML(parameterName);
    }

    public void setNClob(String parameterName, NClob value) throws SQLException {
        realCallableStatement.setNClob(parameterName, value);
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
        realCallableStatement.setRowId(parameterName, x);
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        realCallableStatement.setSQLXML(parameterName, xmlObject);
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        realCallableStatement.setNClob(parameterIndex, value);
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        realCallableStatement.setRowId(parameterIndex, x);
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        realCallableStatement.setSQLXML(parameterIndex, xmlObject);
    }
}
