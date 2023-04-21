package net.sf.jdbcwrappers;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Base class for {@link ResultSet} wrappers.
 * Except for {@link #getStatement()}, all methods delegate to the target {@link ResultSet} object.
 * 
 * @author Andreas Veithen
 * @version $Id: ResultSetWrapper.java 63 2011-11-03 14:32:17Z veithen $
 */
public class ResultSetWrapper extends AbstractWrapper<ResultSet> implements ResultSet {

    Statement statement;

    /**
     * Delegate method for {@link ResultSet#absolute(int)}.
     * 
     * {@inheritDoc}
     */
    public boolean absolute(int row) throws SQLException {
        return parent.absolute(row);
    }

    /**
     * Delegate method for {@link ResultSet#afterLast()}.
     * 
     * {@inheritDoc}
     */
    public void afterLast() throws SQLException {
        parent.afterLast();
    }

    /**
     * Delegate method for {@link ResultSet#beforeFirst()}.
     * 
     * {@inheritDoc}
     */
    public void beforeFirst() throws SQLException {
        parent.beforeFirst();
    }

    /**
     * Delegate method for {@link ResultSet#cancelRowUpdates()}.
     * 
     * {@inheritDoc}
     */
    public void cancelRowUpdates() throws SQLException {
        parent.cancelRowUpdates();
    }

    /**
     * Delegate method for {@link ResultSet#clearWarnings()}.
     * 
     * {@inheritDoc}
     */
    public void clearWarnings() throws SQLException {
        parent.clearWarnings();
    }

    /**
     * Delegate method for {@link ResultSet#close()}.
     * 
     * {@inheritDoc}
     */
    public void close() throws SQLException {
        parent.close();
    }

    /**
     * Delegate method for {@link ResultSet#deleteRow()}.
     * 
     * {@inheritDoc}
     */
    public void deleteRow() throws SQLException {
        parent.deleteRow();
    }

    /**
     * Delegate method for {@link ResultSet#findColumn(String)}.
     * 
     * {@inheritDoc}
     */
    public int findColumn(String columnName) throws SQLException {
        return parent.findColumn(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#first()}.
     * 
     * {@inheritDoc}
     */
    public boolean first() throws SQLException {
        return parent.first();
    }

    /**
     * Delegate method for {@link ResultSet#getArray(int)}.
     * 
     * {@inheritDoc}
     */
    public Array getArray(int i) throws SQLException {
        return parent.getArray(i);
    }

    /**
     * Delegate method for {@link ResultSet#getArray(String)}.
     * 
     * {@inheritDoc}
     */
    public Array getArray(String colName) throws SQLException {
        return parent.getArray(colName);
    }

    /**
     * Delegate method for {@link ResultSet#getAsciiStream(int)}.
     * 
     * {@inheritDoc}
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return parent.getAsciiStream(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getAsciiStream(String)}.
     * 
     * {@inheritDoc}
     */
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return parent.getAsciiStream(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getBigDecimal(int, int)}.
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return parent.getBigDecimal(columnIndex, scale);
    }

    /**
     * Delegate method for {@link ResultSet#getBigDecimal(int)}.
     * 
     * {@inheritDoc}
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return parent.getBigDecimal(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getBigDecimal(String, int)}.
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        return parent.getBigDecimal(columnName, scale);
    }

    /**
     * Delegate method for {@link ResultSet#getBigDecimal(String)}.
     * 
     * {@inheritDoc}
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return parent.getBigDecimal(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getBinaryStream(int)}.
     * 
     * {@inheritDoc}
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return parent.getBinaryStream(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getBinaryStream(String)}.
     * 
     * {@inheritDoc}
     */
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return parent.getBinaryStream(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getBlob(int)}.
     * 
     * {@inheritDoc}
     */
    public Blob getBlob(int i) throws SQLException {
        return parent.getBlob(i);
    }

    /**
     * Delegate method for {@link ResultSet#getBlob(String)}.
     * 
     * {@inheritDoc}
     */
    public Blob getBlob(String colName) throws SQLException {
        return parent.getBlob(colName);
    }

    /**
     * Delegate method for {@link ResultSet#getBoolean(int)}.
     * 
     * {@inheritDoc}
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        return parent.getBoolean(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getBoolean(String)}.
     * 
     * {@inheritDoc}
     */
    public boolean getBoolean(String columnName) throws SQLException {
        return parent.getBoolean(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getByte(int)}.
     * 
     * {@inheritDoc}
     */
    public byte getByte(int columnIndex) throws SQLException {
        return parent.getByte(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getByte(String)}.
     * 
     * {@inheritDoc}
     */
    public byte getByte(String columnName) throws SQLException {
        return parent.getByte(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getBytes(int)}.
     * 
     * {@inheritDoc}
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        return parent.getBytes(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getBytes(String)}.
     * 
     * {@inheritDoc}
     */
    public byte[] getBytes(String columnName) throws SQLException {
        return parent.getBytes(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getCharacterStream(int)}.
     * 
     * {@inheritDoc}
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return parent.getCharacterStream(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getCharacterStream(String)}.
     * 
     * {@inheritDoc}
     */
    public Reader getCharacterStream(String columnName) throws SQLException {
        return parent.getCharacterStream(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getClob(int)}.
     * 
     * {@inheritDoc}
     */
    public Clob getClob(int i) throws SQLException {
        return parent.getClob(i);
    }

    /**
     * Delegate method for {@link ResultSet#getClob(String)}.
     * 
     * {@inheritDoc}
     */
    public Clob getClob(String colName) throws SQLException {
        return parent.getClob(colName);
    }

    /**
     * Delegate method for {@link ResultSet#getConcurrency()}.
     * 
     * {@inheritDoc}
     */
    public int getConcurrency() throws SQLException {
        return parent.getConcurrency();
    }

    /**
     * Delegate method for {@link ResultSet#getCursorName()}.
     * 
     * {@inheritDoc}
     */
    public String getCursorName() throws SQLException {
        return parent.getCursorName();
    }

    /**
     * Delegate method for {@link ResultSet#getDate(int, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return parent.getDate(columnIndex, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getDate(int)}.
     * 
     * {@inheritDoc}
     */
    public Date getDate(int columnIndex) throws SQLException {
        return parent.getDate(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getDate(String, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return parent.getDate(columnName, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getDate(String)}.
     * 
     * {@inheritDoc}
     */
    public Date getDate(String columnName) throws SQLException {
        return parent.getDate(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getDouble(int)}.
     * 
     * {@inheritDoc}
     */
    public double getDouble(int columnIndex) throws SQLException {
        return parent.getDouble(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getDouble(String)}.
     * 
     * {@inheritDoc}
     */
    public double getDouble(String columnName) throws SQLException {
        return parent.getDouble(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getFetchDirection()}.
     * 
     * {@inheritDoc}
     */
    public int getFetchDirection() throws SQLException {
        return parent.getFetchDirection();
    }

    /**
     * Delegate method for {@link ResultSet#getFetchSize()}.
     * 
     * {@inheritDoc}
     */
    public int getFetchSize() throws SQLException {
        return parent.getFetchSize();
    }

    /**
     * Delegate method for {@link ResultSet#getFloat(int)}.
     * 
     * {@inheritDoc}
     */
    public float getFloat(int columnIndex) throws SQLException {
        return parent.getFloat(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getFloat(String)}.
     * 
     * {@inheritDoc}
     */
    public float getFloat(String columnName) throws SQLException {
        return parent.getFloat(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getInt(int)}.
     * 
     * {@inheritDoc}
     */
    public int getInt(int columnIndex) throws SQLException {
        return parent.getInt(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getInt(String)}.
     * 
     * {@inheritDoc}
     */
    public int getInt(String columnName) throws SQLException {
        return parent.getInt(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getLong(int)}.
     * 
     * {@inheritDoc}
     */
    public long getLong(int columnIndex) throws SQLException {
        return parent.getLong(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getLong(String)}.
     * 
     * {@inheritDoc}
     */
    public long getLong(String columnName) throws SQLException {
        return parent.getLong(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getMetaData()}.
     * 
     * {@inheritDoc}
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return parent.getMetaData();
    }

    /**
     * Delegate method for {@link ResultSet#getObject(int, Map)}.
     * 
     * {@inheritDoc}
     */
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return parent.getObject(i, map);
    }

    /**
     * Delegate method for {@link ResultSet#getObject(int)}.
     * 
     * {@inheritDoc}
     */
    public Object getObject(int columnIndex) throws SQLException {
        return parent.getObject(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getObject(String, Map)}.
     * 
     * {@inheritDoc}
     */
    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        return parent.getObject(colName, map);
    }

    /**
     * Delegate method for {@link ResultSet#getObject(String)}.
     * 
     * {@inheritDoc}
     */
    public Object getObject(String columnName) throws SQLException {
        return parent.getObject(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getRef(int)}.
     * 
     * {@inheritDoc}
     */
    public Ref getRef(int i) throws SQLException {
        return parent.getRef(i);
    }

    /**
     * Delegate method for {@link ResultSet#getRef(String)}.
     * 
     * {@inheritDoc}
     */
    public Ref getRef(String colName) throws SQLException {
        return parent.getRef(colName);
    }

    /**
     * Delegate method for {@link ResultSet#getRow()}.
     * 
     * {@inheritDoc}
     */
    public int getRow() throws SQLException {
        return parent.getRow();
    }

    /**
     * Delegate method for {@link ResultSet#getShort(int)}.
     * 
     * {@inheritDoc}
     */
    public short getShort(int columnIndex) throws SQLException {
        return parent.getShort(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getShort(String)}.
     * 
     * {@inheritDoc}
     */
    public short getShort(String columnName) throws SQLException {
        return parent.getShort(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getStatement()}.
     * This method returns the {@link StatementWrapper} or {@link PreparedStatementWrapper}
     * object that created this wrapper. For consistency reasons, it can't be
     * overridden.
     * 
     * {@inheritDoc}
     */
    public final Statement getStatement() throws SQLException {
        return statement;
    }

    /**
     * Delegate method for {@link ResultSet#getString(int)}.
     * 
     * {@inheritDoc}
     */
    public String getString(int columnIndex) throws SQLException {
        return parent.getString(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getString(String)}.
     * 
     * {@inheritDoc}
     */
    public String getString(String columnName) throws SQLException {
        return parent.getString(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(int, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return parent.getTime(columnIndex, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(int)}.
     * 
     * {@inheritDoc}
     */
    public Time getTime(int columnIndex) throws SQLException {
        return parent.getTime(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(String, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return parent.getTime(columnName, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(String)}.
     * 
     * {@inheritDoc}
     */
    public Time getTime(String columnName) throws SQLException {
        return parent.getTime(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getTimestamp(int, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return parent.getTimestamp(columnIndex, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getTimestamp(int)}.
     * 
     * {@inheritDoc}
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return parent.getTimestamp(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(String, Calendar)}.
     * 
     * {@inheritDoc}
     */
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        return parent.getTimestamp(columnName, cal);
    }

    /**
     * Delegate method for {@link ResultSet#getTime(String)}.
     * 
     * {@inheritDoc}
     */
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return parent.getTimestamp(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getType()}.
     * 
     * {@inheritDoc}
     */
    public int getType() throws SQLException {
        return parent.getType();
    }

    /**
     * Delegate method for {@link ResultSet#getUnicodeStream(int)}.
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return parent.getUnicodeStream(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getUnicodeStream(String)}.
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return parent.getUnicodeStream(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getURL(int)}.
     * 
     * {@inheritDoc}
     */
    public URL getURL(int columnIndex) throws SQLException {
        return parent.getURL(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#getURL(String)}.
     * 
     * {@inheritDoc}
     */
    public URL getURL(String columnName) throws SQLException {
        return parent.getURL(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#getWarnings()}.
     * 
     * {@inheritDoc}
     */
    public SQLWarning getWarnings() throws SQLException {
        return parent.getWarnings();
    }

    /**
     * Delegate method for {@link ResultSet#insertRow()}.
     * 
     * {@inheritDoc}
     */
    public void insertRow() throws SQLException {
        parent.insertRow();
    }

    /**
     * Delegate method for {@link ResultSet#isAfterLast()}.
     * 
     * {@inheritDoc}
     */
    public boolean isAfterLast() throws SQLException {
        return parent.isAfterLast();
    }

    /**
     * Delegate method for {@link ResultSet#isBeforeFirst()}.
     * 
     * {@inheritDoc}
     */
    public boolean isBeforeFirst() throws SQLException {
        return parent.isBeforeFirst();
    }

    /**
     * Delegate method for {@link ResultSet#isFirst()}.
     * 
     * {@inheritDoc}
     */
    public boolean isFirst() throws SQLException {
        return parent.isFirst();
    }

    /**
     * Delegate method for {@link ResultSet#isLast()}.
     * 
     * {@inheritDoc}
     */
    public boolean isLast() throws SQLException {
        return parent.isLast();
    }

    /**
     * Delegate method for {@link ResultSet#last()}.
     * 
     * {@inheritDoc}
     */
    public boolean last() throws SQLException {
        return parent.last();
    }

    /**
     * Delegate method for {@link ResultSet#moveToCurrentRow()}.
     * 
     * {@inheritDoc}
     */
    public void moveToCurrentRow() throws SQLException {
        parent.moveToCurrentRow();
    }

    /**
     * Delegate method for {@link ResultSet#moveToInsertRow()}.
     * 
     * {@inheritDoc}
     */
    public void moveToInsertRow() throws SQLException {
        parent.moveToInsertRow();
    }

    /**
     * Delegate method for {@link ResultSet#next()}.
     * 
     * {@inheritDoc}
     */
    public boolean next() throws SQLException {
        return parent.next();
    }

    /**
     * Delegate method for {@link ResultSet#previous()}.
     * 
     * {@inheritDoc}
     */
    public boolean previous() throws SQLException {
        return parent.previous();
    }

    /**
     * Delegate method for {@link ResultSet#refreshRow()}.
     * 
     * {@inheritDoc}
     */
    public void refreshRow() throws SQLException {
        parent.refreshRow();
    }

    /**
     * Delegate method for {@link ResultSet#relative(int)}.
     * 
     * {@inheritDoc}
     */
    public boolean relative(int rows) throws SQLException {
        return parent.relative(rows);
    }

    /**
     * Delegate method for {@link ResultSet#rowDeleted()}.
     * 
     * {@inheritDoc}
     */
    public boolean rowDeleted() throws SQLException {
        return parent.rowDeleted();
    }

    /**
     * Delegate method for {@link ResultSet#rowInserted()}.
     * 
     * {@inheritDoc}
     */
    public boolean rowInserted() throws SQLException {
        return parent.rowInserted();
    }

    /**
     * Delegate method for {@link ResultSet#rowUpdated()}.
     * 
     * {@inheritDoc}
     */
    public boolean rowUpdated() throws SQLException {
        return parent.rowUpdated();
    }

    /**
     * Delegate method for {@link ResultSet#setFetchDirection(int)}.
     * 
     * {@inheritDoc}
     */
    public void setFetchDirection(int direction) throws SQLException {
        parent.setFetchDirection(direction);
    }

    /**
     * Delegate method for {@link ResultSet#setFetchSize(int)}.
     * 
     * {@inheritDoc}
     */
    public void setFetchSize(int rows) throws SQLException {
        parent.setFetchSize(rows);
    }

    /**
     * Delegate method for {@link ResultSet#updateArray(int, Array)}.
     * 
     * {@inheritDoc}
     */
    public void updateArray(int columnIndex, Array x) throws SQLException {
        parent.updateArray(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateArray(String, Array)}.
     * 
     * {@inheritDoc}
     */
    public void updateArray(String columnName, Array x) throws SQLException {
        parent.updateArray(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateAsciiStream(int, InputStream, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        parent.updateAsciiStream(columnIndex, x, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateAsciiStream(String, InputStream, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        parent.updateAsciiStream(columnName, x, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateBigDecimal(int, BigDecimal)}.
     * 
     * {@inheritDoc}
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        parent.updateBigDecimal(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBigDecimal(String, BigDecimal)}.
     * 
     * {@inheritDoc}
     */
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        parent.updateBigDecimal(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBinaryStream(int, InputStream, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        parent.updateBinaryStream(columnIndex, x, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateBinaryStream(String, InputStream, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        parent.updateBinaryStream(columnName, x, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateBlob(int, Blob)}.
     * 
     * {@inheritDoc}
     */
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        parent.updateBlob(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBlob(String, Blob)}.
     * 
     * {@inheritDoc}
     */
    public void updateBlob(String columnName, Blob x) throws SQLException {
        parent.updateBlob(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBoolean(int, boolean)}.
     * 
     * {@inheritDoc}
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        parent.updateBoolean(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBoolean(String, boolean)}.
     * 
     * {@inheritDoc}
     */
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        parent.updateBoolean(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateByte(int, byte)}.
     * 
     * {@inheritDoc}
     */
    public void updateByte(int columnIndex, byte x) throws SQLException {
        parent.updateByte(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateByte(String, byte)}.
     * 
     * {@inheritDoc}
     */
    public void updateByte(String columnName, byte x) throws SQLException {
        parent.updateByte(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBytes(int, byte[])}.
     * 
     * {@inheritDoc}
     */
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        parent.updateBytes(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateBytes(String, byte[])}.
     * 
     * {@inheritDoc}
     */
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        parent.updateBytes(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateCharacterStream(int, Reader, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        parent.updateCharacterStream(columnIndex, x, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateCharacterStream(String, Reader, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        parent.updateCharacterStream(columnName, reader, length);
    }

    /**
     * Delegate method for {@link ResultSet#updateClob(int, Clob)}.
     * 
     * {@inheritDoc}
     */
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        parent.updateClob(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateClob(String, Clob)}.
     * 
     * {@inheritDoc}
     */
    public void updateClob(String columnName, Clob x) throws SQLException {
        parent.updateClob(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateDate(int, Date)}.
     * 
     * {@inheritDoc}
     */
    public void updateDate(int columnIndex, Date x) throws SQLException {
        parent.updateDate(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateDate(String, Date)}.
     * 
     * {@inheritDoc}
     */
    public void updateDate(String columnName, Date x) throws SQLException {
        parent.updateDate(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateDouble(int, double)}.
     * 
     * {@inheritDoc}
     */
    public void updateDouble(int columnIndex, double x) throws SQLException {
        parent.updateDouble(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateDouble(String, double)}.
     * 
     * {@inheritDoc}
     */
    public void updateDouble(String columnName, double x) throws SQLException {
        parent.updateDouble(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateFloat(int, float)}.
     * 
     * {@inheritDoc}
     */
    public void updateFloat(int columnIndex, float x) throws SQLException {
        parent.updateFloat(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateFloat(String, float)}.
     * 
     * {@inheritDoc}
     */
    public void updateFloat(String columnName, float x) throws SQLException {
        parent.updateFloat(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateInt(int, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateInt(int columnIndex, int x) throws SQLException {
        parent.updateInt(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateInt(String, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateInt(String columnName, int x) throws SQLException {
        parent.updateInt(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateLong(int, long)}.
     * 
     * {@inheritDoc}
     */
    public void updateLong(int columnIndex, long x) throws SQLException {
        parent.updateLong(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateLong(String, long)}.
     * 
     * {@inheritDoc}
     */
    public void updateLong(String columnName, long x) throws SQLException {
        parent.updateLong(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateNull(int)}.
     * 
     * {@inheritDoc}
     */
    public void updateNull(int columnIndex) throws SQLException {
        parent.updateNull(columnIndex);
    }

    /**
     * Delegate method for {@link ResultSet#updateNull(String)}.
     * 
     * {@inheritDoc}
     */
    public void updateNull(String columnName) throws SQLException {
        parent.updateNull(columnName);
    }

    /**
     * Delegate method for {@link ResultSet#updateObject(int, Object, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        parent.updateObject(columnIndex, x, scale);
    }

    /**
     * Delegate method for {@link ResultSet#updateObject(int, Object)}.
     * 
     * {@inheritDoc}
     */
    public void updateObject(int columnIndex, Object x) throws SQLException {
        parent.updateObject(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateObject(String, Object, int)}.
     * 
     * {@inheritDoc}
     */
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        parent.updateObject(columnName, x, scale);
    }

    /**
     * Delegate method for {@link ResultSet#updateObject(String, Object)}.
     * 
     * {@inheritDoc}
     */
    public void updateObject(String columnName, Object x) throws SQLException {
        parent.updateObject(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateRef(int, Ref)}.
     * 
     * {@inheritDoc}
     */
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        parent.updateRef(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateRef(String, Ref)}.
     * 
     * {@inheritDoc}
     */
    public void updateRef(String columnName, Ref x) throws SQLException {
        parent.updateRef(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateRow()}.
     * 
     * {@inheritDoc}
     */
    public void updateRow() throws SQLException {
        parent.updateRow();
    }

    /**
     * Delegate method for {@link ResultSet#updateShort(int, short)}.
     * 
     * {@inheritDoc}
     */
    public void updateShort(int columnIndex, short x) throws SQLException {
        parent.updateShort(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateShort(String, short)}.
     * 
     * {@inheritDoc}
     */
    public void updateShort(String columnName, short x) throws SQLException {
        parent.updateShort(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateString(int, String)}.
     * 
     * {@inheritDoc}
     */
    public void updateString(int columnIndex, String x) throws SQLException {
        parent.updateString(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateString(String, String)}.
     * 
     * {@inheritDoc}
     */
    public void updateString(String columnName, String x) throws SQLException {
        parent.updateString(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateTime(int, Time)}.
     * 
     * {@inheritDoc}
     */
    public void updateTime(int columnIndex, Time x) throws SQLException {
        parent.updateTime(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateTime(String, Time)}.
     * 
     * {@inheritDoc}
     */
    public void updateTime(String columnName, Time x) throws SQLException {
        parent.updateTime(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateTimestamp(int, Timestamp)}.
     * 
     * {@inheritDoc}
     */
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        parent.updateTimestamp(columnIndex, x);
    }

    /**
     * Delegate method for {@link ResultSet#updateTime(String, Time)}.
     * 
     * {@inheritDoc}
     */
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        parent.updateTimestamp(columnName, x);
    }

    /**
     * Delegate method for {@link ResultSet#wasNull()}.
     * 
     * {@inheritDoc}
     */
    public boolean wasNull() throws SQLException {
        return parent.wasNull();
    }
}
