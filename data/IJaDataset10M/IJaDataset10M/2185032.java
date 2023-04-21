package org.mgkFramework.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.mgkFramework.exceptions.MgkExternalExceptionWrapper;

/**
 * Wraps resultset and resultset related classes. Mainly to convert any
 * Exceptions to Runtime exceptions
 * 
 * @author vitopn
 * 
 */
public class ResultSetUtil {

    private ResultSet rs;

    private ResultSetMetaData metaData;

    public ResultSetUtil(ResultSet rs) {
        this.rs = rs;
    }

    public ResultSetMetaData getMetaData() {
        if (this.metaData == null) {
            try {
                this.metaData = this.rs.getMetaData();
            } catch (SQLException e) {
                throw new MgkExternalExceptionWrapper(e);
            }
        }
        return this.metaData;
    }

    public int getColumnCount() {
        try {
            return getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new MgkExternalExceptionWrapper(e);
        }
    }

    public String getColumnName(int colIndex) {
        try {
            return getMetaData().getColumnName(colIndex);
        } catch (SQLException e) {
            throw new MgkExternalExceptionWrapper(e);
        }
    }

    public int getColumnType(int colIndex) {
        try {
            return getMetaData().getColumnType(colIndex);
        } catch (SQLException e) {
            throw new MgkExternalExceptionWrapper(e);
        }
    }
}
