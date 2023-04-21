package org.dbvalidator.core.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dbvalidator.core.business.Catalog;
import org.dbvalidator.core.business.ValidationException;
import org.dbvalidator.core.business.Validator;

public class ValidatorJdbc extends JdbcObject implements Validator {

    private DatabaseMetaData md;

    private ResultSet allSchemas;

    private ResultSet allTables;

    private ResultSet allCatalogs;

    private ResultSet allColumns;

    public ValidatorJdbc(Connection conn) {
        super(conn);
    }

    private ResultSet getAllSchemas() throws ValidationException {
        if (allSchemas != null) {
            return allSchemas;
        }
        return allSchemas = JdbcUtils.getAllSchemas(getMetaData());
    }

    private ResultSet getAllCatalogs() throws ValidationException {
        return allCatalogs = CatalogJdbc.getAllCatalogs(getMetaData());
    }

    private ResultSet getAllTables() throws ValidationException {
        if (allTables != null) {
            return allTables;
        }
        return allTables = JdbcUtils.getAllTables(getMetaData());
    }

    private ResultSet getAllColumnsInTable(String tableName) throws ValidationException {
        return JdbcUtils.getAllColumnsInCatalogSchemaOrTable(getMetaData(), null, null, tableName);
    }

    private ResultSet getAllColumns() throws ValidationException {
        if (allColumns != null) {
            return allColumns;
        }
        return allColumns = JdbcUtils.getAllColumns(getMetaData());
    }

    public boolean existsTableName(String tableName) throws ValidationException {
        ResultSet tables = getAllTables();
        try {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString(3))) {
                    tables.beforeFirst();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    public boolean existsCatalogName(String catalogName) throws ValidationException {
        ResultSet catalogs = getAllCatalogs();
        try {
            while (catalogs.next()) {
                if (catalogName.equalsIgnoreCase(catalogs.getString(1))) {
                    catalogs.beforeFirst();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    private DatabaseMetaData getMetaData() throws ValidationException {
        if (md != null) {
            return md;
        }
        return md = JdbcUtils.getMetadata(getConnection());
    }

    public boolean existsSchemaName(String schemaName) throws ValidationException {
        ResultSet schemas = getAllSchemas();
        try {
            while (schemas.next()) {
                if (schemaName.equalsIgnoreCase(schemas.getString(1))) {
                    schemas.beforeFirst();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    public boolean existsTableNameInSchema(String tableName, String schemaName) throws ValidationException {
        ResultSet tables = getAllTables();
        try {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString(3))) {
                    String schema = tables.getString(2);
                    if (schema != null && schema.equalsIgnoreCase(schemaName)) {
                        tables.beforeFirst();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    public boolean existsTableNameInCatalog(String tableName, String catalogName) throws ValidationException {
        ResultSet tables = getAllTables();
        try {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString(3))) {
                    String catalog = tables.getString(1);
                    if (catalog != null && catalog.equalsIgnoreCase(catalogName)) {
                        tables.beforeFirst();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    public boolean existsTableNameInCatalogOrSchema(String tableName, String catalogOrSchemaName) throws ValidationException {
        ResultSet tables = getAllTables();
        try {
            while (tables.next()) {
                if (tableName.equalsIgnoreCase(tables.getString(3))) {
                    String catalog = tables.getString(1);
                    String schema = tables.getString(2);
                    if ((catalog != null && catalog.equalsIgnoreCase(catalogOrSchemaName)) || (schema != null && schema.equalsIgnoreCase(catalogOrSchemaName))) {
                        tables.beforeFirst();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    public boolean existsColumnNameInTable(String columnName, String tableName) throws ValidationException {
        ResultSet columns = getAllColumnsInTable(tableName);
        try {
            while (columns.next()) {
                if (columnName.equalsIgnoreCase(columns.getString(4)) && tableName.equalsIgnoreCase(columns.getString(3))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    public boolean existsColumnNameInTableWithSchema(String columnName, String tableName, String schemaName) throws ValidationException {
        throw new ValidationException("Not implemented yet.");
    }

    @Override
    public boolean existsColumnName(String columnName) throws ValidationException {
        ResultSet allColumns = getAllColumns();
        try {
            while (allColumns.next()) {
                if (columnName.equalsIgnoreCase(allColumns.getString(4))) {
                    allColumns.beforeFirst();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new ValidationException(e);
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        JdbcUtils.closeSilent(allColumns);
        JdbcUtils.closeSilent(allTables);
        JdbcUtils.closeSilent(allCatalogs);
        JdbcUtils.closeSilent(allSchemas);
    }

    @Override
    public boolean existsCatalogOrSchemaName(String catalogOrSchemaName) throws ValidationException {
        return existsCatalogName(catalogOrSchemaName) || existsSchemaName(catalogOrSchemaName);
    }

    @Override
    public Catalog getCatalogInstace() throws ValidationException {
        if (catalog != null) {
            return catalog;
        }
        return catalog = new CatalogJdbc(getConnection());
    }

    private CatalogJdbc catalog;
}
