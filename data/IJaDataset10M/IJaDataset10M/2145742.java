package net.sourceforge.jpotpourri;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author christoph_pickl@users.sourceforge.net
 */
public final class PtTestProperties {

    private static final Log LOG = LogFactory.getLog(PtTestProperties.class);

    private static final String PROPERTIES_FILE_NAME = "test_config";

    private static final Map<String, TestKey> MAIN_KEYS;

    static {
        final Map<String, TestKey> tmp = new HashMap<String, TestKey>();
        tmp.put(TestKey.FOLDER_TESTROOT.getPropKey(), TestKey.FOLDER_TESTROOT);
        MAIN_KEYS = Collections.unmodifiableMap(tmp);
    }

    private static final PtTestProperties INSTANCE = new PtTestProperties();

    private final Map<TestKey, String> values = new HashMap<TestKey, String>();

    private PtTestProperties() {
        final ResourceBundle properties = ResourceBundle.getBundle(PROPERTIES_FILE_NAME);
        for (final Enumeration<String> keys = properties.getKeys(); keys.hasMoreElements(); ) {
            final String propKey = keys.nextElement();
            final TestKey key = MAIN_KEYS.get(propKey);
            if (key == null) {
                throw new IllegalArgumentException("Invalid property key [" + propKey + "]!");
            }
            final String value = properties.getString(propKey);
            LOG.debug("Setting main property [" + key.getPropKey() + "] = [" + value + "]");
            this.values.put(key, value);
        }
    }

    public static PtTestProperties getInstance() {
        return INSTANCE;
    }

    public String getTestRootPath() {
        return this.values.get(TestKey.FOLDER_TESTROOT);
    }

    /**
	 * @author christoph.pickl@bmi.gv.at
	 */
    private static enum TestKey {

        FOLDER_TESTROOT("folder.testroot");

        private final String propKey;

        private TestKey(final String propKey) {
            this.propKey = propKey;
        }

        String getPropKey() {
            return this.propKey;
        }
    }
}
