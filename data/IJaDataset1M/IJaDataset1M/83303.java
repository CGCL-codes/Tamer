package org.streets.eis.ext.analysis.internal.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.streets.database.Limits;
import org.streets.eis.ext.analysis.QuerySet;

public class QuerySetImpl implements QuerySet {

    private static final long serialVersionUID = 1L;

    private Integer _rowCount;

    private Integer _colCount;

    private List<String> _columnTitles = new ArrayList<String>();

    private List<String> _columnNames = new ArrayList<String>();

    private List<String> _columnTypes = new ArrayList<String>();

    private List<Map<Integer, Object>> result = new ArrayList<Map<Integer, Object>>();

    private Limits _limit;

    public QuerySetImpl() {
    }

    public QuerySetImpl(ResultSet rs, List<String> titles, Limits limit) {
        this._limit = limit;
        result.clear();
        ResultSetMetaData rsmd;
        try {
            rsmd = rs.getMetaData();
            _colCount = rsmd.getColumnCount();
            if ((titles == null) || (titles.size() == 0)) {
                _columnTitles.addAll(findFieldTitles(rsmd));
            } else {
                _columnTitles.addAll(titles);
            }
            _columnNames.addAll(findFieldNames(rsmd));
            _columnTypes.addAll(findFieldTypes(rsmd));
            while (rs.next()) {
                Map<Integer, Object> map = new HashMap<Integer, Object>(_colCount);
                for (int i = 0; i < _colCount; i++) {
                    map.put(i, rs.getObject(i + 1));
                }
                result.add(map);
            }
            _rowCount = result.size();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> findFieldTitles(ResultSetMetaData rsmd) {
        List<String> titles = new ArrayList<String>();
        try {
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String title = rsmd.getColumnLabel(i);
                titles.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    private List<String> findFieldNames(ResultSetMetaData rsmd) {
        List<String> names = new ArrayList<String>();
        try {
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String name = rsmd.getColumnName(i);
                names.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    private List<String> findFieldTypes(ResultSetMetaData rsmd) {
        List<String> types = new ArrayList<String>();
        try {
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                String type = rsmd.getColumnTypeName(i);
                types.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    private Integer getColumnIndex(String fieldName) {
        return _columnNames.indexOf(fieldName);
    }

    @SuppressWarnings("unused")
    private String getColumnTitle(Integer index) {
        if ((index < 0) || (index >= _columnTitles.size())) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _columnTitles.size());
        }
        return _columnTitles.get(index);
    }

    @SuppressWarnings("unused")
    private String getColumnType(Integer index) {
        if ((index < 0) || (index >= _columnTypes.size())) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + _columnTypes.size());
        }
        return _columnTypes.get(index);
    }

    public Object getValues(Integer row, Integer col) {
        Map<Integer, Object> map = result.get(row);
        return map.get(col);
    }

    public Object getValues(Integer row, String fieldName) {
        Map<Integer, Object> map = result.get(row);
        Integer col = getColumnIndex(fieldName);
        if (col < 0) {
            throw new RuntimeException("field name (" + fieldName + ") is not exists!");
        }
        return map.get(col);
    }

    public Integer colSize() {
        return _colCount;
    }

    public Integer rowSize() {
        return _rowCount;
    }

    public List<String> getColumnTitles() {
        return _columnTitles;
    }

    public List<String> getColumnTypes() {
        return _columnTypes;
    }

    public List<String> getColumnNames() {
        return _columnNames;
    }

    public Limits getLimit() {
        return _limit;
    }
}
