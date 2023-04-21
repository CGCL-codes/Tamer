package org.kaleidofoundry.core.cache;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.kaleidofoundry.core.context.ProviderException;
import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.lang.annotation.NotNull;
import org.kaleidofoundry.core.lang.annotation.ThreadSafe;
import org.kaleidofoundry.core.util.Registry;

/**
 * Abstract cache manager factory which allow to use different cache implementation with a common interface and behaviors.<br/>
 * <br/>
 * Default cache implementation when you call {@link #provides()} will use {@link LocalCacheManagerImpl} (it use an
 * {@link ConcurrentHashMap} internally)<br/>
 * <br/>
 * <b>You can customize default cache provider, by defining the java env. variable :</b>
 * <ul>
 * <li>-Dkaleido.cacheprovider=cacheImplCode</li>
 * </ul>
 * where cacheImplCode can be :
 * <ul>
 * <li>'ehCache' -> terracotta ehcache (c)</li>
 * <li>'jbossCache3x' -> jboss cache (c)</li>
 * <li>'infinispan4x' -> jboss infinispan (c)</li>
 * <li>'coherence3x' -> oracle coherence (c)</li>
 * <li>'gigaspace7x' -> gigaspace (c)</li>
 * <li>'kaleidoLocalCache' -> kaleidofoundry (c) local (ConcurrentHashMap)</li>
 * </ul>
 * <b>All cache implementations shared a common test case suite, in order to guarantee the same behavior</b>
 * <b>Example :</b>
 * 
 * <pre>
 * 	// Person is a java classic serializeable POJO
 * 	final CacheFactory cacheFactory = CacheFactory.getCacheFactory();
 * 	final Cache<Integer, Person> cache = cacheFactory.getCache(Person.class);
 * 
 * 	// handle cache
 * 	cache.put(1, new Person(...);
 * 	Person p = cache.get(1);
 * 	...
 * </pre>
 * 
 * @author Jerome RADUGET
 * @see CacheProvidersEnum
 */
@ThreadSafe
public abstract class CacheManagerFactory {

    private static final CacheManagerProvider CACHEMANAGER_PROVIDER = new CacheManagerProvider(CacheManager.class);

    /**
    * @return default cache manager provider (java system env. will be used, see class javadoc header)
    */
    @NotNull
    public static CacheManager provides() {
        return CACHEMANAGER_PROVIDER.provides();
    }

    /**
    * @param providerCode
    * @return cache manager using specific providerCodeCode
    * @throws CacheConfigurationException cache configuration resource exception
    * @throws ProviderException encapsulate class implementation constructor call error (like {@link NoSuchMethodException},
    *            {@link InstantiationException}, {@link IllegalAccessException}, {@link InvocationTargetException})
    * @see CacheProvidersEnum for providerCode values
    */
    @NotNull
    public static CacheManager provides(@NotNull final String providerCode) {
        return CACHEMANAGER_PROVIDER.provides(providerCode);
    }

    /**
    * @param context
    * @return cache manager if context specify a specific provider, it will use it, otherwise default cache manager
    *         provider (java system env. will be used, see class java-doc header)
    * @throws CacheConfigurationException cache configuration resource exception
    * @throws ProviderException encapsulate class implementation constructor call error (like {@link NoSuchMethodException},
    *            {@link InstantiationException}, {@link IllegalAccessException}, {@link InvocationTargetException})
    */
    @NotNull
    public static CacheManager provides(final RuntimeContext<CacheManager> context) {
        return CACHEMANAGER_PROVIDER.provides(context);
    }

    /**
    * @param providerCode
    * @param configuration
    * @return cacheFactory implementation
    * @throws CacheConfigurationException cache configuration resource exception
    * @throws ProviderException encapsulate class implementation constructor call error (like {@link NoSuchMethodException},
    *            {@link InstantiationException}, {@link IllegalAccessException}, {@link InvocationTargetException})
    */
    @NotNull
    public static CacheManager provides(@NotNull final String providerCode, final String configuration) {
        return CACHEMANAGER_PROVIDER.provides(providerCode, configuration);
    }

    /**
    * @param providerCode
    * @param context
    * @return cacheFactory implementation
    * @throws CacheConfigurationException cache configuration resource exception
    * @throws ProviderException encapsulate class implementation constructor call error (like {@link NoSuchMethodException},
    *            {@link InstantiationException}, {@link IllegalAccessException}, {@link InvocationTargetException})
    */
    @NotNull
    public static CacheManager provides(@NotNull final String providerCode, @NotNull final RuntimeContext<CacheManager> context) {
        return CACHEMANAGER_PROVIDER.provides(providerCode, null, context);
    }

    /**
    * @param providerCode
    * @param configuration
    * @param context
    * @return cacheFactory implementation
    * @throws CacheConfigurationException cache configuration resource exception
    * @throws ProviderException encapsulate class implementation constructor call error (like {@link NoSuchMethodException},
    *            {@link InstantiationException}, {@link IllegalAccessException}, {@link InvocationTargetException})
    */
    @NotNull
    public static CacheManager provides(@NotNull final String providerCode, final String configuration, @NotNull final RuntimeContext<CacheManager> context) {
        return CACHEMANAGER_PROVIDER.provides(providerCode, configuration, context);
    }

    /**
    * @return set of some reserved cache name<br/>
    *         You can add your own as you need
    */
    public static Set<String> getReservedCacheName() {
        return CacheManagerProvider.getReservedCacheName();
    }

    /**
    * @return cache manager registry. each instance provided will be registered here
    */
    public static Registry<String, CacheManager> getRegistry() {
        return CacheManagerProvider.getRegistry();
    }
}
