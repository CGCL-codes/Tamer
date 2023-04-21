package org.fao.waicent.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletContext;
import org.fao.waicent.util.Debug;
import org.fao.waicent.util.ini;

public class dbConnectionManager extends Thread {

    public static final String PARAM_NAME = "name";

    public static final String PARAM_DRIVER = "driver";

    public static final String PARAM_DATABASE = "database";

    public static final String PARAM_DEBUG = "debug";

    public static final String PARAM_VERBOSE = "verbose";

    public static final String PARAM_SINGLE_SHARED_CONNECTION = "single_shared_connection";

    public static final String PARAM_CONNECTIONS = "connections";

    public static final String PARAM_SCHEMA_NAME = "schema_name";

    public static final String PARAM_CONNECTION_ACTIVE_TIMEOUT = "connection_active_timeout";

    public static final String PARAM_CONNECTION_IDLE_TIMEOUT = "connection_idle_timeout";

    public static final String PARAM_CLIENT_WAIT_TIMEOUT = "client_wait_timeout";

    public static final String PARAM_CLIENT_MAX_ATTEMPTS = "client_max_attempts";

    public static final String PARAM_USERNAME = "username";

    public static final String PARAM_PASSWORD = "password";

    public static final String PARAM_TOKEN = "token";

    public static final String PARAM_XPASSWORD = "xpassword";

    public static final String PARAM_GENERATE = "generate";

    public static final String PARAM_ENCRYPT = "encrypt";

    public static long CONNECTION_ACTIVE_TIMEOUT = 10 * 60 * 1000;

    public static long CONNECTION_IDLE_TIMEOUT = 60 * 1000;

    public static long CLIENT_WAIT_TIMEOUT = 1000;

    public static long CLIENT_MAX_ATTEMPTS = 2;

    private Vector connectionPool;

    private Vector clientQueue;

    private dbConnectionHandle single_shared_connection = null;

    public boolean is_single_shared_connection = false;

    private String name, Driver, dbName, User, Password, schema;

    public final String version = "4 February 2003";

    public boolean auto_commit = false;

    public long connection_active_timeout = CONNECTION_ACTIVE_TIMEOUT;

    public long connection_idle_timeout = CONNECTION_IDLE_TIMEOUT;

    public long client_wait_timeout = CLIENT_WAIT_TIMEOUT;

    public long client_max_attempts = CLIENT_MAX_ATTEMPTS;

