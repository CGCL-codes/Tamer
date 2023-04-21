package com.ewansilver.raindrop.demo.httpserver;

import java.net.URL;
import java.util.Map;
import com.ewansilver.raindrop.nio.ReadEvent;
import junit.framework.TestCase;

/**
 * Test the HttpConnection.
 * 
 * @author Ewan Silver
 *
 */
public class HttpConnectionTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test that an HttpConnection can identify HTTP headers.
	 */
    public void testCanIdentifyHeaders() {
        HttpConnection connection = new HttpConnection(null, null);
        String request = "GET /dir1/dir2/file.txt?name1=value1&name2=value2 HTTP/1.1\r\nHeader1: value1\r\nHost: raindrop\r\n\r\n";
        ReadEvent httpEvent = new ReadEvent(request.getBytes(), null);
        assertTrue(connection.parseRequest(httpEvent));
        Map<String, String> headers = connection.getRequestHeaders();
        assertEquals("raindrop", headers.get("host").trim());
        assertNull(headers.get("noSuchHeader"));
    }

    /**
	 * Test that the inbound URL can be identified.
	 */
    public void testCanIdentifyURL() {
        HttpConnection connection = new HttpConnection(null, null);
        String request = "GET /dir1/dir2/file.txt?name1=value1&name2=value2 HTTP/1.1\r\nHeader1: value1\r\nHost: raindrop\r\n\r\n";
        ReadEvent httpEvent = new ReadEvent(request.getBytes(), null);
        assertTrue(connection.parseRequest(httpEvent));
        assertEquals("/dir1/dir2/file.txt?name1=value1&name2=value2", connection.getPath());
    }

    /**
	 * Test that the inbound URL can identify a GET request.
	 */
    public void testCanIdentifyAGetMethod() {
        HttpConnection connection = new HttpConnection(null, null);
        String request = "GET /dir1/dir2/file.txt?name1=value1&name2=value2 HTTP/1.1\r\nHeader1: value1\r\nHost: raindrop\r\n\r\n";
        ReadEvent httpEvent = new ReadEvent(request.getBytes(), null);
        assertTrue(connection.parseRequest(httpEvent));
        assertEquals("GET", connection.getMethod());
    }

    /**
	 * Test that a partial read request can be handled correctly
	 */
    public void testCanHandlePartialReadEvents() {
        HttpConnection connection = new HttpConnection(null, null);
        String request1 = "GET /dir1/dir2/file.txt?name1";
        String request2 = "=value1&name2=value2 HTTP/1.1\r\nHeader1: value1\r\nHost: raindrop\r\n\r\n";
        ReadEvent httpEvent1 = new ReadEvent(request1.getBytes(), null);
        ReadEvent httpEvent2 = new ReadEvent(request2.getBytes(), null);
        assertFalse(connection.parseRequest(httpEvent1));
        assertTrue(connection.parseRequest(httpEvent2));
    }

    /**
	 * Check that we can retreive the URL that was actually sent in.
	 */
    public void testCanParseURL() {
        HttpConnection connection = new HttpConnection(null, null);
        String request = "GET /dir1/dir2/file.txt?name1=value1&name2=value2 HTTP/1.1\r\nHeader1: value1\r\nHost: raindrop\r\n\r\n";
        ReadEvent httpEvent = new ReadEvent(request.getBytes(), null);
        assertTrue(connection.parseRequest(httpEvent));
        URL url = connection.getURL();
        assertEquals("raindrop", url.getHost());
        assertEquals("/dir1/dir2/file.txt", url.getPath());
        assertEquals("name1=value1&name2=value2", url.getQuery());
    }
}
