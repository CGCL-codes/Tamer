package com.rc.celeritas.generics;

import com.rc.celeritas.exception.CeleritasException;
import com.rc.celeritas.util.UtilConstants;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author rchoudhary
 */
public class PropertyHelper {

    private static ResourceBundle rb;

    private static boolean _initialized = false;

    private static Logger log = Logger.getLogger(PropertyHelper.class);

    /**
     *
     * @param iluRefMap
     * @return
     */
    public static HashMap<String, HashMap<String, String>> generateMap(Properties iluRefMap) {
        HashMap<String, HashMap<String, String>> iluMap = null;
        if (MapUtils.isNotEmpty(iluRefMap)) {
            iluMap = new HashMap<String, HashMap<String, String>>();
            Iterator iter = iluRefMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String luString = MapUtils.getString(iluRefMap, key, "");
                HashMap<String, String> lookup = parseLuFields(luString);
                iluMap.put(key, lookup);
            }
        }
        return iluMap;
    }

    /**
     *
     * @return
     * @throws com.rc.celeritas.exception.CeleritasException
     */
    public static boolean init() throws CeleritasException {
        try {
            rb = ResourceBundle.getBundle("celeritas");
        } catch (Exception e) {
            log.error("Error in Initializing Celeritas : " + e.toString());
            throw new CeleritasException(e);
        }
        return _initialized = true;
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return rb.getString(key);
    }

    /**
     * 
     * @param key
     * @param database
     * @return
     */
    public static String getDBValue(String key, String database) {
        return rb.getString(rb.getString(database) + "." + key);
    }

    /**
     *
     * @param fileName
     * @return
     * @throws com.rc.celeritas.exception.CeleritasException
     */
    public static synchronized Properties loadProperty(String fileName) throws CeleritasException {
        Properties props = new Properties();
        try {
            InputStream in = PropertyHelper.class.getClassLoader().getResourceAsStream(fileName);
            if (StringUtils.endsWithIgnoreCase(fileName, UtilConstants.XML)) {
                props.loadFromXML(in);
            } else if (StringUtils.endsWithIgnoreCase(fileName, UtilConstants.PROPERTIES) || StringUtils.endsWithIgnoreCase(fileName, UtilConstants.PROP)) {
                props.load(in);
            } else {
                throw new CeleritasException("Unknown File Format in file : " + fileName);
            }
        } catch (FileNotFoundException fnfe) {
            log.error("Error in reading file : " + fileName + "\n" + fnfe.toString());
            fnfe.printStackTrace();
            throw new CeleritasException(fnfe);
        } catch (IOException ioe) {
            log.error("Error in loading property for file : " + fileName + "\n" + ioe.toString());
            throw new CeleritasException(ioe);
        }
        return props;
    }

    /**
     *
     * @param luString
     * @return
     */
    public static synchronized HashMap<String, String> parseLuFields(String luString) {
        HashMap<String, String> lookup = null;
        if (StringUtils.isNotEmpty(luString)) {
            lookup = new HashMap<String, String>();
            String lus[] = luString.split(Constants.ILU_SEPERATOR);
            for (int i = 0; i < lus.length; i++) {
                if (StringUtils.isNotEmpty(lus[i])) {
                    String pair[] = lus[i].split(Constants.KEY_VALUE_SEPERATOR);
                    if (pair.length == 1) {
                        lookup.put(pair[0], pair[0]);
                    } else if (pair.length > 1) {
                        lookup.put(pair[0], pair[1]);
                    }
                }
            }
        }
        return lookup;
    }
}
