package com.ryan.test.wiki;

import junit.framework.TestCase;
import com.ryan.wiki.Wiki;

/**
 * @author rmchale
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WikiTester extends TestCase {

    Wiki _testWiki;

    public WikiTester(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        _testWiki = new Wiki("test", "__TEST__");
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRender() {
        _testWiki.render();
        assertEquals(true, _testWiki.getRenderedContent().equals("<b class=\"bold\">TEST</b>"));
    }
}
