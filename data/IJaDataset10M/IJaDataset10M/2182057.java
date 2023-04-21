package org.apache.log4j.jdbcplus.examples;

import java.rmi.RemoteException;
import java.sql.Connection;
import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.jdbcplus.JDBCAppender;
import org.apache.log4j.jdbcplus.examples.FirebirdPoolConnectionHandler;
import org.apache.log4j.jdbcplus.examples.SqlHandler;
import org.apache.log4j.jdbcplus.spi.JDBCPoolConnectionHandler;

/**
 * test Case for FirebirdPoolConnectionHandler, SqlHandler and JDBC logging
 * 
 * @author mann17, $Author: dan_mueller $
 * @version $Revision: 1.2 $
 */
public class FirebirdPoolConnectionHandlerTest extends TestCase {

    static {
        try {
        } catch (Exception e) {
            System.err.println("Could not register firebird driver.");
            e.printStackTrace();
        }
    }

    public void _testGetConnection() throws Exception {
        JDBCPoolConnectionHandler connHandler = new FirebirdPoolConnectionHandler();
        Connection conn = connHandler.getConnection();
        connHandler.freeConnection(conn);
    }

    public void testCustomConfig() throws Exception {
        Logger logger = Logger.getLogger("test");
        JDBCAppender app = new JDBCAppender();
        app.setConnector("org.apache.log4j.jdbcplus.examples.FirebirdPoolConnectionHandler");
        app.setSqlhandler("org.apache.log4j.jdbcplus.examples.SqlHandler");
        app.setLayout(new PatternLayout("%m"));
        logger.addAppender(app);
        logger.setLevel(Level.INFO);
        logger.debug("debug");
        logger.info("info");
        logger.error("error");
        logger.fatal("fatal");
        logger.fatal(null);
        logger.fatal(new SqlHandler());
    }

    /**
     * Recovery test. Currently fails :-( Firebird exception:
     * org.firebirdsql.jca.FBResourceTransactionException: Local transaction active: can't begin
     * another
     * 
     * @throws Exception
     */
    public void _testDbRecovery() throws Exception {
        Logger logger = Logger.getLogger(FirebirdPoolConnectionHandlerTest.class);
        logger.info("before recovery");
        logger.info("problem");
        logger.info("recovered");
    }

    public void testDefaultConfig() throws Exception {
        Logger logger = Logger.getLogger(FirebirdPoolConnectionHandlerTest.class);
        logger.debug("de'bug");
        logger.debug(new SqlHandler());
        NDC.push("MyNDC");
        logger.debug("debug MyNDC");
        MDC.put("MyMDC", "MyMDC");
        MDC.put("MyMDC2", new Exception("MDC2").toString());
        logger.debug("debug MyNDC MyMDC");
        NDC.pop();
        logger.debug("debug MyMDC");
        MDC.remove("MyMDC");
        logger.debug("ex", this.getVeryLongException(10));
        JDBCAppender app = (JDBCAppender) logger.getParent().getAppender("JDBC2");
        app.setBuffer("2");
        MDC.put("MyMDC", "MDC1");
        logger.debug("debug MyMDC1");
        MDC.put("MyMDC", "MDC2");
        logger.debug("debug MyMDC2");
    }

    public void _testDefaultConfigException() throws Exception {
        Logger logger = Logger.getLogger(FirebirdPoolConnectionHandlerTest.class);
        logger.debug("nullpointer debug", new NullPointerException());
    }

    public void _testNestedException() throws Exception {
        Logger logger = Logger.getLogger(FirebirdPoolConnectionHandlerTest.class);
        logger.debug("nested nullpointer debug", new RemoteException("test", new RemoteException("test2", new NullPointerException())));
    }

    private Exception getVeryLongException(int levels) {
        Exception result = null;
        try {
            throwVeryDeepException(levels);
        } catch (Exception e) {
            result = e;
        }
        return result;
    }

    private void throwVeryDeepException(int levels) throws Exception {
        if (levels == 0) throw new Exception("very deep exception");
        try {
            throwVeryDeepException(levels - 1);
        } catch (Exception e) {
            throw new Exception("nested", e);
        }
    }
}
