package de.nava.informa.utils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.FeedIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Feed;
import de.nava.informa.parsers.FeedParser;
import de.nava.informa.parsers.OPMLParser;

/**
 * @author Jean-Guy Avelin
 */
public class TestFeedManager extends InformaTestCase {

    public TestFeedManager(String name) {
        super("TestFeedManager", name);
    }

    public void testaddFeed() throws Exception {
        FeedManager FM = new FeedManager();
        File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
        ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
        String url = new Feed(channel).getLocation().toString();
        FeedIF feed = FM.addFeed(url);
        assertEquals(feed, FM.addFeed(url));
    }

    public void testhasFeed() throws Exception {
        FeedManager FM = new FeedManager();
        File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
        assertFalse(FM.hasFeed(""));
        ChannelIF channel = FeedParser.parse(new ChannelBuilder(), inpFile);
        String url = new Feed(channel).getLocation().toString();
        assertFalse(FM.hasFeed(url));
        FM.addFeed(url);
        assertTrue(FM.hasFeed(url));
    }

    public void testaddFeeds() throws Exception {
        FeedManager FM = new FeedManager();
        File inpFile = new File(getDataDir(), "favchannels.opml");
        String opmlUri = inpFile.toURI().toString();
        System.err.println("parsing " + opmlUri);
        Collection feeds = OPMLParser.parse(inpFile);
        Iterator it = feeds.iterator();
        while (it.hasNext()) {
            FeedIF opmlFeed = (FeedIF) it.next();
            assertFalse(FM.hasFeed(opmlFeed.getLocation().toString()));
        }
        Collection feeds2 = FM.addFeeds(opmlUri);
        assertEquals(19, feeds2.size());
        it = feeds2.iterator();
        while (it.hasNext()) {
            FeedIF opmlFeed = (FeedIF) it.next();
            assertTrue(FM.hasFeed(opmlFeed.getLocation().toString()));
        }
        it = feeds.iterator();
        while (it.hasNext()) {
            FeedIF opmlFeed = (FeedIF) it.next();
            assertTrue(FM.hasFeed(opmlFeed.getLocation().toString()));
        }
        assertTrue(FM.hasFeed("http://www.bbc.co.uk/syndication/feeds/news/ukfs_news/world/rss091.xml"));
    }
}
