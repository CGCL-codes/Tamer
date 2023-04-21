package com.liferay.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * <a href="LiferayCallableStatement.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class LiferayCallableStatement implements CallableStatement {

    public LiferayCallableStatement(CallableStatement cs) {
        _cs = cs;
    }

    public Object getObject(int p1, Map p2) throws SQLException {
        return _cs.getObject(p1, p2);
    }

    public Object getObject(String p1, Map p2) throws SQLException {
        return _cs.getObject(p1, p2);
    }

    public Object getObject(String p1) throws SQLException {
        return _cs.getObject(p1);
    }

    public Object getObject(int p1) throws SQLException {
        return _cs.getObject(p1);
    }

    public boolean getBoolean(int p1) throws SQLException {
        return _cs.getBoolean(p1);
    }

    public boolean getBoolean(String p1) throws SQLException {
        return _cs.getBoolean(p1);
    }

    public byte getByte(String p1) throws SQLException {
        return _cs.getByte(p1);
    }

    public byte getByte(int p1) throws SQLException {
        return _cs.getByte(p1);
    }

    public short getShort(String p1) throws SQLException {
        return _cs.getShort(p1);
    }

    public short getShort(int p1) throws SQLException {
        return _cs.getShort(p1);
    }

    public int getInt(int p1) throws SQLException {
        return _cs.getInt(p1);
    }

    public int getInt(String p1) throws SQLException {
        return _cs.getInt(p1);
    }

    public long getLong(int p1) throws SQLException {
        return _cs.getLong(p1);
    }

    public long getLong(String p1) throws SQLException {
        return _cs.getLong(p1);
    }

    public float getFloat(int p1) throws SQLException {
        return _cs.getFloat(p1);
    }

    public float getFloat(String p1) throws SQLException {
        return _cs.getFloat(p1);
    }

    public double getDouble(String p1) throws SQLException {
        return _cs.getDouble(p1);
    }

    public double getDouble(int p1) throws SQLException {
        return _cs.getDouble(p1);
    }

    public byte[] getBytes(String p1) throws SQLException {
        return _cs.getBytes(p1);
    }

    public byte[] getBytes(int p1) throws SQLException {
        return _cs.getBytes(p1);
    }

    public URL getURL(String p1) throws SQLException {
        return _cs.getURL(p1);
    }

    public URL getURL(int p1) throws SQLException {
        return _cs.getURL(p1);
    }

    public void setBoolean(String p1, boolean p2) throws SQLException {
        _cs.setBoolean(p1, p2);
    }

    public void setByte(String p1, byte p2) throws SQLException {
        _cs.setByte(p1, p2);
    }

    public void setShort(String p1, short p2) throws SQLException {
        _cs.setShort(p1, p2);
    }

    public void setInt(String p1, int p2) throws SQLException {
        _cs.setInt(p1, p2);
    }

    public void setLong(String p1, long p2) throws SQLException {
        _cs.setLong(p1, p2);
    }

    public void setFloat(String p1, float p2) throws SQLException {
        _cs.setFloat(p1, p2);
    }

    public void setDouble(String p1, double p2) throws SQLException {
        _cs.setDouble(p1, p2);
    }

    public String getString(int p1) throws SQLException {
        return _cs.getString(p1);
    }

    public String getString(String p1) throws SQLException {
        return _cs.getString(p1);
    }

    public Ref getRef(int p1) throws SQLException {
        return _cs.getRef(p1);
    }

    public Ref getRef(String p1) throws SQLException {
        return _cs.getRef(p1);
    }

    public void setURL(String p1, URL p2) throws SQLException {
        _cs.setURL(p1, p2);
    }

    public void setTime(String p1, Time p2, Calendar p3) throws SQLException {
        _cs.setTime(p1, p2, p3);
    }

    public void setTime(String p1, Time p2) throws SQLException {
        _cs.setTime(p1, p2);
    }

    public Time getTime(int p1, Calendar p2) throws SQLException {
        return _cs.getTime(p1, p2);
    }

    public Time getTime(int p1) throws SQLException {
        return _cs.getTime(p1);
    }

    public Time getTime(String p1, Calendar p2) throws SQLException {
        return _cs.getTime(p1, p2);
    }

    public Time getTime(String p1) throws SQLException {
        return _cs.getTime(p1);
    }

    public Date getDate(String p1, Calendar p2) throws SQLException {
        return _cs.getDate(p1, p2);
    }

    public Date getDate(int p1, Calendar p2) throws SQLException {
        return _cs.getDate(p1, p2);
    }

    public Date getDate(String p1) throws SQLException {
        return _cs.getDate(p1);
    }

    public Date getDate(int p1) throws SQLException {
        return _cs.getDate(p1);
    }

    public void registerOutParameter(String p1, int p2, int p3) throws SQLException {
        _cs.registerOutParameter(p1, p2, p3);
    }

    public void registerOutParameter(String p1, int p2) throws SQLException {
        _cs.registerOutParameter(p1, p2);
    }

    public void registerOutParameter(int p1, int p2) throws SQLException {
        _cs.registerOutParameter(p1, p2);
    }

    public void registerOutParameter(int p1, int p2, String p3) throws SQLException {
        _cs.registerOutParameter(p1, p2, p3);
    }

    public void registerOutParameter(int p1, int p2, int p3) throws SQLException {
        _cs.registerOutParameter(p1, p2, p3);
    }

    public void registerOutParameter(String p1, int p2, String p3) throws SQLException {
        _cs.registerOutParameter(p1, p2, p3);
    }

    public boolean wasNull() throws SQLException {
        return _cs.wasNull();
    }

    public BigDecimal getBigDecimal(int p1) throws SQLException {
        return _cs.getBigDecimal(p1);
    }

    public BigDecimal getBigDecimal(String p1) throws SQLException {
        return _cs.getBigDecimal(p1);
    }

    public BigDecimal getBigDecimal(int p1, int p2) throws SQLException {
        return _cs.getBigDecimal(p1, p2);
    }

    public Timestamp getTimestamp(String p1) throws SQLException {
        return _cs.getTimestamp(p1);
    }

    public Timestamp getTimestamp(String p1, Calendar p2) throws SQLException {
        return _cs.getTimestamp(p1, p2);
    }

    public Timestamp getTimestamp(int p1) throws SQLException {
        return _cs.getTimestamp(p1);
    }

    public Timestamp getTimestamp(int p1, Calendar p2) throws SQLException {
        return _cs.getTimestamp(p1, p2);
    }

    public Blob getBlob(int p1) throws SQLException {
        return _cs.getBlob(p1);
    }

    public Blob getBlob(String p1) throws SQLException {
        return _cs.getBlob(p1);
    }

    public Clob getClob(String p1) throws SQLException {
        return _cs.getClob(p1);
    }

    public Clob getClob(int p1) throws SQLException {
        return _cs.getClob(p1);
    }

    public Array getArray(int p1) throws SQLException {
        return _cs.getArray(p1);
    }

    public Array getArray(String p1) throws SQLException {
        return _cs.getArray(p1);
    }

    public void setNull(String p1, int p2) throws SQLException {
        _cs.setNull(p1, p2);
    }

    public void setNull(String p1, int p2, String p3) throws SQLException {
        _cs.setNull(p1, p2, p3);
    }

    public void setBigDecimal(String p1, BigDecimal p2) throws SQLException {
        _cs.setBigDecimal(p1, p2);
    }

    public void setString(String p1, String p2) throws SQLException {
        _cs.setString(p1, p2);
    }

    public void setBytes(String p1, byte[] p2) throws SQLException {
        _cs.setBytes(p1, p2);
    }

    public void setDate(String p1, Date p2) throws SQLException {
        _cs.setDate(p1, p2);
    }

    public void setDate(String p1, Date p2, Calendar p3) throws SQLException {
        _cs.setDate(p1, p2, p3);
    }

    public void setTimestamp(String p1, Timestamp p2) throws SQLException {
        _cs.setTimestamp(p1, p2);
    }

    public void setTimestamp(String p1, Timestamp p2, Calendar p3) throws SQLException {
        _cs.setTimestamp(p1, p2, p3);
    }

    public void setAsciiStream(String p1, InputStream p2, int p3) throws SQLException {
        _cs.setAsciiStream(p1, p2, p3);
    }

    public void setBinaryStream(String p1, InputStream p2, int p3) throws SQLException {
        _cs.setBinaryStream(p1, p2, p3);
    }

    public void setObject(String p1, Object p2, int p3, int p4) throws SQLException {
        _cs.setObject(p1, p2, p3, p4);
    }

    public void setObject(String p1, Object p2, int p3) throws SQLException {
        _cs.setObject(p1, p2, p3);
    }

    public void setObject(String p1, Object p2) throws SQLException {
        _cs.setObject(p1, p2);
    }

    public void setCharacterStream(String p1, Reader p2, int p3) throws SQLException {
        _cs.setCharacterStream(p1, p2, p3);
    }

    public void setBoolean(int p1, boolean p2) throws SQLException {
        _cs.setBoolean(p1, p2);
    }

    public void setByte(int p1, byte p2) throws SQLException {
        _cs.setByte(p1, p2);
    }

    public void setShort(int p1, short p2) throws SQLException {
        _cs.setShort(p1, p2);
    }

    public void setInt(int p1, int p2) throws SQLException {
        _cs.setInt(p1, p2);
    }

    public void setLong(int p1, long p2) throws SQLException {
        _cs.setLong(p1, p2);
    }

    public void setFloat(int p1, float p2) throws SQLException {
        _cs.setFloat(p1, p2);
    }

    public void setDouble(int p1, double p2) throws SQLException {
        _cs.setDouble(p1, p2);
    }

    public void setURL(int p1, URL p2) throws SQLException {
        _cs.setURL(p1, p2);
    }

    public void setTime(int p1, Time p2, Calendar p3) throws SQLException {
        _cs.setTime(p1, p2, p3);
    }

    public void setTime(int p1, Time p2) throws SQLException {
        _cs.setTime(p1, p2);
    }

    public boolean execute() throws SQLException {
        return _cs.execute();
    }

    public void setNull(int p1, int p2, String p3) throws SQLException {
        _cs.setNull(p1, p2, p3);
    }

    public void setNull(int p1, int p2) throws SQLException {
        _cs.setNull(p1, p2);
    }

    public void setBigDecimal(int p1, BigDecimal p2) throws SQLException {
        _cs.setBigDecimal(p1, p2);
    }

    public void setString(int p1, String p2) throws SQLException {
        _cs.setString(p1, p2);
    }

    public void setBytes(int p1, byte[] p2) throws SQLException {
        _cs.setBytes(p1, p2);
    }

    public void setDate(int p1, Date p2, Calendar p3) throws SQLException {
        _cs.setDate(p1, p2, p3);
    }

    public void setDate(int p1, Date p2) throws SQLException {
        _cs.setDate(p1, p2);
    }

    public void setTimestamp(int p1, Timestamp p2, Calendar p3) throws SQLException {
        _cs.setTimestamp(p1, p2, p3);
    }

    public void setTimestamp(int p1, Timestamp p2) throws SQLException {
        _cs.setTimestamp(p1, p2);
    }

    public void setAsciiStream(int p1, InputStream p2, int p3) throws SQLException {
        _cs.setAsciiStream(p1, p2, p3);
    }

    public void setBinaryStream(int p1, InputStream p2, int p3) throws SQLException {
        _cs.setBinaryStream(p1, p2, p3);
    }

    public void setObject(int p1, Object p2, int p3) throws SQLException {
        _cs.setObject(p1, p2, p3);
    }

    public void setObject(int p1, Object p2, int p3, int p4) throws SQLException {
        _cs.setObject(p1, p2, p3, p4);
    }

    public void setObject(int p1, Object p2) throws SQLException {
        _cs.setObject(p1, p2);
    }

    public void setCharacterStream(int p1, Reader p2, int p3) throws SQLException {
        _cs.setCharacterStream(p1, p2, p3);
    }

    public ResultSet executeQuery() throws SQLException {
        return _cs.executeQuery();
    }

    public int executeUpdate() throws SQLException {
        return _cs.executeUpdate();
    }

    public void setUnicodeStream(int p1, InputStream p2, int p3) throws SQLException {
        _cs.setUnicodeStream(p1, p2, p3);
    }

    public void clearParameters() throws SQLException {
        _cs.clearParameters();
    }

    public void addBatch() throws SQLException {
        _cs.addBatch();
    }

    public void setRef(int p1, Ref p2) throws SQLException {
        _cs.setRef(p1, p2);
    }

    public void setBlob(int p1, Blob p2) throws SQLException {
        _cs.setBlob(p1, p2);
    }

    public void setClob(int p1, Clob p2) throws SQLException {
        _cs.setClob(p1, p2);
    }

    public void setArray(int p1, Array p2) throws SQLException {
        _cs.setArray(p1, p2);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return _cs.getMetaData();
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        return _cs.getParameterMetaData();
    }

    public void close() throws SQLException {
        _cs.close();
    }

    public boolean execute(String p1, int p2) throws SQLException {
        return _cs.execute(p1, p2);
    }

    public boolean execute(String p1) throws SQLException {
        return _cs.execute(p1);
    }

    public boolean execute(String p1, String[] p2) throws SQLException {
        return _cs.execute(p1, p2);
    }

    public boolean execute(String p1, int[] p2) throws SQLException {
        return _cs.execute(p1, p2);
    }

    public ResultSet executeQuery(String p1) throws SQLException {
        return _cs.executeQuery(p1);
    }

    public int executeUpdate(String p1) throws SQLException {
        return _cs.executeUpdate(p1);
    }

    public int executeUpdate(String p1, String[] p2) throws SQLException {
        return _cs.executeUpdate(p1, p2);
    }

    public int executeUpdate(String p1, int[] p2) throws SQLException {
        return _cs.executeUpdate(p1, p2);
    }

    public int executeUpdate(String p1, int p2) throws SQLException {
        return _cs.executeUpdate(p1, p2);
    }

    public void addBatch(String p1) throws SQLException {
        _cs.addBatch(p1);
    }

    public int getMaxFieldSize() throws SQLException {
        return _cs.getMaxFieldSize();
    }

    public void setMaxFieldSize(int p1) throws SQLException {
        _cs.setMaxFieldSize(p1);
    }

    public int getMaxRows() throws SQLException {
        return _cs.getMaxRows();
    }

    public void setMaxRows(int p1) throws SQLException {
        _cs.setMaxRows(p1);
    }

    public void setEscapeProcessing(boolean p1) throws SQLException {
        _cs.setEscapeProcessing(p1);
    }

    public int getQueryTimeout() throws SQLException {
        return _cs.getQueryTimeout();
    }

    public void setQueryTimeout(int p1) throws SQLException {
        _cs.setQueryTimeout(p1);
    }

    public void cancel() throws SQLException {
        _cs.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        return _cs.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        _cs.clearWarnings();
    }

    public void setCursorName(String p1) throws SQLException {
        _cs.setCursorName(p1);
    }

    public ResultSet getResultSet() throws SQLException {
        return _cs.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        return _cs.getUpdateCount();
    }

    public boolean getMoreResults(int p1) throws SQLException {
        return _cs.getMoreResults(p1);
    }

    public boolean getMoreResults() throws SQLException {
        return _cs.getMoreResults();
    }

    public void setFetchDirection(int p1) throws SQLException {
        _cs.setFetchDirection(p1);
    }

    public int getFetchDirection() throws SQLException {
        return _cs.getFetchDirection();
    }

    public void setFetchSize(int p1) throws SQLException {
        _cs.setFetchSize(p1);
    }

    public int getFetchSize() throws SQLException {
        return _cs.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        return _cs.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        return _cs.getResultSetType();
    }

    public void clearBatch() throws SQLException {
        _cs.clearBatch();
    }

    public int[] executeBatch() throws SQLException {
        return _cs.executeBatch();
    }

    public Connection getConnection() throws SQLException {
        return _cs.getConnection();
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return _cs.getGeneratedKeys();
    }

    public int getResultSetHoldability() throws SQLException {
        return _cs.getResultSetHoldability();
    }

    private CallableStatement _cs;
}
