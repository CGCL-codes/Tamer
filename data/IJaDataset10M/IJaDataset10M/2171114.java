package org.dinopolis.gpstool.gui.layer.location;

import java.util.Vector;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.IllegalArgumentException;

public class JDBCPreparedStatementPart {

    Vector sql_values_;

    Vector sql_types_;

    StringBuffer query_;

    /**
 * Empty Constructor.
 *
 */
    public JDBCPreparedStatementPart() {
        query_ = new StringBuffer();
        sql_values_ = new Vector();
        sql_types_ = new Vector();
    }

    /**
 * Constructor using the query string and the values.
 *
 * @param query the query string (using '?' as placeholders).
 * @param values the values to replace the '?' like the
 * PreparedStatement does.
 */
    public JDBCPreparedStatementPart(String query, Vector values) {
        this();
        setQuery(query);
        setValues(values);
    }

    /**
 * Constructor using the query string and the values and the types.
 *
 * @param query the query string (using '?' as placeholders).
 * @param values the values to replace the '?' like the
 * PreparedStatement does.
 * @param types the types as defined in java.sql.Types (Integer objects).
 * @exception IllegalArgumentException if the length of values and
 * types is not equal.
 */
    public JDBCPreparedStatementPart(String query, Vector values, Vector types) throws IllegalArgumentException {
        this();
        setQuery(query);
        setValues(values, types);
    }

    /**
 * Get the query.
 *
 * @return the query.
 */
    public String getQuery() {
        return ("JDBCPrepStmt[" + query_.toString() + ", values=" + sql_values_ + "]");
    }

    /**
 * Prepends the query to the already existing query.
 *
 * @param query the query to add at the beginning.
 */
    public void prependQuery(String query) {
        query_ = query_.insert(0, query);
    }

    /**
 * Appends the query to the already existing query.
 *
 * @param query the query.
 */
    public void appendQuery(String query) {
        query_ = query_.append(query);
    }

    /**
 * Set the query.
 *
 * @param query the query.
 */
    public void setQuery(String query) {
        query_ = new StringBuffer(query);
    }

    /**
 * Get the values.
 *
 * @return the values.
 */
    public Vector getValues() {
        return (sql_values_);
    }

    /**
 * Set the values using the default type.
 *
 * @param values the values.
 */
    public void setValues(Vector values) {
        sql_values_ = new Vector();
        for (int count = 0; count < values.size(); count++) {
            addValue(values.elementAt(count));
        }
    }

    /**
 * Set the values using the given types. The two vectors need to have
 * the same length!
 *
 * @param values the values.
 * @param types the types as defined in java.sql.Types (Integer objects).
 * @exception IllegalArgumentException if the length of values and
 * types is not equal.
 */
    public void setValues(Vector values, Vector types) throws IllegalArgumentException {
        if (values.size() != types.size()) throw new IllegalArgumentException("Values and types must have equal size!");
        sql_values_ = new Vector();
        for (int count = 0; count < values.size(); count++) {
            addValue(values.elementAt(count), ((Integer) types.elementAt(count)).intValue());
        }
    }

    /**
 * Add a value to the already given values. The type is set to OTHER
 * (as in java.sql.Types) by default.
 *
 * @param value the new value.
 */
    public void addValue(Object value) {
        addValue(value, Types.OTHER);
    }

    /**
 * Add a value to the already given values at the given position. The
 * type is set to OTHER (as in java.sql.Types) by default.
 *
 * @param index index at which the specified element is to be inserted.
 * @param value the new value.
 */
    public void addValue(int index, Object value) {
        addValue(index, value, Types.OTHER);
    }

    /**
 * Add a value to the already given values;
 *
 * @param value the new value.
 * @param target_SQL_type the target sql type as defined in java.sql.Types
 */
    public void addValue(Object value, int target_SQL_type) {
        addValue(sql_values_.size(), value, target_SQL_type);
    }

    /**
 * Add a value to the already given values at the given index.
 *
 * @param index index at which the specified element is to be inserted.
 * @param value the new value.
 * @param target_SQL_type the target sql type as defined in java.sql.Types
 */
    public void addValue(int index, Object value, int target_SQL_type) {
        sql_values_.add(index, value);
        sql_types_.add(index, new Integer(target_SQL_type));
    }

    /**
 * Get the sql types
 *
 * @return the sql types.
 */
    public Vector getSQLTypes() {
        return (sql_types_);
    }

    /**
 * Returns a Prepared Statement from the given query, values and types.
 *
 * @param connection the jdbc connection to create the PreparedStatement from.
 * @exception SQLException if a database error occurs.
 */
    public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query_.toString());
        for (int count = 0; count < sql_values_.size(); count++) {
            stmt.setObject(count + 1, sql_values_.elementAt(count), ((Integer) sql_types_.elementAt(count)).intValue());
        }
        return (stmt);
    }
}
