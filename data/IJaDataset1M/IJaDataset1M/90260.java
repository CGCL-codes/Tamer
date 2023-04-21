package org.ozoneDB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.ozoneDB.core.Server;
import org.ozoneDB.core.storage.GZIPStreamFactory;
import org.ozoneDB.core.storage.wizardStore.WizardStore;
import org.ozoneDB.io.stream.ResolvingObjectInputStream;
import org.ozoneDB.util.EnhProperties;

/**
 * Setup holds all static configuration properties plus all dynamic runtime
 * properties of an ozone environment. Setup has methods to store/update the
 * value of a property to handle such dynamic properties.
 *
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @author <a href="http://www.medium.net/">Medium.net</a>
 * @version $Revision: 1.22 $Date: 2004/11/19 09:44:09 $
 */
public class Setup extends EnhProperties {

    private static final long serialVersionUID = 1L;

    public static final String PROPERTIES_PREFIX = "ozoneDB";

    public static final String DB_ID = PROPERTIES_PREFIX + ".dbID";

    public static final String PORT = PROPERTIES_PREFIX + ".port";

    public static final String STORE = PROPERTIES_PREFIX + ".store";

    public static final String MIN_FREE_MEMORY = PROPERTIES_PREFIX + ".vm.minFreeMemory";

    public static final String TOTAL_MEMORY = PROPERTIES_PREFIX + ".vm.totalMemory";

    public static final String XOID = PROPERTIES_PREFIX + ".xoid";

    public static final String GARBAGE_COLLECTION_LEVEL = PROPERTIES_PREFIX + ".garbageCollection.level";

    public static Setup createEmptySetup() {
        return new Setup();
    }

    public static Setup createDefaultConfig() {
        Setup result = new Setup();
        result.setProperty(STORE, WizardStore.class.getName());
        result.setProperty(DB_ID, String.valueOf(0));
        result.setProperty(PORT, String.valueOf(3333));
        result.setProperty(GARBAGE_COLLECTION_LEVEL, String.valueOf(0));
        result.setProperty(Server.CONFIG_OZONE_BASE + Server.ENCODE_DECODE_STREAM_FACTORY.getKey(), GZIPStreamFactory.class.getName());
        return result;
    }

    protected Setup() {
    }

    public String getProperty(String key, String defaultValue) {
        return filterProperty(key, super.getProperty(key, defaultValue));
    }

    public String getProperty(String key) {
        return filterProperty(key, super.getProperty(key));
    }

    private String filterProperty(String key, String value) {
        if (key.equals(STORE)) {
            if (value == null) {
            } else if (value.matches(".*(experimental).*WizardStore")) {
                value = "org.ozoneDB.core.storage.experimental.wizardStore.WizardStore";
            } else if (value.matches(".*WizardStore")) {
                value = "org.ozoneDB.core.storage.wizardStore.WizardStore";
            } else if (value.matches(".*MagicStore")) {
                value = "org.ozoneDB.core.storage.magicStore.MagicStore";
            } else if (value.matches(".*GammaStore")) {
                value = "org.ozoneDB.core.storage.gammaStore.GammaStore";
            } else if (value.matches(".*ClassicStore")) {
                value = "org.ozoneDB.core.storage.classicStore.ClassicStore";
            }
        }
        return value;
    }
}
