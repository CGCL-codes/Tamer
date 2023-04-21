package edu.pmfbl.mnr.sem.mcmatm.app;

import java.io.*;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * <p>Title: MCMA Test Maker</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: KrndijaSoft</p>
 *
 * @author Ninoslav Krndija
 * @version 1.0
 */
public class Parametri {

    String url, username, password, driver;

    public Parametri(String fileName) throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("database.properties");
        props.load(in);
        in.close();
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        driver = props.getProperty("jdbc.driver");
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
