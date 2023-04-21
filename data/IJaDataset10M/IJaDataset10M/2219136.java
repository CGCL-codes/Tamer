package net.openkoncept.vroom.appserver;

import org.apache.commons.dbcp.BasicDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: fijaz
 * Date: Feb 5, 2010
 * Time: 8:50:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class VasConnectionPoolManager {

    private static Map<String, DataSource> GLOBAL_CONNECTION_POOL_MAP = new HashMap<String, DataSource>();

    public static DataSource test(String driver, String url, String user, String password, Map<String, String> props, boolean closeAfterSuccess) throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        if (props != null) {
            for (String key : props.keySet()) {
                ds.addConnectionProperty(key, props.get(key));
            }
        }
        ds.getConnection();
        if (closeAfterSuccess) {
            ds.close();
        }
        return ds;
    }

    public static void register(String id, String driver, String url, String user, String password, Map<String, String> props) throws SQLException {
        BasicDataSource ds = null;
        ds = (BasicDataSource) test(driver, url, user, password, props, false);
        GLOBAL_CONNECTION_POOL_MAP.put(id, ds);
    }

    public static void unregister(String id) throws SQLException {
        BasicDataSource ds = (BasicDataSource) GLOBAL_CONNECTION_POOL_MAP.get(id);
        if (ds != null) {
            GLOBAL_CONNECTION_POOL_MAP.remove(id);
            ds.close();
        }
    }

    public static Connection getConnection(String id) throws SQLException {
        BasicDataSource ds = null;
        ds = (BasicDataSource) GLOBAL_CONNECTION_POOL_MAP.get(id);
        if (ds != null) {
            return ds.getConnection();
        } else {
            return null;
        }
    }

    public static boolean isRegistered(String id) throws SQLException {
        return GLOBAL_CONNECTION_POOL_MAP.get(id) != null;
    }
}
