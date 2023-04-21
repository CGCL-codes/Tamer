package solidbase;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import solidbase.core.SystemException;
import solidbase.util.Assert;

/**
 * Represents the version of SolidBase. It reads from version.properties.
 * 
 * @author Ren� M. de Bloois
 */
public class Version {

    private static final String SOLIDBASE_VERSION_PROPERTIES = "version.properties";

    /**
	 * The version of SolidBase.
	 */
    protected static String version;

    static {
        URL url = Version.class.getResource(SOLIDBASE_VERSION_PROPERTIES);
        Assert.notNull(url);
        Properties properties = new Properties();
        try {
            properties.load(url.openStream());
        } catch (IOException e) {
            throw new SystemException(e);
        }
        version = properties.getProperty("solidbase.version");
        Assert.notNull(version);
    }

    /**
	 * This class cannot be constructed.
	 */
    private Version() {
        super();
    }

    /**
	 * Get the SolidBase version & copyright info to be displayed to the user.
	 * 
	 * @return The SolidBase version & copyright info to be displayed to the user.
	 */
    public static String getInfo() {
        return "SolidBase v" + version + " (http://solidbase.org)";
    }
}
