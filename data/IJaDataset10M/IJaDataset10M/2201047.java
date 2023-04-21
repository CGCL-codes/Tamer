package newsatort.feed.event;

import newsatort.feed.IFeed;

public class FeedFavIconRetrievedEvent extends AbstractFeedEvent {

    public FeedFavIconRetrievedEvent(IFeed feed) {
        super(feed);
    }
}
