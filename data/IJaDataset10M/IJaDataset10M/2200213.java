package cs278.oasis.test;

import junit.framework.TestCase;
import cs278.oasis.*;
import java.util.HashMap;

public class ConnectionSettingsTest extends TestCase {

    ConnectionSettings settings = new ConnectionSettings();

    public void testLoadFromFile() {
        settings.saveToFile("file");
        ConnectionSettings newSettings = new ConnectionSettings();
        newSettings.loadFromFile("file");
        assert (settings.equals(newSettings));
    }

    public void testSaveToFile() {
        settings.saveToFile("file");
        ConnectionSettings newSettings = new ConnectionSettings();
        newSettings.loadFromFile("file");
        assert (settings.equals(newSettings));
    }

    public void testGetProperties() {
        HashMap<Object, String> newProperties = new HashMap<Object, String>();
        settings.setProperties(newProperties);
        assert (settings.getProperties().equals(newProperties));
    }

    public void testSetProperties() {
        HashMap<Object, String> newProperties = new HashMap<Object, String>();
        settings.setProperties(newProperties);
        assert (settings.getProperties().equals(newProperties));
    }

    public void testPropertyForKey() {
        fail("Not yet implemented");
    }
}
