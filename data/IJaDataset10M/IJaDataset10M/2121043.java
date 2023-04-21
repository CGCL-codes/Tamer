package net.sf.beezle.sushi.rss;

import junit.framework.TestCase;
import net.sf.beezle.sushi.xml.Xml;
import net.sf.beezle.sushi.xml.XmlException;
import java.util.Date;

public class FeedTest extends TestCase {

    private static final Xml XML = new Xml();

    public void testEmpty() throws XmlException {
        Feed feed;
        feed = new Feed();
        feed = Feed.fromXml(XML.getSelector(), feed.toXml(XML.getBuilder()));
        assertEquals(0, feed.channels().size());
    }

    public void testNormal() throws XmlException {
        Feed feed;
        Channel channel;
        Item item;
        Date date = new Date();
        feed = new Feed();
        channel = new Channel();
        channel.items().add(new Item());
        channel.items().add(new Item());
        feed.channels().add(channel);
        channel = new Channel("t", "l", "d");
        channel.items().add(new Item("t", "l", "d", "a", "g", date));
        feed.channels().add(channel);
        feed = Feed.fromXml(XML.getSelector(), feed.toXml(XML.getBuilder()));
        assertEquals(2, feed.channels().size());
        assertEquals(2, feed.channels().get(0).items().size());
        channel = feed.channels().get(1);
        assertEquals("t", channel.getTitle());
        assertEquals("l", channel.getLink());
        assertEquals("d", channel.getDescription());
        assertEquals(1, channel.items().size());
        item = channel.items().get(0);
        assertEquals("t", item.getTitle());
        assertEquals("l", item.getLink());
        assertEquals("d", item.getDescription());
        assertEquals("a", item.getAuthor());
        assertEquals("g", item.getGuid());
        assertEquals(date.toString(), item.getPubDate().toString());
    }
}
