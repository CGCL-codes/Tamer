package com.thinkvirtual.sql.oracle;

import com.crossdb.sql.DefaultResultSet;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleResultSet extends DefaultResultSet {

    public OracleResultSet(ResultSet rs) {
        super(rs);
    }

    /**
	 * if a column returns a single space, then this method will return an empty string.
	 */
    public String getString(String columnName) throws SQLException {
        String ret = super.getString(columnName);
        if (ret != null && ret.equals(" ")) {
            return new String();
        }
        return ret;
    }

    /**
	 * @see getString
	 */
    public String getString(int columnIndex) throws SQLException {
        String ret = super.getString(columnIndex);
        if (ret != null && ret.equals(" ")) {
            return new String();
        }
        return ret;
    }
}
