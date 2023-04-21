package phex.host;

import phex.common.log.NLogger;
import phex.gwebcache.GWebCacheManager;
import phex.servent.Servent;

/**
 * Currently, UDP host caches aren't supported over I2P.
 */
public class I2PHostFetchingStrategy implements HostFetchingStrategy {

    private final GWebCacheManager gWebCacheMgr;

    public I2PHostFetchingStrategy(Servent servent) {
        this.gWebCacheMgr = new GWebCacheManager(servent);
    }

    public void postManagerInitRoutine() {
        gWebCacheMgr.postManagerInitRoutine();
    }

    public void fetchNewHosts(FetchingReason reason) {
        NLogger.info(I2PHostFetchingStrategy.class, "Fetch new Hosts: " + reason.toString());
        gWebCacheMgr.invokeQueryMoreHostsRequest(true);
    }
}
