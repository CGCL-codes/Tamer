package edu.xtec.util.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** Implementation of {@link ConnectionBeanProvider} that uses a pool of 
 * {@link edu.xtec.util.db.ConnectionBean} objects.
 * Based on DbConnectionBroker 1.0.13, by Marc A. Mnich
 * @author fbusquets
 * @version 1.0.13
 */
public class PooledConnectionBeanProvider extends ConnectionBeanProvider implements Runnable {

    /** Key for minimum number of connections to start with. Default is 1. */
    public static final String MIN_CONNS = "dbMinConns";

    private static final String DEFAULT_MIN_CONNS = "1";

    /** Key for maximum number of connections in dynamic pool. Default is 3 */
    public static final String MAX_CONNS = "dbMaxConns";

    private static final String DEFAULT_MAX_CONNS = "3";

    /** Key for absolute path name for log file. e.g. 'c:/temp/mylog.log'. Default is 'pooledConnectionBean.log' in user's dir */
    public static final String LOG_FILE = "dbLogFile";

    private static final String DEFAULT_LOG_FILE = "connectionPool.log";

    /** Key for time in days between connection resets. (Reset does a basic cleanup). Default is 1.0 */
    public static final String MAX_CONN_TIME = "dbMaxConnDays";

    private static final String DEFAULT_MAX_CONN_TIME = "1.0";

    /** Key for append to logfile. Default is true.*/
    public static final String LOG_APPEND = "dbLogAppend";

    private static final String DEFAULT_LOG_APPEND = "true";

    /** Key for max time a connection can be checked out before being recycled. Zero value turns option off. Default is 60.*/
    public static final String MAX_CHECKOUT_SECONDS = "dbMaxCheckoutSeconds";

    private static final String DEFAULT_MAX_CHECKOUT_SECONDS = "60";

    /** Key for level of debug messages output to the log file.  0 -> no messages, 1 -> Errors, 2 -> Warnings, 3 -> Information. Default is 2. */
    public static final String DEBUG_LEVEL = "dbDebugLevel";

    private static final String DEFAULT_DEBUG_LEVEL = "2";

    private Thread runner;

    private ConnectionBean[] connPool;

    private int[] connStatus;

    private long[] connLockTime, connCreateDate;

    private String[] connID;

    private String logFileString, logPIDFileString;

    private int currConnections, connLast, minConns, maxConns, maxConnMSec, maxCheckoutSeconds, debugLevel;

    private boolean available = true;

    private PrintWriter log;

    private SQLWarning currSQLWarning;

    private String pid;

    /**
     * Number of times this ConnectionBean provider has been used since its creation.
     */
    public int globalUsageCount;

