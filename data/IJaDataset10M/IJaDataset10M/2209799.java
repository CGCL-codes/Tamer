package cn.myapps.core.table.ddlutil.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import cn.myapps.core.table.ddlutil.AbstractTableDefinition;
import cn.myapps.core.table.ddlutil.SQLBuilder;
import cn.myapps.util.DbTypeUtil;

/**
 * 
 * @author Chris
 * 
 */
public class OracleTableDefinition extends AbstractTableDefinition {

    protected static Logger log = Logger.getLogger(OracleTableDefinition.class);

    public OracleTableDefinition(Connection conn) {
        super(conn, new OracleBuilder());
        this.schema = DbTypeUtil.getSchema(conn, DbTypeUtil.DBTYPE_ORACLE);
        _builder.setSchema(schema);
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
                        if ((ex.getMessage().indexOf("ORA-00911")) > 0) {
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
