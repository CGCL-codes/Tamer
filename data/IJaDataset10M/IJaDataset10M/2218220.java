package net.grinder.util.http;

import junit.framework.TestCase;

/**
 * Unit tests for {@link URIParser.AbstractParseListener}.
 *
 * @author Philip Aston
 */
public class TestAbstractParseListener extends TestCase {

    public void testAbstractParseListener() throws Exception {
        final URIParser.ParseListener myParseListener = new URIParser.AbstractParseListener() {
        };
        assertTrue(myParseListener.scheme("foo"));
        assertTrue(myParseListener.authority("foo"));
        assertTrue(myParseListener.path("foo"));
        assertTrue(myParseListener.pathParameterNameValue("foo", "bah"));
        assertTrue(myParseListener.queryString("foo"));
        assertTrue(myParseListener.queryStringNameValue("foo", "bah"));
        assertTrue(myParseListener.fragment("foo"));
    }
}
