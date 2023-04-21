package com.sy.easycms;

import java.util.Properties;

public class Config {

    public Config() {
    }

    public static String usedb = "mysql";

    public static String logto = "console";

    public static String connstr = "jdbc:mysql://localhost:3306/easycms?user=root&password=dreaming";

    public static int record = 28;

    static {
        Properties pro = new Properties();
        try {
            pro.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Config.usedb = pro.getProperty("usedb");
        Config.logto = pro.getProperty("logto");
        if (Config.connstr != "" && Config.connstr != null) {
            Config.connstr = pro.getProperty("ConnectionString");
        }
    }
}
