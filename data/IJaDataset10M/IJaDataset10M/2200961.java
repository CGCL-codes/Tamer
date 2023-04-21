package coyousoft.easydao.datasource;

import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据源工厂。
 *
 * @author SunYong
 */
public class DataSourceFactory {

    private static final Log log = LogFactory.getLog(DataSourceFactory.class);

    private static Map<String, DataSource> dsMap = new HashMap<String, DataSource>();

    private static DataSource initDataSource(String name) {
        DataSource dataSource = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup(name);
            log.info("name=" + name + "; dataSource=" + dataSource);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return dataSource;
    }

    /**
     * 根据名称获得数据源对象。
     *
     * @param dataSourceName 数据源名称
     * @return 数据源对象
     */
    public static DataSource getDataSource(String dataSourceName) {
        DataSource dataSource = dsMap.get(dataSourceName);
        if (dataSource == null) {
            dataSource = initDataSource(dataSourceName);
            dsMap.put(dataSourceName, dataSource);
        }
        return dataSource;
    }
}
