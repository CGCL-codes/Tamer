package net.sf.woko.feeds;

import java.util.Date;
import net.sf.woko.persistence.PersistenceUtil;

/**
 * Feed utilities
 */
public class FeedUtil {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FeedUtil.class);

    /**
     * Refreshes the feed for passed entity (creates a
     * new item in the feed).
     */
    public Feed refreshFeed(Object entity, PersistenceUtil persistenceUtil) {
        logger.debug("Refreshing feed for object " + entity);
        FeedItemType type = FeedItemType.UPDATED;
        Feed feed = getFeed(entity, persistenceUtil);
        if (feed == null) {
            String feedKey = getFeedKey(entity, persistenceUtil);
            feed = new Feed();
            feed.setObjectKey(feedKey);
            feed.setLastBuildDate(new Date());
            persistenceUtil.getSession().save(feed);
            type = FeedItemType.CREATED;
        }
        feed.computeNewItem(entity, type, persistenceUtil);
        logger.debug("...feed " + feed + " refreshed OK");
        return feed;
    }

    /**
	 * Return the feed for passed entity.
	 */
    public Feed getFeed(Object entity, PersistenceUtil persistenceUtil) {
        logger.debug("Retrieving feed for object " + entity);
        String feedKey = getFeedKey(entity, persistenceUtil);
        Feed feed = (Feed) persistenceUtil.getSession().get(Feed.class, feedKey);
        logger.debug("Returning " + feed);
        return feed;
    }

    public static String getFeedKey(Object entity, PersistenceUtil persistenceUtil) {
        String pk = persistenceUtil.getId(entity);
        String className = PersistenceUtil.deproxifyCglibClass(entity.getClass()).getName();
        return className + "@" + pk;
    }
}
