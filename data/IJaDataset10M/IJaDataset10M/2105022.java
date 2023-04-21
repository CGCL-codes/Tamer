package consciouscode.seedling.config;

import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.collections.CollectionUtils;
import consciouscode.seedling.ConfigLayer;
import consciouscode.seedling.ConfigResource;
import consciouscode.seedling.ConfigurationException;

/**
    Class documentation.
*/
public class ConfigResourceFactory {

    public ConfigResourceFactory() {
        String loaderClassName = PropertiesConfigResourceLoader.class.getName();
        myLoaders = new ConfigResourceLoader[] { loadLoader(loaderClassName) };
    }

    public void addLoader(Class loaderClass) {
        addLoaders(new Class[] { loaderClass });
    }

    public synchronized void addLoaders(Class[] loaderClasses) {
        if (loaderClasses.length > 0) {
            int maxSize = myLoaders.length + loaderClasses.length;
            HashSet currentClasses = new HashSet(maxSize);
            for (int i = 0; i < myLoaders.length; i++) {
                currentClasses.add(myLoaders[i].getClass());
            }
            ArrayList loaderList = new ArrayList(maxSize);
            CollectionUtils.addAll(loaderList, myLoaders);
            for (int i = 0; i < loaderClasses.length; i++) {
                Class loaderClass = loaderClasses[i];
                if ((loaderClass != null) && !currentClasses.contains(loaderClass)) {
                    loaderList.add(loadLoader(loaderClass));
                }
            }
            ConfigResourceLoader[] newLoaders = new ConfigResourceLoader[loaderList.size()];
            loaderList.toArray(newLoaders);
            myLoaders = newLoaders;
        }
    }

    /**
       Returns a configuration resource for a specified node, if one exists in
       the given ConfigLayer.

       @param nodeAddress is an absolute, local path to the desired node.
       In other
       words, it must start with a single slash character (<code>'/'</code>).
       @return <code>null</code> if no configuration resource exists for the
       node (within this configuration layer).
    */
    public ConfigResource loadConfigResource(ConfigLayer configLayer, String nodeAddress) throws ConfigurationException {
        for (int i = 0; i < myLoaders.length; i++) {
            ConfigResource resource = myLoaders[i].loadConfigResource(configLayer, nodeAddress);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    private ConfigResourceLoader loadLoader(String loaderClassName) {
        try {
            Class loaderClass = Class.forName(loaderClassName);
            return loadLoader(loaderClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class does not exist: " + loaderClassName);
        }
    }

    private ConfigResourceLoader loadLoader(Class loaderClass) {
        try {
            if (!ConfigResourceLoader.class.isAssignableFrom(loaderClass)) {
                String message = ("Class does not implement " + ConfigResourceLoader.class.getName() + ": " + loaderClass.getName());
                throw new IllegalArgumentException(message);
            }
            return (ConfigResourceLoader) loaderClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Could not instantiate " + loaderClass.getName() + ": " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not access " + loaderClass.getName() + "()");
        }
    }

    private ConfigResourceLoader[] myLoaders;
}