    /** Main initialization function, called immediatelly after constructor by
     * getConnectionBeanProvider functions.
     * @param map Collection of key - value pairs that must specify the driver, url, login and
     * password of the just created ConnectionBeanProvider.
     * @throws Exception Throwed if dbDriver does not contain a valid driver name, or if it can't be
     * instantiated.
     */
    protected void setUp(Map map) throws Exception {
        super.setUp(map);
        if (dbDriver == null || dbDriver.length() == 0) throw new Exception("Parameter dbDriver is null!");
        Class.forName(dbDriver);
        if (dbServer == null || dbServer.length() == 0) throw new Exception("Parameter dbServer is null!");
        minConns = Math.max(1, Integer.parseInt(getValue(map, MIN_CONNS, DEFAULT_MIN_CONNS)));
        maxConns = Math.max(minConns, Math.min(15, Integer.parseInt(getValue(map, MAX_CONNS, DEFAULT_MAX_CONNS))));
        logFileString = getValue(map, LOG_FILE, DEFAULT_LOG_FILE);
        double maxConnTime = new Double(getValue(map, MAX_CONN_TIME, DEFAULT_MAX_CONN_TIME)).doubleValue();
        boolean logAppend = new Boolean(getValue(map, LOG_APPEND, DEFAULT_LOG_APPEND)).booleanValue();
        maxCheckoutSeconds = Integer.parseInt(getValue(map, MAX_CHECKOUT_SECONDS, DEFAULT_MAX_CHECKOUT_SECONDS));
        debugLevel = Integer.parseInt(getValue(map, DEBUG_LEVEL, DEFAULT_DEBUG_LEVEL));
        connPool = new ConnectionBean[maxConns];
        connStatus = new int[maxConns];
        connLockTime = new long[maxConns];
        connCreateDate = new long[maxConns];
        connID = new String[maxConns];
        currConnections = minConns;
        File f = new File(logFileString);
        if (!f.isAbsolute()) {
            f = new File(System.getProperty("user.home"));
            f = new File(f, logFileString);
            logFileString = f.getAbsolutePath();
        }
        logPIDFileString = logFileString + ".pid";
        maxConnMSec = (int) (maxConnTime * 86400000.0);
        if (maxConnMSec < 30000) {
            maxConnMSec = 30000;
        }
        if (debugLevel > 0) {
            try {
                log = new PrintWriter(new FileOutputStream(logFileString, logAppend), true);
            } catch (IOException e1) {
                try {
                    log = new PrintWriter(new FileOutputStream("DBConn_" + System.currentTimeMillis() + ".log", logAppend), true);
                } catch (IOException e2) {
                    throw new IOException("Can't open any log file");
                }
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
        Date nowc = new Date();
        pid = formatter.format(nowc);
        BufferedWriter pidout = new BufferedWriter(new FileWriter(logPIDFileString));
        pidout.write(pid);
        pidout.close();
        if (log != null) {
            log.println("-----------------------------------------");
            log.println(new Date());
            log.println("Starting DbConnectionBeanBroker Version 1.0.13:");
            log.println("dbDriver = " + dbDriver);
            log.println("dbServer = " + dbServer);
            log.println("dbLogin = " + dbLogin);
            log.println("log file = " + logFileString);
            log.println("minconnections = " + minConns);
            log.println("maxconnections = " + maxConns);
            log.println("Total refresh interval = " + maxConnTime + " days");
            log.println("logAppend = " + logAppend);
            log.println("maxCheckoutSeconds = " + maxCheckoutSeconds);
            log.println("debugLevel = " + debugLevel);
            log.println("mapStatements = " + mapStatements);
            log.println("-----------------------------------------");
        }
        boolean connectionsSucceeded = false;
        Exception sqlEx = null;
        int dbLoop = 3;
        try {
            for (int i = 1; i < dbLoop; i++) {
                try {
                    for (int j = 0; j < currConnections; j++) {
                        createConn(j);
                    }
                    connectionsSucceeded = true;
                    break;
                } catch (SQLException e) {
                    sqlEx = e;
                    if (log != null && debugLevel > 0) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(new Date()).append(" ->Attempt (").append(i);
                        sb.append(" of ").append(dbLoop).append(") failed to create new connections set at startup:\n");
                        sb.append(e).append("\n");
                        sb.append("Will try again in 15 seconds...");
                        log.println(sb.substring(0));
                    }
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e1) {
                    }
                }
            }
            if (!connectionsSucceeded) {
                if (log != null && debugLevel > 0) {
                    log.println("\r\nAll attempts at connecting to Database exhausted");
                }
                if (sqlEx == null) sqlEx = new IOException("Unable to connect to Database");
                throw sqlEx;
            }
        } catch (Exception e) {
            throw e;
        }
        runner = new Thread(this);
        runner.start();
    }

