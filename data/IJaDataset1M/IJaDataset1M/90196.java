package org.openbroad.client.user.view.control;

import java.io.*;
import java.util.Properties;

/**
 *
 */
public class Config {

    /** Creates a new instance of Config */
    private Config() {
        try {
            props = new Properties();
            String usrhome = System.getProperty("user.home");
            propfile = new File(usrhome + "/OpenBroad.cnf");
            if (propfile.exists()) {
                props.load(new FileInputStream(propfile));
            } else {
                props.store(new FileOutputStream(propfile), "OpenBroad config");
                setProperty("playListX", "200");
                setProperty("playListY", "200");
                setProperty("filListX", "50");
                setProperty("filListY", "200");
                setProperty("server", "localhost");
            }
            System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            System.setProperty("java.naming.factory.url.pkgs", "org.jnp.interfaces");
            System.setProperty("java.naming.provider.url", getProperty("server") + ":1099");
            config = this;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Config getConfig() {
        if (config == null) return new Config(); else return config;
    }

    public String getProperty(String key) {
        String prop = props.getProperty(key);
        if (prop == null) return ""; else return prop;
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
        try {
            props.store(new FileOutputStream(propfile), "OpenBroad config");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String version;

    private Properties props;

    private static Config config;

    private File propfile;
}
