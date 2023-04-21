package com.jdbwc.core;

import com.jdbwc.util.SQLField;

/**
 * A static metadata producer.
 * Primarily for metadata about metadata.<br />
 * This class is database independent. Its results can be used with any of the JDBWC database implementations.
 *
 * @author Tim Gall
 * @version 2010-04-30
 */
public final class WCStaticMetaData {

    public static WCResultSetMetaData getClientInfoProperties() {
        SQLField[] fieldSet = new SQLField[4];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "MAX_LEN", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "DEFAULT_VALUE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DESCRIPTION", java.sql.Types.CHAR, 255, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getAttributes() {
        SQLField[] fieldSet = new SQLField[21];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TYPE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "ATTR_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DATA_TYPE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "ATTR_TYPE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "ATTR_SIZE", java.sql.Types.INTEGER, 11, 0);
        fieldSet[colIndex++] = new SQLField("", "DECIMAL_DIGITS", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "NUM_PREC_RADIX", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "NULLABLE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "REMARKS", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "ATTR_DEF", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATA_TYPE", java.sql.Types.INTEGER, 0, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATETIME_SUB", java.sql.Types.INTEGER, 0, 0);
        fieldSet[colIndex++] = new SQLField("", "CHAR_OCTET_LENGTH", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "ORDINAL_POSITION", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_NULLABLE", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "SCOPE_CATALOG", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "SCOPE_TABLE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_GRANTABLE", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "SOURCE_DATA_TYPE", java.sql.Types.INTEGER, 10, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getCatalogs() {
        SQLField[] fieldSet = new SQLField[1];
        fieldSet[0] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getColumnPrivileges() {
        SQLField[] fieldSet = new SQLField[8];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "GRANTOR", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "GRANTEE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PRIVILEGE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_GRANTABLE", java.sql.Types.CHAR, 3, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getColumns() {
        SQLField[] fieldSet = new SQLField[23];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.VARCHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DATA_TYPE", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_SIZE", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "BUFFER_LENGTH", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "DECIMAL_DIGITS", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "NUM_PREC_RADIX", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "NULLABLE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "REMARKS", java.sql.Types.VARCHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_DEF", java.sql.Types.LONGVARCHAR, Integer.MAX_VALUE, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATA_TYPE", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATETIME_SUB", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "CHAR_OCTET_LENGTH", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "ORDINAL_POSITION", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_NULLABLE", java.sql.Types.VARCHAR, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "SCOPE_CATALOG", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SCOPE_SCHEMA", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SCOPE_TABLE", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SOURCE_DATA_TYPE", java.sql.Types.INTEGER, 2, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_AUTOINCREMENT", java.sql.Types.VARCHAR, 5, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getImportedExportedKeys() {
        SQLField[] fieldSet = new SQLField[14];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "PKTABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "PKTABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PKTABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PKCOLUMN_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "FKTABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "FKTABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "FKTABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "FKCOLUMN_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "KEY_SEQ", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "UPDATE_RULE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "DELETE_RULE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "FK_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PK_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DEFERRABILITY", java.sql.Types.INTEGER, 5, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getIndexInfo() {
        SQLField[] fieldSet = new SQLField[13];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "NON_UNIQUE", java.sql.Types.BOOLEAN, 1, 0);
        fieldSet[colIndex++] = new SQLField("", "INDEX_QUALIFIER", java.sql.Types.CHAR, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "INDEX_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "ORDINAL_POSITION", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "ASC_OR_DESC", java.sql.Types.CHAR, 1, 0);
        fieldSet[colIndex++] = new SQLField("", "CARDINALITY", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "PAGES", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "FILTER_CONDITION", java.sql.Types.CHAR, 64, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getPrimaryKeys() {
        SQLField[] fieldSet = new SQLField[6];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "COLUMN_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "KEY_SEQ", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "PK_NAME", java.sql.Types.CHAR, 64, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getProcedures() {
        SQLField[] fieldSet = new SQLField[9];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "PROCEDURE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "PROCEDURE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PROCEDURE_NAME", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "reserved1", java.sql.Types.CHAR, 0, 0);
        fieldSet[colIndex++] = new SQLField("", "reserved2", java.sql.Types.CHAR, 0, 0);
        fieldSet[colIndex++] = new SQLField("", "reserved3", java.sql.Types.CHAR, 0, 0);
        fieldSet[colIndex++] = new SQLField("", "REMARKS", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "PROCEDURE_TYPE", java.sql.Types.SMALLINT, 6, 0);
        fieldSet[colIndex++] = new SQLField("", "SPECIFIC_NAME", java.sql.Types.CHAR, 255, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getSchemas() {
        SQLField[] fieldSet = new SQLField[2];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_CATALOG", java.sql.Types.VARCHAR, 255, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getSuperTables() {
        SQLField[] fieldSet = new SQLField[4];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SUPERTABLE_NAME", java.sql.Types.CHAR, 64, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getSuperTypes() {
        SQLField[] fieldSet = new SQLField[6];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SUPERTYPE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "SUPERTYPE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SUPERTYPE_NAME", java.sql.Types.CHAR, 64, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getTablePrivileges() {
        SQLField[] fieldSet = new SQLField[7];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "GRANTOR", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "GRANTEE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "PRIVILEGE", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "IS_GRANTABLE", java.sql.Types.CHAR, 3, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getTables() {
        SQLField[] fieldSet = new SQLField[10];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TABLE_CAT", java.sql.Types.VARCHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_SCHEM", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TABLE_TYPE", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "REMARKS", java.sql.Types.VARCHAR, 80, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_CAT", java.sql.Types.VARCHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_SCHEM", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "SELF_REFERENCING_COL_NAME", java.sql.Types.VARCHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "REF_GENERATION", java.sql.Types.VARCHAR, 64, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getTableTypes() {
        SQLField[] fieldSet = new SQLField[1];
        fieldSet[0] = new SQLField("", "TABLE_TYPE", java.sql.Types.VARCHAR, 32, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    /**
	 *
	 * @return A set of static metadata for DataBaseMetaData.getTypeInfo()
	 */
    public static WCResultSetMetaData getTypeInfo() {
        SQLField[] fieldSet = new SQLField[18];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DATA_TYPE", java.sql.Types.SMALLINT, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "PRECISION", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "LITERAL_PREFIX", java.sql.Types.CHAR, 4, 0);
        fieldSet[colIndex++] = new SQLField("", "LITERAL_SUFFIX", java.sql.Types.CHAR, 4, 0);
        fieldSet[colIndex++] = new SQLField("", "CREATE_PARAMS", java.sql.Types.CHAR, 32, 0);
        fieldSet[colIndex++] = new SQLField("", "NULLABLE", java.sql.Types.SMALLINT, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "CASE_SENSITIVE", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "SEARCHABLE", java.sql.Types.SMALLINT, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "UNSIGNED_ATTRIBUTE", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "FIXED_PREC_SCALE", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "AUTO_INCREMENT", java.sql.Types.CHAR, 3, 0);
        fieldSet[colIndex++] = new SQLField("", "LOCAL_TYPE_NAME", java.sql.Types.CHAR, 32, 0);
        fieldSet[colIndex++] = new SQLField("", "MINIMUM_SCALE", java.sql.Types.SMALLINT, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "MAXIMUM_SCALE", java.sql.Types.SMALLINT, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATA_TYPE", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "SQL_DATETIME_SUB", java.sql.Types.INTEGER, 10, 0);
        fieldSet[colIndex++] = new SQLField("", "NUM_PREC_RADIX", java.sql.Types.INTEGER, 10, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    public static WCResultSetMetaData getUDTs() {
        SQLField[] fieldSet = new SQLField[7];
        int colIndex = 0;
        fieldSet[colIndex++] = new SQLField("", "TYPE_CAT", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_SCHEM", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "TYPE_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "CLASS_NAME", java.sql.Types.CHAR, 64, 0);
        fieldSet[colIndex++] = new SQLField("", "DATA_TYPE", java.sql.Types.INTEGER, 5, 0);
        fieldSet[colIndex++] = new SQLField("", "REMARKS", java.sql.Types.CHAR, 255, 0);
        fieldSet[colIndex++] = new SQLField("", "BASE_TYPE", java.sql.Types.INTEGER, 5, 0);
        return new WCResultSetMetaData(fieldSet);
    }

    /**
	 * stop non static class calls
	 */
    private WCStaticMetaData() {
    }
}
