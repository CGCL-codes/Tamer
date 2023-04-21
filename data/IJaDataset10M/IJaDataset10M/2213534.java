package org.infoeng.icws.embedded;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NameClassPair;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.infoeng.icws.util.ICWSConstants;
import org.infoeng.icws.util.Utils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.NCSARequestLog;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.BoundedThreadPool;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("DERBY_ENCRYPTION_PROTOCOL", ICWSConstants.DERBY_ENCRYPTION_PROTOCOL);
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        int icwsServerPort = -1;
        String dbLocation = System.getProperty("icws.db.name");
        if (dbLocation == null) {
            System.out.println("icws.db.name must be specified as a property to locate the ICWS database.  Exiting.");
            System.exit(32);
        }
        String webappLocation = System.getProperty("icws.webapp.location");
        if (webappLocation == null) {
            System.out.println("icws.webapp.location must be specified as a property to locate the ICWS webapp.  Exiting.");
            System.exit(512);
        }
        try {
            File webappDirectory = new File(webappLocation);
            File webXmlFile = new File("" + webappDirectory.getCanonicalPath() + File.separator + "icws" + File.separator + "WEB-INF" + File.separator + "web.xml");
            if (!webXmlFile.exists() || !webXmlFile.isFile()) {
                System.out.println(" The icws web application at icws.webapp.location must be an uncompressed directory, ");
                System.out.println(" and $icws.webapp.location/icws/WEB-INF/web.xml must exist. ");
                System.exit(2048);
            }
            webappLocation = webappDirectory.getCanonicalPath();
        } catch (Exception e) {
            System.out.println("The icws web application at icws.webapp.location must be an uncompressed directory, ");
            System.out.println("and $icws.webapp.location/WEB-INF/web.xml must exist.");
            e.printStackTrace();
            System.exit(1024);
        }
        File servicePropsFile = new File("" + dbLocation + File.separator + "service.properties" + "");
        if (servicePropsFile == null || (!servicePropsFile.exists())) {
            System.out.println("Did not find Derby service.properties file in directory " + dbLocation + ".");
            System.out.println("Please configure the database and specify the Derby directory as the property icws.db.name.");
            System.exit(1);
        }
        Properties serviceProps = new Properties();
        serviceProps.load(new FileInputStream(servicePropsFile));
        if (!"true".equals(serviceProps.get("dataEncryption"))) {
            System.out.println("Your Derby database must be encrypted.  Exiting.");
            System.exit(4);
        }
        dbLocation = new File(dbLocation).getCanonicalPath();
        System.setProperty("icws.db.name", dbLocation);
        System.out.println("Using database location at " + dbLocation + ".");
        String dbParentLocation = new File(dbLocation).getParentFile().getCanonicalPath();
        char[] bootPasswd = null;
        if (System.getProperty("icws.db.password") != null) {
            bootPasswd = System.getProperty("icws.db.password").toCharArray();
        } else {
            bootPasswd = Utils.staticGetPassword("Please enter the boot password for the database at " + dbLocation + ":  ");
        }
        String bootPasswdString = new String(bootPasswd);
        Class.forName(ICWSConstants.DERBY_CLASS_NAME).newInstance();
        Properties connectionProps = new Properties();
        connectionProps.put("dataEncryption", "true");
        connectionProps.put("bootPassword", bootPasswdString);
        connectionProps.put("encryptionAlgorithm", ICWSConstants.DERBY_ENCRYPTION_PROTOCOL);
        try {
            Connection c = DriverManager.getConnection(ICWSConstants.DERBY_PROTOCOL + dbLocation, connectionProps);
            if (c != null) {
                PreparedStatement ps = c.prepareStatement("select icwsServerPort from configurationProfiles where profileIndex=?");
                if (ps == null) {
                    System.out.println("Did not obtain connection to database at " + dbLocation + ".  Exiting.");
                    System.exit(64);
                }
                String configurationProfileName = System.getProperty("icws.db.profile", ICWSConstants.DEFAULT_PROFILE_KEY);
                ps.setString(1, configurationProfileName);
                ResultSet rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    icwsServerPort = rs.getInt("icwsServerPort");
                }
                if (icwsServerPort == -1) {
                    System.out.println("ICWS server port must be defined in profile " + configurationProfileName + ".  Exiting.");
                    System.exit(3084);
                }
                if (rs != null) {
                    rs.close();
                }
                ps.close();
                c.close();
                System.setProperty("icws.db.password", bootPasswdString);
            } else if (c == null) {
                System.out.println("Did not obtain connection to database at " + dbLocation + ".  Exiting.");
                System.exit(64);
            }
        } catch (Exception e) {
            System.out.println("Caught exception.");
            e.printStackTrace();
            System.exit(128);
        }
        String keystoreFilePath = System.getProperty("jetty.ssl.keystore");
        if (keystoreFilePath == null) {
            System.out.println("Please specify the keystore for SSL connections using the system property jetty.ssl.keystore and restart.");
            System.exit(1);
        }
        File keystoreFile = new File(keystoreFilePath);
        if (!keystoreFile.exists()) {
            System.out.println("Keystore at " + keystoreFile.getCanonicalPath() + " must exist.");
        }
        FileInputStream keystoreFIS = new FileInputStream(keystoreFile);
        String serverKeyAlias = System.getProperty("icws.ssl.alias", ICWSConstants.DEFAULT_KEY_ALIAS);
        String jettySslPass = System.getProperty("jetty.ssl.password");
        String jettySslKeypass = System.getProperty("jetty.ssl.keypassword");
        char[] keystorePass = null;
        char[] keyPass = null;
        if ((jettySslPass != null) && (jettySslKeypass != null)) {
            keystorePass = jettySslPass.toCharArray();
            keyPass = jettySslKeypass.toCharArray();
        } else {
            keystorePass = Utils.staticGetPassword("Please enter the password to unlock the keystore at " + keystoreFile.getCanonicalPath() + ":  ");
            keyPass = Utils.staticGetPassword("Please enter the password for the key with alias " + serverKeyAlias + ":  ");
        }
        try {
            KeyStore testKS = KeyStore.getInstance(KeyStore.getDefaultType());
            testKS.load(keystoreFIS, keystorePass);
            testKS.getKey(serverKeyAlias, keyPass);
        } catch (Exception e) {
            System.out.println("Did not access the required key.  Exiting.");
            e.printStackTrace();
            System.exit(256);
        }
        System.setProperty("icws.ssl.alias", serverKeyAlias);
        System.setProperty("jetty.ssl.password", new String(keystorePass));
        System.setProperty("jetty.ssl.keypassword", new String(keyPass));
        System.setProperty("jetty.ssl.keystore", keystoreFile.getCanonicalPath());
        org.mortbay.jetty.Server server = new Server();
        org.mortbay.jetty.security.SslSocketConnector connector = new SslSocketConnector();
        BoundedThreadPool threadPool = new BoundedThreadPool();
        threadPool.setMaxThreads(100);
        server.setThreadPool(threadPool);
        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[] { contexts, new DefaultHandler(), requestLogHandler });
        server.setHandler(handlers);
        connector.setPassword(new String(keystorePass));
        connector.setKeystore(keystoreFile.getCanonicalPath());
        connector.setKeyPassword(new String(keyPass));
        connector.setPort(icwsServerPort);
        server.setConnectors(new Connector[] { connector });
        EmbeddedConnectionPoolDataSource eDS = new EmbeddedConnectionPoolDataSource();
        eDS.setDatabaseName(dbLocation);
        String connectionAttrs = "bootPassword=" + bootPasswdString + ";encryptionAlgorithm=" + ICWSConstants.DERBY_ENCRYPTION_PROTOCOL + ";dataEncryption=true";
        eDS.setConnectionAttributes(connectionAttrs);
        Hashtable jndiEnv = new Hashtable();
        jndiEnv.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        jndiEnv.put(Context.URL_PKG_PREFIXES, "org.apache.naming");
        Context iniCtxt = new InitialContext(jndiEnv);
        Context compCtxt = iniCtxt.createSubcontext("java:comp");
        Context envCtxt = compCtxt.createSubcontext("env");
        Context jdbcCtxt = envCtxt.createSubcontext("jdbc");
        jdbcCtxt.bind("icwsDataSource", eDS);
        String[] configs = new String[] { "org.mortbay.jetty.plus.webapp.Configuration" };
        WebAppContext.addWebApplications(server, webappLocation, null, configs, false, false);
        if (System.getProperty("icws.log.dir") != null) {
            File logDir = new File(System.getProperty("icws.log.dir"));
            if (logDir != null && logDir.isDirectory() && logDir.exists()) {
                NCSARequestLog log = new NCSARequestLog(logDir.getCanonicalPath() + "/yyyy_mm_dd.request.log");
                log.setAppend(true);
                log.setExtended(true);
                log.setLogTimeZone("GMT");
                requestLogHandler.setRequestLog(log);
            } else {
                System.out.println("icws.log.dir system property (value: " + System.getProperty("icws.log.dir") + "");
            }
        }
        server.start();
        server.join();
    }
}
