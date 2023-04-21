package supersync.sync.prefs;

import java.io.File;
import supersync.sync.prefs.server.Server_Local;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import supersync.sync.Logger.LogLevel;
import static org.junit.Assert.*;

/** This class tests the global preferences class.
 *
 * @author Brandon Drake
 */
public class GlobalPreferencesTest {

    public GlobalPreferencesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getDefaultSettings method, of class GlobalPreferences.
     */
    @Test
    public void testGetDefaultSettings() throws Exception {
        GlobalPreferences prefs = GlobalPreferences.getDefaultSettings();
        if (null == prefs.getLogLocation()) {
            fail("The default preferences were not loaded with a log location.");
        }
        if (null == prefs.systemName) {
            fail("The default preferences were not loaded with a system name.");
        }
        if (prefs.getServers().isEmpty() || false == Server_Local.SERVER_TYPE.equals(prefs.getServers().get(0).getType())) {
            fail("No default local file system server found.");
        }
    }

    /**
     * Test of toXML method, of class GlobalPreferences.
     */
    @Test
    public void testXML() throws Exception {
        GlobalPreferences prefs = GlobalPreferences.getDefaultSettings();
        prefs.addRecentlyOpenedFile("/folder/test");
        prefs.setLogLevel(LogLevel.DEBUG);
        prefs.setLogLocation("/Log Location");
        prefs.setShowDonationPrompt(false);
        prefs.setSystemName("New System Name");
        PasswordManager passManager = new PasswordManager();
        passManager.setPasswordKeyFile(new File("test.txt"));
        prefs.setPasswordManager(passManager);
        supersync.sync.prefs.server.Server_Local server = new supersync.sync.prefs.server.Server_Local();
        server.setName("test server");
        prefs.addServer(server);
        prefs = GlobalPreferences.fromXML(prefs.toXML());
        if (false == prefs.getRecentlyOpenedFiles().get(0).equals("/folder/test")) {
            fail("Recently opened file list is wrong.");
        }
        if (prefs.getLogLevel().getLevel() != LogLevel.DEBUG.getLevel()) {
            fail("Log level is wrong.");
        }
        if (false == prefs.getLogLocation().equals("/Log Location")) {
            fail("Log location is wrong.");
        }
        if (false != prefs.getShowDonationPrompt()) {
            fail();
        }
        if (false == prefs.getSystemName().equals("New System Name".toUpperCase())) {
            fail("System name is wrong.");
        }
        if (null == prefs.getServer("test server")) {
            fail("servers were not correctly save.");
        }
        if (false == prefs.getPasswordManager().getPasswordKeyFile().getName().equals("test.txt")) {
            fail();
        }
    }
}
