package org.maestroframework.db.utils;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DataSourceUtils {

    public static DataSource createDataSource(String driverClassName, String url) throws Exception {
        Properties properties = new Properties();
        properties.put("driverClassName", driverClassName);
        properties.put("url", url);
        properties.put("initialSize", 5);
        properties.put("maxActive", 25);
        BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
        return dataSource;
    }

    public static DataSource createDataSource(String driverClassName, String url, String username, String password) throws Exception {
        Properties properties = new Properties();
        properties.put("driverClassName", driverClassName);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        properties.put("initialSize", 5);
        properties.put("maxActive", 25);
        BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
        return dataSource;
    }

    public static DataSource createDataSource(String driverClassName, String url, String username, String password, int initialSize, int maxActive) throws Exception {
        Properties properties = new Properties();
        properties.put("driverClassName", driverClassName);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        properties.put("initialSize", initialSize);
        properties.put("maxActive", maxActive);
        BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
        return dataSource;
    }
}
