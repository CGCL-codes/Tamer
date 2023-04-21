package org.spice.persistence.manager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.spice.persistence.components.Mapper;
import org.spice.persistence.exception.NoDataFoundException;

public class RowMapper<T> {

    private Mapper<T> mapper;

    public RowMapper(final Mapper<T> mapper) {
        this.mapper = mapper;
    }

    /**
	 * 
	 * @param resultSet
	 * @return
	 * @throws NoDataFoundException
	 */
    public List<T> getObject(final ResultSet resultSet) throws NoDataFoundException {
        final List<T> result = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                result.add(this.mapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new NoDataFoundException("No data's to Iterate");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> getResult(ResultSet resultSet) throws NoDataFoundException {
        final List<Map<String, Object>> t = new ArrayList<Map<String, Object>>();
        try {
            while (resultSet.next()) {
                final Map<String, Object> result = new HashMap<String, Object>();
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final Integer coloumnCount = metaData.getColumnCount();
                for (int i = 1; i < coloumnCount; i++) {
                    String columnName = getColoumnName(metaData, i);
                    result.put(columnName, getColumnValue(resultSet, columnName));
                }
                t.add(result);
            }
        } catch (SQLException e) {
            throw new NoDataFoundException(e.getMessage());
        }
        return (List<T>) t;
    }

    /**
	 * 
	 * @param resultSet
	 * @param columnName
	 * @return
	 * @throws NoDataFoundException
	 */
    private Object getColumnValue(final ResultSet resultSet, final String columnName) throws NoDataFoundException {
        try {
            Object value = resultSet.getObject(columnName);
            return value;
        } catch (SQLException e) {
            throw new NoDataFoundException("Column could not be fetched");
        }
    }

    /**
	 * 
	 * @param metaData
	 * @param i
	 * @return
	 * @throws NoDataFoundException
	 */
    private String getColoumnName(final ResultSetMetaData metaData, final int i) throws NoDataFoundException {
        try {
            final String columnName = metaData.getColumnName(i);
            return columnName;
        } catch (SQLException e) {
            throw new NoDataFoundException("column could not be identified");
        }
    }
}
