package net.sf.dewdrop.sql.dialect;

import net.sf.dewdrop.sqlml.SqlDataType;

/**
 * @author Les Hazlewood
 */
public class GenericDialect extends Dialect {

    public GenericDialect() {
        registerColumnType(SqlDataType.BOOLEAN, SqlDataType._BOOLEAN);
        registerColumnType(SqlDataType.TINYINT, SqlDataType._TINYINT);
        registerColumnType(SqlDataType.SMALLINT, SqlDataType._SMALLINT);
        registerColumnType(SqlDataType.INTEGER, SqlDataType._INTEGER);
        registerColumnType(SqlDataType.BIGINT, SqlDataType._BIGINT);
        registerColumnType(SqlDataType.FLOAT, SqlDataType._FLOAT);
        registerColumnType(SqlDataType.DOUBLE, SqlDataType._DOUBLE);
        registerColumnType(SqlDataType.NUMERIC, DEFAULT_NUMERIC_TOKEN);
        registerColumnType(SqlDataType.CHAR, DEFAULT_CHAR_TOKEN);
        registerColumnType(SqlDataType.VARCHAR, DEFAULT_VARCHAR_TOKEN);
        registerColumnType(SqlDataType.CLOB, SqlDataType._CLOB);
        registerColumnType(SqlDataType.BIT, SqlDataType._BIT);
        registerColumnType(SqlDataType.VARBINARY, DEFAULT_VARBINARY_TOKEN);
        registerColumnType(SqlDataType.BLOB, SqlDataType._BLOB);
        registerColumnType(SqlDataType.DATE, SqlDataType._DATE);
        registerColumnType(SqlDataType.TIME, SqlDataType._TIME);
        registerColumnType(SqlDataType.TIMESTAMP, SqlDataType._TIMESTAMP);
    }
}
