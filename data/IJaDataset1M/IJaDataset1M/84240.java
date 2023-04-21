package es.java.otro.service;

import java.util.List;
import es.java.otro.model.Feed;
import es.java.otro.model.Root;

public interface FeedService {

    public void persistRoot(Root root);

    public void addFeed(Feed feed);

    public List<Feed> getFeeds();

    public void test();
}
