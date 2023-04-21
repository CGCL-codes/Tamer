package ch.olsen.products.util.database.otsdb;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import ch.olsen.products.util.ConnectionConfig;
import ch.olsen.products.util.LoginConfig;
import ch.olsen.products.util.configuration.PropertyReference;
import ch.olsen.products.util.database.Pair;
import ch.olsen.products.util.database.RequestException;
import ch.olsen.products.util.database.Request.RequestExceptionConnection;
import ch.olsen.products.util.database.Request.RequestExceptionNoDriver;
import ch.olsen.products.util.database.RequestException.Reason;
import ch.olsen.products.util.database.otsdb.Otsdb.OtsdbLogin.OtsdbLoginParseException;
import ch.olsen.products.util.database.otsdb.OtsdbPool.OtsdbUnit;
import ch.olsen.products.util.database.otsdb.OtsdbPool.OtsdbUnit.OtsdbConnectionException;

/**
 * Database client, uses a pool of connections.
 * Connections are shared until one is locked, then the pool provides a non
 * locked connection.
 * This class implement the arbitrage to know if we are the locker (error in
 * the code that we should handle) or if we should get a new connection.
 */
public class Otsdb implements OtsdbInterface {

    protected static Map<OtsdbLogin, OtsdbPool> pools = new HashMap<OtsdbLogin, OtsdbPool>();

    OtsdbPool pool = null;

    private HashMap<Pair<Statement, ResultSet>, OtsdbUnit> pendingResults = new HashMap<Pair<Statement, ResultSet>, OtsdbUnit>();

    protected OtsdbUnit worker = null;

    private OtsdbLogin login = new OtsdbLogin();

    public ConnectionConfig connectionConfig = new ConnectionConfig();

    public static final String instrumentSeparator = "-";

    public Otsdb clone() {
        Otsdb db = new Otsdb();
        db.login.copy(this.login);
        db.connectionConfig.maxRetry = this.connectionConfig.maxRetry;
        db.connectionConfig.wait = this.connectionConfig.wait;
        return db;
    }

    /**
	 * Information needed to access the database
	 */
    public static class OtsdbLogin extends LoginConfig {

        protected String instanceName = "";

        public String database;

        public Properties properties = null;

        public static class OtsdbLoginParseException extends Exception {

            private static final long serialVersionUID = 1L;

            public OtsdbLoginParseException(String message) {
                super(message);
            }
        }

        public OtsdbLogin(String server, String database, String user, String pwd) {
            super(server, user, pwd);
            this.database = database;
        }

        public OtsdbLogin() {
            super();
            database = "otsdb";
        }

        public OtsdbLogin(String instanceName) {
            super();
            this.instanceName = instanceName;
            database = "otsdb";
        }

        public Properties loadConfig(String configFile) {
            Properties properties = super.loadConfig(configFile);
            database = (String) properties.get("database");
            return properties;
        }

        public void parseArgs(String[] args) throws OtsdbLoginParseException {
            for (int i = 0; i < args.length; i++) {
                if (args[i].compareTo("-dbhost" + instanceName) == 0) {
                    server = args[++i];
                } else if (args[i].compareTo("-dbname" + instanceName) == 0) {
                    database = args[++i];
                } else if (args[i].compareTo("-dbuser" + instanceName) == 0) {
                    user = args[++i];
                } else if (args[i].compareTo("-dbpwd" + instanceName) == 0) {
                    pwd = args[++i];
                } else if (args[i].compareTo("--help") == 0 || args[i].compareTo("-h") == 0) {
                } else if (args[i].compareTo("-db") == 0) {
                    loadConfig(args[++i]);
                }
            }
            if (server.length() == 0) {
                throw new OtsdbLoginParseException("No db host specified");
            }
        }

        public void parseProperties(PropertyReference<OtsDBConfiguration> cfg) {
            server = cfg.get().dbServer.value();
            database = cfg.get().dbName.value();
            user = cfg.get().dbUser.value();
            pwd = cfg.get().dbPwd.value();
        }

        public String toString() {
            String formatted = super.toString() + " on " + database;
            if (properties != null) {
                for (Entry property : properties.entrySet()) {
                    formatted += " " + property.getKey().toString() + "=" + property.getValue().toString();
                }
            }
            return formatted;
        }

