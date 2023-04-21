package edu.upmc.opi.caBIG.caTIES.connector.bridge;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class ResultSetTemplate implements java.sql.ResultSet {

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean absolute(int arg0) throws SQLException {
        return false;
    }

    @Override
    public void afterLast() throws SQLException {
    }

    @Override
    public void beforeFirst() throws SQLException {
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public void deleteRow() throws SQLException {
    }

    @Override
    public int findColumn(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public boolean first() throws SQLException {
        return false;
    }

    @Override
    public Array getArray(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(String arg0) throws SQLException {
        return null;
    }

    @Override
    public InputStream getAsciiStream(int arg0) throws SQLException {
        return null;
    }

    @Override
    public InputStream getAsciiStream(String arg0) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int arg0) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String arg0) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(int arg0) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String arg0) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(int arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean getBoolean(String arg0) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public byte getByte(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public byte[] getBytes(int arg0) throws SQLException {
        return null;
    }

    @Override
    public byte[] getBytes(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(String arg0) throws SQLException {
        return null;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(String arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public double getDouble(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public int getInt(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String arg0) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(int arg0) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(String arg0) throws SQLException {
        return null;
    }

    @Override
    public String getNString(int arg0) throws SQLException {
        return null;
    }

    @Override
    public String getNString(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(String arg0) throws SQLException {
        return null;
    }

    @Override
    public int getRow() throws SQLException {
        return 0;
    }

    @Override
    public RowId getRowId(int arg0) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(String arg0) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {
        return null;
    }

    @Override
    public short getShort(int arg0) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(String arg0) throws SQLException {
        return 0;
    }

    @Override
    public Statement getStatement() throws SQLException {
        return null;
    }

    @Override
    public String getString(int arg0) throws SQLException {
        return null;
    }

    @Override
    public String getString(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int arg0) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String arg0) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException {
        return null;
    }

    @Override
    public int getType() throws SQLException {
        return 0;
    }

    @Override
    public URL getURL(int arg0) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(String arg0) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(int arg0) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(String arg0) throws SQLException {
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void insertRow() throws SQLException {
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return false;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return false;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return false;
    }

    @Override
    public boolean isLast() throws SQLException {
        return false;
    }

    @Override
    public boolean last() throws SQLException {
        return false;
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
    }

    @Override
    public void moveToInsertRow() throws SQLException {
    }

    @Override
    public boolean next() throws SQLException {
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        return false;
    }

    @Override
    public void refreshRow() throws SQLException {
    }

    @Override
    public boolean relative(int arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int arg0) throws SQLException {
    }

    @Override
    public void setFetchSize(int arg0) throws SQLException {
    }

    @Override
    public void updateArray(int arg0, Array arg1) throws SQLException {
    }

    @Override
    public void updateArray(String arg0, Array arg1) throws SQLException {
    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
    }

    @Override
    public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateBlob(int arg0, Blob arg1) throws SQLException {
    }

    @Override
    public void updateBlob(String arg0, Blob arg1) throws SQLException {
    }

    @Override
    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateBoolean(int arg0, boolean arg1) throws SQLException {
    }

    @Override
    public void updateBoolean(String arg0, boolean arg1) throws SQLException {
    }

    @Override
    public void updateByte(int arg0, byte arg1) throws SQLException {
    }

    @Override
    public void updateByte(String arg0, byte arg1) throws SQLException {
    }

    @Override
    public void updateBytes(int arg0, byte[] arg1) throws SQLException {
    }

    @Override
    public void updateBytes(String arg0, byte[] arg1) throws SQLException {
    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
    }

    @Override
    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateClob(int arg0, Clob arg1) throws SQLException {
    }

    @Override
    public void updateClob(String arg0, Clob arg1) throws SQLException {
    }

    @Override
    public void updateClob(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void updateClob(String arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void updateDate(int arg0, Date arg1) throws SQLException {
    }

    @Override
    public void updateDate(String arg0, Date arg1) throws SQLException {
    }

    @Override
    public void updateDouble(int arg0, double arg1) throws SQLException {
    }

    @Override
    public void updateDouble(String arg0, double arg1) throws SQLException {
    }

    @Override
    public void updateFloat(int arg0, float arg1) throws SQLException {
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
    }

    @Override
    public void updateRow() throws SQLException {
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }
}
