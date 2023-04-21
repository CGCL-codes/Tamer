package net.kirke.mp3dj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.kirke.mp3dj.resources.Msgs;
import net.kirke.mp3dj.resources.Props;

public class ConnectionHelper {

    private static ConnectionHelper instance;

    private Msgs msgs;

    private static Props props;

    private static Refresh refresh;

    private static String url;

    private volatile Thread refreshThread;

    public String getUrl() {
        return url;
    }

    private ConnectionHelper() {
        msgs = new Msgs();
        props = Props.getInstance();
        if (props == null) {
            System.out.println("ConnectionHelper:	" + "Unable to create instance of Props singleton");
            return;
        }
        url = props.getProperty("database.url");
        if (url == null) {
            System.out.println("ConnectionHelper:	" + "Unable to get database url" + " (set database.url property in build.properties).");
        }
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
        } catch (Exception x) {
            Object params[] = { driver, x.getMessage() };
            String msg = msgs.getMessage("UNABLE_TO_LOAD_DRIVER", params);
            System.out.println(msg);
            x.printStackTrace();
        }
    }

    public static ConnectionHelper getInstance() {
        if (instance == null) {
            instance = new ConnectionHelper();
        }
        return instance;
    }

    public static Connection getConnection() throws java.sql.SQLException {
        return DriverManager.getConnection(getInstance().getUrl());
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException x) {
            System.out.println(x.getMessage());
        }
    }
}
