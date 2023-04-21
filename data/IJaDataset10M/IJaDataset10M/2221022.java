package wsl.licence;

import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import java.io.IOException;
import java.util.MissingResourceException;

public class Config {

    private static final String PropDbDriver = "database.driver", PropDbURL = "database.url", PropDbUsername = "database.username", PropDbPassword = "database.password";

    private String dbDriver, dbURL, dbUsername, dbPassword;

    private String imageDir;

    public Config(String path) throws Exception {
        PropertyResourceBundle p = new PropertyResourceBundle(new FileInputStream(path));
        dbDriver = p.getString(PropDbDriver);
        dbURL = p.getString(PropDbURL);
        dbUsername = p.getString(PropDbUsername);
        dbPassword = p.getString(PropDbPassword);
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
