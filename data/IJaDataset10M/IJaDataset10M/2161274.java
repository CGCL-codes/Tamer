package org.objectstyle.cayenne.dba.oracle;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.objectstyle.cayenne.access.QueryLogger;
import org.objectstyle.cayenne.access.trans.SelectTranslator;
import org.objectstyle.cayenne.query.QueryMetadata;

/**
 * Select translator that implements Oracle-specific optimizations.
 * 
 * @author Andrei Adamchik
 */
public class OracleSelectTranslator extends SelectTranslator {

    private static Logger logObj = Logger.getLogger(OracleSelectTranslator.class);

    private static boolean testedDriver;

    private static boolean useOptimizations;

    private static Method statementSetRowPrefetch;

    private static final Object[] rowPrefetchArgs = new Object[] { new Integer(100) };

    public String createSqlString() throws Exception {
        String sqlString = super.createSqlString();
        QueryMetadata info = getQuery().getMetaData(getEntityResolver());
        if (info.getFetchLimit() > 0) {
            sqlString = "SELECT * FROM (" + sqlString + ") WHERE rownum <= " + info.getFetchLimit();
        }
        return sqlString;
    }

    /**
     * Determines if we can use Oracle optimizations. If yes, configure this object to use
     * them via reflection.
     */
    private static final synchronized void testDriver(Statement st) {
        if (testedDriver) {
            return;
        }
        if (st == null) {
            return;
        }
        testedDriver = true;
        try {
            Class[] args2 = new Class[] { Integer.TYPE };
            statementSetRowPrefetch = st.getClass().getMethod("setRowPrefetch", args2);
            useOptimizations = true;
        } catch (Exception ex) {
            useOptimizations = false;
            statementSetRowPrefetch = null;
            StringBuffer buf = new StringBuffer();
            buf.append("Unknown Oracle statement type: [").append(st.getClass().getName()).append("]. No Oracle optimizations applied.");
            logObj.info(buf.toString());
        }
    }

    /**
     * Translates internal query into PreparedStatement, applying Oracle optimizations if
     * possible.
     */
    public PreparedStatement createStatement() throws Exception {
        String sqlStr = createSqlString();
        QueryLogger.logQuery(sqlStr, values);
        PreparedStatement stmt = connection.prepareStatement(sqlStr);
        initStatement(stmt);
        if (!testedDriver) {
            testDriver(stmt);
        }
        if (useOptimizations) {
            statementSetRowPrefetch.invoke(stmt, rowPrefetchArgs);
        }
        return stmt;
    }
}
