package com.vertigrated.db;

import com.sun.rowset.CachedRowSetImpl;
import com.vertigrated.text.StringUtil;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for all the vendor specific Database implementations.
 */
public abstract class AbstractDatabase implements Database {

    private static final List<String> TABLE_TYPE;

    private static final List<String> VIEW_TYPE;

    protected boolean debug = true;

    private SQLExceptionHandler seh;

    private final DataSource dataSource;

    private final String defaultSchema;

    static {
        TABLE_TYPE = new ArrayList<String>(1);
        TABLE_TYPE.add("TABLE");
        VIEW_TYPE = new ArrayList<String>(1);
        VIEW_TYPE.add("VIEW");
    }

    protected AbstractDatabase(final DataSource dataSource, final String defaultSchema) {
        this.dataSource = dataSource;
        this.defaultSchema = defaultSchema;
        this.seh = new SQLExceptionHandler(this);
    }

    /**
     * This utility methond get a list of all the Catalogs in the database;
     *
     * @return list of catalogs
     */
    public List<String> getCatalogs() {
        final List<String> catalogs = new ArrayList<String>();
        final Connection cn = this.getConnection();
        try {
            final DatabaseMetaData dmd = cn.getMetaData();
            final ResultSet rs = dmd.getCatalogs();
            while (rs.next()) {
                catalogs.add(rs.getString("TABLE_CAT"));
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            this.seh.handleException(e);
        }
        return catalogs;
    }

    protected DataSource getDataSource() {
        return this.dataSource;
    }

    public Connection getConnection() {
        final DataSource ds = this.getDataSource();
        Connection cn = null;
        try {
            cn = ds.getConnection();
            cn.setAutoCommit(true);
        } catch (SQLException e) {
            this.seh.handleException(e);
        }
        return cn;
    }

    public String getDefaultSchema() {
        return this.defaultSchema;
    }

    /**
     * This utility opertation gets a list of all the schemas available in the database
     *
     * @return list of schemas
     */
    public List<Schema> getSchemas() {
        final List<Schema> schemas = new ArrayList<Schema>();
        final Connection cn = this.getConnection();
        ResultSet rs = null;
        try {
            final DatabaseMetaData dmd = cn.getMetaData();
            rs = dmd.getSchemas();
            String catalog;
            String schemaName;
            Schema schema;
            while (rs.next()) {
                catalog = rs.getString("TABLE_CATALOG");
                schemaName = rs.getString("TABLE_SCHEM");
                schema = new Schema(catalog, schemaName, this.getTables(schemaName));
                schemas.add(schema);
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            this.seh.handleException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                this.seh.handleException(e);
            }
        }
        return schemas;
    }

    /**
     * This utility function returns all the tables and views for a given schema
     *
     * @param schema schema
     * @return list of user tables
     */
    public List<Table> getTables(final String schema) {
        final List<Table> tables = new ArrayList<Table>();
        final Connection cn = this.getConnection();
        ResultSet rs = null;
        try {
            final DatabaseMetaData dmd = cn.getMetaData();
            rs = dmd.getTables(null, schema, null, StringUtil.toArray(AbstractDatabase.TABLE_TYPE));
            Table table;
            String tableName;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                table = new Table(schema, tableName, this.getColumns(schema, tableName));
                tables.add(table);
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            this.seh.handleException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                this.seh.handleException(e);
            }
        }
        return tables;
    }

    public List<Table> getViews(final String schema) {
        final List<Table> views = new ArrayList<Table>();
        final Connection cn = this.getConnection();
        try {
            final DatabaseMetaData dmd = cn.getMetaData();
            final ResultSet rs = dmd.getTables(null, schema, null, StringUtil.toArray(AbstractDatabase.VIEW_TYPE));
            Table table;
            String tableName;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                table = new Table(schema, tableName, this.getColumns(schema, tableName));
                views.add(table);
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            this.seh.handleException(e);
        }
        return views;
    }

