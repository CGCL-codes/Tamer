package tw.qing.lwdba;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.qing.sys.StringManager;

public class Configuration {

    static Log logger = LogFactory.getLog(Configuration.class);

    public static final String LWDBA_PREFIX = "lwdba.pool.";

    private String poolName;

    private int maxConnectionCount = 16;

    private String databaseType;

    private String driverClassName;

    private String encoding;

    private String sqlFile;

    private JDBCInfo jdbcInfo = new JDBCInfo();

    private JDBCInfoProvider jdbcInfoProvider;

    StringManager sm = StringManager.getManager("system");

    public Configuration(String poolName) {
        this.poolName = (poolName == null || "".equals(poolName.trim())) ? "default" : poolName;
        String provider = sm.getString(LWDBA_PREFIX + poolName + ".jdbcInfoProviderClass");
        boolean isDefaultProvider = DefaultJdbcInformationProvider.class.getCanonicalName().equals(provider);
        if (provider != null && !isDefaultProvider) {
            try {
                Class c = Class.forName(provider);
                jdbcInfoProvider = (JDBCInfoProvider) c.getConstructor(new Class[] { JDBCInfo.class }).newInstance(new Object[] { jdbcInfo });
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
        if (jdbcInfoProvider == null) {
            jdbcInfoProvider = new DefaultJdbcInformationProvider(jdbcInfo);
        }
        doConfigure();
    }

    private void doConfigure() {
        databaseType = sm.getString(LWDBA_PREFIX + poolName + ".type");
        driverClassName = sm.getString(LWDBA_PREFIX + poolName + ".driverClassName");
        jdbcInfo.setDriverURL(sm.getString(LWDBA_PREFIX + poolName + ".driverURL"));
        jdbcInfo.setUserName(sm.getString(LWDBA_PREFIX + poolName + ".userName"));
        jdbcInfo.setPassword(sm.getString(LWDBA_PREFIX + poolName + ".password"));
        String _maxConnectionCount = sm.getString(LWDBA_PREFIX + poolName + ".maxConnectionCount");
        if (_maxConnectionCount != null) {
            try {
                maxConnectionCount = Integer.parseInt(_maxConnectionCount);
            } catch (Exception ignored) {
            }
        }
        sqlFile = sm.getString(LWDBA_PREFIX + poolName + ".sqlFile");
        if (sqlFile == null || "".equals(sqlFile.trim())) {
            sqlFile = "sql";
        }
        encoding = sm.getString(LWDBA_PREFIX + poolName + ".encoding");
    }

    public String getPoolName() {
        return poolName;
    }

    public int getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDriverURL() {
        return jdbcInfoProvider.getConnectionString();
    }

    public String getUserName() {
        return jdbcInfoProvider.getUserName();
    }

    public String getPassword() {
        return jdbcInfoProvider.getPassword();
    }

    public String getEncoding() {
        return encoding;
    }

    public String getSqlFile() {
        return sqlFile;
    }

    public Properties getConnectionPoolProperties() {
        Properties props = new Properties();
        if (encoding != null) {
            props.put("characterEncoding", encoding);
        }
        props.put("useUnicode", "true");
        props.put("user", jdbcInfoProvider.getUserName());
        props.put("password", jdbcInfoProvider.getPassword());
        return props;
    }
}
