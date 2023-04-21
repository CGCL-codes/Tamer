package org.magicbox.repository;

import java.io.IOException;
import java.util.Properties;

public class Config {

    public String getDbDriverProduction() {
        return dbDriverProduction;
    }

    public String getDbDriverDebug() {
        return dbDriverDebug;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public Config() {
        Properties props = new Properties();
        try {
            props.load(Config.class.getResourceAsStream("/org/magicbox/repository/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbDriverProduction = props.getProperty("jdbc.production.driver");
        dbDriverDebug = props.getProperty("jdbc.debug.driver");
        dbDriver = props.getProperty("jdbc.driver");
        dbUrl = props.getProperty("jdbc.url");
        dbUser = props.getProperty("jdbc.username");
        dbPass = props.getProperty("jdbc.password");
    }

    private String dbDriverProduction;

    private String dbDriverDebug;

    private String dbDriver;

    private String dbUser;

    private String dbPass;

    private String dbUrl;
}
