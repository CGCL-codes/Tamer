package org.nakedobjects.metamodel.config.loader.resource;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.loader.ConfigurationLoaderDefault;
import org.nakedobjects.metamodel.config.loader.NotFoundPolicy;
import org.nakedobjects.metamodel.config.prop.PropertiesConfiguration;
import org.nakedobjects.metamodel.config.reader.propfile.PropertiesFileReader;

/**
 * Loads in the specified configuration files as resources on the classpath. Properties in the later files
 * overrides properties in earlier files.
 */
public class ConfigurationLoaderResource extends ConfigurationLoaderDefault {

    PropertiesConfiguration configuration = new PropertiesConfiguration();

    public ConfigurationLoaderResource() {
        super();
    }

    public ConfigurationLoaderResource(final String file) {
        super(file);
    }

    @Override
    public NakedObjectConfiguration load() {
        if (configuration.getString(SHOW_EXPLORATION_OPTIONS) == null) {
            configuration.add(SHOW_EXPLORATION_OPTIONS, "yes");
        }
        return configuration;
    }

    @Override
    public void addConfigurationFile(final String path, NotFoundPolicy notFoundPolicy) {
        final PropertiesFileReader loader = new PropertiesFileReader(".", path, notFoundPolicy);
        configuration.add(loader.getProperties());
    }
}
