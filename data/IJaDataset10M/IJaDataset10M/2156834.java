package com.incendiaryblue.hibernate;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import com.incendiaryblue.database.Database;
import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.tracking.applicationlogger.DirectSqlLogger;

public class DirectSQL {

    private static Logger logger = Logger.getLogger(DirectSqlLogger.class);

    protected SQLProvider sqlProvider;

    public SQLProvider getSqlProvider() {
        return sqlProvider;
    }

    public void setSqlProvider(SQLProvider sqlProvider) {
        this.sqlProvider = sqlProvider;
    }

    public interface SQLProvider {

        Connection getConnection() throws SQLException;

        void releaseConnection();
    }

    public static class HibProvider implements SQLProvider {

        Session sess = null;

        Connection con = null;

        public Connection getConnection() throws SQLException {
            try {
                sess = new GenericHibernateDAO().getASession();
                con = sess.connection();
            } catch (HibernateException e) {
                logger.error("getConnection(): Hibernate exception thrown", e);
                throw new SQLException("getConnection(): Hibernate exception thrown");
            }
            return con;
        }

        public void releaseConnection() {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    logger.warn("process(): Close connection exception thrown", e);
                }
            }
            if (sess != null) {
                try {
                    sess.close();
                } catch (HibernateException e) {
                    logger.warn("process(): Close session exception thrown", e);
                }
            }
        }
    }

    public static class SycomaxDBProvider implements SQLProvider {

        private Database database = (Database) AppConfig.getDefaultComponent(Database.class);

        private Connection con;

        public Connection getConnection() throws SQLException {
            con = database.getConnection();
            return con;
        }

        public void releaseConnection() {
            database.releaseConnection(con);
        }
    }
}
