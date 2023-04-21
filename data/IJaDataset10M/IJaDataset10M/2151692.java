package jaxlib.sql.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import jaxlib.sql.SQLTypes;

/**
 * Abstract implementation of the {@link ResultSet} interface.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: AbstractResultSet.java 2964 2011-07-31 15:50:55Z joerg_wassmer $
 */
public abstract class AbstractResultSet extends AbstractWrapper implements ResultSet {

    private transient Calendar localCalendar;

    protected AbstractResultSet() {
        super();
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public abstract void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException;

    protected Calendar getLocalCalendar() {
        if (this.localCalendar == null) this.localCalendar = Calendar.getInstance();
        return this.localCalendar;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public int findColumn(final String name) throws SQLException {
        final ResultSetMetaData meta = getMetaData();
        if (meta instanceof DefaultResultSetMetaData) {
            return ((DefaultResultSetMetaData) meta).findColumn(name);
        } else if (meta != null) {
            for (int i = 1, columnCount = meta.getColumnCount(); i <= columnCount; i++) {
                final String s = meta.getColumnName(i);
                if ((name == null) ? (s == null) : name.equalsIgnoreCase(s)) return i;
            }
            throw new SQLDataException("no such column: " + name);
        } else {
            throw new SQLDataException("no metadata available");
        }
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        return SQLTypes.toArray(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Array getArray(final String columnName) throws SQLException {
        return getArray(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        return SQLTypes.toAsciiStream(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public InputStream getAsciiStream(final String columnName) throws SQLException {
        return getAsciiStream(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return SQLTypes.toBigDecimal(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public BigDecimal getBigDecimal(final String columnName) throws SQLException {
        return getBigDecimal(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    @Deprecated
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        final BigDecimal v = getBigDecimal(columnIndex);
        return (v != null) ? v.setScale(scale, BigDecimal.ROUND_HALF_UP) : null;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    @Deprecated
    public BigDecimal getBigDecimal(final String columnName, final int scale) throws SQLException {
        return getBigDecimal(findColumn(columnName), scale);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        return SQLTypes.toBinaryStream(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public InputStream getBinaryStream(final String columnName) throws SQLException {
        return getBinaryStream(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        return SQLTypes.toBlob(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Blob getBlob(final String columnName) throws SQLException {
        return getBlob(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        return SQLTypes.toBooleanValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public boolean getBoolean(final String columnName) throws SQLException {
        return getBoolean(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        return SQLTypes.toByteValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public byte getByte(final String columnName) throws SQLException {
        return getByte(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        return SQLTypes.toByteArray(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public byte[] getBytes(final String columnName) throws SQLException {
        return getBytes(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        return SQLTypes.toCharacterStream(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Reader getCharacterStream(final String columnName) throws SQLException {
        return getCharacterStream(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        return SQLTypes.toClob(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Clob getClob(final String columnName) throws SQLException {
        return getClob(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public java.sql.Date getDate(final int columnIndex) throws SQLException {
        return SQLTypes.toDate(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public java.sql.Date getDate(final String columnName) throws SQLException {
        return getDate(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public java.sql.Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        final java.sql.Date d = getDate(columnIndex);
        if ((d == null) || (cal == null)) return d;
        final Calendar localCal = getLocalCalendar();
        localCal.setTime(d);
        cal.set(Calendar.YEAR, localCal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, localCal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, localCal.get(Calendar.DAY_OF_MONTH));
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public java.sql.Date getDate(final String columnName, final Calendar cal) throws SQLException {
        return getDate(findColumn(columnName), cal);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        return SQLTypes.toDoubleValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public double getDouble(final String columnName) throws SQLException {
        return getDouble(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        return SQLTypes.toFloatValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public float getFloat(final String columnName) throws SQLException {
        return getFloat(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public int getInt(final int columnIndex) throws SQLException {
        return SQLTypes.toIntValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public int getInt(final String columnName) throws SQLException {
        return getInt(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public long getLong(final int columnIndex) throws SQLException {
        return SQLTypes.toLongValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public long getLong(final String columnName) throws SQLException {
        return getLong(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        return getCharacterStream(columnIndex);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Reader getNCharacterStream(final String columnName) throws SQLException {
        return getNCharacterStream(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        return SQLTypes.toNClob(columnIndex);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public NClob getNClob(final String columnName) throws SQLException {
        return getNClob(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String getNString(final int columnIndex) throws SQLException {
        return getString(columnIndex);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String getNString(final String columnName) throws SQLException {
        return getNString(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Object getObject(final String columnName) throws SQLException {
        return getObject(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Object getObject(final String columnName, final Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(columnName), map);
    }

    /**
   * @since JDK 1.7
   */
    @Override
    public <T> T getObject(final String columnName, final Class<T> type) throws SQLException {
        return getObject(findColumn(columnName), type);
    }

    /**
   * @since JDK 1.7
   */
    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        return JdbcImplementations.getObject(this, columnIndex, type);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        return SQLTypes.toRef(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Ref getRef(final String columnName) throws SQLException {
        return getRef(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        return SQLTypes.toRowId(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RowId getRowId(final String columnName) throws SQLException {
        return getRowId(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public short getShort(final int columnIndex) throws SQLException {
        return SQLTypes.toShortValue(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public short getShort(final String columnName) throws SQLException {
        return getShort(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        return SQLTypes.toSQLXML(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public SQLXML getSQLXML(final String columnName) throws SQLException {
        return getSQLXML(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String getString(final int columnIndex) throws SQLException {
        return SQLTypes.toString(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String getString(final String columnName) throws SQLException {
        return getString(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return SQLTypes.toTime(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Time getTime(final String columnName) throws SQLException {
        return getTime(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        final Time t = getTime(columnIndex);
        if ((t == null) || (cal == null)) return t;
        final Calendar localCal = getLocalCalendar();
        localCal.setTime(t);
        cal.set(Calendar.HOUR_OF_DAY, localCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, localCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, localCal.get(Calendar.SECOND));
        return new Time(cal.getTimeInMillis());
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Time getTime(final String columnName, final Calendar cal) throws SQLException {
        return getTime(findColumn(columnName), cal);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return SQLTypes.toTimestamp(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Timestamp getTimestamp(final String columnName) throws SQLException {
        return getTimestamp(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        Timestamp ts = getTimestamp(columnIndex);
        if ((ts == null) || (cal == null)) return ts;
        final Calendar localCal = getLocalCalendar();
        localCal.setTime(ts);
        ts = null;
        cal.set(Calendar.YEAR, localCal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, localCal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, localCal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, localCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, localCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, localCal.get(Calendar.SECOND));
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Timestamp getTimestamp(final String columnName, final Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnName), cal);
    }

    /**
   * {@inheritDoc}
   */
    @Deprecated
    @Override
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        return SQLTypes.toUnicodeStream(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Deprecated
    @Override
    public InputStream getUnicodeStream(final String columnName) throws SQLException {
        return getUnicodeStream(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        return SQLTypes.toURL(getObject(columnIndex));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public URL getURL(final String columnName) throws SQLException {
        return getURL(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateArray(final int columnIndex, final java.sql.Array x) throws SQLException {
        updateObject(columnIndex, x, 0);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateArray(final String columnName, final Array x) throws SQLException {
        updateArray(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        updateObject(columnIndex, x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateAsciiStream(columnIndex, x, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final String columnName, final InputStream x) throws SQLException {
        updateAsciiStream(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final String columnName, final InputStream x, final int length) throws SQLException {
        updateAsciiStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateAsciiStream(final String columnName, final InputStream x, final long length) throws SQLException {
        updateAsciiStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBigDecimal(final String columnName, final BigDecimal x) throws SQLException {
        updateBigDecimal(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        updateObject(columnIndex, x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateBinaryStream(columnIndex, x, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final String columnName, final InputStream x) throws SQLException {
        updateBinaryStream(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final String columnName, final InputStream x, final int length) throws SQLException {
        updateBinaryStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBinaryStream(final String columnName, final InputStream x, final long length) throws SQLException {
        updateBinaryStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        updateObject(columnIndex, x, 0);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final String columnName, final Blob x) throws SQLException {
        updateBlob(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final int columnIndex, final InputStream x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final String columnName, final InputStream x) throws SQLException {
        updateBlob(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final int columnIndex, final InputStream x, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateObject(columnIndex, x, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBlob(final String columnName, final InputStream x, final long length) throws SQLException {
        updateBlob(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBoolean(final String columnName, final boolean x) throws SQLException {
        updateBoolean(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateByte(final String columnName, final byte x) throws SQLException {
        updateByte(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateBytes(final String columnName, final byte[] x) throws SQLException {
        updateBytes(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final String columnName, final Reader x) throws SQLException {
        updateCharacterStream(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        updateObject(columnIndex, x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final String columnName, final Reader reader, final int length) throws SQLException {
        updateCharacterStream(findColumn(columnName), reader, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final int columnIndex, final Reader reader, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateCharacterStream(columnIndex, reader, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateCharacterStream(final String columnName, final Reader reader, final long length) throws SQLException {
        updateCharacterStream(findColumn(columnName), reader, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        updateObject(columnIndex, x, 0);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateObject(columnIndex, x, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final String columnName, final Clob x) throws SQLException {
        updateClob(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final int columnIndex, final Reader x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final String columnName, final Reader x) throws SQLException {
        updateClob(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateClob(final String columnName, final Reader x, final long length) throws SQLException {
        updateClob(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateDate(final int columnIndex, final java.sql.Date x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateDate(final String columnName, final java.sql.Date x) throws SQLException {
        updateDate(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateDouble(final String columnName, final double x) throws SQLException {
        updateDouble(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateFloat(final String columnName, final float x) throws SQLException {
        updateFloat(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateInt(final String columnName, final int x) throws SQLException {
        updateInt(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateLong(final String columnName, final long x) throws SQLException {
        updateLong(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        updateObject(columnIndex, null);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNull(final String columnName) throws SQLException {
        updateNull(findColumn(columnName));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        if ((length < 0) || (length > Integer.MAX_VALUE)) throw new SQLDataException("length argument out of range: " + length);
        updateNCharacterStream(columnIndex, x, (int) length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNCharacterStream(final String columnName, final Reader x) throws SQLException {
        updateNCharacterStream(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    public void updateNCharacterStream(final String columnName, final Reader x, final int length) throws SQLException {
        updateNCharacterStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNCharacterStream(final String columnName, final Reader x, final long length) throws SQLException {
        updateNCharacterStream(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        updateClob(columnIndex, nClob);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final int columnIndex, final Reader x, final long length) throws SQLException {
        updateClob(columnIndex, x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final String columnName, final NClob nClob) throws SQLException {
        updateNClob(findColumn(columnName), nClob);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final int columnIndex, final Reader x) throws SQLException {
        updateClob(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final String columnName, final Reader x) throws SQLException {
        updateNClob(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNClob(final String columnName, final Reader x, final long length) throws SQLException {
        updateNClob(findColumn(columnName), x, length);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNString(final String columnName, final String nString) throws SQLException {
        updateNString(findColumn(columnName), nString);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException {
        updateObject(columnIndex, nString);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        updateObject(columnIndex, x, 0);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateObject(final String columnName, final Object x) throws SQLException {
        updateObject(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateObject(final String columnName, final Object x, final int scale) throws SQLException {
        updateObject(findColumn(columnName), x, scale);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateRef(final String columnName, final Ref x) throws SQLException {
        updateRef(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateRowId(final String columnName, final RowId x) throws SQLException {
        updateRowId(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateShort(final String columnName, final short x) throws SQLException {
        updateShort(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateSQLXML(final String columnName, final SQLXML x) throws SQLException {
        updateSQLXML(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateString(final String columnName, final String x) throws SQLException {
        updateString(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateTime(final String columnName, final Time x) throws SQLException {
        updateTime(findColumn(columnName), x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        updateObject(columnIndex, x);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void updateTimestamp(final String columnName, final Timestamp x) throws SQLException {
        updateTimestamp(findColumn(columnName), x);
    }
}
