package ytex.uima.resource;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import ytex.uima.ApplicationContextHolder;
import edu.mayo.bmi.uima.core.resource.JdbcConnectionResource;

/**
 * copied from mayo JdbcConnectionResourceImpl.
 * extended to support connection initialization statements.
 * this is required for case-insensitive searches in oracle.
 * <p/>
 * modified to default to settings in ytex.properties in case
 * config parameters not specified in descriptor
 * <p/>
 * remove refs to wrapped sql connection
 * 
 * @author Mayo Clinic
 */
public class InitableJdbcConnectionResourceImpl implements JdbcConnectionResource, SharedResourceObject {

    private Logger iv_logger = Logger.getLogger(getClass().getName());

    /**
	 * JDBC driver ClassName.
	 */
    public static final String PARAM_DRIVER_CLASS = "DriverClassName";

    /**
	 * JDBC URL that specifies db network location and db name.
	 */
    public static final String PARAM_URL = "URL";

    /**
	 * Username for db authentication.
	 */
    public static final String PARAM_USERNAME = "Username";

    /**
	 * Password for db authentication.
	 */
    public static final String PARAM_PASSWORD = "Password";

    /**
	 * Flag that determines whether to keep JDBC connection open no matter what.
	 */
    public static final String PARAM_KEEP_ALIVE = "KeepConnectionAlive";

    /**
     * Transaction isolation level.  Value should be a static fieldname from
     * java.sql.Connection such as TRANSACTION_READ_UNCOMMITTED.  This parameter
     * is optional. 
     */
    public static final String PARAM_ISOLATION = "TransactionIsolation";

    private Connection iv_conn;

    public void load(DataResource dr) throws ResourceInitializationException {
        ConfigurationParameterSettings cps = dr.getMetaData().getConfigurationParameterSettings();
        Properties ytexProperties = ApplicationContextHolder.getYtexProperties();
        String driverClassName = (String) cps.getParameterValue(PARAM_DRIVER_CLASS);
        if (driverClassName == null) driverClassName = ytexProperties.getProperty("db.driver");
        String urlStr = (String) cps.getParameterValue(PARAM_URL);
        if (urlStr == null) urlStr = ytexProperties.getProperty("db.url");
        String username = (String) cps.getParameterValue(PARAM_USERNAME);
        if (username == null) username = ytexProperties.getProperty("db.username");
        String password = (String) cps.getParameterValue(PARAM_PASSWORD);
        if (password == null) password = ytexProperties.getProperty("db.password");
        String isolationStr = (String) cps.getParameterValue(PARAM_ISOLATION);
        try {
            Class.forName(driverClassName);
            iv_conn = DriverManager.getConnection(urlStr, username, password);
            iv_logger.info("Connection established to: " + urlStr);
            if (isolationStr != null) {
                Class<?> connClass = Class.forName("java.sql.Connection");
                Field f = connClass.getField(isolationStr);
                int level = f.getInt(null);
                iv_logger.info("Connection transaction isolation level set: " + isolationStr + "(" + level + ")");
                iv_conn.setTransactionIsolation(level);
            }
        } catch (Exception e) {
            throw new ResourceInitializationException(e);
        }
    }

    public Connection getConnection() {
        return iv_conn;
    }
}
