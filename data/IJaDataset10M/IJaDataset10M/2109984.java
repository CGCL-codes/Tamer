package com.j256.ormlite.h2;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseResults;

/**
 * H2 compiled statement.
 * 
 * @author graywatson
 */
public class H2CompiledStatement implements CompiledStatement {

    private PreparedStatement preparedStatement;

    public H2CompiledStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public int getColumnCount() throws SQLException {
        return preparedStatement.getMetaData().getColumnCount();
    }

    public String getColumnName(int column) throws SQLException {
        return preparedStatement.getMetaData().getColumnName(column + 1);
    }

    public int runUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public DatabaseResults runQuery(ObjectCache objectCache) throws SQLException {
        return new H2DatabaseResults(preparedStatement.executeQuery(), objectCache);
    }

    public int runExecute() throws SQLException {
        preparedStatement.execute();
        return preparedStatement.getUpdateCount();
    }

    public void close() throws SQLException {
        preparedStatement.close();
    }

    public void setObject(int parameterIndex, Object obj, SqlType sqlType) throws SQLException {
        if (obj == null) {
            preparedStatement.setNull(parameterIndex + 1, sqlTypeToJdbcInt(sqlType));
        } else {
            preparedStatement.setObject(parameterIndex + 1, obj, sqlTypeToJdbcInt(sqlType));
        }
    }

    public void setMaxRows(int max) throws SQLException {
        preparedStatement.setMaxRows(max);
    }

    public void setQueryTimeout(long millis) throws SQLException {
        preparedStatement.setQueryTimeout(Long.valueOf(millis).intValue() / 1000);
    }

    public static int sqlTypeToJdbcInt(SqlType sqlType) {
        switch(sqlType) {
            case STRING:
                return Types.VARCHAR;
            case LONG_STRING:
                return Types.LONGVARCHAR;
            case DATE:
                return Types.TIMESTAMP;
            case BOOLEAN:
                return Types.BOOLEAN;
            case CHAR:
                return Types.CHAR;
            case BYTE:
                return Types.TINYINT;
            case BYTE_ARRAY:
                return Types.VARBINARY;
            case SHORT:
                return Types.SMALLINT;
            case INTEGER:
                return Types.INTEGER;
            case LONG:
                return Types.BIGINT;
            case FLOAT:
                return Types.FLOAT;
            case DOUBLE:
                return Types.DOUBLE;
            case SERIALIZABLE:
                return Types.VARBINARY;
            case BLOB:
                return Types.BLOB;
            case BIG_DECIMAL:
                return Types.NUMERIC;
            default:
                throw new IllegalArgumentException("No JDBC mapping for unknown SqlType " + sqlType);
        }
    }
}
