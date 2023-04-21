package edu.arsc.fullmetal.server;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.arsc.fullmetal.commons.ChatStanza;
import static java.util.Arrays.asList;
import static edu.arsc.fullmetal.server.SQLNames.*;

/**
 * Class to provide database abstraction.
 * 
 * @author cgranade
 */
public final class Database {

    private static final int TIMEOUT;

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    private int lastMsgId = 0;

    static {
        int t;
        try {
            t = Configuration.getDBTimeout();
        } catch (ConfigurationException ex) {
            t = 20000;
            Logger.getLogger(Database.class.getName()).log(Level.WARNING, "Reverting to default DB timout value of 20 seconds.", ex);
        }
        TIMEOUT = t;
    }

    public void clearOldSessions() throws SQLException {
        PreparedStatement ps = Statement.CLEAR_OLD_SESSIONS.asJdbc();
        try {
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Exception occured while executing statement.", ex);
            throw ex;
        }
        cleanUp(ps, null);
    }

    /**
	 * Returns a list of chat messages for a given user and updates the session
	 *
	 * @param id User ID whose messages are to be retrieved.
	 * @param remoteAddr The remote URL of the client.
	 * @return A list of messages for the given user, in the form of a list
	 *     of {@link ChatStanza} instances.
	 * @throws java.sql.SQLException
	 */
    public List<ChatStanza> getMessages(int id, String remoteAddr) throws SQLException {
        List<ChatStanza> messages = new LinkedList<ChatStanza>();
        PreparedStatement ps = Statement.GET_MESSAGES_UPDATE.asJdbc();
        ps.setInt(PI_USERID, id);
        ps.setString(PI_SESSIONIP, remoteAddr);
        ResultSet rs = null;
        LOGGER.log(Level.INFO, "Trying to get messages for user: " + id);
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Exception occured while executing statement.", ex);
            throw ex;
        }
        while (rs.next()) messages.add(new ChatStanza(rs.getString(F_CONTENT), "" + rs.getInt(F_AUTHID)));
        LOGGER.log(Level.INFO, "Finished getting messages for user: " + id);
        cleanUp(ps, rs);
        return messages;
    }

    public void postMessage(int authorId, String message) throws SQLException {
        PreparedStatement ps = Statement.POST_MESSAGE.asJdbc();
        ps.setInt(PI_USERID, authorId);
        ps.setString(PI_MESSAGE, message);
        try {
            ps.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Exception occured while executing statement.", ex);
            throw ex;
        }
        cleanUp(ps, null);
    }

    private final Connection CONN;

    static Class<? extends Driver> loadDriverClass() throws ConfigurationException {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL jarURL = Configuration.getDBDriverJar();
        if (jarURL != null) {
            URL[] jars = { jarURL };
            cl = new URLClassLoader(jars, cl);
        }
        try {
            return (Class<? extends Driver>) Class.forName(Configuration.getDBDriver(), true, cl);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "Could not load specified JDBC driver. Dying.", ex);
            throw new Error("JDBC driver could not be loaded!", ex);
        }
    }

    private static final Database inst;

    static {
        try {
            inst = new Database();
        } catch (SQLException ex) {
            throw new Error("Server rejected connection.", ex);
        } catch (ConfigurationException ex) {
            throw new Error("Configuration error prevented database connection.", ex);
        } catch (TimeoutException ex) {
            throw new Error("Timed out connecting to the database server.");
        }
    }

    public static final Database getInstance() {
        return inst;
    }

    Database() throws SQLException, ConfigurationException, TimeoutException {
        if (!Configuration.isDBDriverJarSet()) {
            try {
                DriverManager.getDriver(Configuration.getDBURL());
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Could not load system-default JDBC driver.", ex);
            }
        } else {
            registerDriverFromJar();
        }
        Object lock = new Object();
        LOGGER.log(Level.FINE, "Starting connection thread.");
        ConnectThread t = new ConnectThread(lock);
        t.start();
        synchronized (lock) {
            try {
                lock.wait(TIMEOUT);
                if (!t.done) {
                    t.interrupt();
                    throw new TimeoutException("Could not connect to database; timed out.");
                }
            } catch (InterruptedException ex) {
            }
        }
        if (t.sqlProblem != null) throw t.sqlProblem;
        CONN = t.connTmp;
        Statement.prepareAll(CONN);
    }

    /**
     * Fetches a list of all currently defined robots.
     * 
     * @return A {@link List} containing {@link RobotRecord} instances
     *         corresponding to each robot configured in the database.
     */
    public List<RobotRecord> getRobotRecords() throws SQLException {
        try {
            List<RobotRecord> list = new Vector<RobotRecord>();
            ResultSet rs = Statement.ROBOTDRIVERS.asJdbc().executeQuery();
            while (rs.next()) {
                list.add(new RobotRecord(rs.getString(F_CLASSNAME), rs.getString(F_PARAMS)));
            }
            return list;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NullPointerException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static UserRecord userFromResultSet(ResultSet rs) throws SQLException {
        LOGGER.log(Level.INFO, "Getting User: " + rs.getStatement());
        byte[] b = { 0 };
        try {
            return new UserRecord(rs.getInt(F_ID), rs.getString(F_USERNAME), rs.getBytes(F_SALT), rs.getBytes(F_HASH), rs.getString(F_FULLNAME), rs.getString(F_EMAIL));
        } catch (NullPointerException npe) {
            return new UserRecord(999, "Mr. Nullman", b, b, "Nullister Nullman", "n@u.ll");
        }
    }

    private void cleanUp(PreparedStatement ps, ResultSet rs) {
    }

    private static void logAndForget(Throwable ex) {
        LOGGER.log(Level.SEVERE, "Forgettable exception occured.", ex);
    }

    public List<UserRecord> getUserRecords() throws SQLException {
        List<UserRecord> list = new Vector<UserRecord>();
        try {
            ResultSet rs = Statement.USERS.asJdbc().executeQuery();
            while (rs.next()) {
                list.add(userFromResultSet(rs));
            }
            cleanUp(null, rs);
        } catch (SQLException ex) {
            logAndForget(ex);
        }
        return list;
    }

    /**
     * Retrieves a {@link UserRecord} representing a specific user.
     * 
     * @param username Username of the record to be retrieved.
     * @return User record representing the user with username {@code username}.
     * 
     * @throws java.sql.SQLException, NoSuchUserException
     * @todo Document better! Oh, and fix the security hole here!
     */
    public UserRecord getUserRecordByUsername(String username) throws SQLException, NoSuchUserException {
        PreparedStatement ps = Statement.USER_BYUSERNAME.asJdbc();
        if (false) {
            throw new NoSuchUserException("User does not exist.");
        }
        ResultSet rs = null;
        UserRecord returnRecord = null;
        LOGGER.log(Level.INFO, "Trying to get user with username = '" + username + "'");
        ps.setString(PI_USERNAME, username);
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Exception occured while executing statement.", ex);
            throw ex;
        }
        if (rs.first()) {
            returnRecord = userFromResultSet(rs);
        }
        LOGGER.log(Level.INFO, "Finished getting user with username = '" + returnRecord.getUsername() + "'");
        cleanUp(ps, rs);
        return returnRecord;
    }

    /**
     * Checks if we can still communicate with the database server.
     * 
     * @return {@code true} if the database server is still connected.
     * 
     * @todo Cache reflection info.
     */
    public boolean isValid() {
        try {
            try {
                Method isValidMethod = CONN.getClass().getMethod("isValid");
                Boolean valid = (Boolean) isValidMethod.invoke(CONN);
                return valid.booleanValue();
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Falling back to Java 5 version of " + "Database.isValid().", ex);
                CONN.getMetaData();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void registerDriverFromJar() throws SQLException, ConfigurationException {
        try {
            DriverManager.registerDriver(new DriverWrapper((Driver) loadDriverClass().newInstance()));
        } catch (InstantiationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Connects to database in an isolated thread so that it can be interrupted
     * if need be.
     */
    private class ConnectThread extends Thread {

        private Connection connTmp;

        private SQLException sqlProblem = null;

        private Object lock;

        private boolean done = false;

        private ConnectThread(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                connTmp = DriverManager.getConnection(Configuration.getDBURL(), Configuration.getDBUser(), Configuration.getDBPass());
                System.out.println("Made connection.");
            } catch (SQLException ex) {
                sqlProblem = ex;
            } finally {
                done = true;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }
    }
}
