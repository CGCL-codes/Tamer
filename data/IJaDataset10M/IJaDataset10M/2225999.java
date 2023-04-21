package org.openstreetmap.gui.griddirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ProgramConfiguration {

    private static final Logger log = Logger.getLogger(ProgramConfiguration.class.getName());

    private static final String FILE_NAME = "OSMGridDirectory.config";

    private static final String KEY_TILECACHEDIR = "TileCacheDirectory";

    private static final String KEY_XMLCACHEDIR = "XmlCacheDirectory";

    private static ProgramConfiguration instance;

    private File configFile;

    private Properties properties;

    private transient File baseDirectory;

    /**
	 * Load config file if exists in current directory.
	 */
    private ProgramConfiguration() {
        configFile = new File(FILE_NAME);
        createConfig();
        if (configFile.exists() && !configFile.isDirectory()) {
            loadConfig();
        }
        saveConfig();
    }

    private void saveConfig() {
        try {
            properties.storeToXML(new FileOutputStream(configFile), "");
        } catch (IOException e) {
            log.severe("Error while reading config file " + configFile);
        }
    }

    private void createConfig() {
        properties = new Properties();
        properties.setProperty(KEY_TILECACHEDIR, "TileCache");
        properties.setProperty(KEY_XMLCACHEDIR, "XmlCache");
    }

    private void loadConfig() {
        try {
            properties.loadFromXML(new FileInputStream(configFile));
        } catch (IOException e) {
            log.severe("Error while reading config file " + configFile);
        }
    }

    /**
	 * Return instance of this singleton.
	 * @return
	 */
    public static ProgramConfiguration getInstance() {
        if (instance == null) {
            instance = new ProgramConfiguration();
        }
        return instance;
    }

    /**
	 * @return
	 */
    public String getTileCacheDirectory() {
        return properties.getProperty(KEY_TILECACHEDIR);
    }

    /**
	 * @return
	 */
    public String getXmlCacheDirectory() {
        return properties.getProperty(KEY_XMLCACHEDIR);
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
}
