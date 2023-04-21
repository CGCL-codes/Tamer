package net.sf.javadc.config.gui;

import junit.framework.TestCase;
import net.sf.javadc.config.SettingsAdapter;
import net.sf.javadc.interfaces.ISettings;
import net.sf.javadc.mockups.BaseSettingsLoader;

/**
 * @author Timo Westk�mper
 */
public class ConnectionSettingsPanelTest extends TestCase {

    private ISettings settings = new SettingsAdapter(new BaseSettingsLoader());

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for ConnectionSettingsPanelTest.
     * 
     * @param arg0
     */
    public ConnectionSettingsPanelTest(String arg0) {
        super(arg0);
    }

    public void testCreation() throws Exception {
        ConnectionSettingsPanel panel = new ConnectionSettingsPanel(settings);
        assertTrue(panel != null);
    }
}
