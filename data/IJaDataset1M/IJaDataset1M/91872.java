package org.dcm4chee.xero.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a properties provider for DICOM AEs. Initialize the default properties to connect to the localhost.
 * <p>
 * Definition files must be resources in the classpath and be of the form:  
 * <p>
 * <i> ae-{aePath}.properties</i>
 * <br>
 * Where aePath is of the form <aeTitle>[@hostName][:port] i.e. DCM4CHEE@localhost:104
 * @author smohan
 * 
 */
public class AEProperties {

    private static final Logger log = LoggerFactory.getLogger(AEProperties.class);

    public static final String DEFAULT_LOCAL_TITLE = "XERO";

    public static final String DEFAULT_AE_TITLE = "DCM4CHEE";

    public static final String DEFAULT_TYPE = "idc2";

    public static final int DEFAULT_AE_PORT = 11112;

    public static final String AE_TITLE_KEY = "title";

    public static final String AE_PORT_KEY = "aeport";

    public static final String AE_HOST_KEY = "host";

    public static final String TYPE = "type";

    public static final String EJB_PORT = "ejbport";

    public static final String LOCAL_TITLE = "localTitle";

    /** User ID to use if the AE requires authentication */
    public static final String USER = "user";

    /** Password to use if the AE requires authentication */
    public static final String PASSWORD = "password";

    private static final String FILE_NAME_PREPEND = "ae-";

    private static final String FILE_NAME_EXT = ".properties";

    private static final Pattern validFileNamePattern = Pattern.compile("[a-z0-9_@]+", Pattern.CASE_INSENSITIVE);

    private static final AEProperties aeProperties = new AEProperties();

    /** The key to use for a particular ae */
    public static final String AE = "ae";

    /** The name of hte AE property file. */
    public static final String AE_PROPERTY_NAME = "_aePropertyName";

    /** Set this property to control which issuer is the default one */
    public static final String DEFAULT_ISSUER = "defaultIssuer";

    private Map<String, Object> defaultProperties = null;

    private ConcurrentHashMap<String, Map<String, Object>> remoteProperties = new ConcurrentHashMap<String, Map<String, Object>>();

    ClassLoader cl = Thread.currentThread().getContextClassLoader();

    /**
    * force initialization through this class
    */
    private AEProperties() {
        initLocalProperties();
    }

    /**
    * 
    * @return instance of this class
    */
    public static AEProperties getInstance() {
        return aeProperties;
    }

    /**
    * populate default ae properties.
    */
    private void initLocalProperties() {
        loadRemoteProperty("local");
        defaultProperties = remoteProperties.get("local");
        if (defaultProperties != null) return;
        log.info("Loading local properties from default.");
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put(AE_HOST_KEY, "localhost");
        temp.put(AE_PORT_KEY, 11112);
        temp.put(AE_TITLE_KEY, DEFAULT_AE_TITLE);
        temp.put(LOCAL_TITLE, DEFAULT_LOCAL_TITLE);
        temp.put(AE_PROPERTY_NAME, "local");
        temp.put(TYPE, DEFAULT_TYPE);
        defaultProperties = Collections.unmodifiableMap(temp);
    }

    /**
    * loads the property for the given ae name.
    * 
    * @param aePath
    */
    @SuppressWarnings("unchecked")
    private void loadRemoteProperty(String aePath) {
        String propName = FILE_NAME_PREPEND + aePath + FILE_NAME_EXT;
        InputStream is = cl.getResourceAsStream(propName);
        Properties props = new Properties();
        if (is != null) {
            try {
                props.load(is);
                String hostname = props.getProperty(AE_HOST_KEY);
                String ejbport = props.getProperty("ejbport");
                String aeport = props.getProperty(AE_PORT_KEY);
                if (aeport == null) aeport = Integer.toString(DEFAULT_AE_PORT);
                String title = props.getProperty(AE_TITLE_KEY);
                if (title == null) props.put(AE_TITLE_KEY, DEFAULT_AE_TITLE);
                String localTitle = props.getProperty(LOCAL_TITLE);
                if (localTitle == null) props.put(LOCAL_TITLE, DEFAULT_LOCAL_TITLE);
                String type = props.getProperty(TYPE);
                if (type == null) props.put(TYPE, DEFAULT_TYPE);
                if (hostname != null) {
                    Map mprops = props;
                    Map<String, Object> map = (Map<String, Object>) mprops;
                    map.put(AE_PORT_KEY, Integer.parseInt(aeport));
                    if (ejbport != null) map.put("ejbport", Integer.parseInt(ejbport));
                    map.put(AE_PROPERTY_NAME, aePath);
                    for (Map.Entry<String, Object> me : map.entrySet()) {
                        Object v = me.getValue();
                        if (!(v instanceof String)) continue;
                        String sv = (String) v;
                        if (!(sv.startsWith("${array:[") && sv.endsWith("]}"))) continue;
                        sv = sv.substring(9, sv.length() - 2);
                        String sva[] = sv.split("[ \t]*,[ \t]*");
                        log.info("Split array " + sv + " in ae property " + me.getKey() + " to an array with " + sva.length + " elements");
                        me.setValue(sva);
                    }
                    remoteProperties.putIfAbsent(aePath, map);
                } else {
                    log.error("The host must be specified in ae properties file " + aePath);
                }
            } catch (Exception e) {
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            log.warn("Unable to find ae property file {}", propName);
        }
    }

    /**
    * @return default property set.
    */
    public Map<String, Object> getDefaultAE() {
        return defaultProperties;
    }

    /**
    * 
    * @param ae
    *           name. Locate and load the properties from the file. If value is
    *           'local' return default properties.
    * @return properties for the given ae name. Will return <tt>null</tt> if
    *         unable to determine the property set.
    */
    public Map<String, Object> getAE(String name) {
        if (name.equals("local")) {
            return getDefaultAE();
        }
        if (!remoteProperties.contains(name)) {
            loadRemoteProperty(name);
        }
        if (!validFileNamePattern.matcher(name).matches()) throw new IllegalArgumentException("Invalid AE path:  Contains illegal filesystem characters");
        return remoteProperties.get(name);
    }

    /**
    * Gets the AE object from the parameters.  Throws a runtime exception if the AE
    * isn't found and the AE is specified.
    */
    public static Map<String, Object> getAE(Map<String, Object> params) {
        String aes = (String) params.get(AE);
        if (aes == null) return aeProperties.getDefaultAE();
        Map<String, Object> ret = aeProperties.getAE(aes);
        if (ret == null) throw new RuntimeException("Unknown AE specified:" + aes);
        return ret;
    }
}
