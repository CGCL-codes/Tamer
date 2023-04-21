package org.wahlzeit.services;

import java.io.*;
import java.net.*;
import java.util.*;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.utils.StringUtil;

/**
 * A set of utility functions to retrieve URLs and files.
 * Call setValue(key, value) to change default values, see below.
 * 
 * @author driehle
 *
 */
public class SysConfig extends AbstractConfig {

    /**
	 * 
	 */
    protected static SysConfig instance = null;

    /**
	 * 
	 */
    public static SysConfig getInstance() {
        if (instance == null) {
            SysLog.logInfo("setting generic SysConfig");
            setInstance(new SysConfig("localhost", "8585"));
        }
        return instance;
    }

    /**
	 * Method to set the singleton instance of SysConfig.
	 */
    public static synchronized void setInstance(SysConfig sysConfig) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initalize SysConfig again");
        }
        instance = sysConfig;
    }

    /**
	 * Drop singleton instance to cope with repeated startup/shutdown scenarios
	 */
    public static synchronized void dropInstance() {
        instance = null;
        SysLog.logInfo("dropped current SysConfig");
    }

    /**
	 * 
	 */
    protected ConfigDir scriptsDir = new ConfigDir("config" + File.separator + "scripts");

    protected ConfigDir staticDir = new ConfigDir("config" + File.separator + "static");

    protected ConfigDir templatesDir = new ConfigDir("config" + File.separator + "templates");

    /**
	 * 
	 */
    public SysConfig(String host) {
        this(host, "80");
    }

    /**
	 *
	 */
    public SysConfig(String host, String port) {
        if (!StringUtil.isNullOrEmptyString(port) && !port.equals("80")) {
            doSetValue("HTTP_PORT", port);
            doSetValue("SITE_URL", "http://" + host + ":" + port + "/");
        } else {
            doSetValue("HTTP_PORT", "80");
            doSetValue("SITE_URL", "http://" + host + "/");
        }
        doSetValue("PHOTOS_URL", doGetValue("SITE_URL") + "data/photos/");
        doSetValue("STATIC_URL", doGetValue("SITE_URL") + "config/static/");
        doSetValue("PHOTOS_DIR", "data" + File.separator + "photos" + File.separator);
        doSetValue("BACKUP_DIR", "data" + File.separator + "backup" + File.separator);
        doSetValue("TEMP_DIR", "data" + File.separator + "temp" + File.separator);
        doSetValue("HEADING_IMAGE", "heading.png");
        doSetValue("LOGO_IMAGE", "wahlzeit.png");
        doSetValue("EMPTY_IMAGE", "empty.png");
        doSetValue("DB_CONNECTION", "jdbc:postgresql://localhost:5432/wahlzeit");
        doSetValue("DB_USER", "wahlzeit");
        doSetValue("DB_PASSWORD", "wahlzeit");
    }

    /**
	 * 
	 */
    public static String getSiteUrlAsString() {
        return getInstance().getValue("SITE_URL");
    }

    /**
	 * 
	 */
    public static String getLinkAsUrlString(String link) {
        return getSiteUrlAsString() + link + ".html";
    }

    /**
	 * 
	 */
    public static String getPhotosUrlAsString() {
        return getInstance().getValue("PHOTOS_URL");
    }

    /**
	 * 
	 */
    public static String getHttpPortAsString() {
        return getInstance().getValue("HTTP_PORT");
    }

    /**
	 * 
	 */
    public static int getHttpPortAsInt() {
        return Integer.parseInt(getHttpPortAsString());
    }

    /**
	 * 
	 */
    public static String getPhotosDirAsString() {
        return getInstance().getValue("PHOTOS_DIR");
    }

    /**
	 * 
	 */
    public static String getBackupDirAsString() {
        return getInstance().getValue("BACKUP_DIR");
    }

    /**
	 * 
	 */
    public static String getTempDirAsString() {
        return getInstance().getValue("TEMP_DIR");
    }

    /**
	 * 
	 */
    public static ConfigDir getStaticDir() {
        return getInstance().staticDir;
    }

    /**
	 * 
	 */
    public static ConfigDir getScriptsDir() {
        return getInstance().scriptsDir;
    }

    /**
	 * 
	 */
    public static ConfigDir getTemplatesDir() {
        return getInstance().templatesDir;
    }

    /**
	 * 
	 */
    public static String getHeadingImageAsUrlString(Language l) {
        String sfn = l.asIsoCode() + File.separator + getInstance().getValue("HEADING_IMAGE");
        String ffn = getStaticDir().getFullConfigFileName(sfn);
        return getSiteUrlAsString() + ffn;
    }

    /**
	 * 
	 */
    public static String getLogoImageAsUrlString(Language l) {
        String sfn = l.asIsoCode() + File.separator + getInstance().getValue("LOGO_IMAGE");
        String ffn = getStaticDir().getFullConfigFileName(sfn);
        return getSiteUrlAsString() + ffn;
    }

    /**
	 * 
	 */
    public static String getEmptyImageAsUrlString(Language l) {
        String sfn = l.asIsoCode() + File.separator + getInstance().getValue("EMPTY_IMAGE");
        String ffn = getStaticDir().getFullConfigFileName(sfn);
        return getSiteUrlAsString() + ffn;
    }

    /**
	 * 
	 */
    public static String getDbConnectionAsString() {
        return getInstance().getValue("DB_CONNECTION");
    }

    /**
	 * 
	 */
    public static String getDbUserAsString() {
        return getInstance().getValue("DB_USER");
    }

    /**
	 * 
	 */
    public static String getDbPasswordAsString() {
        return getInstance().getValue("DB_PASSWORD");
    }
}
