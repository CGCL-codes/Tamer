package cn.myapps.core.dynaform.form.ddlutil.mssql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import cn.myapps.core.dynaform.document.dql.DQLASTUtil;
import cn.myapps.core.dynaform.form.alteration.ColumnDataTypeChange;
import cn.myapps.core.dynaform.form.alteration.ColumnRenameChange;
import cn.myapps.core.dynaform.form.ddlutil.AbstractTableDefinition;
import cn.myapps.core.dynaform.form.ddlutil.SQLBuilder;
import cn.myapps.core.table.model.Column;
import cn.myapps.core.table.model.Table;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class MssqlTableDefinition extends AbstractTableDefinition {

    protected static Logger log = Logger.getLogger(MssqlTableDefinition.class);

    public MssqlTableDefinition(Connection conn) {
        super(conn, new MssqlBuilder());
        this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_MSSQL);
        _builder.setSchema(schema);
    }

    public void processChange(ColumnDataTypeChange change) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        getSQLBuilder().setWriter(buffer);
        Table table = change.getTable();
        Column targetColumn = change.getTargetColumn();
        Column sourceColumn = change.getSourceColumn();
        Column tempColumn = sourceColumn;
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
                        if ((ex.getMessage().indexOf("Unexpected parameter")) > 0) {
                            throw new SQLException("[{*[Errors]*}]:  " + " {*[core.create_field.type.incompatible]*}!");
                        }
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
}
