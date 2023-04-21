package cz.fi.muni.xkremser.editor.server.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import com.google.inject.Singleton;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

/**
 * The Class EditorConfigurationImpl.
 */
@Singleton
public class EditorConfigurationImpl extends EditorConfiguration {

    /** The Constant WORKING_DIR. */
    public static final String WORKING_DIR = System.getProperty("user.home") + File.separator + ".meditor";

    /** The Constant DEFAULT_CONF_LOCATION. */
    public static final String DEFAULT_CONF_LOCATION = "configuration.properties";

    /** The Constant DEFAULT_IMAGES_LOCATION. */
    public static final String DEFAULT_IMAGES_LOCATION = WORKING_DIR + File.separator + "images" + File.separator;

    /** The Constant CONFIGURATION. */
    public static final String CONFIGURATION = WORKING_DIR + File.separator + DEFAULT_CONF_LOCATION;

    /** The configuration. */
    private Configuration configuration;

    private static final Logger LOGGER = Logger.getLogger(EditorConfiguration.class);

    public EditorConfigurationImpl() {
        File dir = new File(WORKING_DIR);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                LOGGER.error("cannot create directory '" + dir.getAbsolutePath() + "'");
                throw new RuntimeException("cannot create directory '" + dir.getAbsolutePath() + "'");
            }
        }
        File confFile = new File(CONFIGURATION);
        if (!confFile.exists()) {
            try {
                confFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("cannot create configuration file '" + confFile.getAbsolutePath() + "'", e);
                throw new RuntimeException("cannot create configuration file '" + confFile.getAbsolutePath() + "'");
            }
            FileOutputStream confFos;
            try {
                confFos = new FileOutputStream(confFile);
            } catch (FileNotFoundException e) {
                LOGGER.error("cannot create configuration file '" + confFile.getAbsolutePath() + "'", e);
                throw new RuntimeException("cannot create configuration file '" + confFile.getAbsolutePath() + "'");
            }
            try {
                try {
                    new Properties().store(confFos, "configuration file for module metadata editor");
                } catch (IOException e) {
                    LOGGER.error("cannot create configuration file '" + confFile.getAbsolutePath() + "'", e);
                    throw new RuntimeException("cannot create configuration file '" + confFile.getAbsolutePath() + "'");
                }
            } finally {
                try {
                    if (confFos != null) confFos.close();
                } catch (IOException e) {
                    LOGGER.error("cannot create configuration file '" + confFile.getAbsolutePath() + "'", e);
                    throw new RuntimeException("cannot create configuration file '" + confFile.getAbsolutePath() + "'");
                } finally {
                    confFos = null;
                }
            }
        }
        File imagesDir = new File(DEFAULT_IMAGES_LOCATION);
        if (!imagesDir.exists()) {
            boolean mkdirs = imagesDir.mkdirs();
            if (!mkdirs) {
                LOGGER.error("cannot create directory '" + imagesDir.getAbsolutePath() + "'");
                throw new RuntimeException("cannot create directory '" + imagesDir.getAbsolutePath() + "'");
            }
        }
        CompositeConfiguration constconf = new CompositeConfiguration();
        PropertiesConfiguration file = null;
        try {
            file = new PropertiesConfiguration(confFile);
        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
            new RuntimeException("cannot read configuration");
        }
        file.setReloadingStrategy(new FileChangedReloadingStrategy());
        constconf.addConfiguration(file);
        constconf.setProperty(ServerConstants.EDITOR_HOME, WORKING_DIR);
        this.configuration = constconf;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
