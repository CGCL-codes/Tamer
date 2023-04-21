package com.ewansilver.raindrop.demo.httpserver;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.TestCase;

/**
 * Test for QueryStringParser.
 * 
 * @author Ewan Silver
 *
 */
public class QueryStringParserTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for {@link com.ewansilver.raindrop.demo.httpserver.QueryStringParser#getQuery(java.lang.String)}.
	 * @throws MalformedURLException 
	 */
    public void testGetQuery() throws MalformedURLException {
        URL url = new URL("http://test/dir/file.txt?name1=value1&name2=value2");
        QueryStringParser parser = new QueryStringParser(url);
        assertNull(parser.getQuery("doesNotExist"));
        assertEquals("value1", parser.getQuery("name1"));
        assertEquals("value2", parser.getQuery("name2"));
    }

    /**
	 * Check that a missing QueryString does not cause us any problems.
	 * @throws MalformedURLException
	 */
    public void testGetQueryHandlesMissingQueryString() throws MalformedURLException {
        URL url = new URL("http://test/dir/file.txt");
        QueryStringParser parser = new QueryStringParser(url);
        assertNull(parser.getQuery("doesNotExist"));
        assertNull(parser.getQuery("name1"));
        assertNull(parser.getQuery("name2"));
    }

    /**
	 * Check that we can tell if query string parameters exist or not.
	 * @throws MalformedURLException 
	 */
    public void testCanCheckExistenceOfAQueryString() throws MalformedURLException {
        URL url = new URL("http://test/dir/file.txt?name1=value1&name2=value2");
        QueryStringParser parser = new QueryStringParser(url);
        assertFalse(parser.contains("doesNotExist"));
        assertTrue(parser.contains("name1"));
        assertTrue(parser.contains("name2"));
    }
}