    /**
     * This utility operation returns all the columns of a given table and some useful details about each column.
     *
     * @param schema    schema
     * @param tableName table name
     * @return list of column names
     */
    public List<Column> getColumns(final String schema, final String tableName) {
        final List<Column> columns = new ArrayList<Column>();
        final Connection cn = this.getConnection();
        ResultSet rs = null;
        try {
            final DatabaseMetaData dmd = cn.getMetaData();
            rs = dmd.getColumns(null, schema, tableName, null);
            Column column;
            while (rs.next()) {
                column = new Column(rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), rs.getInt("DATA_TYPE"), rs.getString("TYPE_NAME"), rs.getInt("COLUMN_SIZE"), rs.getString("IS_NULLABLE"));
                columns.add(column);
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            this.seh.handleException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                this.seh.handleException(e);
            }
        }
        return columns;
    }

    public int execute(final SQLStatement sql) {
        final SQLExceptionHandler h = new SQLExceptionHandler(this);
        return this.execute(sql, h);
    }

    public int execute(final SQLStatement sql, final SQLExceptionHandler seh) {
        int executeResult = -1;
        Connection cn = null;
        Statement stmt = null;
        try {
            cn = this.getConnection();
            cn.setAutoCommit(true);
            stmt = cn.createStatement();
            executeResult = stmt.executeUpdate(sql.getCommand());
        } catch (SQLException e) {
            this.seh.handleException(sql, e);
            executeResult = -1;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                this.seh.handleException(e);
            }
        }
        return executeResult;
    }

    public List<String> executeBatch(final List<SQLStatement> sql) {
        return this.executeBatch(sql, false);
    }

    public List<String> executeBatch(final List<SQLStatement> sql, final boolean allowFailures) {
        return this.executeBatch(sql, allowFailures, new SQLExceptionHandler(this));
    }

    public List<String> executeBatch(final List<SQLStatement> sql, final SQLExceptionHandler seh) {
        return this.executeBatch(sql, true, seh);
    }

    public List<String> executeBatch(final List<SQLStatement> sql, final boolean allowFailures, final SQLExceptionHandler seh) {
        final List<String> executeResult = new ArrayList<String>(sql.size());
        final List<Integer> batchResults = new ArrayList<Integer>(sql.size());
        Connection cn = null;
        Statement stmt = null;
        SQLStatement currentSQL = null;
        try {
            cn = this.getConnection();
            cn.setAutoCommit(false);
            stmt = cn.createStatement();
            for (final SQLStatement aSql : sql) {
                currentSQL = aSql;
                stmt.addBatch(currentSQL.getCommand());
            }
            for (final int i : stmt.executeBatch()) {
                batchResults.add(i);
            }
            cn.commit();
        } catch (final BatchUpdateException bue) {
            System.out.println("Batch, exception caught: " + bue.toString());
            for (final int i : bue.getUpdateCounts()) {
                batchResults.add(i);
            }
            batchResults.add(Statement.EXECUTE_FAILED);
            try {
                if (allowFailures) {
                    cn.commit();
                    executeResult.addAll(this.proccessExecuteResults(batchResults));
                    this.pruneBatchBasedOnResults(sql, batchResults);
                    this.executeBatch(sql, allowFailures, seh);
                } else {
                    if (batchResults.size() != sql.size()) {
                        cn.rollback();
                    } else if (this.countBatchFailures(batchResults) > 0) {
                        cn.rollback();
                    } else {
                        cn.commit();
                    }
                }
            } catch (final SQLException se) {
                seh.handleException(se);
            }
        } catch (final SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException e1) {
                seh.handleException(currentSQL, e1);
            }
            seh.handleException(currentSQL, e);
        } finally {
            executeResult.addAll(this.proccessExecuteResults(batchResults));
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (final SQLException e) {
                this.seh.handleException(e);
            }
        }
        return executeResult;
    }

    private List<String> proccessExecuteResults(final List<Integer> batchResults) {
        final List<String> executeResult = new ArrayList<String>(batchResults.size());
        String resultString;
        for (int batchResult : batchResults) {
            if (batchResult == Statement.SUCCESS_NO_INFO) {
                resultString = "SUCCESS_NO_INFO";
            } else if (batchResult == Statement.EXECUTE_FAILED) {
                resultString = "EXECUTE_FAILED";
            } else {
                resultString = batchResult + " RECORDS AFFECTED";
            }
            executeResult.add(resultString);
        }
        return executeResult;
    }

    private void pruneBatchBasedOnResults(final List<SQLStatement> ssl, final List<Integer> batchResults) {
        for (int batchResult : batchResults) {
            if (batchResult == 1 || batchResult == Statement.SUCCESS_NO_INFO || batchResult == Statement.EXECUTE_FAILED) {
                ssl.remove(0);
            }
        }
    }

    private int countBatchFailures(final List<Integer> executeResults) {
        int failureCount = 0;
        for (int executeResult : executeResults) {
            if (executeResult == Statement.EXECUTE_FAILED) {
                failureCount++;
            }
        }
        return failureCount;
    }

    public RowSet executeSelect(final SelectBuilder sql) {
        ResultSet rs = null;
        Statement stmt;
        CachedRowSet crs = null;
        Connection cn = null;
        try {
            crs = new CachedRowSetImpl();
            cn = this.getConnection();
            stmt = cn.createStatement();
            if (sql.getMaxRows() > AbstractSelectBuilder.ALL_ROWS) {
                stmt.setMaxRows(sql.getMaxRows());
            }
            rs = stmt.executeQuery(sql.getCommand());
            crs.populate(rs);
        } catch (SQLException e) {
            this.seh.handleException(sql, e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return crs;
    }
}
