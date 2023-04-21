package org.nakedobjects.metamodel.config.reader.propfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.reader.ConfigurationReader;

/**
 * Superset of the functionality provided by {@link PropertiesFileReader}, in that multiple property files
 * can be loaded.
 */
public class PropertiesFilesReader implements ConfigurationReader {

    private final Properties properties;

    private final boolean ensureFileLoads;

    public PropertiesFilesReader(final boolean ensureFileLoads) {
        properties = new Properties();
        this.ensureFileLoads = ensureFileLoads;
    }

    public PropertiesFilesReader(final String pathname, final boolean ensureFileLoads) {
        this(ensureFileLoads);
        loadProperties(pathname);
    }

    /**
     * Loads properties from supplied path; if there is a clash with properties overridden, then they will be
     * overridden.
     * 
     * @param pathname
     */
    public void loadProperties(final String pathname) {
        try {
            FileInputStream in;
            final File file = new File(pathname);
            in = new FileInputStream(file);
            properties.load(in);
        } catch (final FileNotFoundException e) {
            if (ensureFileLoads) {
                throw new NakedObjectException("Could not find required configuration file: " + pathname);
            }
        } catch (final IOException e) {
            throw new NakedObjectException("Could not load configuration file: " + pathname, e);
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
