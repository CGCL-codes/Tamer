package net.sourceforge.squirrel_sql.fw.sql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

public class ResultSetColumnReader {

    private static final Long LONG_ZERO = Long.valueOf(0);

    private static final Double DOUBLE_ZERO = Double.valueOf(0);

    /** The <TT>ResultSet</TT> being read. */
    private final ResultSet _rs;

    /** <TT>true</TT> if the last column read had a value of SQL NULL. */
    private boolean _wasNull;

    /** Metadata for the <TT>ResultSet</TT>. */
    private ResultSetMetaData _rsmd;

    public ResultSetColumnReader(ResultSet rs) throws SQLException {
        super();
        if (rs == null) {
            throw new IllegalArgumentException("ResultSet == null");
        }
        _rs = rs;
        _rsmd = rs.getMetaData();
    }

    /**
	 * Position <TT>ResultSet</TT> to the next row.
	 *
	 * @return	<TT>true</TT> if there is a &quot;next row&quot; to read.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public boolean next() throws SQLException {
        return _rs.next();
    }

    /**
	 * Retrieve the specifed column as a <TT>Boolean</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	Boolean value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Boolean getBoolean(int columnIdx) throws SQLException {
        Object obj = _rs.getObject(columnIdx);
        Boolean results = Boolean.FALSE;
        _wasNull = true;
        if (obj != null) {
            final int columnType = _rsmd.getColumnType(columnIdx);
            if (columnType != Types.NULL) {
                _wasNull = false;
                switch(columnType) {
                    case Types.BIT:
                    case Types.BOOLEAN:
                        if (obj instanceof Boolean) {
                            results = (Boolean) obj;
                        } else {
                            if (obj instanceof Number) {
                                if (((Number) obj).intValue() == 0) {
                                    results = Boolean.FALSE;
                                } else {
                                    results = Boolean.TRUE;
                                }
                            } else {
                                results = Boolean.valueOf(obj.toString());
                            }
                        }
                        break;
                    default:
                        results = Boolean.valueOf(obj.toString());
                        break;
                }
            }
        }
        return results;
    }

    /**
	 * Retrieve the specifed column as a <TT>Date</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	Time value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Date getDate(int columnIdx) throws SQLException {
        final Date results = _rs.getDate(columnIdx);
        _wasNull = results == null;
        return results;
    }

    /**
	 * Retrieve the specifed column as a <TT>Double</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	Double value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Double getDouble(int columnIdx) throws SQLException {
        Object obj = _rs.getObject(columnIdx);
        Double results = DOUBLE_ZERO;
        _wasNull = true;
        if (obj != null) {
            final int columnType = _rsmd.getColumnType(columnIdx);
            if (columnType != Types.NULL) {
                _wasNull = false;
                switch(columnType) {
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.REAL:
                        if (obj instanceof Number) {
                            results = ((Number) obj).doubleValue();
                        } else {
                            results = new Double(obj.toString());
                        }
                        break;
                    default:
                        results = new Double(obj.toString());
                        break;
                }
            }
        }
        return results;
    }

    /**
	 * Retrieve the specifed column as a <TT>Long</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	long value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Long getLong(int columnIdx) throws SQLException {
        Object obj = _rs.getObject(columnIdx);
        Long results = LONG_ZERO;
        _wasNull = true;
        if (obj != null) {
            final int columnType = _rsmd.getColumnType(columnIdx);
            if (columnType != Types.NULL) {
                _wasNull = false;
                switch(columnType) {
                    case Types.SMALLINT:
                    case Types.TINYINT:
                    case Types.INTEGER:
                    case Types.BIGINT:
                        if (obj instanceof Number) {
                            results = ((Number) obj).longValue();
                        } else {
                            results = new Long(obj.toString());
                        }
                        break;
                    case Types.BIT:
                        if ("true".equalsIgnoreCase(obj.toString())) {
                            results = Long.valueOf(1);
                        } else {
                            results = Long.valueOf(0);
                        }
                        break;
                    default:
                        results = new Long(obj.toString());
                        break;
                }
            }
        }
        return results;
    }

    /**
	 * Retrieve the specifed column as an object.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	Object value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Object getObject(int columnIdx) throws SQLException {
        final Object results = _rs.getObject(columnIdx);
        _wasNull = results == null;
        return results;
    }

    /**
	 * Retrieve the specifed column as a string.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	String value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public String getString(int columnIdx) throws SQLException {
        final String results = _rs.getString(columnIdx);
        _wasNull = results == null;
        return results;
    }

    /**
	 * Retrieve the specifed column as a <TT>Time</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	Time value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Time getTime(int columnIdx) throws SQLException {
        final Time results = _rs.getTime(columnIdx);
        _wasNull = results == null;
        return results;
    }

    /**
	 * Retrieve the specifed column as a <TT>TimeStamp</TT>.
	 *
	 * @param	columnIdx	Column index (starts at 1) of the column to be read.
	 *
	 * @return	TimeStamp value of the specified column.
	 *
	 * @throws	SQLException	SQL error occured.
	 */
    public Timestamp getTimeStamp(int columnIdx) throws SQLException {
        final Timestamp results = _rs.getTimestamp(columnIdx);
        _wasNull = results == null;
        return results;
    }

    /**
	 * Reports whether the last column read had a value of SQL NULL.
	 *
	 * @return	<TT>true</TT> if last column read was <TT>null</TT>.
	 */
    public boolean wasNull() {
        return _wasNull;
    }
}
