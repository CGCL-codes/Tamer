package org.jasen.core.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.jasen.error.ErrorHandlerBroker;
import org.jasen.error.JasenException;
import org.jasen.interfaces.JasenMapStore;
import org.jasen.util.FileUtils;

/**
 * <p>
 *  Singleton class for maintaining in-memory references to loaded JasenMaps
 * </p>
 * @see org.jasen.core.engine.JasenMap
 * @author Jason Polites
 */
public final class JasenMapLoader {

    private static JasenMapLoader instance;

    private static final Object mutex = new Object();

    private Map maps = null;

    private JasenMapLoader() {
        super();
        maps = new HashMap();
    }

    public static final JasenMapLoader getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new JasenMapLoader();
                }
                mutex.notifyAll();
            }
        }
        return instance;
    }

    /**
	 * Gets the map associated with the given store.
	 * @param store The store to access for the map.
	 * @param properties The properties required to load the map from the store (if required)
	 * @return A single instance of the map loaded.
	 * @throws JasenException
	 */
    public JasenMap getJasenMap(JasenMapStore store, Properties properties) throws JasenException {
        JasenMap map = (JasenMap) maps.get(store.getStoreKey());
        if (map == null) {
            synchronized (mutex) {
                map = (JasenMap) maps.get(store.getStoreKey());
                if (map == null) {
                    map = store.load(properties);
                    maps.put(store.getStoreKey(), map);
                }
                mutex.notifyAll();
            }
        }
        return map;
    }

    /**
	 * Initialises and loads the JasenMapStore identified by the properties file provided.
	 * @param propertiesPath
	 * @return The opened and loaded JasenMapStore
	 * @throws JasenException
	 * @deprecated No longer in use
	 */
    public JasenMapStore openJasenMapStore(String propertiesPath) throws JasenException {
        String path = FileUtils.getAbsolutePath(this.getClass().getClassLoader(), propertiesPath);
        File file = new File(path);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            return openJasenMapStore(fin);
        } catch (IOException e) {
            throw new JasenException(e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    ErrorHandlerBroker.getInstance().getErrorHandler().handleException(e);
                }
            }
        }
    }

    /**
	 * Creates and opens the JasenMapStore identified by the properties file stream provided.
	 * @param in A stream which contains a the Properties to create and open the store.
	 * @return The opened and loaded JasenMapStore
	 * @throws JasenException
	 */
    public JasenMapStore openJasenMapStore(InputStream in) throws JasenException {
        Properties props = new Properties();
        try {
            props.load(in);
            return openJasenMapStore(props);
        } catch (Exception e) {
            throw new JasenException(e);
        }
    }

    /**
	 * Creates and opens the JasenMapStore identified by the properties object provided.
	 * @param properties The Properties containing the configuration for the store.
	 * @return The opened and loaded JasenMapStore
	 * @throws JasenException
	 */
    public JasenMapStore openJasenMapStore(Properties properties) throws JasenException {
        try {
            JasenMapStore store = (JasenMapStore) Class.forName(properties.getProperty("map-store-class")).newInstance();
            store.load(properties);
            return store;
        } catch (Exception e) {
            throw new JasenException(e);
        }
    }
}
