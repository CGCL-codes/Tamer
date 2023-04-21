package org.netbeans.modules.keyring;

import java.io.InputStream;
import java.util.logging.LogManager;
import org.netbeans.api.keyring.Keyring;

public class KeyringTest {

    public static void main(String[] args) {
        setupLogging();
        for (String[] oneTest : TEST_PASSWORDS) {
            String key = oneTest[0];
            String password = oneTest[1];
            char[] passwordArray = password.toCharArray();
            Keyring.save(key, passwordArray, null);
            char[] retrieved = Keyring.read(key);
            String retrievedPassword = new String(retrieved);
            if (password.equals(retrievedPassword)) msg("Successfully stored/retrieved password for " + key); else msg("############ Could not store/retrieve password for " + key);
        }
        if (args.length > 0) return;
        for (String[] oneTest : TEST_PASSWORDS) {
            String key = oneTest[0];
            char[] retrieved = Keyring.read(key);
            if (retrieved == null) msg("############ Lost password for " + key);
            Keyring.delete(key);
            retrieved = Keyring.read(key);
            if (retrieved != null) msg("############ Could not delete password for " + key); else msg("Successfully deleted password for " + key);
        }
    }

    private static void msg(String s) {
        System.out.println(s);
    }

    private static final String[][] TEST_PASSWORDS = { { "john", "my password" }, { "someUser@http://pdes.example.com/DataBridge/INST-3nrh72", "This is a longer password to test and make certain we can " + "handle longer strings.  The quick brown fox jumped " + "over the lazy dogs." }, { "zhang", "过程度量板" } };

    /**
     * When we run a test on a particular platform, we absolutely want to
     * know which KeyringProvider is in use.  (If the DummyProvider kicks
     * in, that doesn't tell us much about the viability of platform support.)
     * We must enable logging to display that information.
     */
    private static void setupLogging() {
        if (System.getProperty("java.util.logging.config.file") != null) return;
        try {
            InputStream config = KeyringTest.class.getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(config);
        } catch (Exception e) {
        }
    }
}
