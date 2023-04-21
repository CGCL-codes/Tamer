package org.jailer.database;

import org.jailer.util.SqlScriptExecutor;

/**
 * Renews the statistics for the working-tables
 * by executing a SQL-script.
 * 
 * @author Wisser
 */
public class SqlScriptBasedStatisticRenovator implements StatisticRenovator {

    /**
     * Name of SQL-script file.
     */
    private final String scriptFileName;

    /**
     * Constructor.
     * 
     * @param scriptFileName name of SQL-script file
     */
    public SqlScriptBasedStatisticRenovator(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    /**
     * Renews the DB table statistics for the working-tables
     * by executing the SQL-script.
     * 
     * @param statementExecutor for execution of SQL-statements
     */
    public void renew(StatementExecutor statementExecutor) throws Exception {
        SqlScriptExecutor.executeScript(scriptFileName, statementExecutor);
    }
}
