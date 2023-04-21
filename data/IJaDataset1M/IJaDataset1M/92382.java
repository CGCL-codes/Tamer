package org.webiyo.examples.sourceforge;

import junit.framework.TestCase;
import java.io.IOException;
import java.util.Iterator;

public class NewsTest extends TestCase {

    public void testBasics() throws Exception {
        News reader = new News(FakeProject.FAKE_NEWSFEED);
        assertEquals("Webiyo (pronounced \"webby-O\") is a small Java 1.5 library blah blah blah...", reader.getProjectDescription());
        Iterator<NewsItem> it = reader.iterator();
        checkItem("More to come...", "<p>Still setting up. \n<a href=\"http://www.example.com\">a link</a></p>", it);
        assertFalse(it.hasNext());
    }

    private void checkItem(String expectedTitle, String expectedBody, Iterator<NewsItem> it) throws IOException {
        NewsItem item = it.next();
        assertEquals(expectedTitle, item.getTitle());
        assertEquals(expectedBody, item.getBody().toXml());
    }
}
