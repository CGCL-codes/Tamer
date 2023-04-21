package org.snipsnap.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A configuration object. Contains information about server and admin login.
 * @author Matthias L. Jugel
 * @version $Id: Configuration.java 805 2003-03-17 15:26:53Z leo $
 */
public class Configuration {

    private Properties properties;

    public static final String INIT_PARAM = "config";

    public static final String VERSION = "snipsnap.server.version";

    public static final String ENCODING = "snipsnap.server.encoding";

    public static final String ADMIN_USER = "snipsnap.server.admin.user";

    public static final String ADMIN_PASS = "snipsnap.server.admin.password";

    public static final String ADMIN_PORT = "snipsnap.server.admin.port";

    public static final String ADMIN_EMAIL = "snipsnap.server.admin.email";

    public static final String WEBAPP_ROOT = "snipsnap.server.webapp.root";

    private File file = null;

    /**
   * Create an instance of configuration, unconfigured
   */
    public Configuration() {
        properties = new Properties();
    }

    /**
   * Create an instance of configuration from a file given as string.
   * @param file the config file to load
   * @throws IOException
   */
    public Configuration(String file) throws IOException {
        this(new File(file));
    }

    /**
   * Create an instance of configuration from a file.
   * @param file the config file to load
   * @throws IOException
   */
    public Configuration(File file) throws IOException {
        this();
        load(file);
    }

    public Configuration(Configuration config) {
        properties = (Properties) config.properties.clone();
    }

    /**
   * Change the file to store the configuration in.
   * @param file the new file
   */
    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void load() throws IOException {
        if (file != null) {
            load(file);
        } else {
            throw new IOException("no configuration file known, use load(File file)");
        }
    }

    public void load(Properties properties) {
        this.properties = properties;
    }

    public void load(File configFile) throws IOException {
        setFile(configFile);
        FileInputStream in = new FileInputStream(file);
        load(in);
        in.close();
    }

    public void load(FileInputStream in) throws IOException {
        properties.load(in);
    }

    /**
   * Store configuration in the file it was loaded from.
   * @throws IOException
   */
    public void store() throws IOException {
        if (file != null) {
            store(file);
        } else {
            throw new IOException("no configuration file known, use store(File file)");
        }
    }

    /**
   * Store configuration file explicitely in a specifified file.
   * @param configFile the file to store in
   * @throws IOException
   */
    public void store(File configFile) throws IOException {
        setFile(configFile);
        FileOutputStream out = new FileOutputStream(file);
        store(out);
        out.close();
    }

    /**
   * Store using an output stream. This is used to override defaults in Properties and adds
   * a header.
   * @param out the output stream
   * @throws IOException
   */
    public void store(OutputStream out) throws IOException {
        properties.store(out, "SnipSnap configuration $Revision: 805 $");
    }

    public void setAdminLogin(String login) {
        properties.setProperty(ADMIN_USER, login);
    }

    public String getAdminLogin() {
        return properties.getProperty(ADMIN_USER);
    }

    public void setAdminPassword(String password) {
        properties.setProperty(ADMIN_PASS, password);
    }

    public String getAdminPassword() {
        return properties.getProperty(ADMIN_PASS);
    }

    public void setAdminEmail(String email) {
        properties.setProperty(ADMIN_EMAIL, email);
    }

    public String getAdminEmail() {
        return properties.getProperty(ADMIN_EMAIL);
    }

    public String getVersion() {
        String version = System.getProperty(VERSION);
        if (null == version) {
            version = getProperty(VERSION);
        }
        return version;
    }

    public void setProperty(String name, String value) {
        if (name == null || value == null) return;
        properties.setProperty(name, value);
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public String toString() {
        return properties.toString();
    }
}
