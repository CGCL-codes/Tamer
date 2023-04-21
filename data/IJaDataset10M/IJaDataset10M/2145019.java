package de.objectcode.time4u.client.feeds.job;

import org.eclipse.jface.preference.IPreferenceStore;
import de.objectcode.time4u.client.feeds.FeedType;
import de.objectcode.time4u.store.ITodoStore;

public interface IFeed {

    public static final String FEED_META_CATEGORY = "time4u-feeds.category";

    public static final String FEED_META_TYPE = "time4u-feeds.feedType";

    public static final String FEED_META_URL = "time4u-feeds.feedUrl";

    public static final String FEED_META_REFERENCE = "time4u-feeds.feedRef";

    public FeedType getType();

    public String getDescription();

    public void perform(ITodoStore todoStore);

    public void store(IPreferenceStore store, String base);
}
