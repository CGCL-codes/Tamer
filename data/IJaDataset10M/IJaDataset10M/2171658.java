package org.newsml.toolkit.dom.unittests;

import junit.framework.TestCase;
import org.newsml.toolkit.NewsLine;

public class NewsLineTest extends TestCase {

    public NewsLineTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        sample = SampleFactory.createNewsML().getNewsItem(0).getRootNewsComponent().getNewsLines().getNewsLine(1);
    }

    public void testBase() {
        assertEquals(sample.getXMLName(), "NewsLine");
        assertNotNull(sample.getSession());
    }

    public void testIdentifiers() {
        assertEquals(sample.getDuid().toString(), "newsline01");
        assertEquals(sample.getEuid().toString(), "id1");
    }

    public void testNewsLineType() {
        assertEquals(sample.getNewsLineType().getName().toString(), "newsline.b");
    }

    public void testNewsLineText() {
        assertEquals(sample.getNewsLineTextCount(), 2);
        assertEquals(sample.getNewsLineText().length, 2);
        assertEquals(sample.getNewsLineText(0).toString(), "text.a");
        assertEquals(sample.getNewsLineText(1).toString(), "text.b");
        assertNull(sample.getNewsLineText(2));
    }

    private NewsLine sample;
}
