package org.gridtrust.enforce.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gridtrust.Constants;
import org.gridtrust.enforce.util.EnforceUtil;
import org.gridtrust.enforce.util.ServiceLocator;
import org.gridtrust.util.Configurator;

public abstract class AbstractJDBCDao {

    private static final String ENFORCE_PROPERTIES = "ENFORCE_PROPERTIES";

    private static Map<String, Object> propertyCache = new HashMap<String, Object>();

    private static final Log log = LogFactory.getLog(AbstractJDBCDao.class);

    protected Connection getConnection() throws Exception {
        Properties props = null;
        DataSource ds = null;
        Connection con = null;
        boolean isDataSourceEnabled = false;
        Object objProps = propertyCache.get(ENFORCE_PROPERTIES);
        if (objProps != null) {
            props = (Properties) objProps;
            isDataSourceEnabled = Boolean.parseBoolean(props.getProperty(Constants.ENFORCE_DATASOURCE_ENABLED));
        } else {
            props = EnforceUtil.getEnforceProperties();
            if (props == null) {
                props = Configurator.load(null, Constants.ENFORCE_PROP_FILE);
                if (props == null) {
                    throw new Exception("Can not find property file");
                }
            }
            propertyCache.put(ENFORCE_PROPERTIES, props);
            isDataSourceEnabled = Boolean.parseBoolean(props.getProperty(Constants.ENFORCE_DATASOURCE_ENABLED));
        }
        if (isDataSourceEnabled) {
            ds = ServiceLocator.getDataSource(props.getProperty(Constants.ENFORCE_DATASOURCE_NAME));
            con = ds.getConnection();
        } else {
            String dbDriver = props.getProperty(Constants.ENFORCE_DB_DRIVER);
            String dbURL = props.getProperty(Constants.ENFORCE_DB_URL);
            String dbUser = props.getProperty(Constants.ENFORCE_DB_USER);
            String dbPassword = props.getProperty(Constants.ENFORCE_DB_PASSWORD);
            Class.forName(dbDriver);
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        }
        return con;
    }
}
