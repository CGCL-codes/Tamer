package org.newsml.toolkit.dom.unittests;

import junit.framework.TestCase;
import org.newsml.toolkit.NewsManagement;

public class NewsManagementTest extends TestCase {

    public NewsManagementTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        sample = SampleFactory.createNewsML().getNewsItem(0).getNewsManagement();
    }

    public void testBase() {
        assertEquals(sample.getXMLName(), "NewsManagement");
        assertNotNull(sample.getSession());
    }

    public void testIdentifiers() {
        assertEquals(sample.getDuid().toString(), "newsmanagement01");
        assertEquals(sample.getEuid().toString(), "id1");
    }

    public void testNewsItemType() {
        assertEquals(sample.getNewsItemType().getName().toString(), "sample");
    }

    public void testFirstCreated() {
        assertEquals(sample.getFirstCreated().toString(), "20001231");
    }

    public void testThisRevisionCreated() {
        assertEquals(sample.getThisRevisionCreated().toString(), "20001231T1200-0500");
    }

    public void testStatus() {
        assertEquals(sample.getStatus().getName().toString(), "status.a");
    }

    public void testStatusWillChange() {
        assertNotNull(sample.getStatusWillChange());
    }

    public void testUrgency() {
        assertEquals(sample.getUrgency().getName().toString(), "urgency.a");
    }

    public void testRevisionHistory() {
        assertEquals(sample.getRevisionHistory().getHref().toString(), "http://www.foo.com/revisions.html");
    }

    public void testDerivedFrom() {
        assertEquals(sample.getDerivedFromCount(), 2);
        assertEquals(sample.getDerivedFrom().length, 2);
        assertEquals(sample.getDerivedFrom(0).getRef().toString(), "item.a");
        assertEquals(sample.getDerivedFrom(1).getRef().toString(), "item.b");
        assertNull(sample.getDerivedFrom(2));
    }

    public void testAssociatedWith() {
        assertEquals(sample.getAssociatedWithCount(), 2);
        assertEquals(sample.getAssociatedWith().length, 2);
        assertEquals(sample.getAssociatedWith(0).getRef().toString(), "item.c");
        assertEquals(sample.getAssociatedWith(1).getRef().toString(), "item.d");
        assertNull(sample.getAssociatedWith(2));
    }

    public void testInstruction() {
        assertEquals(sample.getInstructionCount(), 2);
        assertEquals(sample.getInstruction().length, 2);
        assertNotNull(sample.getInstruction(0));
        assertNotNull(sample.getInstruction(1));
        assertNull(sample.getInstruction(2));
    }

    public void testProperty() {
        assertEquals(sample.getPropertyCount(), 2);
        assertEquals(sample.getProperty().length, 2);
        assertNotNull(sample.getProperty(0));
        assertNotNull(sample.getProperty(1));
        assertNull(sample.getProperty(2));
    }

    private NewsManagement sample;
}
