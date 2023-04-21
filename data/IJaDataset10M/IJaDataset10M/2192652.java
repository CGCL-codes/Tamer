package com.litt.core.common;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 * 静态方法获得数据库连接，从JNDI中获取数据源（已作废）.
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2006-08-30
 * @version 1.0
 *
 */
public class ConnectionPool {

    private static final Logger logger = Logger.getLogger(ConnectionPool.class);

    private static String JDNI_NAME = "java:comp/env/jdbc/gobbs";

    private DataSource dataSource;

    private static ConnectionPool instance;

    private ConnectionPool(DataSource ds) {
        this.dataSource = ds;
    }

    public DataSource getDataSource() throws SQLException {
        return dataSource;
    }

    private static ConnectionPool getInstance() {
        if (instance == null) {
            try {
                Context initCtx = new InitialContext();
                if (initCtx == null) {
                    throw new NamingException();
                }
                DataSource ds = null;
                try {
                    ds = (DataSource) initCtx.lookup(JDNI_NAME);
                } catch (NoInitialContextException nice) {
                    logger.error("未找到JDNI_NAME - " + JDNI_NAME);
                }
                instance = new ConnectionPool(ds);
                logger.info("初始化数据库连接池成功");
            } catch (NamingException e) {
                logger.error("数据库连接池初始化失败 - ", e);
            }
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        return getInstance().getDataSource().getConnection();
    }

    public static void main(String[] args) throws Exception {
        ConnectionPool.getConnection();
    }
}
