package net.sourceforge.greenvine.generator.helper.dbextractor.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.greenvine.database.Column;
import net.sourceforge.greenvine.database.ColumnConstraint;
import net.sourceforge.greenvine.database.ColumnRef;
import net.sourceforge.greenvine.database.Database;
import net.sourceforge.greenvine.database.ForeignKey;
import net.sourceforge.greenvine.database.PrimaryKey;
import net.sourceforge.greenvine.database.Table;
import net.sourceforge.greenvine.database.UniqueKey;
import net.sourceforge.greenvine.database.View;
import net.sourceforge.greenvine.database.propertytypes.types.ColumnDatatypeType;
import net.sourceforge.greenvine.dbextractor.config.DatabaseExtractorConfig;
import net.sourceforge.greenvine.dbextractor.config.IncludeObjectsType;
import net.sourceforge.greenvine.generator.helper.dbextractor.DatabaseExtractor;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class JdbcDatabaseExtractorImpl implements DatabaseExtractor {

    private static final String TABLE_NAME = "TABLE_NAME";

    private static final String TABLE_SCHEMA = "TABLE_SCHEM";

    private final DatabaseExtractorConfig config;

    private final File outputFile;

    public JdbcDatabaseExtractorImpl(DatabaseExtractorConfig config, File outputFile) {
        this.config = config;
        this.outputFile = outputFile;
    }

    public Database extractDatabase() throws ClassNotFoundException, SQLException, MarshalException, ValidationException, IOException {
        Connection con = getJDBCConnection();
        Database db = getDatabase(con);
        marshalDatabase(db);
        return db;
    }

    /**
	 * Create a JDBC {@link Connection}
	 * object based on the configuration 
	 * parameters
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    private Connection getJDBCConnection() throws ClassNotFoundException, SQLException {
        Class.forName(config.getConnection().getJdbcDriver());
        return DriverManager.getConnection(config.getConnection().getJdbcUrl(), config.getConnection().getUsername(), config.getConnection().getPassword());
    }

    /** 
	 * Get a {@link Database} object
	 * representing the connection supplied
	 * using the {@link DatabaseMetaData}.
	 * Iterates through tables and views
	 * included in scope and creates a
	 * {@link Table} object for each.
	 * @param con
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
    private Database getDatabase(Connection con) throws SQLException {
        Database db = new Database();
        db.setName(config.getScope().getDatabaseName());
        DatabaseMetaData dbmd = con.getMetaData();
        extractTables(db, dbmd);
        extractViews(db, dbmd);
        return db;
    }

    private void extractViews(Database db, DatabaseMetaData dbmd) throws SQLException {
        ResultSet views = dbmd.getTables(config.getScope().getDatabaseName(), config.getScope().getSchemaName(), null, new String[] { "VIEW" });
        Collection<String> includedNames = getIncludedNames(config.getScope().getIncludeViews());
        Collection<String> excludedNames = getExcludedNames(config.getScope().getIncludeViews());
        if (config.getScope().getIncludeViews().getIncludeAll() != null) {
            while (views.next()) {
                extractViewData(db, dbmd, views);
            }
        } else if (config.getScope().getIncludeViews().getIncludeOnly() != null) {
            while (views.next()) {
                String name = views.getString(TABLE_NAME);
                if (includedNames.contains(name)) {
                    extractViewData(db, dbmd, views);
                }
            }
        } else if (config.getScope().getIncludeViews().getExcludeOnly() != null) {
            while (views.next()) {
                String name = views.getString(TABLE_NAME);
                if (!excludedNames.contains(name)) {
                    extractViewData(db, dbmd, views);
                }
            }
        } else {
        }
    }

    private void extractTables(Database db, DatabaseMetaData dbmd) throws SQLException {
        ResultSet tables = dbmd.getTables(config.getScope().getDatabaseName(), config.getScope().getSchemaName(), null, new String[] { "TABLE" });
        Collection<String> includedNames = getIncludedNames(config.getScope().getIncludeTables());
        Collection<String> excludedNames = getExcludedNames(config.getScope().getIncludeTables());
        if (config.getScope().getIncludeTables().getIncludeAll() != null) {
            while (tables.next()) {
                extractTableData(db, dbmd, tables);
            }
        } else if (config.getScope().getIncludeTables().getIncludeOnly() != null) {
            while (tables.next()) {
                String name = tables.getString(TABLE_NAME);
                if (includedNames.contains(name)) {
                    extractTableData(db, dbmd, tables);
                }
            }
        } else if (config.getScope().getIncludeTables().getExcludeOnly() != null) {
            while (tables.next()) {
                String name = tables.getString(TABLE_NAME);
                if (!excludedNames.contains(name)) {
                    extractTableData(db, dbmd, tables);
                }
            }
        } else {
        }
    }

    private Collection<String> getIncludedNames(IncludeObjectsType includeObjects) {
        Collection<String> included = new ArrayList<String>();
        if (includeObjects.getIncludeOnly() != null) {
            for (String includeObject : includeObjects.getIncludeOnly().getName()) {
                included.add(includeObject);
            }
        }
        return included;
    }

    private Collection<String> getExcludedNames(IncludeObjectsType excludedObjects) {
        Collection<String> excluded = new ArrayList<String>();
        if (excludedObjects.getExcludeOnly() != null) {
            for (String includeObject : excludedObjects.getExcludeOnly().getName()) {
                excluded.add(includeObject);
            }
        }
        return excluded;
    }

    /**
	 * Extract a {@link Table} object from
	 * the {@link DatabaseMetaData}
	 * @param db
	 * @param dbmd
	 * @param tables
	 * @throws SQLException
	 */
    private void extractTableData(Database db, DatabaseMetaData dbmd, ResultSet tables) throws SQLException {
        String tableName = tables.getString(TABLE_NAME);
        String schemaName = tables.getString(TABLE_SCHEMA);
        Table table = new Table();
        String name = tableName;
        if (schemaName != null && schemaName.length() > 0) {
            name = schemaName + "." + tableName;
        }
        table.setName(name);
        db.addTable(table);
        ResultSet columns = dbmd.getColumns(null, schemaName, tableName, null);
        createColumns(table, columns);
        ResultSet primaryKeys = dbmd.getPrimaryKeys(null, schemaName, tableName);
        createPrimaryKey(table, primaryKeys);
        ResultSet importedKeys = dbmd.getImportedKeys(null, schemaName, tableName);
        createForeignKeys(table, importedKeys);
        ResultSet uniques = dbmd.getIndexInfo(null, schemaName, tableName, true, false);
        createUniqueKeys(table, uniques);
    }

    /**
	 * Extract a {@link View} object from
	 * the {@link DatabaseMetaData}
	 * @param db
	 * @param dbmd
	 * @param view
	 * @throws SQLException
	 */
    private void extractViewData(Database db, DatabaseMetaData dbmd, ResultSet views) throws SQLException {
        String viewName = views.getString(TABLE_NAME);
        String schemaName = views.getString(TABLE_SCHEMA);
        View view = new View();
        String name = viewName;
        if (schemaName != null && schemaName.length() > 0) {
            name = schemaName + "." + viewName;
        }
        view.setName(name);
        db.addView(view);
        ResultSet columns = dbmd.getColumns(null, schemaName, viewName, null);
        createColumns(view, columns);
    }

    private void createForeignKeys(Table table, ResultSet importedKeys) throws SQLException {
        Map<String, ForeignKey> foreignKeys = new HashMap<String, ForeignKey>();
        while (importedKeys.next()) {
            String fkName = importedKeys.getString("FK_NAME");
            if (fkName != null) {
                ForeignKey foreignKey = foreignKeys.get(fkName);
                if (foreignKey == null) {
                    ForeignKey constraint = createForeignKey(importedKeys);
                    foreignKeys.put(fkName, constraint);
                } else {
                    ColumnConstraint colCon = createColumnConstraint(importedKeys);
                    foreignKey.addColumnConstraint(colCon);
                }
            }
        }
        for (ForeignKey foreignKey : foreignKeys.values()) {
            table.addForeignKey(foreignKey);
        }
    }

    private ForeignKey createForeignKey(ResultSet importedKeys) throws SQLException {
        String fkName = importedKeys.getString("FK_NAME");
        String pkTableName = importedKeys.getString("PKTABLE_NAME");
        String pkSchemaName = importedKeys.getString("PKTABLE_SCHEM");
        String fkTableName = importedKeys.getString("FKTABLE_NAME");
        String fkSchemaName = importedKeys.getString("FKTABLE_SCHEM");
        String fqPkTableName = pkTableName;
        if (pkSchemaName != null && pkSchemaName.length() > 0) fqPkTableName = pkSchemaName + "." + pkTableName;
        String fqFkTableName = fkTableName;
        if (fkSchemaName != null && fkSchemaName.length() > 0) fqFkTableName = fkSchemaName + "." + fkTableName;
        ForeignKey constraint = new ForeignKey();
        constraint.setName(fkName);
        constraint.setReferencingTable(fqFkTableName);
        constraint.setReferencedTable(fqPkTableName);
        ColumnConstraint colConstraint = createColumnConstraint(importedKeys);
        constraint.addColumnConstraint(colConstraint);
        return constraint;
    }

    private ColumnConstraint createColumnConstraint(ResultSet importedKeys) throws SQLException {
        String pkColumnName = importedKeys.getString("PKCOLUMN_NAME");
        String fkColumnName = importedKeys.getString("FKCOLUMN_NAME");
        ColumnConstraint colConstraint = new ColumnConstraint();
        colConstraint.setReferencingColumn(fkColumnName);
        colConstraint.setReferencedColumn(pkColumnName);
        return colConstraint;
    }

    private void createColumns(Table table, ResultSet columns) throws SQLException {
        while (columns.next()) {
            Column col = createColumn(columns);
            table.addColumn(col);
        }
    }

    private void createColumns(View view, ResultSet columns) throws SQLException {
        while (columns.next()) {
            Column col = createColumn(columns);
            view.addColumn(col);
        }
    }

    private void createUniqueKeys(Table table, ResultSet uniques) throws SQLException {
        Map<String, UniqueKey> uniqueKeys = new HashMap<String, UniqueKey>();
        while (uniques.next()) {
            String indexName = uniques.getString("INDEX_NAME");
            String columnName = uniques.getString("COLUMN_NAME");
            if (indexName != null) {
                UniqueKey uniqueKey = uniqueKeys.get(indexName);
                if (uniqueKey == null) {
                    uniqueKey = new UniqueKey();
                    uniqueKey.setName(indexName);
                    ColumnRef col = new ColumnRef();
                    col.setName(columnName);
                    uniqueKey.addColumnRef(col);
                    uniqueKeys.put(indexName, uniqueKey);
                } else {
                    ColumnRef col = new ColumnRef();
                    col.setName(columnName);
                    uniqueKey.addColumnRef(col);
                }
            }
        }
        for (UniqueKey uniqueKey : uniqueKeys.values()) {
            if (!uniqueKeyIsPrimary(table.getPrimaryKey(), uniqueKey)) {
                table.addUniqueKey(uniqueKey);
            }
        }
    }

    private void createPrimaryKey(Table table, ResultSet primaryKeys) throws SQLException {
        ArrayList<String> primaryKeyColumnNames = new ArrayList<String>();
        String pkName = "PK_" + table.getName().toUpperCase();
        while (primaryKeys.next()) {
            pkName = primaryKeys.getString("PK_NAME");
            String columnName = primaryKeys.getString("COLUMN_NAME");
            primaryKeyColumnNames.add(columnName);
        }
        PrimaryKey primaryKey = new PrimaryKey();
        primaryKey.setName(pkName);
        for (String colName : primaryKeyColumnNames) {
            ColumnRef pkColRef = new ColumnRef();
            pkColRef.setName(colName);
            primaryKey.addColumnRef(pkColRef);
        }
        table.setPrimaryKey(primaryKey);
    }

    private Column createColumn(ResultSet columns) throws SQLException {
        String columnName = columns.getString("COLUMN_NAME");
        int columnType = columns.getInt("DATA_TYPE");
        boolean notNull = true;
        int nullability = columns.getInt("NULLABLE");
        if (nullability == DatabaseMetaData.columnNullable) {
            notNull = false;
        }
        int columnSize = columns.getInt("COLUMN_SIZE");
        int decimalDigits = columns.getInt("DECIMAL_DIGITS");
        Column col = new Column();
        col.setName(columnName);
        col.setType(getColumnDataTypeFromSQLType(columnType));
        col.setNotNull(notNull);
        col.setScale((short) columnSize);
        col.setPrecision((short) decimalDigits);
        return col;
    }

    private boolean uniqueKeyIsPrimary(PrimaryKey primaryKey, UniqueKey uniqueKey) {
        if (primaryKey == null) {
            return false;
        }
        if (primaryKey.getColumnRefCount() != uniqueKey.getColumnRefCount()) {
            return false;
        } else {
            List<String> primaryKeyColumns = getPrimaryKeyColumnNames(primaryKey);
            List<String> uniquekKeyColumns = getUniqueKeyColumnNames(uniqueKey);
            if (primaryKeyColumns.containsAll(uniquekKeyColumns)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getUniqueKeyColumnNames(UniqueKey uniqueKey) {
        List<String> colNames = new ArrayList<String>();
        for (ColumnRef colRef : uniqueKey.getColumnRef()) {
            colNames.add(colRef.getName());
        }
        return colNames;
    }

    private List<String> getPrimaryKeyColumnNames(PrimaryKey primaryKey) {
        List<String> colNames = new ArrayList<String>();
        for (ColumnRef colRef : primaryKey.getColumnRef()) {
            colNames.add(colRef.getName());
        }
        return colNames;
    }

    /**
	 * Write out the database model
	 * to a file using Castor.
	 * @param db
	 * @throws IOException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
    private void marshalDatabase(Database db) throws IOException, MarshalException, ValidationException {
        Writer out = new FileWriter(this.outputFile);
        db.marshal(out);
        out.flush();
        out.close();
    }

    /**
	 * Maps a database ColumDatatypeType to a java.sql.Types
	 * data type
	 * @param sqlType
	 * @return
	 */
    private ColumnDatatypeType getColumnDataTypeFromSQLType(int sqlType) {
        ColumnDatatypeType colType;
        switch(sqlType) {
            case Types.BIGINT:
                colType = ColumnDatatypeType.INTEGER;
                break;
            case Types.BINARY:
                colType = ColumnDatatypeType.CHARACTER;
                break;
            case Types.BIT:
                colType = ColumnDatatypeType.BIT;
                break;
            case Types.BLOB:
                colType = ColumnDatatypeType.BLOB;
                break;
            case Types.BOOLEAN:
                colType = ColumnDatatypeType.BOOLEAN;
                break;
            case Types.CHAR:
                colType = ColumnDatatypeType.CHARACTER;
                break;
            case Types.CLOB:
                colType = ColumnDatatypeType.CLOB;
                break;
            case Types.DATE:
                colType = ColumnDatatypeType.DATE;
                break;
            case Types.DECIMAL:
                colType = ColumnDatatypeType.DECIMAL;
                break;
            case Types.DOUBLE:
                colType = ColumnDatatypeType.DOUBLE;
                break;
            case Types.FLOAT:
                colType = ColumnDatatypeType.FLOAT;
                break;
            case Types.INTEGER:
                colType = ColumnDatatypeType.INTEGER;
                break;
            case Types.LONGVARBINARY:
                colType = ColumnDatatypeType.LONGVARBINARY;
                break;
            case Types.LONGVARCHAR:
                colType = ColumnDatatypeType.LONGVARCHAR;
                break;
            case Types.NUMERIC:
                colType = ColumnDatatypeType.NUMERIC;
                break;
            case Types.REAL:
                colType = ColumnDatatypeType.REAL;
                break;
            case Types.SMALLINT:
                colType = ColumnDatatypeType.SMALLINT;
                break;
            case Types.TIME:
                colType = ColumnDatatypeType.TIME;
                break;
            case Types.TIMESTAMP:
                colType = ColumnDatatypeType.TIMESTAMP;
                break;
            case Types.TINYINT:
                colType = ColumnDatatypeType.TINYINT;
                break;
            case Types.VARBINARY:
                colType = ColumnDatatypeType.VARBINARY;
                break;
            case Types.VARCHAR:
                colType = ColumnDatatypeType.VARCHAR;
                break;
            default:
                throw new RuntimeException("Unrecognised type");
        }
        return colType;
    }
}
