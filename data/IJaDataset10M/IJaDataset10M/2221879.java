package org.geotools.data.hatbox;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.DataAccessFactory.Param;

public class HatBoxH2DataStoreFactory extends AbstractDataStoreFactory {

    private static final Logger LOGGER = Logger.getLogger(HatBoxH2DataStoreFactory.class.getName());

    private static final String DRIVER_CLASS = "org.h2.Driver";

    private static final int DEFAULT_NETWORK_PORT = 9092;

    private static final String DEFAULT_SCHEMA = "PUBLIC";

    static final Param DBTYPE = new Param("dbtype", String.class, "must be 'HATBOX-H2'", true, "HATBOX-H2");

    static final Param HOST = new Param("host", String.class, "host name", false, "localhost");

    static final Param PORT = new Param("port", Integer.class, "network connection port", false, Integer.toString(DEFAULT_NETWORK_PORT));

    static final Param SSL = new Param("ssl", Boolean.class, "Is network connection over SSL?", false, false);

    static final Param FILE = new Param("file", Boolean.class, "Is connection to a local file db?", false, false);

    static final Param PATH = new Param("path", String.class, "filesystem directory", false);

    static final Param ZIP_FILE = new Param("zip", String.class, "zip file", false);

    static final Param PRIVATE_IN_MEM = new Param("privInMem", Boolean.class, "Is connection to a private in-memory db?", false, false);

    static final Param SHARED_IN_MEM = new Param("shareInMem", Boolean.class, "Is connection to a shared in-memory db?", false, false);

    static final Param DATABASE = new Param("database", String.class, "database name", true);

    static final Param OPTIONS = new Param("options", String.class, "connection options", false);

    static final Param USER = new Param("user", String.class, "user name to login as", true);

    static final Param PASSWD = new Param("passwd", String.class, "password used to login", false);

    static final Param MAXCONN = new Param("max connections", Integer.class, "maximum number of open connections", false, new Integer(10));

    static final Param MINCONN = new Param("min connections", Integer.class, "minimum number of pooled connection", false, new Integer(4));

    static final Param VALIDATECONN = new Param("validate connections", Boolean.class, "check connection is alive before using it", false, Boolean.FALSE);

    static final Param SCHEMA = new Param("schema", String.class, "database schema containing spatial tables", false);

    static final Param NAMESPACE = new Param("namespace", String.class, "namespace prefix used", false);

    static final Param[] arrayParameters = { DBTYPE, HOST, PORT, SSL, FILE, PATH, ZIP_FILE, PRIVATE_IN_MEM, SHARED_IN_MEM, DATABASE, OPTIONS, USER, PASSWD, NAMESPACE };