    /**
     * Housekeeping thread.  Runs in the background with low CPU overhead.
     * Connections are checked for warnings and closure and are periodically
     * restarted.
     * This thread is a catchall for corrupted
     * connections and prevents the buildup of open cursors. (Open cursors
     * result when the application fails to close a Statement).
     * This method acts as fault tolerance for bad connection/statement programming.
     */
    public void run() {
        boolean forever = true;
        Statement stmt = null;
        String currCatalog = null;
        long maxCheckoutMillis = maxCheckoutSeconds * 1000;
        while (forever) {
            for (int i = 0; i < currConnections; i++) {
                try {
                    currSQLWarning = connPool[i].getConnection().getWarnings();
                    if (currSQLWarning != null) {
                        if (log != null && debugLevel > 1) {
                            log.println(new Date().toString() + " - Warnings on connection " + String.valueOf(i) + " " + currSQLWarning);
                        }
                        connPool[i].getConnection().clearWarnings();
                    }
                } catch (SQLException e) {
                    if (log != null && debugLevel > 1) {
                        log.println("Cannot access Warnings: " + e);
                    }
                }
            }
            for (int i = 0; i < currConnections; i++) {
                long age = System.currentTimeMillis() - connCreateDate[i];
                try {
                    synchronized (connStatus) {
                        if (connStatus[i] > 0) {
                            long timeInUse = System.currentTimeMillis() - connLockTime[i];
                            if (log != null && debugLevel > 2) {
                                log.println(new Date().toString() + " - Warning. Connection " + i + " in use for " + timeInUse + " ms");
                            }
                            if (maxCheckoutMillis != 0) {
                                if (timeInUse > maxCheckoutMillis) {
                                    if (log != null && debugLevel > 1) {
                                        log.println(new Date().toString() + " Warning. Connection " + i + " failed to be returned in time.  Recycling...");
                                    }
                                    throw new SQLException();
                                }
                            }
                            continue;
                        }
                        connStatus[i] = 2;
                    }
                    if (age > maxConnMSec) {
                        throw new SQLException();
                    }
                    stmt = connPool[i].getConnection().createStatement();
                    connStatus[i] = 0;
                    if (connPool[i].getConnection().isClosed()) {
                        throw new SQLException();
                    }
                } catch (SQLException e) {
                    if (log != null && debugLevel > 1) {
                        log.println(new Date().toString() + " ***** Recycling connection " + String.valueOf(i) + ":");
                    }
                    try {
                        connPool[i].closeConnection();
                    } catch (SQLException e0) {
                        if (log != null && debugLevel > 0) {
                            log.println(new Date().toString() + " - Error! Can't close connection! Might have been closed already. Trying to recycle anyway... (" + e0 + ")");
                        }
                    }
                    try {
                        createConn(i);
                    } catch (SQLException e1) {
                        if (log != null && debugLevel > 0) {
                            log.println(new Date().toString() + " - Failed to create connection: " + e1);
                        }
                        connStatus[i] = 0;
                    }
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException e1) {
                    }
                    ;
                }
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /** This method hands out the connections in round-robin order.
     * This prevents a faulty connection from locking
     * up an application entirely.  A browser 'refresh' will
     * get the next connection while the faulty
     * connection is cleaned up by the housekeeping thread.
     *
     * If the min number of threads are ever exhausted, new
     * threads are added up the the max thread count.
     * Finally, if all threads are in use, this method waits
     * 2 seconds and tries again, up to ten times.  After that, it
     * returns a null.
     * @return The ConnectionBean object, ready to be used. Remember to free it using
     * freeConnectionBean, as explained in {@link
     * ConnectionBeanProvider#freeConnectionBean} ConnectionBeanProvider.
     */
    public ConnectionBean getConnectionBean() {
        ConnectionBean conn = null;
        if (available) {
            boolean gotOne = false;
            for (int outerloop = 1; outerloop <= 10; outerloop++) {
                try {
                    int loop = 0;
                    int roundRobin = connLast + 1;
                    if (roundRobin >= currConnections) roundRobin = 0;
                    do {
                        synchronized (connStatus) {
                            if ((connStatus[roundRobin] < 1) && (!connPool[roundRobin].getConnection().isClosed())) {
                                conn = connPool[roundRobin];
                                connStatus[roundRobin] = 1;
                                connLockTime[roundRobin] = System.currentTimeMillis();
                                connLast = roundRobin;
                                gotOne = true;
                                break;
                            } else {
                                loop++;
                                roundRobin++;
                                if (roundRobin >= currConnections) roundRobin = 0;
                            }
                        }
                    } while ((gotOne == false) && (loop < currConnections));
                } catch (SQLException e1) {
                    if (log != null) log.println(new Date().toString() + " - Error: " + e1);
                }
                if (gotOne) {
                    break;
                } else {
                    synchronized (this) {
                        if (currConnections < maxConns) {
                            try {
                                createConn(currConnections);
                                currConnections++;
                            } catch (SQLException e) {
                                if (log != null && debugLevel > 0) {
                                    log.println(new Date().toString() + " - Error: Unable to create new connection: " + e);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    if (log != null && debugLevel > 0) {
                        log.println(new Date().toString() + " --> Connections Exhausted!  Will wait and try again in loop " + String.valueOf(outerloop));
                    }
                }
            }
        } else {
            if (log != null && debugLevel > 0) {
                log.println(new Date().toString() + " - Unsuccessful getConnection() request during destroy()");
            }
        }
        if (log != null && debugLevel > 2) {
            log.println(new Date().toString() + " - Handing out connection " + idOfConnection(conn));
        }
        if (conn != null) conn.usageCount++;
        return conn;
    }

    /** Returns the local JDBC ID for a connection.
     * @param conn The ConnectionBean owner if the Connection.
     * @return The JDBC ID.
     */
    public int idOfConnection(ConnectionBean conn) {
        int match;
        String tag;
        try {
            tag = conn.getConnection().toString();
        } catch (NullPointerException e1) {
            tag = "none";
        }
        match = -1;
        for (int i = 0; i < currConnections; i++) {
            if (connID[i].equals(tag)) {
                match = i;
                break;
            }
        }
        return match;
    }

    /** Frees a connection.  Replaces connection back into the main pool for
     * reuse.
     * @param conn The ConnectionBean to be released.
     * @return A String useful only for debug purposes.
     */
    public String freeConnectionBean(ConnectionBean conn) {
        StringBuffer res = new StringBuffer();
        int thisconn = idOfConnection(conn);
        if (thisconn >= 0) {
            connStatus[thisconn] = 0;
            res.append("freed ").append(conn.getConnection().toString());
        } else {
            if (log != null && debugLevel > 0) {
                log.println(new Date().toString() + " --> Error: Could not free connection!!!");
            }
        }
        return res.substring(0);
    }

    /** Returns the age of a connection -- the time since it was handed out to
     * an application.
     * @param conn The ConnectionBean to be examined.
     * @return The age of the Connection, measured in milliseconds.
     */
    public long getAge(ConnectionBean conn) {
        int thisconn = idOfConnection(conn);
        return System.currentTimeMillis() - connLockTime[thisconn];
    }

    private void createConn(int i) throws SQLException {
        if (connPool[i] != null) {
            globalUsageCount += connPool[i].usageCount;
        }
        Date now = new Date();
        try {
            Class.forName(dbDriver);
            connPool[i] = new ConnectionBean(DriverManager.getConnection(dbServer, dbLogin, dbPassword), mapStatements);
            connStatus[i] = 0;
            connID[i] = connPool[i].getConnection().toString();
            connLockTime[i] = 0;
            connCreateDate[i] = now.getTime();
        } catch (ClassNotFoundException e2) {
            if (log != null && debugLevel > 0) {
                log.println(now.toString() + " - Error creating connection: " + e2);
            }
        }
        if (log != null) log.println(now.toString() + "  Opening connection " + String.valueOf(i) + " " + connPool[i].getConnection().toString() + ":");
    }

    /**
     * Multi-phase shutdown.  having following sequence:
     * <OL>
     * <LI><code>getConnection()</code> will refuse to return connections.
     * <LI>The housekeeping thread is shut down.<br>
     *    Up to the time of <code>millis</code> milliseconds after shutdown of
     *    the housekeeping thread, <code>freeConnection()</code> can still be
     *    called to return used connections.
     * <LI>After <code>millis</code> milliseconds after the shutdown of the
     *    housekeeping thread, all connections in the pool are closed.
     * <LI>If any connections were in use while being closed then a
     *    <code>SQLException</code> is thrown.
     * <LI>The log is closed.
     * </OL><br>
     * Call this method from a servlet destroy() method.
     *
     * @param      millis   the time to wait in milliseconds.
     * @exception  SQLException if connections were in use after
     * <code>millis</code>.
     */
    public void destroy(int millis) throws SQLException {
        available = false;
        runner.interrupt();
        try {
            runner.join(millis);
        } catch (InterruptedException e) {
        }
        long startTime = System.currentTimeMillis();
        int useCount;
        while ((useCount = getUseCount()) > 0 && System.currentTimeMillis() - startTime <= millis) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        for (int i = 0; i < currConnections; i++) {
            try {
                connPool[i].closeConnection();
            } catch (SQLException e1) {
                if (log != null && debugLevel > 0) {
                    log.println(new Date().toString() + " - Cannot close connections on Destroy");
                }
            }
        }
        if (useCount > 0) {
            String msg = new Date().toString() + " - Unsafe shutdown: Had to close " + useCount + " active DB connections after " + millis + "ms";
            if (log != null) {
                log.println(msg);
                log.close();
            }
            throw new SQLException(msg);
        }
        if (log != null) log.close();
    }

    /**
     * Less safe shutdown.  Uses default timeout value.
     * This method simply calls the <code>destroy()</code> method
     * with a <code>millis</code>
     * value of 10000 (10 seconds) and ignores <code>SQLException</code>
     * thrown by that method.
     * @see     #destroy(int)
     */
    protected void destroy() {
        try {
            destroy(10000);
        } catch (SQLException e) {
        }
    }

    public int getUseCount() {
        int useCount = 0;
        synchronized (connStatus) {
            for (int i = 0; i < currConnections; i++) {
                if (connStatus[i] > 0) {
                    useCount++;
                }
            }
        }
        return useCount;
    }

    /** Returns the number of connections in the dynamic pool.
     * @return The number of ConnectionBean objects created.
     */
    public int getSize() {
        return currConnections;
    }

    /** Provides information about the current state of this ConnectionBeanProvider.
     * @return Information string, formatted in HTML.
     */
    public String getInfo() {
        int totalUsageCount = globalUsageCount;
        StringBuffer sb = new StringBuffer();
        sb.append("<b>PooledConnectionBeanProvider ").append(hashCode()).append("</b><br>\n");
        sb.append(super.getInfo());
        sb.append("PID: ").append(pid).append("<br>\n");
        sb.append("LogFileString: ").append(logFileString).append("<br>\n");
        sb.append("currConnections: ").append(currConnections).append("<br>\n");
        sb.append("connLast: ").append(connLast).append("<br>\n");
        sb.append("minConns: ").append(minConns).append("<br>\n");
        sb.append("maxConns: ").append(maxConns).append("<br>\n");
        sb.append("maxConnMSec: ").append(maxConnMSec).append("<br>\n");
        sb.append("maxCheckoutSeconds: ").append(maxCheckoutSeconds).append("<br>\n");
        sb.append("debugLevel: ").append(debugLevel).append("<br>\n");
        sb.append("CURRENT CONNECTIONS:<br>\n");
        sb.append("<hr>\n");
        for (int i = 0; i < maxConns; i++) {
            ConnectionBean cb = connPool[i];
            if (cb == null) sb.append("Empty ConnectionBean<br>\n"); else {
                sb.append("Id: " + connID[i] + "<br>\n");
                sb.append("Status: " + connStatus[i] + "<br>\n");
                sb.append("LockTime: " + connLockTime[i] + "<br>\n");
                sb.append("CreateDate: " + new Date(connCreateDate[i]) + "<br>\n");
                sb.append("------------\n");
                sb.append(cb.getInfo());
                totalUsageCount += cb.usageCount;
            }
            sb.append("<hr>\n");
        }
        sb.append("TOTAL STATEMENTS USED: ").append(totalUsageCount).append("<br>\n");
        return sb.substring(0);
    }
}
