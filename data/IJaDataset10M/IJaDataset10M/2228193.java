package gov.lanl.util.properties;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;

public class GenericPropertyManagerTest extends TestCase {

    public void testGet() throws Exception {
        GenericPropertyManager loader = new GenericPropertyManager();
        String pfile = "fruit=apple\nanimal=tiger\n";
        loader.addAll(PropertiesUtil.loadProperty(new ByteArrayInputStream(pfile.getBytes())));
        assertEquals("apple", loader.getProperty("fruit"));
        assertEquals("tiger", loader.getProperty("animal"));
    }

    public void testSet() throws Exception {
        GenericPropertyManager loader = new GenericPropertyManager();
        loader.setProperty("fruit", "apple");
        assertEquals("apple", loader.getProperty("fruit"));
    }

    public void testLoadSet() throws Exception {
        GenericPropertyManager loader = new GenericPropertyManager();
        String pfile = "fruit=apple\nanimal=tiger\n";
        loader.addAll(PropertiesUtil.loadProperty(new ByteArrayInputStream(pfile.getBytes())));
        loader.setProperty("fruit", "pineapple");
        assertEquals("pineapple", loader.getProperty("fruit"));
        loader.addAll(PropertiesUtil.loadProperty(new ByteArrayInputStream(pfile.getBytes())));
        loader.setProperty("fruit", "apple");
    }

    public void testOverwrite() throws Exception {
        GenericPropertyManager loader = new GenericPropertyManager();
        loader.setProperty("flower", "sunflower");
        String pfile = "fruit=apple\nanimal=tiger\n";
        loader.addAll(PropertiesUtil.loadProperty(new ByteArrayInputStream(pfile.getBytes())));
        assertEquals("sunflower", loader.getProperty("flower"));
    }

    public void testSystemProperty() throws Exception {
        System.setProperty("flower", "trumpet vine");
        GenericPropertyManager loader = new GenericPropertyManager();
        assertEquals("trumpet vine", loader.getProperty("flower"));
    }

    public void testSystemEnv() throws Exception {
        GenericPropertyManager loader = new GenericPropertyManager();
        assertNotNull(loader.getProperty("HOME"));
        assertEquals(1, loader.getProperties().size());
    }
}