    public DataStore createDataStore(Map params) throws IOException {
        String host = (String) HOST.lookUp(params);
        Integer port = (Integer) PORT.lookUp(params);
        Boolean ssl = (Boolean) SSL.lookUp(params);
        Boolean file = (Boolean) FILE.lookUp(params);
        String path = (String) PATH.lookUp(params);
        String zipFile = (String) ZIP_FILE.lookUp(params);
        Boolean privateInMem = (Boolean) PRIVATE_IN_MEM.lookUp(params);
        Boolean sharedInMem = (Boolean) SHARED_IN_MEM.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        String options = (String) OPTIONS.lookUp(params);
        String schema = (String) SCHEMA.lookUp(params);
        String user = (String) USER.lookUp(params);
        String passwd = (String) PASSWD.lookUp(params);
        Integer maxConn = (Integer) MAXCONN.lookUp(params);
        Integer minConn = (Integer) MINCONN.lookUp(params);
        Boolean validateConn = (Boolean) VALIDATECONN.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        if (!canProcess(params)) {
            LOGGER.warning("Can not process : " + params);
            throw new IOException("The parameters map isn't correct!!");
        }
        boolean validate = (validateConn != null) && validateConn.booleanValue();
        int maxActive = (maxConn != null) ? maxConn.intValue() : 10;
        int maxIdle = (minConn != null) ? minConn.intValue() : 4;
        StringBuffer buf = new StringBuffer();
        buf.append("jdbc:h2:");
        if (host != null) {
            if ((ssl != null) && ssl.booleanValue()) {
                buf.append("ssl:");
            } else {
                buf.append("tcp:");
            }
            buf.append("//");
            buf.append(host);
            if (port != null) {
                buf.append(':');
                buf.append(port);
            }
            buf.append('/');
            if ((sharedInMem != null) && sharedInMem.booleanValue()) {
                buf.append("mem:");
            } else if (path != null) {
                buf.append(path);
                if (!path.endsWith("/")) {
                    buf.append('/');
                }
            }
            buf.append(database);
        } else if ((file != null) && file.booleanValue()) {
            buf.append("file:");
            if (path != null) {
                buf.append(path);
                if (!path.endsWith("/")) {
                    buf.append('/');
                }
            }
            buf.append(database);
        } else if ((privateInMem != null) && privateInMem.booleanValue()) {
            buf.append("mem:");
        } else if ((sharedInMem != null) && sharedInMem.booleanValue()) {
            buf.append("mem:");
            buf.append(database);
        } else if (zipFile != null) {
            buf.append("zip:");
            if (path != null) {
                buf.append(path);
                if (!path.endsWith("/")) {
                    buf.append('/');
                }
            }
            buf.append(zipFile);
            buf.append("!/");
            buf.append(database);
        } else {
            if (path != null) {
                buf.append(path);
                if (!path.endsWith("/")) {
                    buf.append('/');
                }
            }
            buf.append(database);
        }
        if (options != null) {
            if (!options.startsWith(";")) {
                buf.append(';');
            }
            buf.append(options);
        }
        DataSource ds = DataSourceUtil.buildDefaultDataSource(buf.toString(), DRIVER_CLASS, user, passwd, maxActive, maxIdle, validate ? "select 1" : null, false, 0);
        if (schema == null) {
            if (user == null) {
                schema = DEFAULT_SCHEMA;
            } else {
                schema = user;
            }
        }
        return new HatBoxDataStore(ds, JDBCDataStoreConfig.createWithNameSpaceAndSchemaName(namespace, schema), new H2Sql(schema));
    }

    public DataStore createNewDataStore(Map params) throws IOException {
        return createDataStore(params);
    }

    public String getDescription() {
        return "HatBox H2";
    }

    public Param[] getParametersInfo() {
        return arrayParameters;
    }

    public boolean isAvailable() {
        try {
            Class.forName(DRIVER_CLASS);
            return true;
        } catch (ClassNotFoundException cnfe) {
        }
        return false;
    }

    public boolean canProcess(Map params) {
        if (!super.canProcess(params)) {
            return false;
        }
        if (!((String) params.get("dbtype")).equalsIgnoreCase("HATBOX-H2")) {
            return false;
        }
        if (!isAvailable()) {
            return false;
        }
        try {
            boolean host = (HOST.lookUp(params) != null);
            Boolean fileParam = (Boolean) FILE.lookUp(params);
            boolean file = (fileParam != null) ? fileParam.booleanValue() : false;
            Boolean privateInMemParam = (Boolean) PRIVATE_IN_MEM.lookUp(params);
            boolean privateInMem = (privateInMemParam != null) ? privateInMemParam.booleanValue() : false;
            Boolean sharedInMemParam = (Boolean) SHARED_IN_MEM.lookUp(params);
            boolean sharedInMem = (sharedInMemParam != null) ? sharedInMemParam.booleanValue() : false;
            boolean zip = (ZIP_FILE.lookUp(params) != null);
            int count = 0;
            if (host) {
                count++;
            }
            if (file) {
                count++;
            }
            if (privateInMem) {
                count++;
            }
            if (sharedInMem) {
                if (!host) {
                    count++;
                }
            }
            if (zip) {
                count++;
            }
            if (count == 0) {
            } else if (count > 1) {
                StringBuffer buf = new StringBuffer("Multiple connection params:");
                if (host) {
                    buf.append(" Network");
                }
                if (file) {
                    buf.append(" Local-File");
                }
                if (privateInMem) {
                    buf.append(" Private-In-Memory");
                }
                if (sharedInMem) {
                    buf.append(" Shared-In-Memory");
                }
                if (zip) {
                    buf.append(" Zip");
                }
                LOGGER.fine(buf.toString());
                return false;
            }
        } catch (IOException ioe) {
        }
        return true;
    }
}
