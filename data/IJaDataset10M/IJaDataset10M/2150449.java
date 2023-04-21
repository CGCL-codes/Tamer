package org.tigr.common;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.tigr.seq.log.*;

/**
 *
 * Class for determining runtime configuration properties.  Create a
 * java.util.Properties object based on the name of the requested key
 * Note that the default implementation of this will merge any
 * properties found under the <user.home>/<appdir>/ directory with
 * properties found in the org/tigr/seq/rtconfig directory (compiled
 * into the application).  It is an error to specify a base name for a
 * property file that doesn't exist in the compiled-in directory.  The
 * application directory <appdir> is specified in the
 * RuntimeConfigUtil.properties file set up in the
 * org/tigr/seq/rtconfig directory under the key "appbase".
 *
 * <p>
 *
 * Copyright &copy; 2001 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: RuntimeConfigUtil.java,v $
 * $Revision: 1.4 $
 * $Date: 2002/05/21 19:03:46 $
 * $Author: mcovarr $
 * </pre>
 * 
 * @author Miguel Covarrubias
 * @version 1.0 */
public class RuntimeConfigUtil {

    /**
     * Create a java.util.Properties object based on the name of the
     * file requested.  Note that the default implementation of this
     * will merge any properties found under the <user.home>/<appdir>/
     * directory with properties found in the org/tigr/seq/rtconfig
     * directory (compiled into Chrome).  It is an error to specify a
     * base name for a property file that doesn't exist in the
     * compiled-in directory.  The application directory <appdir> is
     * specified in the RuntimeConfigUtil.properties file set up in
     * the org/tigr/seq/rtconfig directory under the key "appbase".  */
    private static String userBaseDir;

    /**
     * A Hashtable from a <code>String</code> key to a
     * <code>Properties</code> object.
     */
    private static Hashtable<String, Properties> propertiesHash = new Hashtable<String, Properties>();

    static {
        String filename;
        ResourceBundle rb = null;
        filename = "org/tigr/seq/rtconfig/RuntimeConfigUtil";
        try {
            rb = ResourceBundle.getBundle(filename);
        } catch (MissingResourceException mre) {
        }
        if (rb == null) {
            Log.log(Log.FATAL, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "preset_bundle_not_found", filename));
        } else {
            String appbase = rb.getString("appbase");
            if (appbase == null) {
                Log.log(Log.FATAL, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "appbase_not_set", filename));
            } else {
                if (!appbase.endsWith("/")) appbase = appbase + "/";
                String home = System.getProperty("user.home");
                if (home == null) {
                    Log.log(Log.FATAL, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "user_home_not_set"));
                } else {
                    if (!home.endsWith("/")) home = home + "/";
                    RuntimeConfigUtil.userBaseDir = home + appbase;
                }
            }
        }
        Log.log(Log.INFO, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "user_base_dir", RuntimeConfigUtil.userBaseDir));
    }

    /**
     * Get the <code>Properties</code> bundle for the specified key.
     *
     * @param name a <code>String</code> key value
     *
     * @return the <code>Properties</code> value for this key */
    public static Properties getProperties(String pName) {
        Properties ret;
        ret = (Properties) RuntimeConfigUtil.propertiesHash.get(pName);
        if (ret == null) {
            ResourceBundle rb;
            String filename;
            filename = "org/tigr/seq/rtconfig/" + pName;
            rb = null;
            try {
                rb = ResourceBundle.getBundle(filename);
            } catch (MissingResourceException mre) {
            }
            if (rb == null) {
                Log.log(Log.ERROR, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "app_bundle_not_found", filename));
            } else {
                Enumeration keys = rb.getKeys();
                ret = new Properties();
                String key, value;
                while (keys.hasMoreElements()) {
                    key = (String) keys.nextElement();
                    value = rb.getString(key);
                    ret.put(key, value);
                }
                filename = RuntimeConfigUtil.userBaseDir + pName + ".properties";
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(filename);
                } catch (FileNotFoundException fnfe) {
                }
                if (fis == null) {
                    Log.log(Log.INFO, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "app_bundle_not_found", filename));
                } else {
                    Log.log(Log.INFO, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "app_bundle_found", filename));
                    try {
                        ret.load(fis);
                    } catch (IOException iox) {
                        Log.log(Log.ERROR, new Throwable(), ResourceUtil.getMessage(RuntimeConfigUtil.class, "ioexception_merging_properties", filename, iox.getMessage()));
                    }
                }
            }
            Properties sysprops = System.getProperties();
            Enumeration enumeration = sysprops.keys();
            String key, value;
            while (enumeration.hasMoreElements()) {
                key = (String) enumeration.nextElement();
                value = sysprops.getProperty(key);
                ret.put(key, value);
            }
            Properties tmp = (Properties) RuntimeConfigUtil.propertiesHash.get(pName);
            if (tmp == null) {
                synchronized (RuntimeConfigUtil.propertiesHash) {
                    tmp = (Properties) RuntimeConfigUtil.propertiesHash.get(pName);
                    if (tmp == null) {
                        RuntimeConfigUtil.propertiesHash.put(pName, ret);
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Get the base application directory, something like
     * <user.home>/.cloe.
     *
     *
     * @return a <code>String</code> value
     * */
    public static String getUserBaseDir() {
        return RuntimeConfigUtil.userBaseDir;
    }
}