    public dbConnectionManager(String ini_filename) throws IOException {
        try {
            init(ini_filename, new ini(ini_filename));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public dbConnectionManager(Hashtable hashtable) {
        init(hashtable.toString(), hashtable);
    }

    public dbConnectionManager(ServletContext context) {
        logger = context;
        init(context.getRealPath("/WEB-INF/web.xml"), context);
    }

    public dbConnectionManager(String _driver, String _dbName, String _token, int max_cons) throws Exception {
        Hashtable hashtable = new Hashtable();
        hashtable.put(PARAM_NAME, "");
        hashtable.put(PARAM_DRIVER, _driver);
        hashtable.put(PARAM_DATABASE, _dbName);
        hashtable.put(PARAM_CONNECTIONS, Integer.toString(max_cons));
        hashtable.put(PARAM_TOKEN, _token);
        init("", hashtable);
    }

    public dbConnectionManager(String _driver, String _dbName, String _user, String _password, int max_cons) {
        Hashtable hashtable = new Hashtable();
        hashtable.put(PARAM_NAME, "");
        hashtable.put(PARAM_DRIVER, _driver);
        hashtable.put(PARAM_DATABASE, _dbName);
        hashtable.put(PARAM_CONNECTIONS, Integer.toString(max_cons));
        hashtable.put(PARAM_USERNAME, _user);
        hashtable.put(PARAM_PASSWORD, _password);
        init("", hashtable);
    }

    public dbConnectionManager(String name, String _driver, String _dbName, String _user, String _password, int max_cons) {
        Hashtable hashtable = new Hashtable();
        hashtable.put(PARAM_NAME, name);
        hashtable.put(PARAM_DRIVER, _driver);
        hashtable.put(PARAM_DATABASE, _dbName);
        hashtable.put(PARAM_CONNECTIONS, Integer.toString(max_cons));
        hashtable.put(PARAM_USERNAME, _user);
        hashtable.put(PARAM_PASSWORD, _password);
        init("", hashtable);
    }

    protected void init(String name, Object o) {
        this.name = name;
        if (getParameter(o, PARAM_DEBUG) != null) {
            try {
                setLog(Boolean.valueOf(getParameter(o, PARAM_DEBUG)).booleanValue());
            } catch (Exception e) {
                log("dbConnectionManager: invalid debug : " + name, e);
            }
        }
        if (getParameter(o, PARAM_VERBOSE) != null) {
            try {
                setVerbose(Boolean.valueOf(getParameter(o, PARAM_VERBOSE)).booleanValue());
            } catch (Exception e) {
                log("dbConnectionManager: invalid verbose : " + name + " " + e);
            }
        }
        int max_connections = 3;
        if (getParameter(o, PARAM_SINGLE_SHARED_CONNECTION) != null) {
            try {
                is_single_shared_connection = Boolean.valueOf(getParameter(o, PARAM_SINGLE_SHARED_CONNECTION)).booleanValue();
            } catch (Exception e) {
                log("dbConnectionManager: invalid single_shared_connection : " + name, e);
            }
        }
        if (getParameter(o, PARAM_CONNECTIONS) != null) {
            try {
                max_connections = Integer.parseInt(getParameter(o, PARAM_CONNECTIONS));
            } catch (Exception e) {
                log("dbConnectionManager: invalid connections : " + name, e);
            }
        }
        if (getParameter(o, PARAM_SCHEMA_NAME) != null) {
            schema = getParameter(o, PARAM_SCHEMA_NAME);
        }
        if (getParameter(o, PARAM_CONNECTION_ACTIVE_TIMEOUT) != null) {
            try {
                connection_active_timeout = Integer.parseInt(getParameter(o, PARAM_CONNECTION_ACTIVE_TIMEOUT));
            } catch (Exception e) {
                log("dbConnectionManager: invalid connection_active_timeout : " + name, e);
            }
        }
        if (getParameter(o, PARAM_CONNECTION_IDLE_TIMEOUT) != null) {
            try {
                connection_idle_timeout = Integer.parseInt(getParameter(o, PARAM_CONNECTION_IDLE_TIMEOUT));
            } catch (Exception e) {
                log("dbConnectionManager: invalid connection_idle_timeout : " + name, e);
            }
        }
        if (getParameter(o, PARAM_CLIENT_WAIT_TIMEOUT) != null) {
            try {
                client_wait_timeout = Integer.parseInt(getParameter(o, PARAM_CLIENT_WAIT_TIMEOUT));
            } catch (Exception e) {
                log("dbConnectionManager: invalid client_wait_timeout : " + name, e);
            }
        }
        if (getParameter(o, PARAM_CLIENT_MAX_ATTEMPTS) != null) {
            try {
                client_max_attempts = Integer.parseInt(getParameter(o, PARAM_CLIENT_MAX_ATTEMPTS));
            } catch (Exception e) {
                log("dbConnectionManager: invalid client_max_attempts : " + name, e);
            }
        }
        String username = getParameter(o, PARAM_USERNAME);
        String password = getParameter(o, PARAM_PASSWORD);
        String token = getParameter(o, PARAM_TOKEN);
        String xpassword = getParameter(o, PARAM_XPASSWORD);
        if (token != null && token.length() > 0) {
            String[] decypted = decrypt(token);
            username = decypted[0];
            password = decypted[1];
            verbose("dbConnectionManager: token (enrcypted username and password)");
        } else if (xpassword != null && xpassword.length() > 0) {
            password = base64Decode(xpassword);
            verbose("dbConnectionManager: xpassword (encoded):");
        } else {
            password = getParameter(o, PARAM_PASSWORD);
        }
        String generate = getParameter(o, PARAM_GENERATE);
        if (generate != null) {
            log("dbConnectionManager: generate no longer supported use encrypt: \"" + "\"");
        }
        String encrypt = getParameter(o, PARAM_ENCRYPT);
        if (encrypt != null) {
            log("dbConnectionManager: encrypted username/password: \"token=" + encrypt(username, password) + "\"");
        }
        init(name, getParameter(o, PARAM_DRIVER), getParameter(o, PARAM_DATABASE), username, password, max_connections);
    }

    protected void init(String _name, String _driver, String _dbName, String _user, String _password, int max_cons) {
        name = _name;
        Driver = _driver;
        dbName = _dbName;
        User = _user;
        if (_password != null && _password.trim().length() != _password.length()) {
            log("--] dbConnectionManager: WARNING password starts or end with whitespace !");
        }
        Password = _password;
        log("--] dbConnectionManager:" + version + ": driver=\"" + Driver + "\" dbName=\"" + dbName + "\" User=\"" + User + "\" maxConnects=" + max_cons + " ini file=" + name + " max_cons=" + max_cons + " auto_commit=" + auto_commit + " connection_active_timeout=" + connection_active_timeout + " connection_idle_timeout=" + connection_idle_timeout + " client_wait_timeout=" + client_wait_timeout + " client_max_attempts=" + client_max_attempts + " single_shared_connection=" + is_single_shared_connection);
        if (is_single_shared_connection) {
            single_shared_connection = new dbConnectionHandle(this);
            single_shared_connection.setActive();
        } else {
            connectionPool = new Vector(max_cons);
            clientQueue = new Vector(3);
            for (int x = 0; x < max_cons; x++) {
                connectionPool.addElement(new dbConnectionHandle(this));
            }
            setDaemon(true);
            start();
        }
    }

    public Connection popConnection() {
        if (is_single_shared_connection) {
            return single_shared_connection.con;
        }
        for (int attempt = 0; attempt < Math.max(client_max_attempts, 1); attempt++) {
            if (client_max_attempts == 0) {
                attempt = -1;
            }
            synchronized (clientQueue) {
                if (clientQueue.indexOf(Thread.currentThread()) == -1) {
                    clientQueue.addElement(Thread.currentThread());
                }
                dbConnectionHandle h = commisionConnection();
                if (h != null || client_wait_timeout <= 0) {
                    if (h == null) {
                        verbose("--] dbConnectionManager.popConnection: attempt=" + attempt + ", client_wait_timeout = 0, return NULL connection: " + getDescriptor() + ":" + Thread.currentThread());
                    }
                    clientQueue.removeElement(Thread.currentThread());
                    return h.con;
                }
            }
            try {
                verbose("--] dbConnectionManager.popConnection: attempt=" + attempt + ", wait for a free connection: " + getDescriptor() + ":" + Thread.currentThread());
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait(client_wait_timeout);
                }
            } catch (InterruptedException E) {
            }
            verbose("--] dbConnectionManager.popConnection: attempt=" + attempt + ", wake and try for a free connection: " + getDescriptor() + ":" + Thread.currentThread());
        }
        verbose("--] dbConnectionManager.popConnection client_max_attempts=" + client_max_attempts + " exceeded: return NULL connection: " + getDescriptor() + ":" + Thread.currentThread());
        return null;
    }

    public void pushConnection(Connection con) {
        if (con == null) {
            log("--] dbConnectionManager.push null connection: " + getDescriptor());
            return;
        }
        if (is_single_shared_connection) {
            return;
        }
        if (decommisionConnection(con) == null) {
            return;
        }
        if (connection_idle_timeout <= 0) {
            timeOutConnections();
        }
        wakeUpClients();
    }

    public void run() {
        if (is_single_shared_connection) {
            return;
        }
        long sleep = Math.min(connection_idle_timeout, connection_active_timeout);
        sleep = Math.max(sleep, 1000);
        while (true) {
            try {
                sleep(sleep);
            } catch (InterruptedException e) {
            }
            timeOutConnections();
            wakeUpClients();
        }
    }

    private void timeOutConnections() {
        Enumeration E = connectionPool.elements();
        while (E.hasMoreElements()) {
            ((dbConnectionHandle) E.nextElement()).timeOut();
        }
    }

    private void wakeUpClients() {
        synchronized (clientQueue) {
            Thread waiting = null;
            while (clientQueue.size() > 0) {
                waiting = (Thread) clientQueue.elementAt(0);
                clientQueue.removeElement(waiting);
                if (waiting.isAlive()) {
                    synchronized (waiting) {
                        waiting.notify();
                    }
                    break;
                }
            }
        }
    }

    private dbConnectionHandle decommisionConnection(Connection _Con) {
        Enumeration E = connectionPool.elements();
        dbConnectionHandle h;
        while (E.hasMoreElements()) {
            h = (dbConnectionHandle) E.nextElement();
            if (h.setInActive(_Con)) {
                return h;
            }
        }
        verbose("--] dbConnectionManager.close timed out connection comming in" + getDescriptor());
        if (_Con != null) {
            try {
                _Con.close();
            } catch (Exception E2) {
            }
        }
        return null;
    }

    private dbConnectionHandle commisionConnection() {
        Enumeration E = connectionPool.elements();
        dbConnectionHandle W;
        while (E.hasMoreElements()) {
            W = (dbConnectionHandle) E.nextElement();
            if (W.setActive()) {
                return W;
            }
        }
        return null;
    }

    public void close() {
        if (is_single_shared_connection) {
            try {
                single_shared_connection.con.close();
            } catch (Exception E2) {
            }
        }
        if (connectionPool == null) {
            return;
        }
        Enumeration E = connectionPool.elements();
        dbConnectionHandle h;
        while (E.hasMoreElements()) {
            h = (dbConnectionHandle) E.nextElement();
            try {
                h.con.close();
            } catch (Exception E2) {
            }
        }
        log("--] dbConnectionManager.close: Database Connections have been closed: " + getDescriptor());
    }

    protected void finalize() throws Throwable {
        close();
    }

    public String getDescriptor() {
        return name;
    }

    public String getIniFilename() {
        return name;
    }

    public void dump(PrintWriter out) {
        out.println("dbConnectionManager:" + version + ": ini file=" + name + " driver=" + Driver + " dbName=" + dbName + " User=" + User);
        if (is_single_shared_connection) {
            out.println("[single shared connection]= inUse=" + single_shared_connection.inUse + " since=" + (single_shared_connection.since - System.currentTimeMillis()) + " MilliSecs con=" + single_shared_connection.con + " client=" + single_shared_connection.client);
        } else {
            out.println("connection pool:");
            for (int x = 0; x < connectionPool.size(); x++) {
                dbConnectionHandle h = (dbConnectionHandle) connectionPool.elementAt(x);
                out.println("[" + x + "]= inUse=" + h.inUse + " since=" + (h.since - System.currentTimeMillis()) + " MilliSecs con=" + h.con + " client=" + h.client);
            }
            out.println("client queue:");
            for (int q = 0; q < clientQueue.size(); q++) {
                out.println("[" + q + "]=" + (Thread) clientQueue.elementAt(q));
            }
        }
        out.println("done.");
    }

    class dbConnectionHandle extends Object {

        private dbConnectionManager owner;

        private Connection con;

        private boolean inUse;

        private long since;

        private Thread client = null;

        public dbConnectionHandle(dbConnectionManager _owner) {
            owner = _owner;
            inUse = false;
            con = null;
            since = 0;
            client = null;
        }

        public synchronized void timeOut() {
            if (con != null) {
                if (inUse && (System.currentTimeMillis() - since) > connection_active_timeout) {
                    String OWNER_INIFILE_str = "Unknown owner !";
                    if (owner != null) {
                        OWNER_INIFILE_str = owner.getDescriptor();
                        if (OWNER_INIFILE_str == null) {
                            OWNER_INIFILE_str = "Unknown ini file !";
                        }
                    }
                    String CLIENT_str = "Unknown client !";
                    if (client != null) {
                        OWNER_INIFILE_str = client.toString();
                    }
                    log("--] dbConnectionManager.timeOut: connection_active_timeout: " + OWNER_INIFILE_str + ":" + CLIENT_str);
                    inUse = false;
                    con = null;
                    since = 0;
                    client = null;
                } else if (!inUse && (System.currentTimeMillis() - since) > connection_idle_timeout) {
                    String OWNER_INIFILE_str = "Unknown owner !";
                    if (owner != null) {
                        OWNER_INIFILE_str = owner.getDescriptor();
                        if (OWNER_INIFILE_str == null) {
                            OWNER_INIFILE_str = "Unknown ini file !";
                        }
                    }
                    verbose("--] dbConnectionManager.timeOut: connection_idle_timeout: " + OWNER_INIFILE_str);
                    try {
                        con.close();
                    } catch (SQLException e) {
                    }
                    con = null;
                    since = 0;
                    client = null;
                }
            }
        }

        public synchronized boolean setActive() {
            if (!inUse) {
                try {
                    if (con == null || con.isClosed()) {
                        Class.forName(owner.Driver);
                        con = DriverManager.getConnection(owner.dbName, owner.User, owner.Password);
                        con.setAutoCommit(auto_commit);
                        verbose("--] dbConnectionManager.new connection: " + owner.getDescriptor());
                    }
                    inUse = true;
                    since = System.currentTimeMillis();
                    client = Thread.currentThread();
                    return true;
                } catch (Exception E) {
                    log("dbConnectionManager:connection: setActive failed: " + owner.getDescriptor(), E);
                }
            }
            return false;
        }

        public synchronized boolean setInActive(Connection _con) {
            if (con == _con) {
                inUse = false;
                since = System.currentTimeMillis();
                client = null;
                try {
                    if (con != null) {
                        con.commit();
                        con.clearWarnings();
                    }
                } catch (Exception E) {
                }
                return true;
            }
            return false;
        }
    }

    public String getSchema() {
        return schema;
    }

    private static final int ENCODE_XORMASK = 0x5A;

    private static final char ENCODE_DELIMETER = '\002';

    private static final char ENCODE_CHAR_OFFSET1 = 'A';

    private static final char ENCODE_CHAR_OFFSET2 = 'h';

    /**     * Builds a cookie string containing a username and password.<p>     *     * Note: with open source this is not really secure, but it prevents users     * from snooping the cookie file of others and by changing the XOR mask and     * character offsets, you can easily tweak results.     *     * @param username The username.     * @param password The password.     * @return String encoding the input parameters, an empty string if one of     *      the arguments equals <code>null</code>.     */
    private static String encrypt(String username, String password) {
        StringBuffer buf = new StringBuffer();
        if (username != null && password != null) {
            byte[] bytes = (username + ENCODE_DELIMETER + password).getBytes();
            int b;
            for (int n = 0; n < bytes.length; n++) {
                b = bytes[n] ^ (ENCODE_XORMASK + n);
                buf.append((char) (ENCODE_CHAR_OFFSET1 + (b & 0x0F)));
                buf.append((char) (ENCODE_CHAR_OFFSET2 + ((b >> 4) & 0x0F)));
            }
        }
        return buf.toString();
    }

    /**     * Unrafels a cookie string containing a username and password.     * @param value The cookie value.     * @return String[] containing the username at index 0 and the password at     *      index 1, or <code>{ null, null }</code> if cookieVal equals     *      <code>null</code> or the empty string.     */
    private static String[] decrypt(String cookieVal) {
        if (cookieVal == null || cookieVal.length() <= 0) {
            return null;
        }
        char[] chars = cookieVal.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int b;
        for (int n = 0, m = 0; n < bytes.length; n++) {
            b = chars[m++] - ENCODE_CHAR_OFFSET1;
            b |= (chars[m++] - ENCODE_CHAR_OFFSET2) << 4;
            bytes[n] = (byte) (b ^ (ENCODE_XORMASK + n));
        }
        cookieVal = new String(bytes);
        int pos = cookieVal.indexOf(ENCODE_DELIMETER);
        String username = (pos < 0) ? "" : cookieVal.substring(0, pos);
        String password = (pos < 0) ? "" : cookieVal.substring(pos + 1);
        return new String[] { username, password };
    }

    static int base64[] = { 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 62, 64, 64, 64, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 64, 64, 64, 64, 64, 64, 64, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 64, 64, 64, 64, 64, 64, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64 };

    public static String base64Decode(String orig) {
        char chars[] = orig.toCharArray();
        StringBuffer sb = new StringBuffer();
        int i = 0;
        int shift = 0;
        int acc = 0;
        for (i = 0; i < chars.length; i++) {
            int v = base64[chars[i] & 0xFF];
            if (v >= 64) {
                if (chars[i] != '=') {
                    System.out.println("Wrong char in base64: " + chars[i]);
                }
            } else {
                acc = (acc << 6) | v;
                shift += 6;
                if (shift >= 8) {
                    shift -= 8;
                    sb.append((char) ((acc >> shift) & 0xff));
                }
            }
        }
        return sb.toString();
    }

    public String getParameter(Object o, String p) {
        if (o instanceof ini) {
            return ((ini) o).get(p);
        } else if (o instanceof Hashtable) {
            return (String) ((Hashtable) o).get(p);
        } else {
            return ((ServletContext) o).getInitParameter(p);
        }
    }

    public boolean isLog = true;

    void setLog(boolean f) {
        isLog = f;
        if (logger == null) {
            if (!isLog) {
                Debug.Off();
            }
        }
    }

    public boolean isVerbose = false;

    void setVerbose(boolean f) {
        isVerbose = f;
    }

    Object logger = null;

    void log(String message) {
        if (isLog) {
            if (logger != null && logger instanceof ServletContext) {
                ((ServletContext) logger).log(message);
            } else {
                Debug.println(message);
            }
        }
    }

    void log(String message, Throwable throwable) {
        if (isLog) {
            if (logger != null && logger instanceof ServletContext) {
                ((ServletContext) logger).log(message, throwable);
            } else {
                Debug.println(message + ":" + throwable);
            }
        }
    }

    void verbose(String message) {
        if (isVerbose) {
            log(message);
        }
    }

    public static void main(String[] args) {
    }
}
