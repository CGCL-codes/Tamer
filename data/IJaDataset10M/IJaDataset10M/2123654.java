package net.sf.plexian.store;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;

public class ContainerTest extends TestCase {

    public void testAdd() throws Exception {
        Container container = new Container("target/suggest-test".replace('/', File.separatorChar), ContainerType.NEW);
        container.addWeight("Moscow", 1);
        assertEquals(1, container.getIndexedItems());
        container.addWeight("Moscow", 1);
        assertEquals(1, container.getIndexedItems());
        List<String> result = container.searchLowerCase("Moscow");
        assertEquals(1, result.size());
        assertEquals("moscow", result.get(0));
        assertEquals(2, container.getWeight("Moscow").intValue());
        result = container.searchLowerCase("M");
        assertEquals(1, result.size());
        assertEquals("moscow", result.get(0));
        assertNull(container.getWeight("M"));
        container.remove("Moscow");
        assertNull(container.getWeight("Moscow"));
        assertEquals(0, container.getIndexedItems());
        container.close();
    }

    public void testKeywords() throws Exception {
        Container container = new Container("target/suggest-test".replace('/', File.separatorChar), ContainerType.NEW);
        container.addWeight("manager for pr", 100);
        container.addWeight("manager", 1000);
        container.addWeight("programmer", 500);
        List<String> result = container.searchByKeywords("manager for");
        assertTrue(result.size() > 0);
        assertEquals(result.get(0), "manager for pr");
        container.close();
    }

    public void testKeywordsNull() throws Exception {
        Container container = new Container("target/suggest-test".replace('/', File.separatorChar), ContainerType.NEW);
        container.addWeight("pr", 100);
        container.addWeight("pro", 1000);
        container.addWeight("programmer", 500);
        container.addWeight("posting", 5500);
        List<String> result = container.searchByKeywords("p p");
        container.close();
        assertTrue(result.size() > 0);
        assertEquals(result.get(0), "p posting");
    }
}
