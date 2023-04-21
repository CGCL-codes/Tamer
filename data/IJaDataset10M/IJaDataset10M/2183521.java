package org.jailer.database;

/**
 * Renews the DB2 table statistics for the working-tables
 * by executing a shell-script.
 * 
 * @author Wisser
 */
public class DB2ShellScriptBasedStatisticRenovator extends ShellScriptBasedStatisticRenovator {

    /**
     * Constructor.
     * 
     * @param scriptInvocation invocation of the script-file
     */
    public DB2ShellScriptBasedStatisticRenovator(String scriptInvocation) {
        super(scriptInvocation);
    }

    /**
     * Gets shell-invocation.
     * 
     * @param statementExecutor for execution of SQL-statements
     * @return shell-invocation
     */
    protected String getScriptInvocation(StatementExecutor statementExecutor) throws Exception {
        String s = statementExecutor.dbUrl.substring(statementExecutor.dbUrl.indexOf("://") + 3);
        String dbName = s.substring(s.indexOf("/") + 1);
        return super.getScriptInvocation(statementExecutor) + " " + dbName + " " + statementExecutor.dbUser + " " + statementExecutor.dbPassword;
    }
}
