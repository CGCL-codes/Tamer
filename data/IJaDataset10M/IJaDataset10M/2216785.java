package org.actioncenters.cometd.channelcache;

import net.sf.ehcache.constructs.blocking.BlockingCache;
import org.actioncenters.core.spring.ApplicationContextHelper;

/**
 * @author dougk
 *
 */
public class ChannelCacheController {

    /** this is a utility class; hide the default constructor */
    private ChannelCacheController() {
    }

    /** The channel cache. */
    private static BlockingCache channelCache = (BlockingCache) (ApplicationContextHelper.getApplicationContext("actioncenters.xml").getBean("channelCache"));

    static {
        channelCache.getCacheEventNotificationService().registerListener(new ChannelCacheEventListener());
    }

    /**
     * @return the channelCache
     */
    public static BlockingCache getChannelCache() {
        return channelCache;
    }
}
