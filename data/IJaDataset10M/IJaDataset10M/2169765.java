package org.verus.newgenlib.uc.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public final class DBConnector {

    HashMap sessionFactoryMap = new HashMap();

    private static DBConnector instance = null;

    private static BasicDataSource bs = null;

    /** Creates a new instance of DBConnector */
    private DBConnector() {
        String propsname = StaticValues.getInstance().getRootDirectory() + "/database.properties";
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File(propsname)));
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUsername(props.getProperty("username"));
            ds.setPassword(props.getProperty("password"));
            ds.setUrl("jdbc:postgresql://" + props.getProperty("host") + ":" + props.getProperty("port") + "/" + props.getProperty("databaseName"));
            bs = ds;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBConnector getInstance() {
        if (instance == null) instance = new DBConnector();
        return instance;
    }

    public java.sql.Connection getDBConnection() {
        Connection con = null;
        try {
            con = bs.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
