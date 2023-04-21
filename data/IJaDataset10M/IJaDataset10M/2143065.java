package com.spagettikod.t437;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import com.spagettikod.SystemUtil;
import com.xerox.amazonws.sdb.SimpleDB;

public class Configurator {

    private static final Log log = LogFactory.getLog(Configurator.class);

    private static final String ACCESS_KEY = "access.key";

    private static final String SECRET_KEY = "secret.key";

    private static final String MAIN_WINDOW_X_POS = "main.window.pos.x";

    private static final String MAIN_WINDOW_Y_POS = "main.window.pos.y";

    private static final String MAIN_WINDOW_HEIGHT = "main.window.height";

    private static final String MAIN_WINDOW_WIDTH = "main.window.width";

    private static final String SDB_SERVER = "server.sdb.url";

    private static final String SDB_IS_SECURE = "server.sdb.secure.connection";

    private static final String SDB_PORT = "server.sdb.port";

    private static final String SDB_SHOW_ROW_NUMBER_COLUMN = "simpledb.show_row_no";

    private static final String SDB_MULTI_ATTR_BACKGROUND = "simpledb.multi.attr.background";

    private static final String MAIN_WINDOW_SPLIT_PANE_DIVIDER = "main.window.splitpane.divider";

    private static File file = new File(System.getProperty("user.home") + File.separator + ".t437.properties");

    private static Properties properties = new Properties();

    private boolean saveSecretKey;

    private SimpleDB simpleDB;

    private void configureLogging() {
        if (T437.VERSION.endsWith("-SNAPSHOT")) {
            PropertyConfigurator.configure(getClass().getResource("/logging_dev.properties"));
        } else {
            PropertyConfigurator.configure(getClass().getResource("/logging_prod.properties"));
        }
    }

    private void logSystemInfo() {
        if (log.isDebugEnabled()) {
            log.debug("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + ", " + System.getProperty("os.arch"));
            log.debug("Java runtime: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        }
    }

    public Configurator() {
        configureLogging();
        logSystemInfo();
        if (file.exists()) {
            try {
                log.debug("Found configuration file, loading...");
                properties.load(new FileInputStream(file));
                if (getSecretKey() == null || getSecretKey().equals("")) {
                    setSaveSecretKey(false);
                } else {
                    setSaveSecretKey(true);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        String tempSecret = getSecretKey();
        if (!isSecretKeySaved()) {
            setSecretKey("");
        }
        try {
            log.debug("Saving configuration file...");
            properties.store(new FileOutputStream(file), "");
        } catch (FileNotFoundException e) {
            log.error("Configuration file not found.", e);
        } catch (IOException e) {
            log.error("Configuration file could not be read.", e);
        }
        setSecretKey(tempSecret);
    }

    public String getAccessKey() {
        String key = properties.getProperty(ACCESS_KEY);
        if (key != null) {
            key.trim();
        }
        return key;
    }

    public void setAccessKey(String key) {
        if (key != null) {
            key.trim();
        }
        properties.setProperty(ACCESS_KEY, key);
    }

    public String getSecretKey() {
        String key = properties.getProperty(SECRET_KEY);
        if (key != null) {
            key.trim();
        }
        return key;
    }

    public void setSecretKey(String key) {
        if (key != null) {
            key.trim();
            properties.setProperty(SECRET_KEY, key);
        }
    }

    public void setSaveSecretKey(boolean value) {
        this.saveSecretKey = value;
    }

    public boolean isSecretKeySaved() {
        return this.saveSecretKey;
    }

    public boolean isAccessConfigured() {
        return (getAccessKey() != null && getSecretKey() != null && getSecretKey().length() > 0 && getAccessKey().length() > 0);
    }

    public void setMainWindowBounds(Rectangle bounds) {
        properties.setProperty(MAIN_WINDOW_X_POS, Integer.toString(bounds.x));
        properties.setProperty(MAIN_WINDOW_Y_POS, Integer.toString(bounds.y));
        properties.setProperty(MAIN_WINDOW_HEIGHT, Integer.toString(bounds.height));
        properties.setProperty(MAIN_WINDOW_WIDTH, Integer.toString(bounds.width));
    }

    public Rectangle getMainWindowBounds() {
        int x = SystemUtil.stringToInt(properties.getProperty(MAIN_WINDOW_X_POS), 0);
        int y = SystemUtil.stringToInt(properties.getProperty(MAIN_WINDOW_Y_POS), 0);
        int width = SystemUtil.stringToInt(properties.getProperty(MAIN_WINDOW_WIDTH), 661);
        int height = SystemUtil.stringToInt(properties.getProperty(MAIN_WINDOW_HEIGHT), 523);
        return new Rectangle(x, y, width, height);
    }

    public boolean isSDBSecureConnection() {
        String value = properties.getProperty(SDB_IS_SECURE);
        if (value == null) {
            return true;
        }
        return SystemUtil.stringToBoolean(properties.getProperty(SDB_IS_SECURE));
    }

    public void setSDBSecureConnection(boolean secureConnection) {
        properties.setProperty(SDB_IS_SECURE, SystemUtil.booleanToString(secureConnection));
    }

    public void setSDBShowRowNumber(boolean show) {
        properties.setProperty(SDB_SHOW_ROW_NUMBER_COLUMN, SystemUtil.booleanToString(show));
    }

    public boolean isSDBRowNumberShown() {
        return SystemUtil.stringToBoolean(properties.getProperty(SDB_SHOW_ROW_NUMBER_COLUMN));
    }

    public void setSDBServerPort(int port) {
        if (port < 0 || port > 65535) {
            throw new NumberFormatException(port + "is not a valid port number, please enter a number between 0 and 65535.");
        }
        properties.setProperty(SDB_PORT, String.valueOf(port));
    }

    public int getDefaultSDBServerPort() {
        if (isSDBSecureConnection()) {
            return 443;
        } else {
            return 80;
        }
    }

    public int getSDBServerPort() {
        return SystemUtil.stringToInt(properties.getProperty(SDB_PORT), getDefaultSDBServerPort());
    }

    public String getDefaultSDBServerURL() {
        return "sdb.amazonaws.com";
    }

    public String getSDBServerURL() {
        return properties.getProperty(SDB_SERVER, getDefaultSDBServerURL());
    }

    public void setSDBServerULR(String url) {
        if (url == null || url.length() == 0) {
            properties.setProperty(SDB_SERVER, getDefaultSDBServerURL());
        } else {
            properties.setProperty(SDB_SERVER, url);
        }
    }

    public void resetSimpleDB() {
        simpleDB = null;
    }

    public SimpleDB getSimpleDB() {
        if (simpleDB == null) {
            simpleDB = new SimpleDB(getAccessKey(), getSecretKey(), isSDBSecureConnection(), getSDBServerURL(), getSDBServerPort());
        }
        return simpleDB;
    }

    public void setSDBMultipleBackgroundColorize(boolean value) {
        properties.setProperty(SDB_MULTI_ATTR_BACKGROUND, SystemUtil.booleanToString(value));
    }

    public boolean getSDBMultipleBackgroundColorize() {
        return SystemUtil.stringToBoolean(properties.getProperty(SDB_MULTI_ATTR_BACKGROUND));
    }

    public void setSplitDividerLocation(int location) {
        properties.setProperty(MAIN_WINDOW_SPLIT_PANE_DIVIDER, Integer.toString(location));
    }

    public int getSplitDividerLocation() {
        return new Integer(properties.getProperty(MAIN_WINDOW_SPLIT_PANE_DIVIDER, "-1")).intValue();
    }
}