        public boolean equals(Object other) {
            OtsdbLogin o = null;
            if (other instanceof OtsdbLogin) o = (OtsdbLogin) other; else return false;
            return (super.equals(o) && database.equals(o.database) && (properties == null ? o.properties == null : properties.equals(o.properties)));
        }

        public void copy(OtsdbLogin other) {
            database = other.database;
            if (other.properties == null) {
                properties = null;
            } else {
                properties = new Properties();
                properties.putAll(other.properties);
            }
            super.copy(other);
        }

        public void setExtraProperties(Properties properties) {
            this.properties = properties;
        }
    }

    public void connect() throws OtsdbConnectionException {
        getWorker().connect();
    }

    public long executeUpdate(String sql) throws RequestException {
        return getWorker().executeUpdate(sql);
    }

    public Pair<Statement, ResultSet> executeQuery(String sql) throws RequestException {
        Pair<Statement, ResultSet> result = getWorker().executeQuery(sql);
        synchronized (pendingResults) {
            pendingResults.put(result, worker);
        }
        return result;
    }

    public final boolean isLocked() {
        return getWorker().isLocked();
    }

    public void prepareLock() {
        if (worker == null || worker == getPool().getOtsdb()) worker = getPool().getLockWorker();
    }

    public synchronized void lock(String[] read, String[] write) throws RequestException {
        if (isLocked()) throw new RequestException("failed to lock because we already own a lock", Reason.LOCKED);
        prepareLock();
        worker.lock(read, write);
    }

    public synchronized void unlock() throws RequestException {
        if (worker != null) worker.unlock();
        worker = null;
        worker = getWorker();
    }

    public ConnectionConfig getTemplateConnectInfo() {
        return connectionConfig;
    }

    public ConnectionConfig getConnectInfo() {
        return getWorker().getConnectInfo();
    }

    public void connect(String host, String database, String user, String pwd) throws RequestException, RequestExceptionConnection, RequestExceptionNoDriver {
        OtsdbLogin login = new OtsdbLogin(host, database, user, pwd);
        getNewPool(login).getOtsdb().connect();
    }

    public void disconnect() throws RequestExceptionConnection {
        closeAll();
        getWorker().disconnect();
        worker = null;
    }

    public void close(Pair<Statement, ResultSet> result) {
        OtsdbUnit worker = null;
        synchronized (pendingResults) {
            worker = pendingResults.remove(result);
        }
        if (worker != null) worker.close(result);
    }

    public void closeAll() {
        synchronized (pendingResults) {
            for (Entry<Pair<Statement, ResultSet>, OtsdbUnit> entry : pendingResults.entrySet()) {
                OtsdbUnit worker = entry.getValue();
                if (worker != null) worker.close(entry.getKey());
            }
            pendingResults.clear();
        }
    }

    /**
	 * get current worker or a new one if needed
	 * this function makes the arbitrage so:
	 * internal data member worker should not be accessed directly
	 * @return worker
	 */
    protected synchronized OtsdbUnit getWorker() {
        if (worker == null) worker = getPool().getOtsdb();
        return worker;
    }

    protected final synchronized OtsdbPool getPool() {
        if (pool == null) {
            pool = getNewPool(login);
        }
        return pool;
    }

    private synchronized OtsdbPool getNewPool(OtsdbLogin login) {
        if (!this.login.equals(login)) {
            this.login = login;
            pool = null;
        }
        synchronized (pools) {
            if (pool == null) pool = pools.get(login);
            if (pool == null) {
                pool = new OtsdbPool(login);
                pool.setConnectionInfo(connectionConfig);
                pools.put(login, pool);
            }
            return pool;
        }
    }

    public void loadConfig(String otsdbCfg) {
        OtsdbLogin login = new OtsdbLogin();
        login.loadConfig(otsdbCfg);
        getNewPool(login).getOtsdb();
    }

    public void parseArgs(String[] args) throws OtsdbLoginParseException {
        OtsdbLogin login = new OtsdbLogin();
        login.parseArgs(args);
        getNewPool(login);
    }

    public void parseProperties(PropertyReference<OtsDBConfiguration> cfg) {
        OtsdbLogin login = new OtsdbLogin();
        login.parseProperties(cfg);
        getNewPool(login);
    }

    public OtsdbLogin getLogin() {
        return login;
    }

    public void setLogin(OtsdbLogin login) {
        getNewPool(login);
    }

    public void finalize() {
        closeAll();
        try {
            unlock();
        } catch (RequestException e) {
        }
    }
}
