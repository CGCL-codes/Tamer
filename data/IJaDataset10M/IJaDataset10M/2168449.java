package edu.uiuc.ncsa.myproxy.delegation.server;

import edu.uiuc.ncsa.myproxy.delegation.server.storage.DSTransactionTable;
import edu.uiuc.ncsa.security.core.Configuration;
import edu.uiuc.ncsa.security.storage.sql.SQLStore;
import edu.uiuc.ncsa.security.storage.sql.mysql.MySQLConnectionParameters;
import edu.uiuc.ncsa.security.storage.sql.mysql.MySQLConnectionPool;
import java.net.URI;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 26, 2011 at  4:14:42 PM
 */
public class DSConfiguration implements Configuration {

    public static final String DOMAIN = "edu.uiuc.ncsa.myproxy.delegation.server";

    public static final String MYPROXY_DOMAIN = DOMAIN + ".myproxy.service";

    /**
     * This is the name of the context parameter (in i.e. web.xml) that points to a properties file that contains
     * configuration information.
     */
    public static final String PROPERTY_FILE_DOMAIN = DOMAIN + ".properties";

    /**
     * The password associated with the gateway registration approver's account.
     */
    public static final String APPROVER_PASSWORD_KEY = PROPERTY_FILE_DOMAIN + ".approver.password";

    /**
     * The password associated with the client's store.
     */
    public static final String CLIENT_PASSWORD_KEY = PROPERTY_FILE_DOMAIN + ".client.password";

    /**
     * The password associated with the service's account.
     */
    public static final String SERVICE_PASSWORD_KEY = PROPERTY_FILE_DOMAIN + ".service.password";

    /**
     * The address of this portal.
     */
    public static final String SERVICE_ADDRESS = PROPERTY_FILE_DOMAIN + ".service.address";

    /**
     * Specify the service address of the MyProxy service.
     */
    public static final String MYPROXY_SERVICE_ADDRESS = MYPROXY_DOMAIN + ".address";

    /**
     * The port for the MyProxy service (default is 7514).
     */
    public static final String MYPROXY_SERVICE_PORT = MYPROXY_DOMAIN + ".port";

    /**
     * The username the service should use when connecting to the database.
     */
    public static final String SERVICE_USERNAME = DOMAIN + ".service.username";

    /**
     * The username the client database manager.
     */
    public static final String CLIENT_USERNAME = DOMAIN + ".client.username";

    /**
     * The username the approver of client requests should use.
     */
    public static final String APPROVER_USERNAME = DOMAIN + ".approver.username";

    public static final String ENABLE_DEBUG = DOMAIN + ".debug";

    /**
     * The host where the database resides. This defaults localhost if not specified.
     */
    public static final String DATABASE_HOST = DOMAIN + ".db.host";

    public static final String CONFIG_FILE_PATH = "/WEB-INF/";

    public static final String CONFIG_FILE_NAME = "src/main/resources/cfg.properties";

    public URI getIdentifier() {
        return null;
    }

    String myProxyServer = "myproxy.teragrid.org";

    int myProxyPort = 7514;

    String serviceAddress;

    public URI getServiceAddressUri() {
        if (serviceAddressUri == null) {
            return serviceAddressUri = URI.create(getServiceAddress());
        }
        return serviceAddressUri;
    }

    URI serviceAddressUri = null;

    public int getMyProxyPort() {
        return myProxyPort;
    }

    public void setMyProxyPort(int myProxyPort) {
        this.myProxyPort = myProxyPort;
    }

    public String getMyProxyServer() {
        return myProxyServer;
    }

    public void setMyProxyServer(String myProxyServer) {
        this.myProxyServer = myProxyServer;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    String servicePassword;

    String approverPassword;

    String clientPassword;

    protected String serviceUsername;

    protected String approverUsername;

    public String getApproverUsername() {
        return approverUsername;
    }

    public void setApproverUsername(String approverUsername) {
        this.approverUsername = approverUsername;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
    }

    protected String clientUsername;

    public String getApproverPassword() {
        return approverPassword;
    }

    public void setApproverPassword(String approverPassword) {
        this.approverPassword = approverPassword;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public void setServicePassword(String portalPassword) {
        this.servicePassword = portalPassword;
    }

    static final String DATABASE_NAME = "oauth";

    static final String DATABASE_SCHEMA = "oauth";

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    protected String databaseHost = "localhost";

    static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";

    static final int DATABASE_PORT = 3306;

    public void remove() {
    }

    public void setupServiceConnection(SQLStore sqlStore) {
        MySQLConnectionParameters pg = new MySQLConnectionParameters();
        pg.setDatabaseName(DATABASE_NAME);
        pg.setHost(getDatabaseHost());
        pg.setJdbcDriver(DATABASE_DRIVER);
        pg.setPort(DATABASE_PORT);
        pg.setSchema(DATABASE_SCHEMA);
        pg.setUserName(getServiceUsername());
        pg.setPassword(getServicePassword());
        sqlStore.setConnectionPool(new MySQLConnectionPool(pg));
    }

    public void setupClientConnection(SQLStore sqlStore) {
        MySQLConnectionParameters pg = new MySQLConnectionParameters();
        pg.setDatabaseName(DATABASE_NAME);
        pg.setHost(getDatabaseHost());
        pg.setJdbcDriver(DATABASE_DRIVER);
        pg.setPort(DATABASE_PORT);
        pg.setSchema(DATABASE_SCHEMA);
        pg.setUserName(getClientUsername());
        pg.setPassword(getClientPassword());
        sqlStore.setConnectionPool(new MySQLConnectionPool(pg));
    }

    public void setupApproverConnection(SQLStore sqlStore) {
        MySQLConnectionParameters pg = new MySQLConnectionParameters();
        pg.setDatabaseName(DATABASE_NAME);
        pg.setHost(getDatabaseHost());
        pg.setJdbcDriver(DATABASE_DRIVER);
        pg.setPort(DATABASE_PORT);
        pg.setSchema(DATABASE_SCHEMA);
        pg.setUserName(getApproverUsername());
        pg.setPassword(getApproverPassword());
        sqlStore.setConnectionPool(new MySQLConnectionPool(pg));
    }

    /**
     * This gives the columns of the table so queries can be constructed.
     * Generally changes to this are a bad idea because the result will not be spec
     * compliant. It is included in cases the spec is extended or modified. Otherwise,
     * do not change this.
     *
     * @return
     */
    public DSTransactionTable createTransactionTable() {
        DSTransactionTable stt = new DSTransactionTable();
        stt.setAccessToken("access_token");
        stt.setAccessTokenValid("access_token_valid");
        stt.setCallbackUri("oauth_callback");
        stt.setTempCredValid("temp_token_valid");
        stt.setTempCred("temp_token");
        stt.setVerifier("oauth_verifier");
        stt.setCertReq("certreq");
        stt.setCert("certificate");
        stt.setClientKey("oauth_consumer_key");
        stt.setLifetime("certlifetime");
        stt.setSchema("oauth");
        stt.setTablename("transactions");
        stt.setTablenamePrefix(null);
        return stt;
    }

    boolean debug = false;

    public void setDebugOn(boolean b) {
        debug = b;
    }

    public boolean isDebugOn() {
        return debug;
    }
}
