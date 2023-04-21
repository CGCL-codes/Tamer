package env3d;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A singleton class for loading default properties.
 * Only textures for now.
 *
 * @author Jason Madar
 */
public class Default {

    private static Default instance;

    private Properties properties;

    /** Creates a new instance of Defaults. A singleton constructor */
    private Default() {
        properties = new Properties();
        try {
            java.io.InputStream str = getClass().getResourceAsStream("/env3d.properties");
            properties.load(str);
        } catch (IOException e) {
            System.err.println("Cannot load properties!  Exiting...");
            System.exit(1);
        }
    }

    /**
     * Get the instance of this singleton
     * @return a single instance of this object
     */
    public static Default getInstance() {
        if (instance == null) {
            instance = new Default();
        }
        return instance;
    }

    /**
     * Returns the default texture, or null if property not found.
     * @return The texture filename
     */
    public String getObjectTexture() {
        return properties.getProperty("texture.object");
    }

    public String getBottomTexture() {
        return properties.getProperty("texture.bottom");
    }

    public String getTopTexture() {
        return properties.getProperty("texture.top");
    }

    public String getWallTexture() {
        return properties.getProperty("texture.wall");
    }

    public String getExitTexture() {
        return properties.getProperty("texture.exit");
    }
}
