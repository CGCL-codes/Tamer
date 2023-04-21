package nuts.core.orm.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Date (and time) implementation of TypeHandler
 */
public class DateTypeHandler implements TypeHandler {

    /**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        java.sql.Timestamp sqlTimestamp = rs.getTimestamp(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return new java.util.Date(sqlTimestamp.getTime());
        }
    }

    /**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        java.sql.Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return new java.util.Date(sqlTimestamp.getTime());
        }
    }

    /**
	 * Gets a column from a callable statement
	 * 
	 * @param cs - the statement
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        java.sql.Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return new java.util.Date(sqlTimestamp.getTime());
        }
    }

    /**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @param value - the value to update
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if getting the value fails
	 */
    public void updateResult(ResultSet rs, String columnName, Object value, String jdbcType) throws SQLException {
        if (value == null) {
            rs.updateNull(columnName);
        } else {
            rs.updateTimestamp(columnName, new java.sql.Timestamp(((Date) value).getTime()));
        }
    }

    /**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param columnIndex - the column to get (by index)
	 * @param value - the value to update
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if getting the value fails
	 */
    public void updateResult(ResultSet rs, int columnIndex, Object value, String jdbcType) throws SQLException {
        if (value == null) {
            rs.updateNull(columnIndex);
        } else {
            rs.updateTimestamp(columnIndex, new java.sql.Timestamp(((Date) value).getTime()));
        }
    }

    /**
	 * Sets a parameter on a prepared statement
	 * 
	 * @param ps - the prepared statement
	 * @param i - the parameter index
	 * @param parameter - the parameter value
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if setting the parameter fails
	 */
    public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType) throws SQLException {
        ps.setTimestamp(i, new java.sql.Timestamp(((Date) parameter).getTime()));
    }
}
