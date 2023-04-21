package org.avaje.ebean.server.lib.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.avaje.lib.log.LogFactory;

/**
 * Performs table searches for jdbc meta data. The results of query() are used
 * to build TableInfo objects.
 */
public class TableSearch {

    private static final Logger logger = LogFactory.get(TableSearch.class);

    private final String[] types;

    private String catalog;

    private String schemaPattern;

    private String tableNamePattern;

    private boolean withReferences;

    private DatabaseMetaData metaData;

    private Connection connection;

    private TableInfo tableInfo;

    private ResultSet resultSet;

    public TableSearch(String[] types) {
        this.types = types;
    }

    /**
     * A unique key representing the search pattern for catalog, schema and table name.
     */
    public String getKey() {
        return "c[" + catalog + "]s[" + schemaPattern + "]t[" + tableNamePattern + "]";
    }

    /**
     * Return the catalog pattern.
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * Set the catalog pattern.
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * Return the schema name pattern.
     */
    public String getSchemaPattern() {
        return schemaPattern;
    }

    /**
     * Set the schema name pattern. can be null.
     */
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    /**
     * Return the tableInfo that was built as a result of a query.
     */
    public TableInfo getTableInfo() {
        return tableInfo;
    }

    /**
     * Set the tableInfo that was built as a result of a query.
     */
    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    /**
     * Return the table name pattern.
     */
    public String getTableNamePattern() {
        return tableNamePattern;
    }

    /**
     * Set the table name pattern.
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * Return the table types used in the query.
     */
    public String[] getTypes() {
        return types;
    }

    /**
     * Returns true if exported and imported keys are read when a table meta
     * data is read.
     */
    public boolean isWithReferences() {
        return withReferences;
    }

    /**
     * Set this to true to fetch exported and imported keys when a table meta
     * data is read. Turn this to false if you want to stop that behaviour.
     */
    public void setWithReferences(boolean withReferences) {
        this.withReferences = withReferences;
    }

    /**
     * Convert the patterns to upper case. Used to determine the case of the
     * dictionary.
     */
    public TableSearch setToUpperCase() {
        if (catalog != null) {
            catalog = catalog.toUpperCase();
        }
        if (schemaPattern != null) {
            schemaPattern = schemaPattern.toUpperCase();
        }
        if (tableNamePattern != null) {
            tableNamePattern = tableNamePattern.toUpperCase();
        }
        return this;
    }

    /**
     * Convert the patterns to lower case. Used to determine the case of the
     * dictionary.
     */
    public TableSearch setToLowerCase() {
        if (catalog != null) {
            catalog = catalog.toLowerCase();
        }
        if (schemaPattern != null) {
            schemaPattern = schemaPattern.toLowerCase();
        }
        if (tableNamePattern != null) {
            tableNamePattern = tableNamePattern.toLowerCase();
        }
        return this;
    }

    /**
     * Return the DatabaseMetaData associated with the current connection.
     */
    public DatabaseMetaData getMetaData() {
        return metaData;
    }

    /**
     * Return the current connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Set a new Connection.
     */
    public void setConnection(Connection conn) throws SQLException {
        if (connection != null) {
            throw new SQLException("Already has a connection set?");
        }
        this.connection = conn;
        this.metaData = conn.getMetaData();
    }

    /**
     * Return the current ResultSet that was created by the last query().
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
     * Execute a query based on the patterns that have been set.
     */
    public ResultSet query() throws SQLException {
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        if (metaData == null) {
            throw new NullPointerException("metaData has not been set?");
        }
        resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        return resultSet;
    }

    /**
     * Close the ResultSet resource from the last query run.
     */
    public void closeQuery() {
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Close any open resources. Closes ResultSet and Connection properly.
     */
    public void closeAll() {
        if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            connection = null;
        }
    }

    /**
     * Return some summary information.
     */
    public String toString() {
        return "[" + catalog + "][" + schemaPattern + "][" + tableNamePattern + "][" + withReferences + "]";
    }
}
