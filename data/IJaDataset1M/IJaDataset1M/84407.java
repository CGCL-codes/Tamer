package net.xiaoxishu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * 根据给定的文件名,返回一个ConfigFactory的实例.<br>
 * 这其实是对Properties的一个封装.<br>
 * 实现了load和save的功能,以及getXXX(type)的功能<br>
 * 
 * 在具体的Project中可以通过这个getConfig(String configFile)的方法,
 * 构造一个封装了特殊属性的set/get方法的Config对象.
 * @see net.xiaoxishu.heiyou.Config
 * @author lushu
 *
 */
public class ConfigFactory {

    protected static final Logger logger = Logger.getLogger(ConfigFactory.class);

    private String configFile;

    Properties property = new Properties();

    boolean getResource = false;

    public static ConfigFactory getConfig(String configFile) {
        return new ConfigFactory(configFile);
    }

    public String getProperty(String key) {
        return trim(getPropertyInternal(key));
    }

    public void setProperty(String key, String value) {
        property.setProperty(key, value);
    }

    protected String getPropertyInternal(String key) {
        return property.getProperty(key);
    }

    private ConfigFactory(String configFile) {
        this.configFile = configFile;
        InputStream stream;
        try {
            stream = new FileInputStream(configFile);
            property.load(stream);
            getResource = true;
            stream.close();
        } catch (IOException e) {
            logger.debug("Error in loading Config file: " + e.toString());
        }
        if (!getResource) logger.debug("Config file doesn't exist yet.");
    }

    private static String trim(String str) {
        if (null != str) return str.trim(); else return null;
    }

    public void save() {
        try {
            if (!getResource) {
                File file = new File(configFile);
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(configFile);
            property.store(out, null);
        } catch (IOException e) {
            logger.error("Error in save config file", e);
        }
    }

    public void clear() {
        if (getResource) {
            property.clear();
        }
    }
}
