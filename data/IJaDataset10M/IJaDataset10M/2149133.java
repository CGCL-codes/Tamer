package prisms.osql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import prisms.util.DualKey;

/**
 * Represents an objectified database connection, by which inserts, selects, updates and deletes may
 * be done on this Connection's SQL connection
 */
public class Connection {

    private static final Logger log = Logger.getLogger(Connection.class);

    private final java.sql.Connection theSqlConn;

    private final prisms.arch.ds.Transactor<PrismsSqlException> theTransactor;

    private String[] theSchemas;

    private String[][] theSchemaTables;

    private java.util.HashMap<DualKey<String, String>, BaseTable> theTables;

    /**
	 * Wraps a JDBC SQL connection in a PRISMS OSQL connection
	 * 
	 * @param sqlConn The JDBC SQL connection to wrap
	 */
    public Connection(java.sql.Connection sqlConn) {
        theSqlConn = sqlConn;
        theTransactor = null;
        theTables = new java.util.HashMap<DualKey<String, String>, BaseTable>();
    }

    /**
	 * Wraps the JDBC SQL connection managed by a connection factory in a PRISMS OSQL connection
	 * 
	 * @param factory The connection factory to get the transactor from
	 * @param connEl The configuration for the connection
	 */
    public Connection(prisms.arch.ConnectionFactory factory, prisms.arch.PrismsConfig connEl) {
        theTransactor = factory.getConnection(connEl, null, new prisms.arch.ds.Transactor.Thrower<PrismsSqlException>() {

            public void error(String message) throws PrismsSqlException {
                throw new PrismsSqlException(message);
            }

            public void error(String message, Throwable cause) throws PrismsSqlException {
                throw new PrismsSqlException(message, cause);
            }
        });
        theSqlConn = null;
        theTables = new java.util.HashMap<DualKey<String, String>, BaseTable>();
    }

    /**
	 * @return The JDBC SQL connection that this object wraps
	 * @throws PrismsSqlException If the connection cannot be made
	 */
    public java.sql.Connection getSqlConnection() throws PrismsSqlException {
        if (theSqlConn != null) return theSqlConn; else return theTransactor.getConnection();
    }

    /**
	 * @return The brand or type of this connection
	 * @throws PrismsSqlException If an error occurs getting the connection to examine
	 */
    public prisms.util.DBUtils.ConnType getType() throws PrismsSqlException {
        return prisms.util.DBUtils.getType(getSqlConnection());
    }

