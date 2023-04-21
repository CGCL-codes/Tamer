package org.kaleidofoundry.core.cache;

import static org.kaleidofoundry.core.cache.CacheManagerContextBuilder.FileStoreUri;
import static org.kaleidofoundry.core.cache.CacheManagerContextBuilder.ProviderCode;
import static org.kaleidofoundry.core.cache.CacheManagerSample01.feedCache;
import org.kaleidofoundry.core.context.Context;
import org.kaleidofoundry.core.context.Parameter;

/**
 * <p>
 * <h3>Simple cache manager usage</h3> Inject {@link CacheManager} context and instance using {@link Context} annotation mixing the use of
 * parameters and external configuration <br/>
 * Parameters have priority to the external configuration
 * </p>
 * <br/>
 * <b>Precondition :</b> The following java env. variable have been set
 * 
 * <pre>
 * -Dkaleido.configurations=classpath:/cache/myContext.properties
 * </pre>
 * 
 * Resource file : "classpath:/cache/myContext.properties" contains :
 * 
 * <pre>
 * cacheManagers.myCacheManager.providerCode=ehCache
 * cacheManagers.myCacheManager.fileStoreUri=classpath:/cache/ehcache.xml
 * 
 * # sample if your cache configuration is accessible from an external file store
 * #cacheManagers.myCacheManager.fileStoreUri=http://localhost:8380/kaleido-it/cache/ehcache.xml
 * #cacheManagers.myCacheManager.fileStoreRef=myHttpCtx
 * 
 * # sample if you need proxy settings, uncomment and configure followings :
 * #fileStores.myHttpCtx.proxySet=true
 * #fileStores.myHttpCtx.proxyHost=yourProxyHost
 * #fileStores.myHttpCtx.proxyUser=yourProxyUser
 * #fileStores.myHttpCtx.proxyPassword=proxyUserPassword
 * 
 * </pre>
 * 
 * @author Jerome RADUGET
 */
public class CacheManagerSample03 {

    @Context(value = "myCacheManager", parameters = { @Parameter(name = ProviderCode, value = "local"), @Parameter(name = FileStoreUri, value = "") })
    private CacheManager myCacheManager;

    private final Cache<String, YourBean> myCache;

    public CacheManagerSample03() {
        myCache = myCacheManager.getCache(YourBean.class);
        feedCache(myCache);
    }

    /**
    * method example that use the injected cache
    */
    public void echo() {
        System.out.printf("cache name: %s\n", myCache.getName());
        System.out.printf("cache size: %s\n", myCache.size());
        System.out.printf("cache entry[%s]: %s\n", "bean1", myCache.get("bean1").toString());
        System.out.printf("cache entry[%s]: %s\n", "bean2", myCache.get("bean2").toString());
    }

    /**
    * used only for junit assertions
    * 
    * @return current cache instance
    */
    Cache<String, YourBean> getMyCache() {
        return myCache;
    }
}
