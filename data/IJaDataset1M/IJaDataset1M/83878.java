package com.jdbwc.core.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.jdbwc.core.WCConnection;
import com.jdbwc.core.WCResultSet;
import com.jdbwc.core.WCStatement;
import com.jdbwc.util.SQLField;
import com.jdbwc.util.SQLUtils;
import com.ozdevworx.dtype.ObjectArray;

/**
 * Gets meta data from MySQL databases.
 *
 * @author Tim Gall
 * @version 2008-05-29
 * @version 2010-04-27
 */
public class SQLMetaGetaImp implements com.jdbwc.util.SQLMetaGeta {

    private transient WCConnection myConnection = null;

    /**
	 * Constructs a new instance of this.
	 */
    public SQLMetaGetaImp(WCConnection connection) {
        myConnection = connection;
    }

    /**
	 * @see com.jdbwc.util.SQLMetaGeta#getResultSetMetaData(java.lang.String, com.ozdevworx.dtype.ObjectArray)
	 */
    @Override
    public SQLField[] getResultSetMetaData(String sql, ObjectArray columns) throws SQLException {
        if (myConnection.versionMeetsMinimum(5, 0, 0)) {
            return getRsMetaData(sql, columns);
        } else {
            throw new SQLException("MySQL versions less than 5.0.0 are not supported in this release.");
        }
    }

    /**
	 * @see com.jdbwc.util.SQLMetaGeta#getParameterMetaData(com.ozdevworx.dtype.ObjectArray, com.ozdevworx.dtype.ObjectArray, com.ozdevworx.dtype.ObjectArray)
	 */
    @Override
    public SQLField[] getParameterMetaData(ObjectArray tableNames, ObjectArray columns, ObjectArray params) throws SQLException {
        SQLField[] paramMdFields = new SQLField[0];
        if (myConnection.versionMeetsMinimum(5, 0, 0)) {
            for (int tIdx = 0; tIdx < tableNames.length(); tIdx++) {
                paramMdFields = getParamMetaData(paramMdFields, tableNames.getKey(tIdx), columns, params);
            }
        } else {
            throw new SQLException("MySQL versions less than 5.0.0 are not supported in this release.");
        }
        return paramMdFields;
    }

