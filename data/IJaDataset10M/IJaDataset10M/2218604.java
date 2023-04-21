package cn.myapps.core.dynaform.form.ddlutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.form.alteration.AddColumnChange;
import cn.myapps.core.dynaform.form.alteration.AddTableChange;
import cn.myapps.core.dynaform.form.alteration.ColumnDataTransferChange;
import cn.myapps.core.dynaform.form.alteration.ColumnDataTypeChange;
import cn.myapps.core.dynaform.form.alteration.ColumnRenameChange;
import cn.myapps.core.dynaform.form.alteration.DropColumnChange;
import cn.myapps.core.dynaform.form.alteration.DropTableChange;
import cn.myapps.core.dynaform.form.alteration.ModelChange;
import cn.myapps.core.dynaform.form.alteration.TableDataTransferChange;
import cn.myapps.core.dynaform.form.alteration.TableRenameChange;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;

/**
 * @author nicholas
 */
public abstract class AbstractTableDefinition {

    protected static Logger log = Logger.getLogger(AbstractTableDefinition.class);

    protected Connection conn;

    protected String schema;

    protected SQLBuilder _builder;

    protected AbstractTableDefinition(Connection conn, SQLBuilder _builder) {
        this._builder = _builder;
        this.conn = conn;
    }

    public void processChanges(ChangeLog changeLog) throws Exception {
        Collection changes = changeLog.getChanges();
        for (Iterator iterator = changes.iterator(); iterator.hasNext(); ) {
            ModelChange change = (ModelChange) iterator.next();
            invokeChangeHandler(change);
        }
    }

    protected void invokeChangeHandler(ModelChange change) throws Exception {
        Class curClass = getClass();
        while ((curClass != null) && !Object.class.equals(curClass)) {
            Method method = null;
            try {
                try {
                    method = curClass.getDeclaredMethod("processChange", new Class[] { change.getClass() });
                } catch (NoSuchMethodException ex) {
                }
                if (method != null) {
                    method.invoke(this, new Object[] { change });
                    return;
                } else {
                    curClass = curClass.getSuperclass();
                }
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof SQLException) {
                    throw (SQLException) ex.getTargetException();
                } else {
                    throw new Exception(ex.getTargetException());
                }
            }
        }
    }

    public void processChange(AddTableChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().createTable(change.getTable(), false);
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(AddColumnChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().addColumn(change.getTable(), change.getTargetColumn());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(ColumnDataTransferChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().columnDataCopy(change.getTable(), change.getSourceColumn(), change.getTargetColumn());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(ColumnDataTypeChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        Table table = change.getTable();
        Column targetColumn = change.getTargetColumn();
        Column sourceColumn = change.getSourceColumn();
        Column tempColumn = sourceColumn;
        boolean isDataExists = false;
        try {
            tempColumn = (Column) sourceColumn.clone();
            log.debug(new Integer(sourceColumn.hashCode()));
            log.debug(new Integer(tempColumn.hashCode()));
            tempColumn.setName(DQLASTUtil.TEMP_PREFIX + sourceColumn.getName());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        getSQLBuilder().columnRename(table, sourceColumn, tempColumn);
        getSQLBuilder().addColumn(table, targetColumn);
        getSQLBuilder().columnDataCopy(table, tempColumn, targetColumn);
        getSQLBuilder().dropColumn(table, tempColumn);
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(ColumnRenameChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().columnRename(change.getTable(), change.getSourceColumn(), change.getTargetColumn());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(DropColumnChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().dropColumn(change.getTable(), change.getSourceColumn());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(DropTableChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().dropTable(change.getTable());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(TableDataTransferChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().tableDataCopy(change.getSourceTable(), change.getTargetTable());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public void processChange(TableRenameChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        getSQLBuilder().createTable(change.getTargetTable(), false);
        getSQLBuilder().tableDataCopy(change.getChangedTable(), change.getTargetTable());
        getSQLBuilder().dropTable(change.getChangedTable());
        String sql = getSQLBuilder().getSQL();
        evaluateBatch(sql, false);
    }

    public SQLBuilder getSQLBuilder() {
        return _builder;
    }

    public int evaluateBatch(String sql, boolean continueOnError) throws SQLException {
        Statement statement = null;
        int errors = 0;
        int commandCount = 0;
        try {
            statement = conn.createStatement();
            String[] commands = sql.split(SQLBuilder.SQL_DELIMITER);
            for (int i = 0; i < commands.length; i++) {
                String command = commands[i];
                if (command.trim().length() == 0) {
                    continue;
                }
                commandCount++;
                try {
                    log.info("executing SQL: " + command);
                    int results = statement.executeUpdate(command);
                    log.info("After execution, " + results + " row(s) have been changed");
                } catch (SQLException ex) {
                    if (continueOnError) {
                        log.warn("SQL Command " + command + " failed with: " + ex.getMessage());
                        errors++;
                    } else {
                        throw ex;
                    }
                }
            }
            log.info("Executed " + commandCount + " SQL command(s) with " + errors + " error(s)");
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeStatement(statement);
        }
        return errors;
    }

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                log.info("Ignoring exception that occurred while closing statement", e);
            }
        }
    }
}
