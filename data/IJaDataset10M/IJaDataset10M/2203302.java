package com.manydesigns.portofino.database;

import com.manydesigns.portofino.base.ConnectionProvider;
import com.manydesigns.portofino.base.MDConfig;
import com.manydesigns.portofino.base.MDObject;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public interface DatabaseAbstraction {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    public String getDatabaseProductName();

    public String getDatabaseProductVersion();

    public Integer getDatabaseMajorVersion();

    public Integer getDatabaseMinorVersion();

    public String getDatabaseMajorMinorVersion();

    public String getDriverName();

    public String getDriverVersion();

    public Integer getDriverMajorVersion();

    public Integer getDriverMinorVersion();

    public String getDriverMajorMinorVersion();

    public Integer getJDBCMajorVersion();

    public Integer getJDBCMinorVersion();

    public String getJDBCMajorMinorVersion();

    public String getClassForName();

    public Connection getConnection(String host, int port, String dbName, String login, String password) throws SQLException;

    public String createDataBase(String escapedName) throws SQLException;

    public void createRemoteDb(String host, int port, String initialDb, String name, String login, String password, Locale locale) throws Exception;

    public void setSchemaAfterLoadMDTemplate(Connection conn) throws Exception;

    public void constraintsOff(Connection conn, String schema) throws Exception;

    public void constraintsOn(Connection conn, String schema) throws Exception;

    public String createTable(String schema, String escapedTableName);

    public String dropTable(String schema, String escapedTableName);

    public String addColumn(String schema, String escapedTableName, String escapedColumnName, String attrType);

    public String dropColumn(String schema, String escapedTableName, String escapedColumnName);

    public String setColumnNotNull(String schema, String escapedTableName, String escapedColumnName, String attrType);

    public String setColumnNull(String schema, String escapedTableName, String escapedColumnName, String attrType);

    public String verifyEmptyColumn(String schema, String escapedCName, String escapedColumnName, boolean create, boolean textAttrFlag);

    public void renameColumn(Collection<String> query, String schema, String escapedTableName, String escapedOldName, String newName, String escapedNewName, String typeString, boolean required);

    public String getIntegerType();

    public String getDecimalType();

    public String getBooleanType();

    public String getTextType(int length);

    public String getDateType();

    public String getWfType();

    public String getRelAttrType();

    public String getBlobType();

    public void changeTextAttributeLength(Collection<String> query, String schema, String escapedTableName, String escapedOldTableName, String escapedColumnName, int newLength, int oldLength, boolean oldRequired, Locale locale);

    public String getMaxTextLength(String schema, String escapedTableName, String escapedColumnName);

    public String addPk(String schema, String escapedName);

    public String dropPk(String schema, String escapedName);

    public String addConstraintDeleteCascade(String schema, String escapedTableName, String escapedConstraintName, String escapedTableNameRef);

    public String addConstraintOnRelation(String schema, String relationName, String escapedConstraintName, String nameForeign, String ref, String campoRef);

    public String dropConstraint(String schema, String escapedTableName, String constraint);

    public String dropForeignKey(String schema, String escapedTableName, String constraint);

    public String dropView(String schema, String viewName);

    public String renameTableName(String schema, String oldTableName, String newName, String escapedNewName);

    public String addContrainstUnique(String schema, String escapedTableName, String escapedConstraintName, String nameForeign);

    public String addIndex(String schema, String escapedTableName, String escapedColumnName, String escapedIndexName);

    public String addUniqueIndex(String schema, String escapedTableName, String escapedColumnName, String escapedIndexName);

    public String dropIndex(String schema, String escapedTableName, String indexName);

    public void dropIndexForOneToOneRelationship(Collection<String> queries, String schema, String escapedTableName, int relId);

    public String verifyEmptyColumn(String schema, String escapedTableName, String escapedColumnName);

    public String verifyEmptyTextColumn(String schema, String escapedTableName, String escapedColumnName);

    public int getStringLength(Connection conn, String escapedColumnName) throws Exception;

    public String getDbDataEscape(String name);

    public String createUpAndDownViews(String schema, String escapedNewName, String escapedName, String upOrDown);

    public StringBuffer addSqlSpace(StringBuffer sb);

    public boolean findTableInJdbcMetaData(String schema, String tableName, String[] tabletypes, DatabaseMetaData dbmd) throws SQLException;

    public boolean findColumnTableInJdbcMetaData(String schema, String tableName, String attrName, DatabaseMetaData dbmd) throws SQLException;

    public String countAttributeInhe(String schema, String inhe, String attrCls);

    public StringBuffer caseTextToString(StringBuffer sb, String escapedAttributeName);

    public StringBuffer caseCastIntegerToString(StringBuffer sb, String escapedAttributeName);

    public StringBuffer caseCastDecimalToString(StringBuffer sb, String escapedAttributeName);

    public StringBuffer caseCastDateToString(StringBuffer sb, String escapedAttributeName, String format) throws Exception;

    public StringBuffer caseCastBooleanToString(StringBuffer sb, String escapedAttributeName, String formatTrueValue, String formatFalseValue) throws Exception;

    public void createTableTemp(Collection<String> queries, String schema, String escapedTableNameTemp, String escapedTableName);

    public void dropSchema(Connection conn, String schema) throws Exception;

    public void dropAllViews(Connection conn, String schema) throws Exception;

    public void dropAllConstraints(Connection conn, String schema) throws Exception;

    public void dropAllTables(Connection conn, String schema) throws Exception;

    public void loadMDTemplate(Connection conn, File mdtemplate) throws Exception;

    public void logDML(String file, String schema, MDConfig config) throws Exception;

    public void dropTableTemp(Collection<String> queries, String schema, String escapedTableName);

    public void copyDataFromtTableToOtherTable(Connection connn, String schema, String escapedFromTableName, String escapedToTableName) throws SQLException;

    public void dropTempInstance(Connection conn) throws SQLException;

    public ResultSet getTables(String schema, DatabaseMetaData dbmd) throws SQLException;

    public ResultSet getTable(String schema, String tableName, String[] tabletypes, DatabaseMetaData dbmd) throws SQLException;

    public ResultSet getPrimaryKey(String schema, String tableName, DatabaseMetaData dbmd) throws SQLException;

    public String getExpectedPkName(String tableName) throws SQLException;

    public ResultSet getImportedKeys(String schema, String tableName, DatabaseMetaData dbmd) throws SQLException;

    public String getPkTableSchema();

    public ResultSet getIndexInfo(String schema, String tableName, DatabaseMetaData dbmd) throws SQLException;

    public String getTableSchema();

    public ResultSet getView(String schema, String viewName, DatabaseMetaData dbmd) throws SQLException;

    public int getJavaIntegerSqlType();

    public int getJavaTextSqlType();

    public int getJavaBooleanSqlType();

    public int getJavaDateSqlType();

    public int getJavaDecimalSqlType();

    public Integer getExpectedDecimalPrecision();

    public Integer getExpectedDecimalScale();

    public ResultSet getColumns(String schema, String tableName, DatabaseMetaData dbmd) throws SQLException;

    public ResultSet getColumn(String schema, String tableName, String column, DatabaseMetaData dbmd) throws SQLException;

    public int getExpectedDeferrability();

    public int getExpectedUpdateRule();

    public int getExpectedDeleteRule();

    public boolean getExpectedUnique(boolean oneToOne);

    public StringBuffer dbUpper(String escapedAttribute, boolean like);

    public void addNotInNameAttribute(StringBuffer sb, String escapedAttributeName);

    public void addRelAttributeInName(StringBuffer sb, String escapedAttributeName, String oppositeEscapedAttributeName);

    public int getMaxTableNameLength();

    public int getRangeLength();

    public String verifyUserGroup(String schema1, String schema2, String metaUserGrp, String userGrp);

    public void createOneToOneRelationship(Collection<String> queries1, Collection<String> queries2, String schema, String escapedTableName, String escapedColumnName, int id) throws Exception;

    public void changeRelToOneToOneRel(Collection<String> queries, Collection<String> queries2, Collection<String> queries3, String schema, String escapedTableName, int id, String escapedNewName, String escapedOldTableName) throws Exception;

    public void changeOneToOneRelToRel(Collection<String> queries, Collection<String> queries2, Collection<String> queries3, String schema, String escapedOldTableName, int id, String escapedNewName, String escapedTableName) throws Exception;

    public void changeNameOneToOneRel(Collection<String> queries, Collection<String> queries2, Collection<String> queries3, Collection<String> queries4, String schema, String escapedTableName, String escapedNewTableName, String escapedOldTableName, int id) throws Exception;

    public void deleteEnds(Collection<String> queries, Collection<String> queries2, String schema, String escapedTableName, int id) throws Exception;

    public void checkForeignKey(String schema, String tableName, String fullName, DatabaseMetaData dbmd, String expectedPkTable, String expectedFkName, String expectedPkColumnName, String expectedFkColumnName, int expectedDeferrability, int expectedUpdateRule, int expectedDeleteRule, int expectedFkCounter, Locale locale, Collection<String> errors);

    public void checkIndex(Connection conn, String schema, String fullName, DatabaseMetaData dbmd, String expectedTableName, String expectedColumnName, int expectedIdxCounter, Locale locale, Collection<String> errors, boolean isOneToOne, int attrId);

    public void checkView(Connection conn, String schema, String viewName, DatabaseMetaData dbmd, Locale locale, Collection<String> errors);

    public void checkNotView(String schema, String viewName, DatabaseMetaData dbmd, Locale locale, Collection<String> errors);

    public String getNameForQueryFragment(String name);

    public void testPkName(String msg, String expectedPkName, String pkName, Locale locale) throws Exception;

    public String createTableVersion(String schema, String escapedTableName, String escapedColumnName);

    public void objMarkForDeletion(MDObject obj) throws Exception;

    public void addOperationForCreateOneToOneRelationship(Collection<String> queries, String schema, int id, String escapedAttrName, String escapedTableName);

    public void addOperationForDropOneToOneRelationship(Collection<String> queries, String schema, String escapedTableName, int id);

    public void recursiveDelete(MDObject obj) throws Exception;

    public void recursiveAuxDelete(MDObject obj) throws Exception;

    public String queryInheritanceId(String schema, String inheritanceType);

    public void handleUniqueAndIndexForUpdate(Collection<String> vincoliDaDroppare, Collection<String> vincoliDaCreare, Collection<String> indiciDaDroppare, Collection<String> indiciDaCreare, String schema3, MDObject relAttrObj, String escapedNewName, String escapedClsName, String escapedOldClsName, boolean newOto, boolean oldOto) throws Exception;

    public void dropConstraintForClassNameChange(String schema0, int raId, Collection<String> vincoliDaDroppare, Collection<String> indiciDaDroppare, String escapedOwnerCls, boolean oto);

    public void createConstraintForClassNameChange(String escapedOwnerCls, String schema, int raId, String escapedAttrName, Collection<String> vincoliDaCreare, Collection<String> indiciDaCreare, String escapedOppositeCls, boolean oto);

    @Override
    String toString();

    public ConnectionProvider getConnectionProvider();
}