    /**
	 * Gets ResultSetMetaData from the INFORMATION_SCHEMA database.<br />
	 * This method gets bypassed for MySQL.
	 * It was originally the second official version for harvesting ResultSetMetaData.
	 * Now MySQL metadata is harvested in the same pass as the queries ResultSet.
	 *
	 * @param sql
	 * @param columns
	 * @return an array of SQLField Objects
	 * @throws SQLException
	 */
    private SQLField[] getRsMetaData(String sql, ObjectArray columns) throws SQLException {
        if (sql.isEmpty()) throw new SQLException("Could not determine the resultsets query to get metadata for.");
        final String[] keys = { "PRI", "UNI", "MUL" };
        List<SQLField> fieldList = new ArrayList<SQLField>();
        String catalogName;
        String schemaName = "";
        String tableName;
        String columnName;
        String columnAlias;
        String columnDefault;
        boolean isNullable;
        int sqlType;
        int maxLength;
        int maxPrecision;
        int maxScale;
        String collationName;
        boolean isAutoIndex;
        boolean isUnsigned;
        boolean isPrimaryKey;
        boolean isUniqueKey;
        boolean isIndex;
        String viewName = "wc_rsmeta_" + com.jdbwc.util.Security.getHash("md5", sql);
        if (!sql.endsWith(";")) sql += ";";
        WCStatement statement = myConnection.createInternalStatement();
        statement.addBatch("DROP VIEW IF EXISTS " + viewName + ";");
        statement.addBatch("CREATE VIEW " + viewName + " AS " + sql);
        StringBuilder sqlBuilder = new StringBuilder(200);
        sqlBuilder.append("SELECT ").append("TABLE_SCHEMA, ").append("TABLE_NAME, ").append("COLUMN_NAME, ").append("COLUMN_DEFAULT, ").append("IF(IS_NULLABLE='YES', 'TRUE', 'FALSE') AS NULLABLE, ").append("DATA_TYPE, ").append("COLUMN_TYPE, ").append("CHARACTER_MAXIMUM_LENGTH, ").append("NUMERIC_PRECISION, ").append("NUMERIC_SCALE, ").append("COLLATION_NAME, ").append("COLUMN_KEY, ").append("IF(EXTRA LIKE '%auto_increment%', 'TRUE', 'FALSE') AS IS_AUTOINC ").append("FROM INFORMATION_SCHEMA.COLUMNS ").append("WHERE TABLE_SCHEMA LIKE '" + myConnection.getCatalog() + "' AND TABLE_NAME = '" + viewName + "' ").append("ORDER BY TABLE_SCHEMA, TABLE_NAME;");
        statement.addBatch(sqlBuilder.toString());
        statement.addBatch("DROP VIEW " + viewName + ";");
        statement.executeBatch();
        statement.getMoreResults();
        statement.getMoreResults();
        if (statement.getMoreResults()) {
            WCResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                catalogName = resultSet.getString("TABLE_SCHEMA");
                tableName = resultSet.getString("TABLE_NAME");
                columnName = resultSet.getString("COLUMN_NAME");
                columnAlias = columnName;
                if (columns.hasKey(columnAlias)) {
                    columnName = columns.getString(columnName);
                }
                columnDefault = resultSet.getString("COLUMN_DEFAULT");
                isNullable = resultSet.getBoolean("NULLABLE");
                sqlType = myConnection.nativeNameToNativeType(resultSet.getString("DATA_TYPE"));
                try {
                    maxLength = resultSet.getInt("CHARACTER_MAXIMUM_LENGTH");
                } catch (Exception e) {
                    maxLength = 0;
                }
                try {
                    maxPrecision = resultSet.getInt("NUMERIC_PRECISION");
                } catch (Exception e) {
                    maxPrecision = 0;
                }
                try {
                    maxScale = resultSet.getInt("NUMERIC_SCALE");
                } catch (Exception e) {
                    maxScale = 0;
                }
                collationName = resultSet.getString("COLLATION_NAME");
                isAutoIndex = resultSet.getBoolean("IS_AUTOINC");
                isUnsigned = isUnsigned(resultSet.getString("COLUMN_TYPE"));
                String colKey = resultSet.getString("COLUMN_KEY");
                isPrimaryKey = colKey.equalsIgnoreCase(keys[0]);
                isUniqueKey = colKey.equalsIgnoreCase(keys[1]);
                isIndex = colKey.equalsIgnoreCase(keys[2]);
                SQLField field = new SQLField(catalogName, schemaName, tableName, columnName, columnAlias, columnDefault, collationName, sqlType, maxLength, maxPrecision, maxScale, isNullable, isAutoIndex, isUnsigned, isPrimaryKey, isUniqueKey, isIndex);
                fieldList.add(field);
            }
            resultSet.close();
        }
        statement.close();
        return fieldList.toArray(new SQLField[fieldList.size()]);
    }

    /**
	 *
	 * @param paramMdFields
	 * @param tableName
	 * @param columns
	 * @param params
	 * @return an array of SQLField Objects containing parameter metadata
	 * @throws SQLException
	 */
    private SQLField[] getParamMetaData(SQLField[] paramMdFields, String tableName, ObjectArray columns, ObjectArray params) throws SQLException {
        final String nullable = "YES";
        final String MY_MODE_UNKNOWN = "UNKNOWN";
        String fieldName = "";
        String typeName = "";
        boolean isNullable = false;
        String mode = MY_MODE_UNKNOWN;
        WCStatement statement = myConnection.createInternalStatement();
        String sql = "DESCRIBE `" + tableName + "`;";
        WCResultSet resultSet = statement.executeQuery(sql);
        while (!columns.isEmpty()) {
            while (resultSet.next() && !columns.isEmpty()) {
                fieldName = resultSet.getString("Field");
                if ((columns.hasKey(fieldName) && (columns.hasElement(fieldName, tableName)) || columns.hasElement("?", tableName))) {
                    if (columns.hasKey(fieldName)) {
                        columns.removeByKey(fieldName);
                    } else if (columns.hasKey("?")) {
                        columns.removeByKey("?");
                    }
                    typeName = resultSet.getString("Type");
                    isNullable = resultSet.getString("Null").equalsIgnoreCase(nullable);
                    mode = params.getString(fieldName);
                    SQLField field = new SQLField(fieldName, typeName, isNullable, mode);
                    paramMdFields = SQLUtils.rebuildFieldSet(field, paramMdFields);
                }
            }
            while (!columns.isEmpty() && resultSet.previous()) {
                fieldName = resultSet.getString("Field");
                if ((columns.hasKey(fieldName) && (columns.hasElement(fieldName, tableName)) || columns.hasElement("?", tableName))) {
                    if (columns.hasKey(fieldName)) {
                        columns.removeByKey(fieldName);
                    } else if (columns.hasKey("?")) {
                        columns.removeByKey("?");
                    }
                    typeName = resultSet.getString("Type");
                    isNullable = resultSet.getString("Null").equalsIgnoreCase(nullable);
                    mode = params.getString(fieldName);
                    SQLField field = new SQLField(fieldName, typeName, isNullable, mode);
                    paramMdFields = SQLUtils.rebuildFieldSet(field, paramMdFields);
                }
            }
        }
        statement.close();
        return paramMdFields;
    }

    private boolean isUnsigned(final String typeName) {
        return typeName.toLowerCase().endsWith("unsigned");
    }
}
