package dbaccess.util2;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * This class establishes and closes connections to a database via JDBC.
 * It uses the <b>MySQL connectorj</b> JDBC driver available from the MySQL
 * web site.
 * <p>
 * These JDBC properties can be supplied on the command line or in the
 * SPIDR property files.  See the <i>DBProperties</i> class for a description
 * of the properties.
 * <p>
 * Connection properties can be specified by either providing the JDBC
 * connection URL.  If not supplied a default URL is build based on defaults
 * for all the JDBC connection URL components (such as host, port, dbms,
 * database, etc.).  You can override any of these JDBC connection URL
 * components defaults either by placing a value in the SPIDR configuration
 * files or setting a value on the command line.  If a connection URL is
 * provided, it takes precedence, even if all the components of a JDBC
 * connection are also provided.
 * <p>
 * More specifically, you must provide either:
 * <table>
 * <tr><td colspan=2>These must be provided (or the defaults used) and are common to both methods.</td></tr>
 * <tr><td><b>--login</b></td>   <td>Database login name for connecting</td></tr>
 * <tr><td><b>--password</b></td><td>Database login password</td></tr>
 * <tr><td><b>--driver</b></td>  <td>JDBC driver</td></tr>
 * <tr><td colspan=2>You must provide either the JDBC connection URL (no default available)</td></tr>
 * <tr><td><b>--url</b></td>     <td>JDBC database connection URL</td></tr>
 * <tr><td colspan=2>Or the indivdual components that make up the URL (these have defaults provided.</td></tr>
 * <tr><td><b>--database</b></td><td>Database</td></tr>
 * <tr><td><b>--dbms</b></td>    <td>DBMS</td></tr>
 * <tr><td><b>--host</b></td>    <td>Name of host system</td></tr>
 * <tr><td><b>--port</b></td>    <td>Port on host for connection</td></tr>
 * <tr><td><b>--protocol</b></td><td>Database protocol</td></tr>
 * </table>
 * <p>
 * The command line options are parsed by and stored in a object of the
 * {@link dbaccess.util2.DBProperties DBProperties} class (which extends the
 * {@link java.util.Properties java.util.Properties} class).
 * <p>
 *  The <i>DBProperties</i> class supports short-cut forms for these JDBC properties.
 * <p>
 * The DBMS defaults to <b>mysql</b> on port <b>3306</b>.  The
 * constructors connect to the database and create the JDBC Connection
 * and Statement objects.  The connection can be closed with the <i>close</i>
 * method.
 * <p>
 * The database, login id and password may or may not be passed as
 * parameters to the constructors.  The defaults used depend on the 
 * parameters supplied:
 * <p>
 * Login:
 * <ul>
 * <li><b>login parameter supplied</b> - This is used overriding all defaults.
 * <li><b>login=""</b> - The login defaults to the operating system login ID.
 * <li><b>login not supplied</b> - The login defaults <b>guest</b>.
 * </ul>
 * <p>
 * Password:
 * <ul>
 * <li><b>password parameter supplied</b> - This overrides all defaults.
 * <li><b>login="guest"</b> - The password defaults to guest's password.
 * <li><b>login!="guest"; password=""</b> - The password is prompted.
 * <li><b>Neither login nor password supplied</b> - The default login <b>guest</b>
 * and its password are used.
 * </ul>
 * <p>
 * Likewise the URL, login id and password may or may not be passed as a
 * parameter to the constructors.  The default URL used depends on the 
 * parameters:
 * <p>
 * URL:
 * <ul>
 * <li><b>URL parameter supplied</b> - This overrides all defaults.
 * <li><b>URL="local"</b> - URL defaults to the localhost URL.
 * <li><b>URL not supplied</b> - URL defaults to esg3.ngdc.noaa.gov.
 * </ul>
 */
public class DBConnect {

    private static Log logger = LogFactory.getLog(DBConnect.class);

    Connection Con;

    Statement Stmt;

    DBProperties spidrProp = null;

    String dbms;

    String dbprotocol;

    String host;

    String port;

    String database;

    String login;

    String password;

    String driver;

    String url = null;

    String propPrefix;

    static final String LOCAL = "localhost.localdomain";

    static final String LOCAL_ALIAS = "local";

    static final String NGDC = ".ngdc.noaa.gov";

    static final String NGDC_ALIAS = "ngdc";

    static final String MYSQL = "mysql";

    static final String MYSQL_PROTOCOL = "mysql";

    static final String MYSQL_PORT = "3306";

    static final String MYSQL_DATABASE = "ionodb";

    static final String SQLITE = "sqlite";

    static final String SQLITE_PROTOCOL = "sqlite";

    static final String SQLITE_PORT = null;

    static final String SQLITE_DATABASE = "ionodb";

    static final String MM = "com.mysql.jdbc.Driver";

    static final String MM_ALIAS = "mm";

    static final String LITE = "org.sqlite.JDBC";

    static final String DEFAULT_DBMS = MYSQL;

    static final String DEFAULT_HOST = LOCAL;

    static final String DEFAULT_DBPROTOCOL = MYSQL_PROTOCOL;

    static final String DEFAULT_PORT = MYSQL_PORT;

    static final String DEFAULT_DATABASE = MYSQL_DATABASE;

    static final String DEFAULT_DRIVER = MM;

    static final String DEFAULT_LOGIN = "guest";

    static final String DEFAULT_PASSWORD = "ReadOnly";

    /**
   * Use this constructor when you want to set properties with the
   * <i>set</i> methods prior to connecting.
   */
    public DBConnect() {
        setDefaultProp();
        getConnectionProp();
    }

    /**
   * Use this constructor when you want to connect using properties from
   * the command line or properties file.
   */
    public DBConnect(DBProperties prop) {
        setDefaultProp();
        this.spidrProp = prop;
        getConnectionProp();
    }

    /**
   * Use this constructor when you want to connect using properties from
   * the command line or properties file.
   */
    public DBConnect(DBProperties prop, String prefix) {
        setDefaultProp();
        this.propPrefix = prefix;
        this.spidrProp = prop;
        getConnectionProp();
    }

    /**
   * Use this constructor when you want to use the default login, <b>guest</b>,
   * for read only access to the JDBC connection.
   * @param db Database to connect to
   */
    public DBConnect(String db) {
        setDefaultProp();
        this.database = db;
        this.spidrProp = new DBProperties();
        getConnectionProp();
        connect();
    }

    /**
   * Use this constructor when you want to use the default login, <b>guest</b>,
   * for read only access to the JDBC connection.
   * @param url URL (without the database)
   * @param db Database to connect to
   */
    public DBConnect(String url, String db) {
        setDefaultProp();
        this.url = url;
        this.database = db;
        getConnectionProp();
        connect();
    }

    /**
   * Use this constructor when you have a login and password known when
   * you want to use for the JDBC connection.  This is useful, for 
   * example, if the login id and password is hardcoded in the program
   * or parsed or prompted ahead of time.
   * @param db Database to connect to
   * @param uid database login ID
   * @param pwd Password for the database login
   */
    public DBConnect(String db, String uid, String pwd) {
        setDefaultProp();
        this.database = db;
        this.login = uid;
        this.password = pwd;
        this.spidrProp = new DBProperties();
        getConnectionProp();
        connect();
    }

    /**
   * Use this constructor when you want to change the default URL and
   * also have a login and password known when
   * you want to use for the JDBC connection.  This is useful, for
   * example, if the login id and password is hardcoded in the program
   * or parsed or prompted ahead of time.
   * @param url URL (without the database) to connect to
   * @param db Database to connect to
   * @param login database login ID
   * @param pwd Password for the database login
   */
    public DBConnect(String url, String db, String login, String pwd) {
        setDefaultProp();
        this.url = url;
        this.database = db;
        this.login = login;
        this.password = pwd;
        getConnectionProp();
        connect();
    }

    /**
   * Use this constructor when you want to create a second connection to the
   * same url, login and password as the first.
   * @param c1 First connection
   */
    public DBConnect(DBConnect c1) {
        this.database = c1.getDatabase();
        this.login = c1.getLogin();
        this.password = c1.getPassword();
        this.driver = c1.getDriver();
        this.dbprotocol = c1.getDBProtocol();
        this.dbms = c1.getDbms();
        this.host = c1.getHost();
        this.port = c1.getPort();
        this.url = c1.getUrl();
        if (this.url == null) this.url = makeURL();
        try {
            this.Con = DriverManager.getConnection(this.url, this.login, this.password);
            this.Stmt = this.Con.createStatement();
        } catch (SQLException e) {
            logger.error("Unable to establish another connection. " + e.getMessage());
            print();
        }
    }

    /**
    * Accessor method to get the JDBC Connection object
    * @return The database Connection object
    */
    public Connection getConnection() {
        return this.Con;
    }

    /**
    * Accessor method to get the JDBC Statement object
    * @return The database Statement object
    */
    public Statement getStatement() {
        return this.Stmt;
    }

    /**
    * Get the table of properties
    * @return The hash table of properties
    */
    public Properties getProp() {
        return this.spidrProp;
    }

    /**
    * Get the login id
    * @return The login id
    */
    public String getLogin() {
        return this.login;
    }

    /**
    * Get the login password
    * @return The login password
    */
    public String getPassword() {
        return this.password;
    }

    /**
    * Get the url
    * @return The url (without database)
    */
    public String getUrl() {
        return this.url;
    }

    /**
    * Get the database
    * @return The database
    */
    public String getDatabase() {
        return this.database;
    }

    /**
    * Get the JDBC driver
    * @return The database
    */
    public String getDriver() {
        return this.driver;
    }

    /**
    * Get the host name
    * @return The database
    */
    public String getHost() {
        return this.host;
    }

    /**
    * Get the port
    * @return The port
    */
    public String getPort() {
        return this.port;
    }

    /**
    * Get the database protocol
    * @return The database
    */
    public String getDBProtocol() {
        return this.dbprotocol;
    }

    /**
    * Get the database management system name (DBMS)
    * @return The dbms
    */
    public String getDbms() {
        return this.dbms;
    }

    /**
    * Get a properties value
    * @return The value of the property requested
    */
    public String getProperty(String propName) {
        return System.getProperty(propName);
    }

    /**
    * Set the configuration properties.
    * @param prop SPIDR properties from <i>DBProperties</i> class.
    */
    public void setProperties(DBProperties prop) {
        this.spidrProp = prop;
    }

    /**
    * Set the login.
    * @param uid The login id.  If an empty uid or a uid of <i>default</i>
    * is sent, the default login id and password is set.
    */
    public void setLogin(String uid) {
        this.login = uid;
    }

    /**
    * Set the login password.
    * @param pwd The login password.  If an empty pwd or a pwd of <i>default</i>
    * is sent, the default passord is set.
    */
    public void setPassword(String pwd) {
        this.password = pwd;
    }

    /**
    * Set the database
    * @param db The database
    */
    public void setDatabase(String db) {
        if (db == null) return;
        this.database = db;
    }

    /**
    * Set the database management system (DBMS)
    * @param dbmsname The dbms
    */
    public void setDbms(String dbmsname) {
        if (dbmsname == null) return;
        this.dbms = dbmsname;
    }

    /**
    * Set the database protocol
    * @param protocol The protocol identifier
    */
    public void setDBProtocol(String protocol) {
        if (dbprotocol == null) return;
        this.dbprotocol = protocol;
    }

    /**
    * Set the host name
    * @param hostname The hostname
    */
    public void setHost(String hostname) {
        if (hostname == null) return;
        if (hostname.equalsIgnoreCase(LOCAL_ALIAS)) this.host = LOCAL; else {
            this.host = hostname;
            if (this.host.indexOf('.') < 0) this.host += NGDC;
        }
    }

    /**
    * Set the property prefix
    * @param prefix The property prefix
    */
    public void setPropPrefix(String prefix) {
        this.propPrefix = prefix;
    }

    /**
    * Set the database port
    * @param dbport The port
    */
    public void setPort(String dbport) {
        if (dbport == null) return;
        this.port = dbport;
    }

    /**
    * Set the JDBC driver
    * @param jdbcDriver The JDBC driver name (actually this is the name of the
    * JDBC driver package in the class directory.  Valid URLs include
    * <i>local</i> to connect to a database on the local host.
    */
    public void setDriver(String jdbcDriver) {
        if (jdbcDriver == null) return;
        if (jdbcDriver.equalsIgnoreCase(MM_ALIAS)) this.driver = MM; else driver = jdbcDriver;
    }

    /**
    * Set the url
    * @param url The url
    */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
    * This creates a URL if one was not supplied
    */
    private String makeURL() {
        if (host != null && port != null) return "jdbc:" + dbprotocol + "://" + host + ":" + port + "/" + database; else return "jdbc:" + dbprotocol + ":" + database;
    }

    /**
    * This overrides the database in a URL if one was provided on the command
    * line.
    */
    private String checkDB() {
        if (url == null) return makeURL();
        if (database == null) return url;
        Pattern pattern = Pattern.compile("(.*://.*/)(\\w+)(\\?.*)");
        Matcher matcher = pattern.matcher(url);
        boolean matchFound = matcher.find();
        String urldb = null;
        if (matchFound) urldb = matcher.group(2);
        if (urldb == null) return url;
        if (urldb.equals(this.database)) return url;
        return matcher.group(1) + this.database + matcher.group(3);
    }

    /**
    * Prompt for password.
    */
    private String passwordPrompt() {
        String pwd = "";
        System.out.print("Password: ");
        System.out.flush();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            pwd = in.readLine();
        } catch (Exception e) {
            logger.error("Password prompt: " + e.getMessage());
        }
        return pwd;
    }

    /**
    * Establish a connection supplying a url
    * @param url URL (without the database) to connect to
    */
    public boolean connect(String url) {
        this.url = url;
        return connect();
    }

    /**
    * This does the actual work to make the connection.
    */
    public boolean connect() {
        try {
            Class.forName(this.driver);
        } catch (Exception e) {
            logger.error("Unable to load driver. " + e.getMessage());
            print();
            return false;
        }
        if (this.url == null) this.url = makeURL(); else this.url = checkDB();
        try {
            if (this.login != null) this.Con = DriverManager.getConnection(this.url, this.login, this.password); else this.Con = DriverManager.getConnection(this.url);
            this.Stmt = Con.createStatement();
        } catch (SQLException e) {
            logger.error("Unable to establish a connection. " + e.getMessage());
            print();
            return false;
        }
        return true;
    }

    /**
    * This method sets connection defaults.  Login and passwords are no
    * longer defaulted.  They must be set in either the spidr config file
    * (/etc/spidr/iono.conf) or on the command line.
    */
    public void setDefaultProp() {
        this.dbms = DEFAULT_DBMS;
        this.database = DEFAULT_DATABASE;
        this.host = DEFAULT_HOST;
        this.dbprotocol = DEFAULT_DBPROTOCOL;
        this.port = DEFAULT_PORT;
        this.driver = DEFAULT_DRIVER;
        this.propPrefix = "";
        this.url = null;
    }

    /**
    * This method sets connection variables from the SPIDR properties object.
    */
    public void getConnectionProp() {
        if (spidrProp == null) return;
        this.login = spidrProp.getProperty("dbLogin");
        this.password = spidrProp.getProperty("dbPassword");
        this.driver = spidrProp.getProperty("dbDriver", driver);
        this.url = spidrProp.getProperty("dbUrl", url);
        this.host = spidrProp.getProperty("host", host);
        this.dbms = spidrProp.getProperty("dbms", dbms);
        this.dbprotocol = spidrProp.getProperty("dbprotocol", dbprotocol);
        this.port = spidrProp.getProperty("port", port);
        this.database = spidrProp.getProperty("database", database);
        String prefix = spidrProp.getProperty("propPrefix", propPrefix);
        if (!prefix.equals("")) {
            this.driver = spidrProp.getProperty(prefix + ".dbDriver", driver);
            this.url = spidrProp.getProperty(prefix + ".dbUrl", url);
            this.host = spidrProp.getProperty(prefix + ".host", host);
            this.dbms = spidrProp.getProperty(prefix + ".dbms", dbms);
            this.dbprotocol = spidrProp.getProperty(prefix + ".dbprotocol", dbprotocol);
            this.port = spidrProp.getProperty(prefix + ".port", port);
            this.database = spidrProp.getProperty(prefix + ".database", database);
        }
        if (this.host.equalsIgnoreCase(LOCAL_ALIAS)) this.host = LOCAL; else if (this.host.indexOf('.') < 0) this.host += NGDC;
        if (this.driver.equalsIgnoreCase(MM_ALIAS)) this.driver = MM;
        if (this.password.equals("") && this.login.equals(DEFAULT_LOGIN)) this.password = DEFAULT_PASSWORD;
        if (this.password.equals("")) this.password = passwordPrompt();
        if (this.dbprotocol.equals("")) {
            if (this.dbms.equals(MYSQL)) this.dbprotocol = MYSQL_PROTOCOL; else this.dbprotocol = DEFAULT_DBPROTOCOL;
        }
        if (this.port.equals("")) {
            if (this.dbms.equals(MYSQL)) this.port = MYSQL_PORT; else this.port = DEFAULT_PORT;
        }
        if (driver.equals("")) {
            if (this.dbms.equals(MYSQL)) this.driver = MM; else this.driver = DEFAULT_DRIVER;
        }
    }

    /**
    * Close the connection to the database
    */
    public void close() {
        try {
            this.Stmt.close();
            this.Con.close();
        } catch (Exception e) {
            logger.error("Problem closing the database connection. " + e.getMessage());
        }
    }

    /**
    * Display connection variables
    */
    private void print() {
        logger.info("Connection Variables:");
        logger.info("   Login: " + this.login);
        String pwd = this.password;
        if (pwd != null) pwd = "********";
        logger.info("   Password: " + pwd);
        if (this.url == null) {
            logger.info("   Host: " + this.host);
            logger.info("   Port: " + this.port);
            logger.info("   DBMS: " + this.dbms);
            logger.info("   Database Protocol: " + this.dbprotocol);
            logger.info("   Database: " + this.database);
        } else {
            logger.info("   URL: " + this.url);
        }
        logger.info("   JDBC Driver: " + this.driver);
    }
}
