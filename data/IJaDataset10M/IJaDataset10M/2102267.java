package org.dbstore.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 配置文件读取
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * 
 * @version 1.0
 * 
 * @since 2011-2-17
 */
public class PropertiesUtil {

    private static final String DEFAULT = "config";

    public static String rootPath;

    private static Map<String, Properties> pMap;

    static {
        rootPath = System.getProperty("user.dir") + File.separator;
        PropertyConfigurator.configure(rootPath + "conf" + File.separator + "log4j.properties");
        pMap = new HashMap<String, Properties>();
        PropertiesUtil.load();
    }

    private static void load() {
        File confFolder = new File(rootPath + "conf");
        File[] confs = confFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        });
        for (File file : confs) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                Properties p = new Properties();
                p.load(new InputStreamReader(fis, "UTF-8"));
                pMap.put(file.getName().substring(0, file.getName().indexOf(".")), p);
            } catch (FileNotFoundException e) {
                Logger.getLogger(PropertiesUtil.class).error(e.getMessage(), e);
            } catch (IOException e) {
                Logger.getLogger(PropertiesUtil.class).error(e.getMessage(), e);
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.getLogger(PropertiesUtil.class).error(e.getMessage(), e);
                }
            }
        }
    }

    /**
	 * 获取字符串参数值
	 * 
	 * @param confName 配置文件名，不包括.properties
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
    public static String getString(String confName, String key) {
        String value = pMap.get(confName).getProperty(key);
        if (value == null) {
            load();
            value = pMap.get(confName).getProperty(key);
        }
        return value;
    }

    /**
	 * 获取字符串参数值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
    public static String getString(String key) {
        return getString(DEFAULT, key);
    }

    /**
	 * 获取字符串参数值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
    public static String getString(String key, String oldCode, String newCode) {
        return getString(DEFAULT, key, oldCode, newCode);
    }

    /**
	 * 获取字符串参数值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
    public static String getString(String confName, String key, String oldCode, String newCode) {
        String value = pMap.get(confName).getProperty(key);
        if (value == null) {
            load();
            if (oldCode != null) {
                try {
                    if (newCode != null) value = new String(value.getBytes(oldCode), newCode); else value = new String(value.getBytes(oldCode));
                } catch (UnsupportedEncodingException e) {
                    Logger.getLogger(PropertiesUtil.class).error(e.getMessage(), e);
                }
            }
        }
        return value;
    }

    /**
	 * 获取int型参数值
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
    public static int getInt(String key) {
        String v = getString(key);
        if (v != null) return Integer.valueOf(v); else throw new NullPointerException("No parameter!");
    }

    /**
	 * 获取int型参数值
	 * 
	 * @param confName 配置文件名，不包括.properties
	 * @param key
	 *            键
	 * @return 值
	 */
    public static int getInt(String confName, String key) {
        String v = getString(confName, key);
        if (v != null) return Integer.valueOf(v); else throw new NullPointerException("No parameter!");
    }

    /**
	 * 获取long型参数值
	 * 
	 * @param confName 配置文件名，不包括.properties
	 * @param key
	 *            键
	 * @return 值
	 */
    public static long getLong(String confName, String key) {
        String v = getString(confName, key);
        if (v != null) return Long.valueOf(v); else throw new NullPointerException("No parameter!");
    }

    /**
	 * 获取double型参数值
	 * 
	 * @param confName 配置文件名，不包括.properties
	 * @param key
	 *            键
	 * @return 值
	 */
    public static double getDouble(String confName, String key) {
        String v = getString(confName, key);
        if (v != null) return Double.valueOf(v); else throw new NullPointerException("No parameter!");
    }

    /**
	 * 获取所有键
	 * @param confName 配置文件名
	 * @return
	 */
    public static Set<String> getAllKeys(String confName) {
        Properties p = pMap.get(confName);
        if (p != null) return p.stringPropertyNames();
        return null;
    }

    /**
	 * 获取所有键
	 * @param confName 配置文件名
	 * @return
	 */
    public static Set<String> getAllKeys() {
        Properties p = pMap.get(DEFAULT);
        if (p != null) return p.stringPropertyNames();
        return null;
    }

    /**
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        System.out.println(PropertiesUtil.getAllKeys());
    }
}
