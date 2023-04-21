package fileHandling;

import static org.junit.Assert.*;
import logic.nodes.nodeSettings.Settings;
import logic.nodes.nodeSettings.upgrades.UpgradableSettings;
import org.junit.Test;

public class SettingsLoaderTest {

    private static final String CONFIG_FILE = "data/defaultSettings/config.xml";

    private static final String MS_FILE = "data/fractions/alien/ships/mothership/properties.xml";

    @Test
    public void testSettingsLoading() {
        Settings s = SettingsLoader.loadSettings(CONFIG_FILE);
        assertTrue("Settings should not be null", s != null);
        s = SettingsLoader.loadSettings("");
        assertTrue("Settings should be null", s == null);
    }

    @Test
    public void testUpgradeSettingsLoading() {
        UpgradableSettings s = SettingsLoader.loadUpgradableSettings(MS_FILE);
        assertTrue("Settings should not be null", s != null);
        s = SettingsLoader.loadUpgradableSettings("");
        assertTrue("Settings should be null", s == null);
    }

    @Test
    public void testSettingsCache() {
        SettingsLoader.loadSettings(CONFIG_FILE);
        assertTrue("Settings should be get cached", SettingsLoader.objectIsLoaded(CONFIG_FILE));
        SettingsLoader.loadUpgradableSettings(MS_FILE);
        assertTrue("Settings should be get cached", SettingsLoader.objectIsLoaded(MS_FILE));
    }
}
