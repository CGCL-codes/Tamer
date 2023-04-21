package kr.pe.javarss.dao;

import static org.junit.Assert.*;
import java.util.List;
import kr.pe.javarss.BaseTransactionalTestCase;
import kr.pe.javarss.model.Feed;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FeedDaoTest extends BaseTransactionalTestCase {

    @Autowired
    private FeedDao feedDao = null;

    @Test
    public void getFeed() {
        Feed feed = feedDao.getFeed(new Integer(1));
        assertEquals(new Integer(1), feed.getId());
    }

    @Test
    public void insertFeed() {
        List<Feed> feeds = feedDao.getAllFeeds();
        int found = feeds.size();
        String feedUrl = "http://ecogeo.tistory.com/rss";
        Feed feed = new Feed();
        feed.setUrl(feedUrl);
        feed.setActive(true);
        feedDao.saveFeed(feed);
        assertNotNull(feed.getId());
        flushAndClearSession();
        feed = feedDao.getFeed(feed.getId());
        assertNotNull(feed);
        assertEquals(feedUrl, feed.getUrl());
        assertEquals(true, feed.isActive());
        feeds = feedDao.getAllFeeds();
        assertEquals(found + 1, feeds.size());
    }

    @Test
    public void updateFeed() {
        Feed feed = feedDao.getFeed(new Integer(1));
        String old = feed.getTitle();
        feed.setTitle(old + "X");
        feedDao.saveFeed(feed);
        flushAndClearSession();
        feed = feedDao.getFeed(new Integer(1));
        assertEquals(old + "X", feed.getTitle());
    }

    @Test
    public void removeFeed() {
        Feed feed = feedDao.getFeed(new Integer(1));
        feedDao.removeFeed(feed.getId());
        flushSession();
        feed = feedDao.getFeed(feed.getId());
        assertNull(feed);
    }

    @Test
    public void getAllFeeds() {
        List<Feed> feeds = feedDao.getAllFeeds();
        assertEquals(countRowsInTable("feeds"), feeds.size());
    }

    @Test
    public void getAllActiveFeeds() {
        List<Feed> feeds = feedDao.getAllActiveFeeds();
        for (Feed feed : feeds) {
            assertTrue(feed.isActive());
        }
        int expectedSize = simpleJdbcTemplate.queryForInt("select count(*) from feeds where active = true");
        assertEquals(expectedSize, feeds.size());
    }
}
