package com.jdbwc.core.mysql;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.jdbwc.core.WCDriverPropertiesInfo;
import com.jdbwc.core.WCConnection;
import com.jdbwc.core.WCResultSet;
import com.jdbwc.core.WCStatement;
import com.jdbwc.core.WCStaticMetaData;
import com.jdbwc.exceptions.NotImplemented;
import com.jdbwc.util.SQLCondition;
import com.ozdevworx.dtype.ObjectArray;

/**
 * This MetaData class is designed for MySql implementations
 * that use the INFORMATION_SCHEMA metadatabase.<br />
 * <br />
 * Moved all methods that use INFORMATION_SCHEMA into this class
 * to seperate the query logic from MySQLDatabaseMetaData and to
 * make Driver maintenance and development a little easier.<br />
 * <br />
 * The important thing to note is that MySql INFORMATION_SCHEMA tables
 * refer to the databases as schemas, when in fact they are commonly known as catalogs.
 * This is a very important factor to make this driver work with SQL clients.
 * <br />
 * Whenever a metadata query requests a schema, we give it NULL,
 * and for catalog we give it the INFORMATION_SCHEMA's schema.
 * This is a very important difference between MySql and most other transactional databases.
 *
 * @author Tim Gall (Oz-DevWorX)
 * @version 2008-05-29
 * @version 2010-04-27
 */
public class MySQLDBMDFromInfoSchema {

    /** The connection that created this WCDatabaseMetaData */
    protected transient WCConnection myConnection = null;

    private final boolean hasReferentialConstraintsView;

    /** Known Table types supported by the MySQL database server */
    protected static final String[] myTabletypes = { "TABLE", "VIEW", "LOCAL TEMPORARY" };

    protected MySQLDBMDFromInfoSchema(WCConnection connection) throws SQLException {
        myConnection = connection;
        hasReferentialConstraintsView = myConnection.versionMeetsMinimum(5, 1, 10);
    }

