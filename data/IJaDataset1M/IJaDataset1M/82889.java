package net.sf.picasto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

public class FeedTest extends TestCase {

    public void testFeed() {
        Date now = new Date();
        SyndFeed feed = new SyndFeedImpl();
        feed.setLink("http://www.example.com/bla");
        feed.setTitle("Hello World");
        feed.setDescription("This is an example feed.");
        feed.setPublishedDate(now);
        List entries = new LinkedList();
        SyndEntry entry1 = new SyndEntryImpl();
        entry1.setUri("http://www.example.com/2006/07/13/foo");
        entry1.setTitle("Foo!");
        SyndContent content = new SyndContentImpl();
        content.setType("text");
        content.setValue("Bar.");
        entry1.setDescription(content);
        entry1.setLink("http://www.example.com/foobar");
        entry1.setUpdatedDate(now);
        entries.add(entry1);
        SyndEntry entry2 = new SyndEntryImpl();
        entry2.setUri("http://www.example.com/2006/07/13/blurp");
        entry2.setTitle("Blur!");
        content = new SyndContentImpl();
        content.setType("text");
        content.setValue("Baz.");
        entry2.setDescription(content);
        entry2.setLink("http://www.example.com/moose");
        entry2.setUpdatedDate(now);
        entries.add(entry2);
        feed.setEntries(entries);
        try {
            feed.setFeedType("atom_1.0");
            System.out.println(new SyndFeedOutput().outputString(feed));
            feed.setFeedType("rss_2.0");
            System.out.println(new SyndFeedOutput().outputString(feed));
        } catch (FeedException e) {
            e.printStackTrace();
        }
    }
}
