package magoffin.matt.meta;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import org.apache.log4j.Logger;

/**
 * A simple manager of {@link MetadataResourceFactory} instances.
 * 
 * <p>This class manages finding {@link MetadataResourceFactory} implementations
 * for files based on a classpath {@link Properties} resource. When 
 * {@link #isDisableSearchPath()} is <em>false</em> (the default setting)
 * then this class will search in the following locations:</p>
 * 
 * <ol>
 *   <li>The path configured via {@link #getManagerProperties()}</li>
 *   <li>smeta.properties</li>
 *   <li>META-INF/smeta.properties</li>
 * </ol>
 * 
 * <p>If the {@link #isDisableSearchPath()} is <em>true</em> then
 * <em>only</em> the {@link #getManagerProperties()} path will be used. The
 * properties are searched until the first suitable key is found. The 
 * properties resource is loaded via the same ClassLoader that loaded this class, i.e.
 * <code>getClass().getClassLoader().getResource()</code> is used. The properties 
 * file must have the format</p>
 * 
 * <pre>smeta.factory.KEY = CLASS</pre>
 * 
 * <p>where <code>KEY</code> is either a file suffix or a MIME type, and 
 * <code>CLASS</code> is a fully-qualified class name for a class that 
 * implements {@link MetadataResourceFactory}. This class must provide a default
 * no-argument constructor. The class will be loaded in the same ClassLoader that
 * loaded this class, i.e. <code>getClass().getClassLoader().loadClass()</code>
 * is used.</p>
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 1.10 $ $Date: 2008/09/21 22:26:29 $
 */
public class MetadataResourceFactoryManager {

    /** A custom class-path properties file. */
    public static final String CUSTOM_MANAGER_PROPERTIES = "smeta.properties";

    /** The default class-path properties file. */
    public static final String DEFAULT_MANAGER_PROPERTIES = "META-INF/smeta.properties";

    /** The prefix used for {@link MetadataResourceFactory} configuration properties. */
    public static final String FACTORY_PROPERTY_KEY_PREFIX = "smeta.factory.";

    private static MetadataResourceFactoryManager DEFAULT_INSTANCE = null;

    private String managerProperties = null;

    private boolean disableSearchPath = false;

    private Map<String, MetadataResourceFactory> factoryCache = Collections.synchronizedMap(new WeakHashMap<String, MetadataResourceFactory>());

    private final Logger log = Logger.getLogger(getClass());

    /**
	 * Get the default {@link MetadataResourceFactoryManager} instance.
	 * @return the default instance
	 */
    public static MetadataResourceFactoryManager getDefaultManagerInstance() {
        if (DEFAULT_INSTANCE != null) {
            return DEFAULT_INSTANCE;
        }
        synchronized (DEFAULT_MANAGER_PROPERTIES) {
            if (DEFAULT_INSTANCE != null) {
                return DEFAULT_INSTANCE;
            }
            DEFAULT_INSTANCE = new MetadataResourceFactoryManager();
            return DEFAULT_INSTANCE;
        }
    }

    /**
	 * Get a MetadataResourceFactory instance for a particular file.
	 * 
	 * <p>The file suffix (anything after the last period in the file name)
	 * is used as the key to lookup the factory implementation to use.</p>
	 * 
	 * @param file the file
	 * @return the MetadataResourceFactory instance, or <em>null</em> if 
	 * unable to find a suitable implementation
	 * @throws MetadataConfigurationException if unable to load configuration
	 */
    public MetadataResourceFactory getMetadataResourceFactory(File file) throws MetadataConfigurationException {
        String key = file.getName().toLowerCase();
        int dot = key.lastIndexOf('.');
        if (dot != -1 && dot != (key.length() - 1)) {
            key = key.substring(key.lastIndexOf('.') + 1);
        }
        return getFactoryInstance(file, key);
    }

    private MetadataResourceFactory getFactoryInstance(File file, String key) {
        MetadataResourceFactory result = factoryCache.get(key);
        if (result == null) {
            result = findFactory(file, key);
            if (result != null) {
                synchronized (factoryCache) {
                    for (MetadataResourceFactory factory : factoryCache.values()) {
                        if (factory.getClass() == result.getClass()) {
                            result = factory;
                            break;
                        }
                    }
                }
                factoryCache.put(new String(key), result);
            }
        }
        return result;
    }

    /**
	 * Get a MetadataResourceFactory instance for a particular MIME type.
	 * 
	 * <p>The MIME type itself is used as the key to lookup the factory 
	 * implementation to use.</p>
	 * 
	 * @param mime the MIME type of the resource
	 * @return the MetadataResourceFactory instance, or <em>null</em> if 
	 * unable to find a suitable implementation
	 * @throws MetadataConfigurationException if unable to load configuration
	 */
    public MetadataResourceFactory getMetadataResourceFactory(String mime) throws MetadataConfigurationException {
        String key = mime.toLowerCase();
        return getFactoryInstance(null, key);
    }