    /**
	 * Retrieves a description of the given attribute of the given type for
	 * a user-defined type (UDT) that is available in the given schema and catalog.<br />
	 * <br />
	 * Descriptions are returned only for attributes of UDTs matching the catalog,
	 * schema, type, and attribute name criteria.<br />
	 * They are ordered by TYPE_CAT, TYPE_SCHEM, TYPE_NAME and ORDINAL_POSITION.
	 * This description does not contain inherited attributes.<br />
	 * <br />
	 * The ResultSet object that is returned has the following columns:
	 * <ol>
	 * <li><b>TYPE_CAT</b> String => type catalog (may be null)</li>
	 * <li><b>TYPE_SCHEM</b> String => type schema (may be null)</li>
	 * <li><b>TYPE_NAME</b> String => type name</li>
	 * <li><b>ATTR_NAME</b> String => attribute name</li>
	 * <li><b>DATA_TYPE</b> int => attribute type SQL type from java.sql.Types</li>
	 * <li><b>ATTR_TYPE_NAME</b> String => Data source dependent type name. For a UDT, the type name is fully qualified. For a REF, the type name is fully qualified and represents the target type of the reference type.</li>
	 * <li><b>ATTR_SIZE</b> int => column size. For char or date types this is the maximum number of characters; for numeric or decimal types this is precision.</li>
	 * <li><b>DECIMAL_DIGITS</b> int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.</li>
	 * <li><b>NUM_PREC_RADIX</b> int => Radix (typically either 10 or 2)</li>
	 * <li><b>NULLABLE</b> int => whether NULL is allowed</li>
	 * <ul>
	 *	<li>attributeNoNulls - might not allow NULL values</li>
	 *	<li>attributeNullable - definitely allows NULL values</li>
	 *	<li>attributeNullableUnknown - nullability unknown</li>
	 * </ul>
	 * <li><b>REMARKS</b> String => comment describing column (may be null)</li>
	 * <li><b>ATTR_DEF</b> String => default value (may be null)</li>
	 * <li><b>SQL_DATA_TYPE</b> int => unused</li>
	 * <li><b>SQL_DATETIME_SUB</b> int => unused</li>
	 * <li><b>CHAR_OCTET_LENGTH</b> int => for char types the maximum number of bytes in the column</li>
	 * <li><b>ORDINAL_POSITION</b> int => index of the attribute in the UDT (starting at 1)</li>
	 * <li><b>IS_NULLABLE</b> String => ISO rules are used to determine the nullability for a attribute.</li>
	 * <ul>
	 * 	<li>YES --- if the attribute can include NULLs</li>
	 * 	<li>NO --- if the attribute cannot include NULLs</li>
	 * 	<li>empty string --- if the nullability for the attribute is unknown</li>
	 * </ul>
	 * <li><b>SCOPE_CATALOG</b> String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)</li>
	 * <li><b>SCOPE_SCHEMA</b> String => schema of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)</li>
	 * <li><b>SCOPE_TABLE</b> String => table name that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)</li>
	 * <li><b>SOURCE_DATA_TYPE</b> short => source type of a distinct type or user-generated Ref type,SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)</li>
	 * </ol>
	 *
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schemaPattern a schema name pattern; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param typeNamePattern a type name pattern; must match the type name as it is stored in the database
	 * @param attributeNamePattern an attribute name pattern; must match the attribute name as it is declared in the database
	 * @return a ResultSet object in which each row is an attribute description
	 * @throws SQLException if a database access error occurs
	 * @since 1.4
	 */
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        return new WCResultSet(myConnection, WCStaticMetaData.getAttributes());
    }

    /**
     * Retrieves a description of a table's optimal set of columns that
     * uniquely identifies a row. They are ordered by SCOPE.
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>SCOPE</B> short => actual scope of result
     *      <UL>
     *      <LI> bestRowTemporary - very temporary, while using row
     *      <LI> bestRowTransaction - valid for remainder of current transaction
     *      <LI> bestRowSession - valid for remainder of current session
     *      </UL>
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> int => SQL data type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name,
     *  for a UDT the type name is fully qualified
     *	<LI><B>COLUMN_SIZE</B> int => precision
     *	<LI><B>BUFFER_LENGTH</B> int => not used
     *	<LI><B>DECIMAL_DIGITS</B> short	 => scale - Null is returned for data types where
     * DECIMAL_DIGITS is not applicable.
     *	<LI><B>PSEUDO_COLUMN</B> short => is this a pseudo column
     *      like an Oracle ROWID
     *      <UL>
     *      <LI> bestRowUnknown - may or may not be pseudo column
     *      <LI> bestRowNotPseudo - is NOT a pseudo column
     *      <LI> bestRowPseudo - is a pseudo column
     *      </UL>
     *  </OL>
     *
     * <p>The COLUMN_SIZE column represents the specified column size for the given column.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schema a schema name; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param table a table name; must match the table name as it is stored
     *        in the database
     * @param scope the scope of interest; use same values as SCOPE
     * @param nullable include columns that are nullable.
     * @return <code>ResultSet</code> - each row is a column description
     * @exception SQLException if a database access error occurs
     */
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        throw new NotImplemented("getBestRowIdentifier(...)");
    }

    /**
     * Retrieves the catalog names available in this database.  The results
     * are ordered by catalog name.
     *
     * <P>The catalog column is:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => catalog name
     *  </OL>
     *
     *  <b>NOTE:</b> This is one of the key areas to make this driver work with SQL clients.
     *  For MySql, always return the schema in place of the catalog.
     *  Also see {@link #getSchemas(String catalog, String schemaPattern)} for more information on this subject.
     *
     * @return a <code>ResultSet</code> object in which each row has a
     *         single <code>String</code> column that is a catalog name
     * @exception SQLException if a database access error occurs
     */
    public ResultSet getCatalogs() throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuild = new StringBuilder(100);
        String procSql = sqlBuild.append("SELECT ").append("SCHEMA_NAME AS TABLE_CAT ").append("FROM INFORMATION_SCHEMA.SCHEMATA ").append("ORDER BY TABLE_CAT;").toString();
        WCStatement procStmnt = myConnection.createInternalStatement();
        if (procStmnt.execute(procSql)) {
            res = procStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getCatalogs());
        return res;
    }

    /**
	 * Retrieves a list of the client info properties that the driver supports.
	 * The result set contains the following columns <br />
	 * <ol>
	 * <li><b>NAME</b> String=> The name of the client info property</li>
	 * <li><b>MAX_LEN</b> int=> The maximum length of the value for the property</li>
	 * <li><b>DEFAULT_VALUE</b> String=> The default value of the property</li>
	 * <li><b>DESCRIPTION</b> String=> A description of the property.
	 * This will typically contain information as to where this property is stored in the database.</li>
	 * </ol>
	 * The ResultSet is sorted by the NAME column<br />
	 *
	 * @return A ResultSet object; each row is a supported client info property
	 * @throws SQLException if a database access error occurs
	 * @since 1.6
	 */
    public ResultSet getClientInfoProperties() throws SQLException {
        WCDriverPropertiesInfo clientInfoProps = new WCDriverPropertiesInfo(this.myConnection);
        return clientInfoProps.getClientInfoResultSet();
    }

    /**
	 * Retrieves a description of the access rights for a table's columns.<br />
	 * <br />
	 * Only privileges matching the column name criteria are returned.
	 * They are ordered by COLUMN_NAME and PRIVILEGE.<br />
	 * <br />
	 * Each privilige description has the following columns:<br />
	 * <ol>
	 * <li><b>TABLE_CAT</b> String => table catalog (may be null)</li>
	 * <li><b>TABLE_SCHEM</b> String => table schema (may be null)</li>
	 * <li><b>TABLE_NAME</b> String => table name</li>
	 * <li><b>COLUMN_NAME</b> String => column name</li>
	 * <li><b>GRANTOR</b> String => grantor of access (may be null)</li>
	 * <li><b>GRANTEE</b> String => grantee of access</li>
	 * <li><b>PRIVILEGE</b> String => name of access (SELECT, INSERT, UPDATE, REFRENCES, ...)</li>
	 * <li><b>IS_GRANTABLE</b> String => "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown</li>
	 * </ol>
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schema a schema name; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param table a table name; must match the table name as it is stored in the database
	 * @param columnNamePattern a column name pattern; must match the column name as it is stored in the database
	 * @return ResultSet - each row is a column privilege description
	 * @throws SQLException if a database access error occurs
	 */
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append("TABLE_SCHEMA AS TABLE_CAT, ").append("'' AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("COLUMN_NAME, ").append("NULL AS GRANTOR, ").append("GRANTEE, ").append("PRIVILEGE_TYPE AS PRIVILEGE, ").append("IS_GRANTABLE ").append("FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES ");
        SQLCondition condition = new SQLCondition();
        if ("".equals(catalog)) {
            sqlBuilder.append(condition.getKeyWord() + "(TABLE_SCHEMA IS NULL OR TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append(condition.getKeyWord() + "TABLE_SCHEMA = '").append(catalog).append("' ");
        }
        sqlBuilder.append(condition.getKeyWord() + "TABLE_NAME = '").append(table).append("' ");
        sqlBuilder.append(condition.getKeyWord() + "COLUMN_NAME LIKE '").append(columnNamePattern).append("' ");
        sqlBuilder.append("ORDER BY COLUMN_NAME, PRIVILEGE_TYPE;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getColumnPrivileges());
        return res;
    }

    /**
     * Retrieves a description of table columns available in
     * the specified catalog.
     *
     * <P>Only column descriptions matching the catalog, schema, table
     * and column name criteria are returned.  They are ordered by
     * <code>TABLE_CAT</code>,<code>TABLE_SCHEM</code>,
     * <code>TABLE_NAME</code>, and <code>ORDINAL_POSITION</code>.
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name,
     *  for a UDT the type name is fully qualified
     *	<LI><B>COLUMN_SIZE</B> int => column size.
     *	<LI><B>BUFFER_LENGTH</B> is not used.
     *	<LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits. Null is returned for data types where
     * DECIMAL_DIGITS is not applicable.
     *	<LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
     *	<LI><B>NULLABLE</B> int => is NULL allowed.
     *      <UL>
     *      <LI> columnNoNulls - might not allow <code>NULL</code> values
     *      <LI> columnNullable - definitely allows <code>NULL</code> values
     *      <LI> columnNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>REMARKS</B> String => comment describing column (may be <code>null</code>)
     * 	<LI><B>COLUMN_DEF</B> String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
     *	<LI><B>SQL_DATA_TYPE</B> int => unused
     *	<LI><B>SQL_DATETIME_SUB</B> int => unused
     *	<LI><B>CHAR_OCTET_LENGTH</B> int => for char types the
     *       maximum number of bytes in the column
     *	<LI><B>ORDINAL_POSITION</B> int	=> index of column in table
     *      (starting at 1)
     *	<LI><B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
     *       <UL>
     *       <LI> YES           --- if the parameter can include NULLs
     *       <LI> NO            --- if the parameter cannot include NULLs
     *       <LI> empty string  --- if the nullability for the
     * parameter is unknown
     *       </UL>
     *  <LI><B>SCOPE_CATLOG</B> String => catalog of table that is the scope
     *      of a reference attribute (<code>null</code> if DATA_TYPE isn't REF) [NOTE: typo in specs. Should be SCOPE_CATALOG]
     *  <LI><B>SCOPE_SCHEMA</B> String => schema of table that is the scope
     *      of a reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
     *  <LI><B>SCOPE_TABLE</B> String => table name that this the scope
     *      of a reference attribure (<code>null</code> if the DATA_TYPE isn't REF)
     *  <LI><B>SOURCE_DATA_TYPE</B> short => source type of a distinct type or user-generated
     *      Ref type, SQL type from java.sql.Types (<code>null</code> if DATA_TYPE
     *      isn't DISTINCT or user-generated REF)
     *   <LI><B>IS_AUTOINCREMENT</B> String  => Indicates whether this column is auto incremented
     *       <UL>
     *       <LI> YES           --- if the column is auto incremented
     *       <LI> NO            --- if the column is not auto incremented
     *       <LI> empty string  --- if it cannot be determined whether the column is auto incremented
     * parameter is unknown
     *       </UL>
     *  </OL>
     *
     * <p>The COLUMN_SIZE column the specified column size for the given column.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param tableNamePattern a table name pattern; must match the
     *        table name as it is stored in the database
     * @param columnNamePattern a column name pattern; must match the column
     *        name as it is stored in the database
     * @return <code>ResultSet</code> - each row is a column description
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     */
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        WCResultSet res;
        if ("*".equals(tableNamePattern)) {
            tableNamePattern = "%";
        }
        if ("*".equals(columnNamePattern)) {
            columnNamePattern = "%";
        }
        StringBuilder procSql = new StringBuilder(1750);
        procSql.append("SELECT ").append("TABLE_SCHEMA AS TABLE_CAT, ").append("NULL AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("COLUMN_NAME, ").append("DATA_TYPE, ").append("UCASE(DATA_TYPE) AS TYPE_NAME, ").append("CASE WHEN CHARACTER_MAXIMUM_LENGTH>" + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE + " ELSE CASE WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION ELSE CHARACTER_MAXIMUM_LENGTH END END AS COLUMN_SIZE, ").append("65535 AS BUFFER_LENGTH, ").append("IF(NUMERIC_SCALE IS NULL, 0, NUMERIC_SCALE) AS DECIMAL_DIGITS, ").append("10 AS NUM_PREC_RADIX, ").append("CASE WHEN IS_NULLABLE='YES' THEN '" + DatabaseMetaData.attributeNullable + "' WHEN IS_NULLABLE='NO' THEN '" + DatabaseMetaData.attributeNoNulls + "' ELSE '" + DatabaseMetaData.attributeNullableUnknown + "' END AS NULLABLE, ").append("COLUMN_COMMENT AS REMARKS, ").append("IF((IS_NULLABLE='NO' AND COLUMN_DEFAULT IS NULL), '', COLUMN_DEFAULT) AS COLUMN_DEF, ").append("0 AS SQL_DATA_TYPE, ").append("0 AS SQL_DATETIME_SUB, ").append("CASE WHEN CHARACTER_OCTET_LENGTH>" + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE + " WHEN CHARACTER_OCTET_LENGTH IS NULL THEN 0 ELSE CHARACTER_OCTET_LENGTH END AS CHAR_OCTET_LENGTH, ").append("ORDINAL_POSITION, ").append("IS_NULLABLE, ").append("NULL AS SCOPE_CATALOG, ").append("NULL AS SCOPE_SCHEMA, ").append("NULL AS SCOPE_TABLE, ").append("NULL AS SOURCE_DATA_TYPE, ").append("IF(EXTRA LIKE '%auto_increment%', 'YES', 'NO') AS IS_AUTOINCREMENT ").append("FROM INFORMATION_SCHEMA.COLUMNS ");
        SQLCondition condition = new SQLCondition();
        if ("".equals(catalog)) {
            procSql.append(condition.getKeyWord() + "(TABLE_SCHEMA IS NULL OR TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            procSql.append(condition.getKeyWord() + "TABLE_SCHEMA = '").append(catalog).append("' ");
        }
        procSql.append(condition.getKeyWord() + "TABLE_NAME LIKE '").append(tableNamePattern).append("' ");
        procSql.append(condition.getKeyWord() + "COLUMN_NAME LIKE '").append(columnNamePattern).append("' ");
        procSql.append("ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION;");
        WCStatement procStmnt = myConnection.createInternalStatement();
        if (procStmnt.execute(procSql.toString())) {
            res = procStmnt.getResultSet();
            for (int i = 0; i < res.myRows.length(); i++) {
                ObjectArray resTmp = (ObjectArray) res.myRows.getObject(i);
                String dtype = resTmp.getString("DATA_TYPE");
                resTmp.setData("DATA_TYPE", myConnection.nativeNameToJdbcType(dtype));
                dtype = resTmp.getString("COLUMN_SIZE");
                if (dtype == null || dtype.isEmpty()) resTmp.setData("COLUMN_SIZE", 0);
                res.myRows.setData(i, resTmp);
            }
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getColumns());
        return res;
    }

    /**
     * Retrieves a description of the foreign key columns in the given foreign key
     * table that reference the primary key or the columns representing a unique constraint of the  parent table (could be the same or a different table).
     * The number of columns returned from the parent table must match the number of
     * columns that make up the foreign key.  They
     * are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and
     * KEY_SEQ.
     *
     * <P>Each foreign key column description has the following columns:
     *  <OL>
     *	<LI><B>PKTABLE_CAT</B> String => parent key table catalog (may be <code>null</code>)
     *	<LI><B>PKTABLE_SCHEM</B> String => parent key table schema (may be <code>null</code>)
     *	<LI><B>PKTABLE_NAME</B> String => parent key table name
     *	<LI><B>PKCOLUMN_NAME</B> String => parent key column name
     *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be <code>null</code>)
     *      being exported (may be <code>null</code>)
     *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be <code>null</code>)
     *      being exported (may be <code>null</code>)
     *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
     *      being exported
     *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
     *      being exported
     *	<LI><B>KEY_SEQ</B> short => sequence number within foreign key( a value
     *  of 1 represents the first column of the foreign key, a value of 2 would
     *  represent the second column within the foreign key).
     *	<LI><B>UPDATE_RULE</B> short => What happens to
     *       foreign key when parent key is updated:
     *      <UL>
     *      <LI> importedNoAction - do not allow update of parent
     *               key if it has been imported
     *      <LI> importedKeyCascade - change imported key to agree
     *               with parent key update
     *      <LI> importedKeySetNull - change imported key to <code>NULL</code> if
     *               its parent key has been updated
     *      <LI> importedKeySetDefault - change imported key to default values
     *               if its parent key has been updated
     *      <LI> importedKeyRestrict - same as importedKeyNoAction
     *                                 (for ODBC 2.x compatibility)
     *      </UL>
     *	<LI><B>DELETE_RULE</B> short => What happens to
     *      the foreign key when parent key is deleted.
     *      <UL>
     *      <LI> importedKeyNoAction - do not allow delete of parent
     *               key if it has been imported
     *      <LI> importedKeyCascade - delete rows that import a deleted key
     *      <LI> importedKeySetNull - change imported key to <code>NULL</code> if
     *               its primary key has been deleted
     *      <LI> importedKeyRestrict - same as importedKeyNoAction
     *                                 (for ODBC 2.x compatibility)
     *      <LI> importedKeySetDefault - change imported key to default if
     *               its parent key has been deleted
     *      </UL>
     *	<LI><B>FK_NAME</B> String => foreign key name (may be <code>null</code>)
     *	<LI><B>PK_NAME</B> String => parent key name (may be <code>null</code>)
     *	<LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key
     *      constraints be deferred until commit
     *      <UL>
     *      <LI> importedKeyInitiallyDeferred - see SQL92 for definition
     *      <LI> importedKeyInitiallyImmediate - see SQL92 for definition
     *      <LI> importedKeyNotDeferrable - see SQL92 for definition
     *      </UL>
     *  </OL>
     *
     * @param parentCatalog a catalog name; must match the catalog name
     * as it is stored in the database; "" retrieves those without a
     * catalog; <code>null</code> means drop catalog name from the selection criteria
     * @param parentSchema a schema name; must match the schema name as
     * it is stored in the database; "" retrieves those without a schema;
     * <code>null</code> means drop schema name from the selection criteria
     * @param parentTable the name of the table that exports the key; must match
     * the table name as it is stored in the database
     * @param foreignCatalog a catalog name; must match the catalog name as
     * it is stored in the database; "" retrieves those without a
     * catalog; <code>null</code> means drop catalog name from the selection criteria
     * @param foreignSchema a schema name; must match the schema name as it
     * is stored in the database; "" retrieves those without a schema;
     * <code>null</code> means drop schema name from the selection criteria
     * @param foreignTable the name of the table that imports the key; must match
     * the table name as it is stored in the database
     * @return <code>ResultSet</code> - each row is a foreign key column description
     * @exception SQLException if a database access error occurs
     * @see DatabaseMetaData#getImportedKeys
     */
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        throw new NotImplemented("getCrossReference(...)");
    }

    /**
     * Retrieves this database's default transaction isolation level.  The
     * possible values are defined in <code>java.sql.Connection</code>.
     *
     * @return the default isolation level
     * @exception SQLException if a database access error occurs
     * @see java.sql.Connection
     */
    public int getDefaultTransactionIsolation() throws SQLException {
        return WCConnection.TRANSACTION_REPEATABLE_READ;
    }

    /**
	 * Retrieves a description of the foreign key columns that reference
	 * the given table's primary key columns (the foreign keys exported by a table).<br />
	 * They are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ. <br />
	 * <br />
	 * Each foreign key column description has the following columns:
	 * <ol>
	 * 	<li><b>PKTABLE_CAT</b> String => primary key table catalog (may be null) </li>
	 * 	<li><b>PKTABLE_SCHEM</b> String => primary key table schema (may be null) </li>
	 * 	<li><b>PKTABLE_NAME</b> String => primary key table name </li>
	 * 	<li><b>PKCOLUMN_NAME</b> String => primary key column name </li>
	 * 	<li><b>FKTABLE_CAT</b> String => foreign key table catalog (may be null) being exported (may be null) </li>
	 * 	<li><b>FKTABLE_SCHEM</b> String => foreign key table schema (may be null) being exported (may be null) </li>
	 * 	<li><b>FKTABLE_NAME</b> String => foreign key table name being exported </li>
	 * 	<li><b>FKCOLUMN_NAME</b> String => foreign key column name being exported </li>
	 * 	<li><b>KEY_SEQ</b> short => sequence number within foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). </li>
	 * 	<li><b>UPDATE_RULE</b> short => What happens to foreign key when primary is updated: </li>
	 * 	<ul>
	 * 		<li>importedNoAction - do not allow update of primary key if it has been imported </li>
	 * 		<li>importedKeyCascade - change imported key to agree with primary key update </li>
	 * 		<li>importedKeySetNull - change imported key to NULL if its primary key has been updated </li>
	 * 		<li>importedKeySetDefault - change imported key to default values if its primary key has been updated </li>
	 * 		<li>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) </li>
	 * 	</ul>
	 * 	<li><b>DELETE_RULE</b> short => What happens to the foreign key when primary is deleted. </li>
	 * 	<ul>
	 * 		<li>importedKeyNoAction - do not allow delete of primary key if it has been imported </li>
	 * 		<li>importedKeyCascade - delete rows that import a deleted key </li>
	 * 		<li>importedKeySetNull - change imported key to NULL if its primary key has been deleted </li>
	 * 		<li>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) </li>
	 * 		<li>importedKeySetDefault - change imported key to default if its primary key has been deleted </li>
	 * 	</ul>
	 * 	<li><b>FK_NAME</b> String => foreign key name (may be null) </li>
	 * 	<li><b>PK_NAME</b> String => primary key name (may be null) </li>
	 * 	<li><b>DEFERRABILITY</b> short => can the evaluation of foreign key constraints be deferred until commit </li>
	 * 	<ul>
	 * 		<li>importedKeyInitiallyDeferred - see SQL92 for definition </li>
	 * 		<li>importedKeyInitiallyImmediate - see SQL92 for definition </li>
	 * 		<li>importedKeyNotDeferrable - see SQL92 for definition </li>
	 * 	</ul>
	 * </ol>
	 *
	 * @see java.sql.DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 * @param catalog a catalog name; must match the catalog name as it is stored in this database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schema a schema name; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param table a table name; must match the table name as it is stored in this database
	 * @return a ResultSet object in which each row is a foreign key column description
	 * @throws SQLException if a database access error occurs
	 */
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append("A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT, ").append("'' AS PKTABLE_SCHEM, ").append("A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, ").append("A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, ").append("A.TABLE_SCHEMA AS FKTABLE_CAT, ").append("NULL AS FKTABLE_SCHEM, ").append("A.TABLE_NAME AS FKTABLE_NAME, ").append("A.COLUMN_NAME AS FKCOLUMN_NAME, ").append("A.ORDINAL_POSITION AS KEY_SEQ, ").append(generateUpdateRuleClause()).append(" AS UPDATE_RULE, ").append(generateDeleteRuleClause()).append(" AS DELETE_RULE, ").append("A.CONSTRAINT_NAME AS FK_NAME, ").append("(SELECT CONSTRAINT_NAME ").append(" FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS ").append(" WHERE TABLE_SCHEMA = REFERENCED_TABLE_SCHEMA ").append(" AND TABLE_NAME = A.REFERENCED_TABLE_NAME ").append(" AND CONSTRAINT_TYPE IN('UNIQUE','PRIMARY KEY') LIMIT 1) ").append("AS PK_NAME, ").append(DatabaseMetaData.importedKeyNotDeferrable).append(" AS DEFERRABILITY ").append("FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS B ").append("LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE A ").append("ON(B.TABLE_SCHEMA=A.TABLE_SCHEMA AND B.TABLE_NAME=A.TABLE_NAME AND B.CONSTRAINT_NAME=A.CONSTRAINT_NAME) ").append(generateOptionalRefContraintsJoin()).append("WHERE B.CONSTRAINT_TYPE = 'FOREIGN KEY' ").append("AND A.REFERENCED_TABLE_NAME = '").append(table).append("' ");
        if ("".equals(catalog)) {
            sqlBuilder.append("	AND (A.REFERENCED_TABLE_SCHEMA IS NULL OR A.REFERENCED_TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append("AND A.REFERENCED_TABLE_SCHEMA = '").append(catalog).append("' ");
        }
        sqlBuilder.append("ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getImportedExportedKeys());
        return res;
    }

    /**
	 * Retrieves a description of the given catalog's system or
	 * user function parameters and return type. <br />
	 * <br />
	 * Only descriptions matching the schema,
	 * function and parameter name criteria are returned. <br />
	 * They are ordered by FUNCTION_CAT, FUNCTION_SCHEM, FUNCTION_NAME and SPECIFIC_ NAME.
	 * Within this, the return value, if any, is first.
	 * Next are the parameter descriptions in call order.
	 * The column descriptions follow in column number order. <br />
	 * <br />
	 * Each row in the ResultSet is a parameter description, column description or return type description with the following fields: <br />
	 * <ol>
	 * <li><b>FUNCTION_CAT</b> String => function catalog (may be null) <br />
	 * <li><b>FUNCTION_SCHEM</b> String => function schema (may be null) <br />
	 * <li><b>FUNCTION_NAME</b> String => function name. This is the name used to invoke the function <br />
	 * <li><b>COLUMN_NAME</b> String => column/parameter name <br />
	 * <li><b>COLUMN_TYPE</b> Short => kind of column/parameter: <br />
	 * <ul>
	 * <li>functionColumnUnknown - nobody knows <br />
	 * <li>functionColumnIn - IN parameter <br />
	 * <li>functionColumnInOut - INOUT parameter <br />
	 * <li>functionColumnOut - OUT parameter <br />
	 * <li>functionColumnReturn - function return value <br />
	 * <li>functionColumnResult - Indicates that the parameter or column is a column in the ResultSet <br />
	 * </ul>
	 * <li><b>DATA_TYPE</b> int => SQL type from java.sql.Types <br />
	 * <li><b>TYPE_NAME</b> String => SQL type name, for a UDT type the type name is fully qualified <br />
	 * <li><b>PRECISION</b> int => precision <br />
	 * <li><b>LENGTH</b> int => length in bytes of data <br />
	 * <li><b>SCALE</b> short => scale - null is returned for data types where SCALE is not applicable. <br />
	 * <li><b>RADIX</b> short => radix <br />
	 * <li><b>NULLABLE</b> short => can it contain NULL. <br />
	 * <ul>
	 * <li>functionNoNulls - does not allow NULL values <br />
	 * <li>functionNullable - allows NULL values <br />
	 * <li>functionNullableUnknown - nullability unknown <br />
	 * </ul>
	 * <li><b>REMARKS</b> String => comment describing column/parameter <br />
	 * <li><b>CHAR_OCTET_LENGTH</b> int => the maximum length of binary and character based parameters or columns. For any other datatype the returned value is a NULL <br />
	 * <li><b>ORDINAL_POSITION</b> int => the ordinal position, starting from 1, for the input and output parameters. A value of 0 is returned if this row describes the function's return value. For result set columns, it is the ordinal position of the column in the result set starting from 1. <br />
	 * <li><b>IS_NULLABLE</b> String => ISO rules are used to determine the nullability for a parameter or column. <br />
	 * <ul>
	 * <li>YES --- if the parameter or column can include NULLs <br />
	 * <li>NO --- if the parameter or column cannot include NULLs <br />
	 * <li>empty string --- if the nullability for the parameter or column is unknown <br />
	 * </ul>
	 * <li><b>SPECIFIC_NAME</b> String => the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions <br />
	 * </ol>
	 *
	 * <br />
	 * <ul>
	 * <li>The <b>PRECISION</b> column represents the specified column size for the given parameter or column. <br />
	 * <ul>
	 * <li>For numeric data, this is the maximum precision.<br />
	 * <li>For character data, this is the length in characters.<br />
	 * <li>For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). <br />
	 * <li>For binary data, this is the length in bytes. <br />
	 * <li>For the ROWID datatype, this is the length in bytes.
	 * <li>Null is returned for data types where the column size is not applicable.<br />
	 * </ul>
	 * </ul>
	 *
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schemaPattern a schema name pattern; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param functionNamePattern a procedure name pattern; must match the function name as it is stored in the database
	 * @param columnNamePattern a parameter name pattern; must match the parameter or column name as it is stored in the database
	 * @return ResultSet - each row describes a user function parameter, column or return type
	 * @throws SQLException if a database access error occurs
	 * @since 1.6
	 */
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        throw new NotImplemented("getFunctionColumns(...)");
    }

    /**
     * Retrieves a description of the  system and user functions available
     * in the given catalog.
     * <P>
     * Only system and user function descriptions matching the schema and
     * function name criteria are returned.  They are ordered by
     * <code>FUNCTION_CAT</code>, <code>FUNCTION_SCHEM</code>,
     * <code>FUNCTION_NAME</code> and
     * <code>SPECIFIC_ NAME</code>.
     *
     * <P>Each function description has the the following columns:
     *  <OL>
     *	<LI><B>FUNCTION_CAT</B> String => function catalog (may be <code>null</code>)
     *	<LI><B>FUNCTION_SCHEM</B> String => function schema (may be <code>null</code>)
     *	<LI><B>FUNCTION_NAME</B> String => function name.  This is the name
     * used to invoke the function
     *	<LI><B>REMARKS</B> String => explanatory comment on the function
     * <LI><B>FUNCTION_TYPE</B> short => kind of function:
     *      <UL>
     *      <LI>functionResultUnknown - Cannot determine if a return value
     *       or table will be returned
     *      <LI> functionNoTable- Does not return a table
     *      <LI> functionReturnsTable - Returns a table
     *      </UL>
     *	<LI><B>SPECIFIC_NAME</B> String  => the name which uniquely identifies
     *  this function within its schema.  This is a user specified, or DBMS
     * generated, name that may be different then the <code>FUNCTION_NAME</code>
     * for example with overload functions
     *  </OL>
     * <p>
     * A user may not have permission to execute any of the functions that are
     * returned by <code>getFunctions</code>
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param functionNamePattern a function name pattern; must match the
     *        function name as it is stored in the database
     * @return <code>ResultSet</code> - each row is a function description
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     * @since 1.6
     */
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append("ROUTINE_SCHEMA AS FUNCTION_CAT, ").append("'' AS FUNCTION_SCHEM, ").append("ROUTINE_NAME AS FUNCTION_NAME, ").append("ROUTINE_COMMENT AS REMARKS, ").append(DatabaseMetaData.functionResultUnknown).append(" AS FUNCTION_TYPE, ").append("SPECIFIC_NAME ").append("FROM INFORMATION_SCHEMA.ROUTINES ").append("WHERE ROUTINE_TYPE = 'FUNCTION' ");
        if ("".equals(catalog)) {
            sqlBuilder.append("	AND (ROUTINE_SCHEMA IS NULL OR ROUTINE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append("	AND ROUTINE_SCHEMA = '").append(catalog).append("' ");
        }
        if (functionNamePattern != null) {
            sqlBuilder.append("	AND ROUTINE_NAME LIKE '").append(functionNamePattern).append("' ");
        }
        sqlBuilder.append("ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME, SPECIFIC_NAME;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        return res;
    }

    public String getIdentifierQuoteString() throws SQLException {
        return "`";
    }

    /**
	 * Retrieves a description of the primary key columns that are referenced
	 * by the given table's foreign key columns (the primary keys imported by a table). <br />
	 * They are ordered by PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ. <br />
	 * <br />
	 * Each primary key column description has the following columns: <br />
	 * <ol>
	 * <li><b>PKTABLE_CAT</b> String => primary key table catalog being imported (may be null) <br />
	 * <li><b>PKTABLE_SCHEM</b> String => primary key table schema being imported (may be null) <br />
	 * <li><b>PKTABLE_NAME</b> String => primary key table name being imported <br />
	 * <li><b>PKCOLUMN_NAME</b> String => primary key column name being imported <br />
	 * <li><b>FKTABLE_CAT</b> String => foreign key table catalog (may be null) <br />
	 * <li><b>FKTABLE_SCHEM</b> String => foreign key table schema (may be null) <br />
	 * <li><b>FKTABLE_NAME</b> String => foreign key table name <br />
	 * <li><b>FKCOLUMN_NAME</b> String => foreign key column name <br />
	 * <li><b>KEY_SEQ</b> short => sequence number within a foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). <br />
	 * <li><b>UPDATE_RULE</b> short => What happens to a foreign key when the primary key is updated: <br />
	 * <ul>
	 * <li>importedNoAction - do not allow update of primary key if it has been imported <br />
	 * <li>importedKeyCascade - change imported key to agree with primary key update <br />
	 * <li>importedKeySetNull - change imported key to NULL if its primary key has been updated <br />
	 * <li>importedKeySetDefault - change imported key to default values if its primary key has been updated <br />
	 * <li>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) <br />
	 * </ul>
	 * <li><b>DELETE_RULE</b> short => What happens to the foreign key when primary is deleted. <br />
	 * <ul>
	 * <li>importedKeyNoAction - do not allow delete of primary key if it has been imported <br />
	 * <li>importedKeyCascade - delete rows that import a deleted key <br />
	 * <li>importedKeySetNull - change imported key to NULL if its primary key has been deleted <br />
	 * <li>importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) <br />
	 * <li>importedKeySetDefault - change imported key to default if its primary key has been deleted <br />
	 * </ul>
	 * <li><b>FK_NAME</b> String => foreign key name (may be null) <br />
	 * <li><b>PK_NAME</b> String => primary key name (may be null) <br />
	 * <li><b>DEFERRABILITY</b> short => can the evaluation of foreign key constraints be deferred until commit <br />
	 * <ul>
	 * <li>importedKeyInitiallyDeferred - see SQL92 for definition <br />
	 * <li>importedKeyInitiallyImmediate - see SQL92 for definition <br />
	 * <li>importedKeyNotDeferrable - see SQL92 for definition
	 * </ul>
	 * </ol>
	 *
	 * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schema a schema name; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param table a table name; must match the table name as it is stored in the database
	 * @return ResultSet - each row is a primary key column description
	 * @throws SQLException if a database access error occurs
	 */
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append("A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT, ").append("'' AS PKTABLE_SCHEM, ").append("A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, ").append("A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, ").append("A.TABLE_SCHEMA AS FKTABLE_CAT, ").append("NULL AS FKTABLE_SCHEM, ").append("A.TABLE_NAME AS FKTABLE_NAME, ").append("A.COLUMN_NAME AS FKCOLUMN_NAME, ").append("A.ORDINAL_POSITION AS KEY_SEQ, ").append(generateUpdateRuleClause()).append(" AS UPDATE_RULE, ").append(generateDeleteRuleClause()).append(" AS DELETE_RULE, ").append("A.CONSTRAINT_NAME AS FK_NAME, ").append("(SELECT CONSTRAINT_NAME ").append(" FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS ").append(" WHERE TABLE_SCHEMA = REFERENCED_TABLE_SCHEMA ").append(" AND TABLE_NAME = A.REFERENCED_TABLE_NAME ").append(" AND CONSTRAINT_TYPE IN('UNIQUE','PRIMARY KEY') LIMIT 1) ").append("AS PK_NAME,").append(DatabaseMetaData.importedKeyNotDeferrable).append(" AS DEFERRABILITY ").append("FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS B ").append("LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE A ").append("USING(CONSTRAINT_NAME, TABLE_NAME) ").append(generateOptionalRefContraintsJoin()).append("WHERE B.CONSTRAINT_TYPE LIKE 'FOREIGN KEY' ").append("AND B.TABLE_NAME = '").append(table).append("' ");
        if ("".equals(catalog)) {
            sqlBuilder.append("	AND (A.TABLE_SCHEMA IS NULL OR A.TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append("AND A.TABLE_SCHEMA = '").append(schema).append("' ");
        }
        sqlBuilder.append("ORDER BY A.REFERENCED_TABLE_SCHEMA, A.REFERENCED_TABLE_NAME, A.ORDINAL_POSITION;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getImportedExportedKeys());
        return res;
    }

    /**
	 * Retrieves a description of the given table's indices and statistics.
	 * They are ordered by NON_UNIQUE, TYPE, INDEX_NAME, and ORDINAL_POSITION. <br />
	 * <br />
	 * Each index column description has the following columns: <br />
	 * <ol>
	 * <li><b>TABLE_CAT</b> String => table catalog (may be null) <br />
	 * <li><b>TABLE_SCHEM</b> String => table schema (may be null) <br />
	 * <li><b>TABLE_NAME</b> String => table name <br />
	 * <li><b>NON_UNIQUE</b> boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic <br />
	 * <li><b>INDEX_QUALIFIER</b> String => index catalog (may be null); null when TYPE is tableIndexStatistic <br />
	 * <li><b>INDEX_NAME</b> String => index name; null when TYPE is tableIndexStatistic <br />
	 * <li><b>TYPE</b> short => index type: <br />
	 * <ul>
	 * <li>tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions <br />
	 * <li>tableIndexClustered - this is a clustered index <br />
	 * <li>tableIndexHashed - this is a hashed index <br />
	 * <li>tableIndexOther - this is some other style of index <br />
	 * </ul>
	 * <li><b>ORDINAL_POSITION</b> short => column sequence number within index; zero when TYPE is tableIndexStatistic <br />
	 * <li><b>COLUMN_NAME</b> String => column name; null when TYPE is tableIndexStatistic <br />
	 * <li><b>ASC_OR_DESC</b> String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic <br />
	 * <li><b>CARDINALITY</b> int => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index. <br />
	 * <li><b>PAGES</b> int => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index. <br />
	 * <li><b>FILTER_CONDITION</b> String => Filter condition, if any. (may be null)
	 * </ol>
	 *
	 * @param catalog a catalog name; must match the catalog name as it is stored in this database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schema a schema name; must match the schema name as it is stored in this database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param table a table name; must match the table name as it is stored in this database
	 * @param unique when true, return only indices for unique values; when false, return indices regardless of whether unique or not
	 * @param approximate when true, result is allowed to reflect approximate or out of data values; when false, results are requested to be accurate
	 * @return ResultSet - each row is an index column description
	 * @throws SQLException  if a database access error occurs
	 */
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder(300);
        sqlBuilder.append("SELECT ").append("TABLE_SCHEMA AS TABLE_CAT, ").append("NULL AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("IF(NON_UNIQUE = '0', 'false', 'true') AS NON_UNIQUE, ").append("NULL AS INDEX_QUALIFIER, ").append("INDEX_NAME, ").append(DatabaseMetaData.tableIndexOther).append(" AS TYPE, ").append("SEQ_IN_INDEX AS ORDINAL_POSITION, ").append("COLUMN_NAME, ").append("COLLATION AS ASC_OR_DESC, ").append("CARDINALITY, ").append("0 AS PAGES, ").append("NULL AS FILTER_CONDITION ").append("FROM INFORMATION_SCHEMA.STATISTICS ").append("WHERE TABLE_NAME = '").append(table).append("' ");
        if ("".equals(catalog)) {
            sqlBuilder.append("	AND (TABLE_SCHEMA IS NULL OR TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append("AND TABLE_SCHEMA = '").append(catalog).append("' ");
        }
        if (unique) {
            sqlBuilder.append("AND NON_UNIQUE=0 ");
        }
        sqlBuilder.append("ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getIndexInfo());
        return res;
    }

    /**
	 * Retrieves a description of the given table's primary key columns.<br />
	 * They are ordered by COLUMN_NAME. <br />
	 * <br />
	 * Each primary key column description has the following columns: <br />
	 * <ol>
	 * <li><b>TABLE_CAT</b> String => table catalog (may be null) <br />
	 * <li><b>TABLE_SCHEM</b> String => table schema (may be null) <br />
	 * <li><b>TABLE_NAME</b> String => table name <br />
	 * <li><b>COLUMN_NAME</b> String => column name <br />
	 * <li><b>KEY_SEQ</b> short => sequence number within primary key( a value of 1 represents the first column of the primary key, a value of 2 would represent the second column within the primary key). <br />
	 * <li><b>PK_NAME</b> String => primary key name (may be null)
	 * </ol>
	 *
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schema a schema name; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param table a table name; must match the table name as it is stored in the database
	 * @return ResultSet - each row is a primary key column description
	 * @throws SQLException if a database access error occurs
	 */
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder(250);
        sqlBuilder.append("SELECT ").append("CONSTRAINT_SCHEMA AS TABLE_CAT, ").append("NULL AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("COLUMN_NAME, ").append("ORDINAL_POSITION AS KEY_SEQ, ").append("CONSTRAINT_NAME AS PK_NAME ").append("FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE ").append("WHERE TABLE_NAME = '").append(table).append("' ").append("AND CONSTRAINT_NAME = 'PRIMARY' ");
        if ("".equals(catalog)) {
            sqlBuilder.append("	AND (CONSTRAINT_SCHEMA IS NULL OR CONSTRAINT_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append("	AND CONSTRAINT_SCHEMA = '").append(catalog).append("' ");
        }
        sqlBuilder.append("ORDER BY COLUMN_NAME;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getPrimaryKeys());
        return res;
    }

    /**
     * Retrieves a description of the given catalog's stored procedure parameter
     * and result columns.
     *
     * <P>Only descriptions matching the schema, procedure and
     * parameter name criteria are returned.  They are ordered by
     * PROCEDURE_CAT, PROCEDURE_SCHEM, PROCEDURE_NAME and SPECIFIC_NAME. Within this, the return value,
     * if any, is first. Next are the parameter descriptions in call
     * order. The column descriptions follow in column number order.
     *
     * <P>Each row in the <code>ResultSet</code> is a parameter description or
     * column description with the following fields:
     *  <OL>
     *	<LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be <code>null</code>)
     *	<LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be <code>null</code>)
     *	<LI><B>PROCEDURE_NAME</B> String => procedure name
     *	<LI><B>COLUMN_NAME</B> String => column/parameter name
     *	<LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
     *      <UL>
     *      <LI> procedureColumnUnknown - nobody knows
     *      <LI> procedureColumnIn - IN parameter
     *      <LI> procedureColumnInOut - INOUT parameter
     *      <LI> procedureColumnOut - OUT parameter
     *      <LI> procedureColumnReturn - procedure return value
     *      <LI> procedureColumnResult - result column in <code>ResultSet</code>
     *      </UL>
     *  <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => SQL type name, for a UDT type the
     *  type name is fully qualified
     *	<LI><B>PRECISION</B> int => precision
     *	<LI><B>LENGTH</B> int => length in bytes of data
     *	<LI><B>SCALE</B> short => scale -  null is returned for data types where
     * SCALE is not applicable.
     *	<LI><B>RADIX</B> short => radix
     *	<LI><B>NULLABLE</B> short => can it contain NULL.
     *      <UL>
     *      <LI> procedureNoNulls - does not allow NULL values
     *      <LI> procedureNullable - allows NULL values
     *      <LI> procedureNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>REMARKS</B> String => comment describing parameter/column
     * 	<LI><B>COLUMN_DEF</B> String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
     *      <UL>
     *      <LI> The string NULL (not enclosed in quotes) - if NULL was specified as the default value
     *      <LI> TRUNCATE (not enclosed in quotes)        - if the specified default value cannot be represented without truncation
     *      <LI> NULL                                     - if a default value was not specified
     *      </UL>
     *	<LI><B>SQL_DATA_TYPE</B> int  => reserved for future use
     *	<LI><B>SQL_DATETIME_SUB</B> int  => reserved for future use
     *	<LI><B>CHAR_OCTET_LENGTH</B> int  => the maximum length of binary and character based columns.  For any other datatype the returned value is a
     * NULL
     *	<LI><B>ORDINAL_POSITION</B> int  => the ordinal position, starting from 1, for the input and output parameters for a procedure. A value of 0
     *is returned if this row describes the procedure's return value.  For result set columns, it is the
     *ordinal position of the column in the result set starting from 1.  If there are
     *multiple result sets, the column ordinal positions are implementation
     * defined.
     *	<LI><B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
     *       <UL>
     *       <LI> YES           --- if the parameter can include NULLs
     *       <LI> NO            --- if the parameter cannot include NULLs
     *       <LI> empty string  --- if the nullability for the
     * parameter is unknown
     *       </UL>
     *	<LI><B>SPECIFIC_NAME</B> String  => the name which uniquely identifies this procedure within its schema.
     *  </OL>
     *
     * <P><B>Note:</B> Some databases may not return the column
     * descriptions for a procedure.
     *
     * <p>The PRECISION column represents the specified column size for the given column.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param procedureNamePattern a procedure name pattern; must match the
     *        procedure name as it is stored in the database
     * @param columnNamePattern a column name pattern; must match the column name
     *        as it is stored in the database
     * @return <code>ResultSet</code> - each row describes a stored procedure parameter or
     *      column
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     */
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        throw new NotImplemented("getProcedureColumns(...)");
    }

    /**
     * Retrieves the database vendor's preferred term for "procedure".
     *
     * @return the vendor term for "procedure"
     * @exception SQLException if a database access error occurs
     */
    public String getProcedureTerm() throws SQLException {
        return "PROCEDURE";
    }

    /**
     * Retrieves a description of the stored procedures available in the given
     * catalog.
     * <P>
     * Only procedure descriptions matching the schema and
     * procedure name criteria are returned.  They are ordered by
     * <code>PROCEDURE_CAT</code>, <code>PROCEDURE_SCHEM</code>,
     * <code>PROCEDURE_NAME</code> and <code>SPECIFIC_ NAME</code>.
     *
     * <P>Each procedure description has the the following columns:
     *  <OL>
     *	<LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be <code>null</code>)
     *	<LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be <code>null</code>)
     *	<LI><B>PROCEDURE_NAME</B> String => procedure name
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *	<LI><B>REMARKS</B> String => explanatory comment on the procedure
     *	<LI><B>PROCEDURE_TYPE</B> short => kind of procedure:
     *      <UL>
     *      <LI> procedureResultUnknown - Cannot determine if  a return value
     *       will be returned
     *      <LI> procedureNoResult - Does not return a return value
     *      <LI> procedureReturnsResult - Returns a return value
     *      </UL>
     *	<LI><B>SPECIFIC_NAME</B> String  => The name which uniquely identifies this
     * procedure within its schema.
     *  </OL>
     * <p>
     * A user may not have permissions to execute any of the procedures that are
     * returned by <code>getProcedures</code>
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param procedureNamePattern a procedure name pattern; must match the
     *        procedure name as it is stored in the database
     * @return <code>ResultSet</code> - each row is a procedure description
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     */
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        WCResultSet res;
        StringBuilder procSql = new StringBuilder(250);
        procSql.append("SELECT ").append("ROUTINE_SCHEMA AS PROCEDURE_CAT, ").append("NULL AS PROCEDURE_SCHEM, ").append("ROUTINE_NAME AS PROCEDURE_NAME, ").append("NULL AS reserved1, ").append("NULL AS reserved2, ").append("NULL AS reserved3, ").append("ROUTINE_COMMENT AS REMARKS, ").append(DatabaseMetaData.procedureResultUnknown).append(" AS PROCEDURE_TYPE, ").append("SPECIFIC_NAME ").append("FROM INFORMATION_SCHEMA.ROUTINES ").append("WHERE ROUTINE_TYPE = '").append(getProcedureTerm()).append("' ");
        if ("".equals(catalog)) {
            procSql.append("AND ROUTINE_SCHEMA IS NULL OR ROUTINE_SCHEMA = '' ");
        } else if (catalog != null) {
            procSql.append("AND ROUTINE_SCHEMA LIKE '").append(catalog).append("' ");
        }
        if (procedureNamePattern != null) {
            procSql.append("AND ROUTINE_NAME LIKE '").append(procedureNamePattern).append("' ");
        }
        procSql.append("ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME, SPECIFIC_NAME;");
        WCStatement procStmnt = myConnection.createInternalStatement();
        if (procStmnt.execute(procSql.toString())) {
            res = procStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getProcedures());
        return res;
    }

    /**
     * Retrieves the schema names available in this database.  The results
     * are ordered by <code>TABLE_CATALOG</code> and
     * <code>TABLE_SCHEM</code>.
     *
     * <P>The schema columns are:
     *  <OL>
     *	<LI><B>TABLE_SCHEM</B> String => schema name
     *  <LI><B>TABLE_CATALOG</B> String => catalog name (may be <code>null</code>)
     *  </OL>
     *
     * @return a <code>ResultSet</code> object in which each row is a
     *         schema description
     * @exception SQLException if a database access error occurs
     *
     */
    public ResultSet getSchemas() throws SQLException {
        return getSchemas(null, null);
    }

    /**
     * Retrieves the schema names available in this database.  The results
     * are ordered by <code>TABLE_CATALOG</code> and
     * <code>TABLE_SCHEM</code>.
     *
     * <P>The schema columns are:
     *  <OL>
     *	<LI><B>TABLE_SCHEM</B> String => schema name
     *  <LI><B>TABLE_CATALOG</B> String => catalog name (may be <code>null</code>)
     *  </OL>
     *
     *  <b>NOTE:</b> MySQL doesn't use schemas. This can be a very confusing subject
     *  as theres places in the information_schems table for schemas and they contain the database names.
     *  However in database terms the schema names are actually the catalog names
     *  therefore we return an empty resulset from here instead of retrieving anything.<br />
     *  To explain this further see {@link #getCatalogs}
     *
     *
     * @param catalog a catalog name; must match the catalog name as it is stored
     * in the database;"" retrieves those without a catalog; null means catalog
     * name should not be used to narrow down the search.
     * @param schemaPattern a schema name; must match the schema name as it is
     * stored in the database; null means
     * schema name should not be used to narrow down the search.
     * @return a <code>ResultSet</code> object in which each row is a
     *         schema description
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     * @since 1.6
     */
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        WCResultSet res = new WCResultSet(myConnection, WCStaticMetaData.getSchemas());
        return res;
    }

    /**
     * Retrieves a description of the tables available in the given catalog.
     * Only table descriptions matching the catalog, schema, table
     * name and type criteria are returned.  They are ordered by
     * <code>TABLE_TYPE</code>, <code>TABLE_CAT</code>,
     * <code>TABLE_SCHEM</code> and <code>TABLE_NAME</code>.
     * <P>
     * Each table description has the following columns:
     *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
     *			"VIEW",	"SYSTEM TABLE", "GLOBAL TEMPORARY",
     *			"LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     *	<LI><B>REMARKS</B> String => explanatory comment on the table
     *  <LI><B>TYPE_CAT</B> String => the types catalog (may be <code>null</code>)
     *  <LI><B>TYPE_SCHEM</B> String => the types schema (may be <code>null</code>)
     *  <LI><B>TYPE_NAME</B> String => type name (may be <code>null</code>)
     *  <LI><B>SELF_REFERENCING_COL_NAME</B> String => name of the designated
     *                  "identifier" column of a typed table (may be <code>null</code>)
     *	<LI><B>REF_GENERATION</B> String => specifies how values in
     *                  SELF_REFERENCING_COL_NAME are created. Values are
     *                  "SYSTEM", "USER", "DERIVED". (may be <code>null</code>)
     *  </OL>
     *
     * <P><B>Note:</B> Some databases may not return information for
     * all tables.
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param tableNamePattern a table name pattern; must match the
     *        table name as it is stored in the database
     * @param types a list of table types, which must be from the list of table types
     *         returned from {@link java.sql.DatabaseMetaData#getTableTypes},to include; <code>null</code> returns
     * all types
     * @return <code>ResultSet</code> - each row is a table description
     * @exception SQLException if a database access error occurs
     * @see DatabaseMetaData#getSearchStringEscape
     */
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        WCResultSet res;
        if ("*".equals(schemaPattern)) {
            schemaPattern = "%";
        }
        if ("*".equals(tableNamePattern)) {
            tableNamePattern = "%";
        }
        StringBuilder sql = new StringBuilder(750);
        sql.append("SELECT ").append("TABLE_SCHEMA AS TABLE_CAT, ").append("NULL AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("CASE WHEN TABLE_TYPE='BASE TABLE' THEN 'TABLE' WHEN TABLE_TYPE='BASE VIEW' THEN 'VIEW' WHEN TABLE_TYPE='TEMPORARY TABLE' THEN 'LOCAL TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, ").append("TABLE_COMMENT AS REMARKS, ").append("NULL AS TYPE_CAT, ").append("NULL AS TYPE_SCHEM, ").append("NULL AS TYPE_NAME, ").append("NULL AS SELF_REFERENCING_COL_NAME, ").append("NULL AS REF_GENERATION ").append("FROM INFORMATION_SCHEMA.TABLES ");
        SQLCondition condition = new SQLCondition();
        if ("".equals(catalog)) {
            sql.append(condition.getKeyWord() + "(TABLE_SCHEMA IS NULL OR TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sql.append(condition.getKeyWord() + "TABLE_SCHEMA LIKE '").append(catalog).append("' ");
        }
        if (tableNamePattern != null) {
            sql.append(condition.getKeyWord() + "TABLE_NAME LIKE '").append(tableNamePattern).append("' ");
        }
        if (types != null && types.length > 0) {
            sql.append(condition.getKeyWord() + "(");
            for (int i = 0; i < types.length; i++) {
                if (i > 0) {
                    sql.append(" OR ");
                }
                sql.append("TABLE_TYPE LIKE '").append(types[i]).append("' OR ");
                sql.append("TABLE_TYPE LIKE '").append(getSQLType(types[i])).append("' ");
            }
            sql.append(") ");
        }
        sql.append("ORDER BY TABLE_TYPE, TABLE_SCHEMA, TABLE_NAME;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sql.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getTables());
        return res;
    }

    /**
	 * Retrieves a description of the access rights for each table available in a catalog.
	 * Note that a table privilege applies to one or more columns in the table.
	 * It would be wrong to assume that this privilege applies to all columns
	 * (this may be true for some systems but is not true for all.)<br />
	 * <br />
	 * Only privileges matching the schema and table name criteria are returned.
	 * They are ordered by TABLE_CAT, TABLE_SCHEM, TABLE_NAME, and PRIVILEGE.<br />
	 * <br />
	 * Each privilige description has the following columns:<br />
	 * <ol>
	 * <li><b>TABLE_CAT</b> String => table catalog (may be null) <br />
	 * <li><b>TABLE_SCHEM</b> String => table schema (may be null) <br />
	 * <li><b>TABLE_NAME</b> String => table name <br />
	 * <li><b>GRANTOR</b> String => grantor of access (may be null) <br />
	 * <li><b>GRANTEE</b> String => grantee of access <br />
	 * <li><b>PRIVILEGE</b> String => name of access (SELECT, INSERT, UPDATE, REFRENCES, ...) <br />
	 * <li><b>IS_GRANTABLE</b> String => "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown
	 * </ol>
	 *
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 * @param catalog a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
	 * @param schemaPattern a schema name pattern; must match the schema name as it is stored in the database; "" retrieves those without a schema; null means that the schema name should not be used to narrow the search
	 * @param tableNamePattern a table name pattern; must match the table name as it is stored in the database
	 * @return ResultSet - each row is a table privilege description
	 * @throws SQLException if a database access error occurs
	 */
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        WCResultSet res;
        StringBuilder sqlBuilder = new StringBuilder(200);
        sqlBuilder.append("SELECT ").append("TABLE_SCHEMA AS TABLE_CAT, ").append("'' AS TABLE_SCHEM, ").append("TABLE_NAME, ").append("NULL AS GRANTOR, ").append("GRANTEE, ").append("PRIVILEGE_TYPE AS PRIVILEGE, ").append("IS_GRANTABLE ").append("FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES ");
        SQLCondition condition = new SQLCondition();
        if ("".equals(catalog)) {
            sqlBuilder.append(condition.getKeyWord() + "(TABLE_SCHEMA IS NULL OR TABLE_SCHEMA = '') ");
        } else if (catalog != null) {
            sqlBuilder.append(condition.getKeyWord() + "TABLE_SCHEMA = '").append(catalog).append("' ");
        }
        sqlBuilder.append(condition.getKeyWord() + "TABLE_NAME LIKE '").append(tableNamePattern).append("' ");
        sqlBuilder.append("ORDER BY TABLE_SCHEMA, TABLE_NAME, PRIVILEGE_TYPE;");
        WCStatement sqlStmnt = myConnection.createInternalStatement();
        if (sqlStmnt.execute(sqlBuilder.toString())) {
            res = sqlStmnt.getResultSet();
        } else {
            res = new WCResultSet(myConnection);
        }
        res.addMetaData(WCStaticMetaData.getTablePrivileges());
        return res;
    }

    /**
     * Retrieves a description of the user-defined types (UDTs) defined
     * in a particular schema.  Schema-specific UDTs may have type
     * <code>JAVA_OBJECT</code>, <code>STRUCT</code>,
     * or <code>DISTINCT</code>.
     *
     * <P>Only types matching the catalog, schema, type name and type
     * criteria are returned.  They are ordered by <code>DATA_TYPE</code>,
     * <code>TYPE_CAT</code>, <code>TYPE_SCHEM</code>  and
     * <code>TYPE_NAME</code>.  The type name parameter may be a fully-qualified
     * name.  In this case, the catalog and schemaPattern parameters are
     * ignored.
     *
     * <P>Each type description has the following columns:
     *  <OL>
     *	<LI><B>TYPE_CAT</B> String => the type's catalog (may be <code>null</code>)
     *	<LI><B>TYPE_SCHEM</B> String => type's schema (may be <code>null</code>)
     *	<LI><B>TYPE_NAME</B> String => type name
     *  <LI><B>CLASS_NAME</B> String => Java class name
     *	<LI><B>DATA_TYPE</B> int => type value defined in java.sql.Types.
     *     One of JAVA_OBJECT, STRUCT, or DISTINCT
     *	<LI><B>REMARKS</B> String => explanatory comment on the type
     *  <LI><B>BASE_TYPE</B> short => type code of the source type of a
     *     DISTINCT type or the type that implements the user-generated
     *     reference type of the SELF_REFERENCING_COLUMN of a structured
     *     type as defined in java.sql.Types (<code>null</code> if DATA_TYPE is not
     *     DISTINCT or not STRUCT with REFERENCE_GENERATION = USER_DEFINED)
     *  </OL>
     *
     * <P><B>Note:</B> If the driver does not support UDTs, an empty
     * result set is returned.
     *
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema pattern name; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param typeNamePattern a type name pattern; must match the type name
     *        as it is stored in the database; may be a fully qualified name
     * @param types a list of user-defined types (JAVA_OBJECT,
     *        STRUCT, or DISTINCT) to include; <code>null</code> returns all types
     * @return <code>ResultSet</code> object in which each row describes a UDT
     * @exception SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#getSearchStringEscape
     * @since 1.2
     */
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        return new WCResultSet(myConnection, WCStaticMetaData.getUDTs());
    }

    /**
     * Retrieves a description of a table's columns that are automatically
     * updated when any value in a row is updated.  They are
     * unordered.
     *
     * <P>Each column description has the following columns:
     *  <OL>
     *	<LI><B>SCOPE</B> short => is not used
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> int => SQL data type from <code>java.sql.Types</code>
     *	<LI><B>TYPE_NAME</B> String => Data source-dependent type name
     *	<LI><B>COLUMN_SIZE</B> int => precision
     *	<LI><B>BUFFER_LENGTH</B> int => length of column value in bytes
     *	<LI><B>DECIMAL_DIGITS</B> short	 => scale - Null is returned for data types where
     * DECIMAL_DIGITS is not applicable.
     *	<LI><B>PSEUDO_COLUMN</B> short => whether this is pseudo column
     *      like an Oracle ROWID
     *      <UL>
     *      <LI> versionColumnUnknown - may or may not be pseudo column
     *      <LI> versionColumnNotPseudo - is NOT a pseudo column
     *      <LI> versionColumnPseudo - is a pseudo column
     *      </UL>
     *  </OL>
     *
     * <p>The COLUMN_SIZE column represents the specified column size for the given column.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schema a schema name; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param table a table name; must match the table name as it is stored
     *        in the database
     * @return a <code>ResultSet</code> object in which each row is a
     *         column description
     * @exception SQLException if a database access error occurs
     */
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        throw new NotImplemented("getVersionColumns(...)");
    }

    /**
	 * JDBC types:<br />
	 * "TABLE", "VIEW",	"SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".<br />
	 * <br />
	 * Known MySQL Types:<br />
	 * "BASE TABLE", "BASE VIEW" ,"SYSTEM TABLE", "SYSTEM VIEW", "TEMPORARY TABLE", "TEMPORARY", "LOCAL TEMPORARY"
	 *
	 * @param sqlType JDBC Type
	 * @return MySQL Type
	 */
    private String getSQLType(String sqlType) {
        String jdbcType = sqlType;
        if ("TABLE".equalsIgnoreCase(jdbcType)) jdbcType = "BASE TABLE"; else if ("TEMPORARY".equalsIgnoreCase(jdbcType)) jdbcType = "LOCAL TEMPORARY"; else if ("VIEW".equalsIgnoreCase(jdbcType)) jdbcType = "BASE VIEW";
        return jdbcType;
    }

    private String generateOptionalRefContraintsJoin() {
        return ((hasReferentialConstraintsView) ? "LEFT JOIN " + "INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS R " + " ON (B.TABLE_SCHEMA = R.CONSTRAINT_SCHEMA " + "  AND B.TABLE_NAME = R.TABLE_NAME " + "  AND B.CONSTRAINT_NAME = R.CONSTRAINT_NAME) " : "");
    }

    private String generateDeleteRuleClause() {
        return ((hasReferentialConstraintsView) ? "CASE WHEN R.DELETE_RULE='CASCADE' THEN " + String.valueOf(DatabaseMetaData.importedKeyCascade) + " WHEN R.DELETE_RULE='SET NULL' THEN " + String.valueOf(DatabaseMetaData.importedKeySetNull) + " WHEN R.DELETE_RULE='SET DEFAULT' THEN " + String.valueOf(DatabaseMetaData.importedKeySetDefault) + " WHEN R.DELETE_RULE='RESTRICT' THEN " + String.valueOf(DatabaseMetaData.importedKeyRestrict) + " WHEN R.DELETE_RULE='NO ACTION' THEN " + String.valueOf(DatabaseMetaData.importedKeyNoAction) + " ELSE " + String.valueOf(DatabaseMetaData.importedKeyNoAction) + " END " : String.valueOf(DatabaseMetaData.importedKeyRestrict));
    }

    private String generateUpdateRuleClause() {
        return ((hasReferentialConstraintsView) ? "CASE WHEN R.UPDATE_RULE='CASCADE' THEN " + String.valueOf(DatabaseMetaData.importedKeyCascade) + " WHEN R.UPDATE_RULE='SET NULL' THEN " + String.valueOf(DatabaseMetaData.importedKeySetNull) + " WHEN R.UPDATE_RULE='SET DEFAULT' THEN " + String.valueOf(DatabaseMetaData.importedKeySetDefault) + " WHEN R.UPDATE_RULE='RESTRICT' THEN " + String.valueOf(DatabaseMetaData.importedKeyRestrict) + " WHEN R.UPDATE_RULE='NO ACTION' THEN " + String.valueOf(DatabaseMetaData.importedKeyNoAction) + " ELSE " + String.valueOf(DatabaseMetaData.importedKeyNoAction) + " END " : String.valueOf(DatabaseMetaData.importedKeyRestrict));
    }
}
