package org.pinkypipes.aspect;

import com.sun.syndication.feed.synd.*;

/**
 * Aspect to hold a Feed - based on Rome object model.
 * @author  pjr
 */
public class FeedAspect implements IAspectFeed {

    private SyndFeed mFeed;

    /** Creates a new instance of FeedAspect */
    public FeedAspect(SyndFeed aFeed) {
        mFeed = aFeed;
    }

    /**
	 *Get feed - this method is immutable-safe and clones the underlying Rome feed object.
	 */
    public SyndFeed getFeed() throws Exception {
        return (SyndFeed) mFeed.clone();
    }

    /**
	 *<i>Advanced Use Only</i>Get feed - this method returns a feed that is <b>not</b> immutable.  Do not
	 *use if you are going to make any changes to the feed.  This feed must be use only for read-only operations.
	 */
    public SyndFeed getFeedExpertsOnly() {
        return mFeed;
    }
}