    /**
	 * Find a {@link MetadataResourceFactory} for a given file and key.
	 * 
	 * <p>This method will use a search path of up to three locations in 
	 * the class path to look for a {@link Properties} file with sMeta
	 * configuration settings. The first configuration setting found will 
	 * be used. The three locations are:</p>
	 * 
	 * <ol>
	 *   <li>The path configured via {@link #getManagerProperties()}</li>
	 *   <li>smeta.properties</li>
	 *   <li>META-INF/smeta.properties</li>
	 * </ol>
	 * 
	 * <p>If the {@link #isDisableSearchPath()} returns <em>true</em> then
	 * <em>only</em> the {@link #getManagerProperties()} path will be used.</p>
	 * 
	 * <p>The Properties resource will be queried for a key in the form of 
	 * <code>smeta.factory.KEY</code> where <code>KEY</code> is the value
	 * of the <em>key</em> parameter passed to this method. The property 
	 * value must be the fully qualified class name of something that 
	 * implements {@link MetadataResourceFactory}, which must provide a 
	 * no-argument constructor to be instantiated by this class.</p>
	 * 
	 * <p>Extending classes can override this method to provide more functionality.</p>
	 * 
	 * @param file the File being queried (if available)
	 * @param key the key to look up, eg. a file extension or MIME type
	 * @return the MetadataResourceFactory instance, or <em>null</em> if no configuration
	 * compatible for this file and key
	 * @throws MetadataConfigurationException if a configuration error occurs
	 */
    protected MetadataResourceFactory findFactory(File file, String key) throws MetadataConfigurationException {
        String[] searchPaths = disableSearchPath ? new String[] { managerProperties } : managerProperties != null ? new String[] { managerProperties, CUSTOM_MANAGER_PROPERTIES, DEFAULT_MANAGER_PROPERTIES } : new String[] { CUSTOM_MANAGER_PROPERTIES, DEFAULT_MANAGER_PROPERTIES };
        for (String aPath : searchPaths) {
            try {
                return lookupConfiguration(key, aPath);
            } catch (MetadataConfigurationException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to use configuration path [" + aPath + "]: " + e.getMessage());
                }
            }
        }
        throw new MetadataConfigurationException("Unable to locate suitable factory configuration. Searched in " + Arrays.toString(searchPaths));
    }

    private MetadataResourceFactory lookupConfiguration(String key, String propPath) {
        if (propPath == null) {
            throw new MetadataConfigurationException("Manager properties not specified");
        }
        URL resource = getClass().getClassLoader().getResource(propPath);
        if (resource == null) {
            throw new MetadataConfigurationException("Manager properties not found: " + propPath);
        }
        Properties properties = new Properties();
        try {
            properties.load(resource.openStream());
        } catch (IOException e) {
            throw new MetadataConfigurationException("Unable to load factory configuration properties [" + propPath + "]", e);
        }
        String factoryClassName = properties.getProperty(FACTORY_PROPERTY_KEY_PREFIX + key);
        if (factoryClassName == null) {
            if (log.isDebugEnabled()) {
                log.debug("Factory configuration not found: " + key);
            }
            return null;
        }
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(factoryClassName);
            Object factory = clazz.newInstance();
            if (!(factory instanceof MetadataResourceFactory)) {
                throw new MetadataConfigurationException("Manager property [" + FACTORY_PROPERTY_KEY_PREFIX + key + "] factory class does not implement " + MetadataResourceFactory.class);
            }
            return (MetadataResourceFactory) factory;
        } catch (Exception e) {
            throw new MetadataConfigurationException("Unable to instantiate factory class", e);
        }
    }

    /**
	 * Get the resource path of the manager Properties resource.
	 * 
	 * @return the managerProperties
	 */
    public String getManagerProperties() {
        return managerProperties;
    }

    /**
	 * Set the resource path of the manager Properties resource.
	 * 
	 * <p>If configured, this will be the first properties resource to look
	 * for while searching for {@link MetadataResourceFactory} instances. If
	 * {@link #isDisableSearchPath()} returns <em>true</em> then this will 
	 * be the <em>only</em> properties resource looked in.</p>
	 * 
	 * @param managerProperties the managerProperties to set
	 */
    public void setManagerProperties(String managerProperties) {
        this.managerProperties = managerProperties;
    }

    /**
	 * Return the search path flag.
	 * 
	 * <p>If this returns <em>true</em> then this instance will not search for
	 * {@link MetadataResourceFactory} implementations as described in 
	 * {@link #findFactory(File, String)}. Rather it will use only the configured 
	 * properties resource set via {@link #setManagerProperties(String)}. This 
	 * property defaults to <em>false</em>, meaning searching will be enabled.</p>
	 * 
	 * @return the disableSearchPath
	 */
    public boolean isDisableSearchPath() {
        return disableSearchPath;
    }

    /**
	 * Set the search path flag.
	 * 
	 * <p>If set to <em>true</em> then this instance will not search for
	 * {@link MetadataResourceFactory} implementations as described in 
	 * {@link #findFactory(File, String)}. Rather it will use only the configured 
	 * properties resource set via {@link #setManagerProperties(String)}.</p>
	 * 
	 * @param disableSearchPath the disableSearchPath to set
	 */
    public void setDisableSearchPath(boolean disableSearchPath) {
        this.disableSearchPath = disableSearchPath;
    }
}