    /**
	 * @return All schema names accessible to this connection
	 * @throws PrismsSqlException If an error occurs retrieving the data
	 */
    public String[] getSchemas() throws PrismsSqlException {
        if (theSchemas == null) {
            java.sql.ResultSet rs = null;
            try {
                DatabaseMetaData meta = getSqlConnection().getMetaData();
                rs = meta.getSchemas();
                java.util.ArrayList<String> ret = new java.util.ArrayList<String>();
                while (rs.next()) ret.add(rs.getString("TABLE_SCHEM"));
                theSchemas = ret.toArray(new String[ret.size()]);
                theSchemaTables = new String[theSchemas.length][];
            } catch (java.sql.SQLException e) {
                throw new PrismsSqlException("Could not query database schemas", e);
            } finally {
                if (rs != null) try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    System.err.println("Connection: Connection error");
                }
            }
        }
        return theSchemas;
    }

    private synchronized String[] getSchemaTableNames(String schema) throws PrismsSqlException {
        if (theSchemas == null) getSchemas();
        int schemaIdx = -1;
        for (int s = 0; s < theSchemas.length; s++) if (theSchemas[s].equalsIgnoreCase(schema)) {
            schemaIdx = s;
            break;
        }
        if (schemaIdx < 0) throw new PrismsSqlException("No such schema \"" + schema + "\"");
        if (theSchemaTables[schemaIdx] != null) return theSchemaTables[schemaIdx];
        java.sql.ResultSet rs = null;
        java.util.ArrayList<String> tableNames = new java.util.ArrayList<String>();
        try {
            DatabaseMetaData meta = getSqlConnection().getMetaData();
            rs = meta.getTables(null, schema, null, new String[] { "TABLE" });
            while (rs.next()) tableNames.add(rs.getString("TABLE_NAME"));
        } catch (SQLException e) {
            throw new PrismsSqlException("Could not query tables for schema " + schema, e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (java.sql.SQLException e) {
                log.error("Connection error");
            }
        }
        theSchemaTables[schemaIdx] = tableNames.toArray(new String[tableNames.size()]);
        return theSchemaTables[schemaIdx];
    }

    /**
	 * Gets a table by name
	 * 
	 * @param schema The name of the schema to get the table from
	 * @param name The name of the table to get
	 * @return The table with the given name in the given schema available to this connection, or
	 *         null if the table does not exist
	 * @throws PrismsSqlException If the table could not be parsed from the database
	 */
    public synchronized BaseTable getTable(String schema, String name) throws PrismsSqlException {
        DualKey<String, String> key = new DualKey<String, String>(schema, name);
        BaseTable ret = theTables.get(key);
        if (ret != null) return ret;
        String[] tableNames = getSchemaTableNames(schema);
        int tableIdx = -1;
        for (int t = 0; t < tableNames.length; t++) if (tableNames[t].equalsIgnoreCase(name)) {
            tableIdx = t;
            break;
        }
        if (tableIdx < 0) return null;
        ret = new BaseTable(this, theSchemas[prisms.util.ArrayUtils.indexOf(theSchemaTables, tableNames)], tableNames[tableIdx]);
        theTables.put(key, ret);
        return ret;
    }

    /**
	 * Gets all tables in the given schema in the database
	 * 
	 * @param schema The schema to get the tables for
	 * @return All tables in the given schema
	 * @throws PrismsSqlException If the tables could not be parsed from the database
	 */
    public synchronized BaseTable[] getTables(String schema) throws PrismsSqlException {
        String[] tableNames = getSchemaTableNames(schema);
        BaseTable[] ret = new BaseTable[tableNames.length];
        for (int t = 0; t < ret.length; t++) ret[t] = getTable(schema, tableNames[t]);
        return ret;
    }

    private java.sql.Statement getStatement() throws PrismsSqlException {
        try {
            return getSqlConnection().createStatement();
        } catch (SQLException e) {
            throw new PrismsSqlException("Could not create statement", e);
        }
    }

    private int execute(String sql) throws PrismsSqlException {
        java.sql.Statement stmt = getStatement();
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PrismsSqlException("Could not execute database call: " + sql, e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("Connection error", e);
            }
        }
    }

    private java.sql.PreparedStatement executePrep(String sql, ValueSet[] values, WhereExpression where) throws PrismsSqlException {
        java.sql.PreparedStatement pStmt;
        try {
            pStmt = getSqlConnection().prepareStatement(sql.toString());
        } catch (SQLException e) {
            throw new PrismsSqlException("Could not prepare database call: " + sql, e);
        }
        int paramIdx = 0;
        boolean error = false;
        if (values != null) try {
            error = true;
            for (ValueSet vs : values) for (prisms.osql.ValueSet.ColumnValue cv : vs) {
                Column<Object> c = (Column<Object>) cv.getColumn();
                if (!c.getDataType().isStringable(cv.getValue(), c)) c.getDataType().setParam(cv.getValue(), pStmt, paramIdx++, c);
            }
            error = false;
        } catch (SQLException e) {
            throw new PrismsSqlException("Could not set parameters in prepared database call: " + sql, e);
        } finally {
            if (error) try {
                pStmt.close();
            } catch (SQLException e) {
                log.error("Connection error", e);
            }
        }
        if (where != null) try {
            error = true;
            paramIdx = where.fillPrepared(pStmt, paramIdx);
            error = false;
        } finally {
            if (error) try {
                pStmt.close();
            } catch (SQLException e) {
                log.error("Connection error", e);
            }
        }
        return pStmt;
    }

    /**
	 * Inserts data into a table in the database
	 * 
	 * @param table The table to insert into
	 * @param values The data to insert, one value set for each new row to create
	 * @return The number of rows inserted
	 * @throws PrismsSqlException If an error occurs inserting the data
	 */
    public int insert(BaseTable table, ValueSet... values) throws PrismsSqlException {
        if (values.length == 0) return 0;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        table.toSQL(sql);
        java.util.LinkedHashSet<Column<?>> columns = new java.util.LinkedHashSet<Column<?>>();
        for (ValueSet vs : values) for (Column<?> c : vs.getColumns()) {
            if (!c.getTable().equals(table)) throw new PrismsSqlException("Column " + c + " is not applicable to table " + table);
            columns.add(c);
        }
        if (columns.size() > 0) {
            sql.append('(');
            boolean first = true;
            for (Column<?> c : columns) {
                if (!first) sql.append(", ");
                first = false;
                c.toSQL(sql);
            }
            sql.append(')');
        }
        boolean stringable = true;
        sql.append(" VALUES ");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sql.append(", ");
            sql.append('(');
            boolean first = true;
            for (Column<?> c : columns) {
                if (!first) sql.append(", ");
                first = false;
                c.toSQL(sql);
                Column<Object> co = (Column<Object>) c;
                if (co.getDataType().isStringable(values[i].get(co), co)) co.getDataType().toSQL(values[i].get(co), sql, co); else {
                    sql.append('?');
                    stringable = false;
                }
            }
            sql.append(')');
        }
        if (stringable) return execute(sql.toString()); else {
            java.sql.PreparedStatement pStmt = executePrep(sql.toString(), values, null);
            try {
                return pStmt.executeUpdate();
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute prepared database call: " + sql, e);
            } finally {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
    }

    /**
	 * @param query The query to get the row count of
	 * @return The number of rows matching the given query
	 * @throws PrismsSqlException If an error occurs selecting the data
	 */
    public int selectCount(Query query) throws PrismsSqlException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(");
        if (query.isDistinct()) sql.append(" DISTINCT");
        Column<?>[] columns = query.getColumns();
        if (columns != null && columns.length == 0) throw new PrismsSqlException("No columns selected for query");
        if (columns != null) for (int c = 0; c < columns.length; c++) {
            if (c > 0) sql.append(", ");
            columns[c].toSQL(sql);
        } else sql.append('*');
        sql.append(") FROM ");
        query.getTable().toSQL(sql);
        WhereExpression where = query.getFilter();
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        if (where == null || where.isStringable()) {
            java.sql.ResultSet rs = null;
            java.sql.Statement stmt = getStatement();
            try {
                rs = stmt.executeQuery(sql.toString());
                return rs.getInt(1);
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute database call: " + sql, e);
            } finally {
                try {
                    if (rs != null) rs.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        } else {
            java.sql.ResultSet rs = null;
            java.sql.PreparedStatement pStmt = executePrep(sql.toString(), null, where);
            try {
                rs = pStmt.executeQuery();
                return rs.getInt(1);
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute prepared database call: " + sql, e);
            } finally {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
    }

    /**
	 * Selects data from a table
	 * 
	 * @param query The query to search the database with
	 * @return The result set to use to iterate throgh the results
	 * @throws PrismsSqlException If an error occurs selecting the data
	 */
    public ResultSet select(Query query) throws PrismsSqlException {
        prisms.util.DBUtils.ConnType type = null;
        if (query.getOffset() > 0 || query.getLimit() > 0) type = getType();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (query.isDistinct()) sql.append(" DISTINCT");
        Column<?>[] columns = query.getColumns();
        if (columns != null && columns.length == 0) throw new PrismsSqlException("No columns selected for query");
        if (columns != null) for (int c = 0; c < columns.length; c++) {
            if (c > 0) sql.append(", ");
            columns[c].toSQL(sql);
        } else sql.append('*');
        sql.append(" FROM ");
        query.getTable().toSQL(sql);
        WhereExpression where = query.getFilter();
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        prisms.util.Sorter<Column<?>> sorter = query.getSorter();
        if (sorter != null) {
            sql.append(" ORDER BY ");
            for (int f = 0; f < sorter.getSortCount(); f++) {
                if (f > 0) sql.append(", ");
                sorter.getField(f).toSQL(sql);
                if (!sorter.isAscending(f)) sql.append(" DESC");
            }
        }
        if (columns == null) columns = query.getTable().getColumns();
        if (type == prisms.util.DBUtils.ConnType.HSQL) {
            sql.append(" LIMIT ");
            if (query.getLimit() > 0) sql.append(query.getLimit()); else sql.append(Integer.MAX_VALUE);
            if (query.getOffset() > 0) {
                sql.append(" OFFSET ");
                sql.append(query.getOffset());
            }
        } else if (type == prisms.util.DBUtils.ConnType.ORACLE) {
            StringBuilder insert = new StringBuilder("SELECT ");
            for (int c = 0; c < columns.length; c++) {
                if (c > 0) insert.append(", ");
                columns[c].toSQL(insert);
            }
            if (columns.length > 0) insert.append(", ");
            insert.append("ROWNUM FROM (");
            sql.insert(0, insert.toString());
            insert = null;
            sql.append(" WHERE ROWNUM ");
            if (query.getLimit() > 0) {
                if (query.getOffset() > 0) sql.append(" BETWEEN ").append(query.getOffset() + 1).append(" AND ").append(query.getOffset() + 1 + query.getLimit()); else sql.append("<=").append(query.getLimit());
            } else sql.append('>').append(query.getOffset());
        } else throw new PrismsSqlException("offset/limit not implemented for " + type);
        if (where == null || where.isStringable()) {
            java.sql.Statement stmt = getStatement();
            try {
                return new ResultSet(this, stmt.executeQuery(sql.toString()), columns);
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute database call: " + sql, e);
            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        } else {
            java.sql.PreparedStatement pStmt = executePrep(sql.toString(), null, where);
            try {
                return new ResultSet(this, pStmt.executeQuery(), columns);
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute prepared database call: " + sql, e);
            } finally {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
    }

    /**
	 * Updates data in existing rows in a table
	 * 
	 * @param table The table structure to update the data in
	 * @param where The where clause determining which rows to update
	 * @param values The value set determining the data to set in the updated rows
	 * @return The number of rows updated by this call
	 * @throws PrismsSqlException If an error occurs updating the data
	 */
    public int update(Table table, WhereExpression where, ValueSet values) throws PrismsSqlException {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        table.toSQL(sql);
        sql.append(" SET ");
        boolean first = true;
        for (prisms.osql.ValueSet.ColumnValue cv : values) {
            if (!first) sql.append(", ");
            first = false;
            cv.getColumn().toSQL(sql);
            sql.append('=');
            Column<Object> c = (Column<Object>) cv.getColumn();
            if (c.getDataType().isStringable(cv.getValue(), c)) c.getDataType().toSQL(cv.getValue(), sql, c); else sql.append('?');
        }
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        if ((where == null || where.isStringable()) && values.isStringable()) return execute(sql.toString()); else {
            java.sql.PreparedStatement pStmt = executePrep(sql.toString(), new ValueSet[] { values }, where);
            try {
                return pStmt.executeUpdate();
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute prepared database call: " + sql, e);
            } finally {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
    }

    /**
	 * Deletes rows in a table
	 * 
	 * @param table The table structure to delete rows from
	 * @param where The where expression determining which rows to delete
	 * @return The number of rows deleted by this call
	 * @throws PrismsSqlException If an error occurs deleting the data
	 */
    public int delete(Table table, WhereExpression where) throws PrismsSqlException {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        table.toSQL(sql);
        if (where != null) {
            sql.append(" WHERE ");
            where.toSQL(sql);
        }
        if (where == null || where.isStringable()) return execute(sql.toString()); else {
            java.sql.PreparedStatement pStmt = executePrep(sql.toString(), null, where);
            try {
                return pStmt.executeUpdate();
            } catch (SQLException e) {
                throw new PrismsSqlException("Could not execute prepared database call: " + sql, e);
            } finally {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    log.error("Connection error", e);
                }
            }
        }
    }

    /**
	 * A unit test of a OSQL connection on the PRISMS database
	 * 
	 * @param args Command-line arguments, ignored
	 * @throws PrismsSqlException If the test fails
	 */
    public static void main(String[] args) throws PrismsSqlException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        java.sql.Connection sqlConn;
        try {
            sqlConn = java.sql.DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/prisms", "PRISMS", "PRISMS");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        try {
            Connection osqlConn = new Connection(sqlConn);
            String[] schemas = osqlConn.getSchemas();
            String schema = null;
            BaseTable changeRecords = null;
            for (int s = 0; s < schemas.length; s++) {
                changeRecords = osqlConn.getTable(schemas[s], "prisms_change_record");
                if (changeRecords != null) {
                    schema = schemas[s];
                    break;
                }
            }
            System.out.println("Schema=" + schema + ": table=" + changeRecords + " columns:");
            for (Column<?> c : changeRecords.getColumns()) System.out.println(c.toString());
            System.out.println("\n" + changeRecords + " content:");
            WhereExpression.ValueComparison<String> vc = WhereExpression.Util.valEqual(changeRecords.getColumn("changeType", String.class), null);
            WhereExpression.ValueComparison<String> vc2 = WhereExpression.Util.valEqual(changeRecords.getColumn("changeType", String.class), "name");
            for (ValueSet vs : osqlConn.select(new Query(changeRecords, null, WhereExpression.Util.or(vc, vc2), null))) System.out.println(vs.toString());
        } finally {
            try {
                sqlConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
