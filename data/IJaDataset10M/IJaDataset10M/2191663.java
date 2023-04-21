package sk.naive.talker.persistence;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * ConnectionPool.
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.3 $ $Date: 2005/02/21 18:39:14 $
 */
public interface DbConnectionPool {

    Connection getConnection() throws PersistenceException;

    void releaseConnection(Connection conn) throws PersistenceException;

    Connection getRealConnection(boolean autoCommit) throws SQLException;

    static class DefaultDbConnectionPool implements DbConnectionPool {

        private static Logger logger = Logger.getLogger("nt.dbLayer");

        private LinkedList freeConnections;

        private Set usedConnections;

        private int connectionsReserve = 4;

        private int connectionsMaxUsed = 10;

        private String dbUrl;

        public DefaultDbConnectionPool(String dbDriversString, String dbUrl) {
            this.dbUrl = dbUrl;
            String[] dbDrivers = dbDriversString.split(" *, *");
            for (String dbDriver : dbDrivers) {
                try {
                    Class.forName(dbDriver);
                } catch (ClassNotFoundException e) {
                    logger.warning("DB Driver class '" + dbDriver + "' not found!");
                }
            }
            freeConnections = new LinkedList();
            usedConnections = new HashSet();
            try {
                for (int i = 0; i < connectionsReserve; i++) {
                    freeConnections.add(getRealConnection(false));
                }
                logger.config("JDBC init OK...");
            } catch (SQLException e) {
                logger.warning("DB connection problem (db connection string = '" + dbUrl + "').");
            }
        }

        public Connection getRealConnection(boolean autoCommit) throws SQLException {
            Connection conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(autoCommit);
            return conn;
        }

        public Connection getConnection() throws PersistenceException {
            try {
                Connection conn = null;
                if (freeConnections.isEmpty()) {
                    if (usedConnections.size() >= connectionsMaxUsed) {
                        logger.warning("Maximum amount of used connections reached (used = " + usedConnections.size() + ").");
                        return null;
                    }
                    conn = getRealConnection(false);
                    logger.finest("New connection " + conn + " created (used = " + usedConnections.size() + ").");
                } else {
                    conn = (Connection) freeConnections.removeFirst();
                }
                usedConnections.add(conn);
                return conn;
            } catch (SQLException e) {
                throw new PersistenceException(e);
            }
        }

        public void releaseConnection(Connection conn) throws PersistenceException {
            try {
                if (conn == null) {
                    return;
                }
                if (usedConnections.remove(conn)) {
                    if (conn.isClosed()) {
                        logger.warning("Connection " + conn + " was already closed (why?) - removed from pool.");
                    } else if (usedConnections.size() + connectionsReserve > freeConnections.size()) {
                        freeConnections.add(conn);
                    } else {
                        logger.finest("Connection " + conn + " released and closed (free = " + freeConnections.size() + ").");
                        conn.close();
                    }
                } else {
                    logger.warning("Connection supposed to be used but not found in used connections set.");
                }
            } catch (SQLException e) {
                throw new PersistenceException(e);
            }
        }
    }
}
