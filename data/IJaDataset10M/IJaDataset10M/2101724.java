package com.liferay.portal.kernel.dao.jdbc;

import com.liferay.portal.kernel.util.CharPool;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="SmartResultSet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Minhchau Dang
 * @author Brian Wing Shun Chan
 *
 */
public class SmartResultSet {

    public SmartResultSet(ResultSet rs) throws SQLException {
        _rs = rs;
        _metaData = _rs.getMetaData();
        _columnCount = _metaData.getColumnCount();
        _columnIndexCache = new HashMap<String, Integer>();
    }

    public int findColumn(String columnName) throws SQLException {
        Integer columnIndex = _columnIndexCache.get(columnName);
        if (columnIndex != null) {
            return columnIndex;
        }
        for (int i = 1; i <= _columnCount; ++i) {
            String availableName = _metaData.getColumnName(i);
            if (availableName.equalsIgnoreCase(columnName)) {
                _columnIndexCache.put(columnName, i);
                return i;
            }
        }
        int pos = columnName.indexOf(CharPool.PERIOD);
        if (pos != -1) {
            String shortName = columnName.substring(pos + 1);
            for (int i = 1; i <= _columnCount; ++i) {
                String availableName = _metaData.getColumnName(i);
                if (availableName.equalsIgnoreCase(shortName)) {
                    _columnIndexCache.put(columnName, i);
                    return i;
                }
            }
        }
        columnIndex = _rs.findColumn(columnName);
        _columnIndexCache.put(columnName, columnIndex);
        return columnIndex;
    }

    public boolean first() throws SQLException {
        return _rs.first();
    }

    public Date getDate(int columnIndex) throws SQLException {
        return _rs.getDate(columnIndex);
    }

    public Date getDate(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getDate(columnIndex);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return _rs.getDouble(columnIndex);
    }

    public double getDouble(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getDouble(columnIndex);
    }

    public float getFloat(int columnIndex) throws SQLException {
        return _rs.getFloat(columnIndex);
    }

    public float getFloat(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getFloat(columnIndex);
    }

    public int getInt(int columnIndex) throws SQLException {
        return _rs.getInt(columnIndex);
    }

    public int getInt(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getInt(columnIndex);
    }

    public long getLong(int columnIndex) throws SQLException {
        return _rs.getLong(columnIndex);
    }

    public long getLong(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getLong(columnIndex);
    }

    public short getShort(int columnIndex) throws SQLException {
        return _rs.getShort(columnIndex);
    }

    public short getShort(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getShort(columnIndex);
    }

    public String getString(int columnIndex) throws SQLException {
        return _rs.getString(columnIndex);
    }

    public String getString(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getString(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return _rs.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        int columnIndex = findColumn(columnName);
        return _rs.getTimestamp(columnIndex);
    }

    public boolean last() throws SQLException {
        return _rs.last();
    }

    public boolean next() throws SQLException {
        return _rs.next();
    }

    public boolean previous() throws SQLException {
        return _rs.previous();
    }

    private final ResultSet _rs;

    private final ResultSetMetaData _metaData;

    private final int _columnCount;

    private final Map<String, Integer> _columnIndexCache;
}
