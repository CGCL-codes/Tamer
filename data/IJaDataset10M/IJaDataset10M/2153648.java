package net.sourceforge.jdbclogger.spring;

/**
 * @author Catalin Kormos (latest modification by $Author: catalean $)
 * @version $Revision: 131 $ $Date: 2007-09-29 09:52:01 -0400 (Sat, 29 Sep 2007) $
 */
public class JdbcLoggerDataSourceConfiguration {

    private static final String DEFAULT_DATA_SOURCE_BEAN_NAME = "dataSource";

    private static final String DEFAULT_DRIVER_CLASS_NAME_PROPERY_NAME = "driverClassName";

    private String dataSourceBeanName;

    private String driverClassNamePropertyName;

    /**
	 * Constructor
	 */
    public JdbcLoggerDataSourceConfiguration() {
        this(DEFAULT_DATA_SOURCE_BEAN_NAME, DEFAULT_DRIVER_CLASS_NAME_PROPERY_NAME);
    }

    /**
	 * @param dataSourceBeanName
	 * @param driverClassNamePropertyName
	 */
    public JdbcLoggerDataSourceConfiguration(String dataSourceBeanName, String driverClassNamePropertyName) {
        this.dataSourceBeanName = dataSourceBeanName;
        this.driverClassNamePropertyName = driverClassNamePropertyName;
    }

    /**
	 * @return the dataSourceBeanName
	 */
    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    /**
	 * @param dataSourceBeanName the dataSourceBeanName to set
	 */
    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }

    /**
	 * @return the driverClassNamePropertyName
	 */
    public String getDriverClassNamePropertyName() {
        return driverClassNamePropertyName;
    }

    /**
	 * @param driverClassNamePropertyName the driverClassNamePropertyName to set
	 */
    public void setDriverClassNamePropertyName(String driverClassNamePropertyName) {
        this.driverClassNamePropertyName = driverClassNamePropertyName;
    }
}
