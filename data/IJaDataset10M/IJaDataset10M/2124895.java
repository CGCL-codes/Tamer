package com.gorillalogic.faces.util;

import java.io.*;
import java.util.*;
import com.gorillalogic.dal.Table;
import com.gorillalogic.dal.AccessException;
import com.gorillalogic.config.ConfigurationException;
import com.gorillalogic.config.Preferences;
import com.gorillalogic.faces.beans.GlSession;
import com.gorillalogic.gosh.Script;
import com.gorillalogic.gosh.GoshOptions;
import com.gorillalogic.util.IOUtil;
import org.apache.log4j.Logger;

/**
 * manages the import/cache/refresh of custom jsps
 *  
 */
public class CustomJspMgr {

    static Logger logger = Logger.getLogger(CustomJspMgr.class);

    private static String _cacheDir = ".";

    private static String CACHE_PATH_A = "../..";

    private static String CACHE_PATH_B = "pages/faces";

    private static String CACHE_PATH = CACHE_PATH_A + "/" + CACHE_PATH_B;

    private static Hashtable _cacheMap = new Hashtable();

    public static void setCacheDir(String targetRelativeToGXEJsps) {
        _cacheDir = targetRelativeToGXEJsps;
    }

    public static String getCacheDir() {
        return _cacheDir;
    }

    public static String getJspName(String trueName) throws ConfigurationException {
        return getJspName(trueName, true);
    }

    public static String getJspName(String trueName, boolean check) throws ConfigurationException {
        if (!isInitialized) {
            initialize();
            isInitialized = true;
        }
        String cacheName = (String) _cacheMap.get(trueName);
        if (cacheName == null || check) {
            putInCache(trueName, _cacheDir);
            cacheName = (String) _cacheMap.get(trueName);
        }
        return cacheName;
    }

    private static void setHtml(String jsp, String key) throws ConfigurationException {
        String baseDir = "pages/faces";
        putInCache(jsp, key, baseDir);
    }

    private static void putInCache(String jsp, String baseDir) throws ConfigurationException {
        putInCache(jsp, baseDir, jsp);
    }

    private static void putInCache(String jsp, String baseDir, String key) throws ConfigurationException {
        if (key == null || key.length() == 0) {
            key = jsp;
        }
        File sourceJsp = getSourceFile(jsp);
        if (!sourceJsp.exists()) {
            throw new ConfigurationException(sourceJsp.getPath() + " not found. ");
        }
        logger.debug("Checking for updated version of " + sourceJsp.getPath());
        String targetJspName = sourceJsp.getName();
        File targetJsp = new File(baseDir, targetJspName);
        if (!targetJsp.exists() || targetJsp.lastModified() < sourceJsp.lastModified()) {
            logger.info("Copying " + sourceJsp.getAbsolutePath() + " to " + targetJsp.getAbsolutePath());
            try {
                IOUtil.copy(sourceJsp, targetJsp);
            } catch (IOException e) {
                String msg = "Error copying jsp file \"" + sourceJsp.getPath() + "\"";
                ConfigurationException ex = new ConfigurationException(msg, e);
                logger.error(msg, ex);
                throw ex;
            }
        } else {
            logger.info(targetJsp.getAbsolutePath() + " is up to date.");
        }
        String cacheValue = "/" + CACHE_PATH_B + "/" + targetJspName;
        logger.debug("cache value will be: " + cacheValue);
        _cacheMap.put(key, cacheValue);
    }

    private static File getSourceFile(String path) throws ConfigurationException {
        try {
            path = Script.factory.findScript(path).getFilePath();
        } catch (Script.InvalidException e) {
            throw new ConfigurationException("error getting custom jsp \"" + path + "\": " + e.getMessage(), e);
        }
        return new File(path);
    }

    private static boolean isInitialized = false;

    private static void initialize() {
        setCacheBase();
        registerScriptHandler();
    }

    private static void setCacheBase() {
        File rootDir = new File(Preferences.getBootDir("."), CACHE_PATH_A);
        File userDir = new File(rootDir, CACHE_PATH_B);
        userDir.mkdir();
        _cacheDir = userDir.getAbsolutePath();
    }

    private static void registerScriptHandler() {
        Script.factory.registerExtension("jsp", new Script.Handler() {

            public void run(Script script, PrintWriter out, Table context, GoshOptions options) throws AccessException {
            }
        });
    }
}
