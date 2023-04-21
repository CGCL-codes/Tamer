package com.aaspring.util.locale;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import com.aaspring.util.file.FileAccessor;

/**
 * @author Balazs
 * 
 */
public abstract class BundleFactory {

    @SuppressWarnings("unchecked")
    private static Map<String, BundleFactory> bundleFactories;

    static {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("META-INF/bundle-factories.properties");
            bundleFactories = new LinkedHashMap<String, BundleFactory>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Properties bundleProperties = new Properties();
                InputStream is = null;
                try {
                    is = resource.openStream();
                    bundleProperties.load(is);
                } finally {
                    if (is != null) is.close();
                }
                Set<Object> propKeys = bundleProperties.keySet();
                for (Object key : propKeys) {
                    String className = bundleProperties.getProperty((String) key);
                    try {
                        bundleFactories.put((String) key, (BundleFactory) Class.forName(className).newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot instantiate BundleFactory class", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bundle createEmptyBundle(final BundleProps bundleProperties, final String bundleType) {
        return bundleFactories.get(bundleType).createEmptyBundleImpl(bundleProperties);
    }

    public static Bundle getBundle(final BundleProps bundleProperties, final String bundleType, final FileAccessor fileAccessor) {
        return bundleFactories.get(bundleType).getBundleFromCacheOrCreate(bundleProperties, fileAccessor);
    }

    protected final Map<BundleProps, Bundle> cache = new LinkedHashMap<BundleProps, Bundle>();

    protected abstract Bundle createEmptyBundleImpl(BundleProps bundleProps);

    private Bundle getBundleFromCacheOrCreate(final BundleProps bundleProperties, final FileAccessor fileAccessor) {
        Bundle bundle = cache.get(bundleProperties);
        if (bundle == null) {
            bundle = getBundleImpl(bundleProperties, fileAccessor);
            cache.put(bundleProperties, bundle);
        }
        return bundle;
    }

    protected abstract Bundle getBundleImpl(BundleProps bundleProperties, FileAccessor fileAccessor);
}
