package com.google.gxp.rss;

import com.google.gxp.testing.BaseFunctionalTestCase;

/**
 * Tests for {@code RssClosures}
 */
public class RssClosuresTest extends BaseFunctionalTestCase {

    public void testEmpty() throws Exception {
        RssClosures.EMPTY.write(out, gxpContext);
        assertOutputEquals("");
    }

    public void testFromRss() throws Exception {
        RssClosures.fromRss("foo < bar > baz & quux").write(out, gxpContext);
        assertOutputEquals("foo < bar > baz & quux");
        try {
            RssClosures.fromRss(null);
            fail("should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
