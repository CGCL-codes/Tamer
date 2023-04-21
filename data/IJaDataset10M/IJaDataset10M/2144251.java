package com.iehyou.dao.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import com.iehyou.utils.GlobalConfigUtils;

/**
 * 数据库管理
 */
public class DBManager {

    private static final Logger log = Logger.getLogger(DBManager.class);

    private static final ThreadLocal<Connection> conns = new ThreadLocal<Connection>();

    private static DataSource dataSource;

    private static boolean show_sql = false;

    public static Properties properties = null;

    static {
        initDataSource(null);
    }

    /**
	 * 初始化连接池
	 * @param props
	 * @param show_sql
	 */
    private static final void initDataSource(Properties dbProperties) {
        try {
            if (dbProperties == null) {
                dbProperties = GlobalConfigUtils.getProperties("db.properties");
            }
            properties = dbProperties;
            if ("cache".equals(dbProperties.getProperty("jdbc.database"))) {
                return;
            }
            Properties cp_props = new Properties();
            for (Object key : dbProperties.keySet()) {
                String skey = (String) key;
                if (skey.startsWith("jdbc.")) {
                    String name = skey.substring(5);
                    cp_props.put(name, dbProperties.getProperty(skey));
                    if ("show_sql".equalsIgnoreCase(name)) {
                        show_sql = "true".equalsIgnoreCase(dbProperties.getProperty(skey));
                    }
                }
            }
            dataSource = (DataSource) Class.forName(cp_props.getProperty("datasource")).newInstance();
            if (dataSource.getClass().getName().indexOf("c3p0") > 0) {
                System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", "com.mchange.v2.c3p0.management.NullManagementCoordinator");
            }
            log.info("Using DataSource : " + dataSource.getClass().getName());
            BeanUtils.populate(dataSource, cp_props);
            Connection conn = getConnection();
            DatabaseMetaData mdm = conn.getMetaData();
            log.info("Connected to " + mdm.getDatabaseProductName() + " " + mdm.getDatabaseProductVersion());
            closeConnection();
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new DBException(e);
        }
    }

    /**
	 * 断开连接池
	 */
    public static final void closeDataSource() {
        try {
            dataSource.getClass().getMethod("close").invoke(dataSource);
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            log.error("Unabled to destroy DataSource!!! ", e);
        }
    }

    public static final Connection getConnection() throws SQLException {
        Connection conn = conns.get();
        if (conn == null || conn.isClosed()) {
            conn = dataSource.getConnection();
            conns.set(conn);
        }
        return (show_sql && !Proxy.isProxyClass(conn.getClass())) ? new _DebugConnection(conn).getConnection() : conn;
    }

    /**
	 * 关闭连接
	 */
    public static final void closeConnection() {
        Connection conn = conns.get();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            log.error("Unabled to close connection!!! ", e);
        }
        conns.set(null);
    }

    /**
	 * 用于跟踪执行的SQL语句
	 * @author Winter Lau
	 */
    static class _DebugConnection implements InvocationHandler {

        private static final Log log = LogFactory.getLog(_DebugConnection.class);

        private Connection conn = null;

        public _DebugConnection(Connection conn) {
            this.conn = conn;
        }

        /**
		 * Returns the conn.
		 * @return Connection
		 */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces(), this);
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if ("prepareStatement".equals(method) || "createStatement".equals(method)) {
                    if (args != null) {
                        log.info("[SQL] >>> " + args[0]);
                    }
                }
                return m.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }

    public static String getDataBase() {
        return (String) getProperties().get("jdbc.database");
    }

    public static Properties getProperties() {
        return properties;
    }
}
