package net.sourceforge.dbtoolbox.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import net.sourceforge.dbtoolbox.io.FileResource;
import net.sourceforge.dbtoolbox.io.Resource;

/**
 * Metadata validator configuration
 */
public class ValidatorConfigImpl implements ValidatorConfig {

    /**
     * Delegate validator config
     */
    private ValidatorConfig delegate;

    /**
     * Configuration resource
     */
    private Resource configResource;

    /**
     * Configuration file
     */
    private File configFile;

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Configuration format
     */
    private String configFormat;

    public String getConfigFormat() {
        return configFormat;
    }

    public void setConfigFormat(String configFormat) {
        this.configFormat = configFormat;
    }

    /**
     * Configuration format constant: Properties file
     */
    public static final String CONFIG_FORMAT_PROPERTIES = "properties";

    /**
     * Configuration format constant: XML file
     */
    public static final String CONFIG_FORMAT_XML = "xml";

    public ValidatorConfigImpl(Resource validatorConfigResource) {
        this.configResource = validatorConfigResource;
    }

    /**
     * Default constructor
     */
    public ValidatorConfigImpl() {
    }

    public void prepareValidators() throws ConfigException {
        if ((configResource == null) && (configFile != null)) {
            try {
                this.configResource = new FileResource(configFile);
            } catch (IOException iOException) {
                throw new ConfigException("Validator config file read fail", iOException);
            }
        }
        if (configFormat == null) {
            String fileName = configResource.getName();
            if (fileName.endsWith(".properties")) {
                configFormat = CONFIG_FORMAT_PROPERTIES;
            } else if (fileName.endsWith(".xml")) {
                configFormat = CONFIG_FORMAT_XML;
            } else {
                throw new ConfigException("Unable to determine validator config file format");
            }
        }
        if (configFormat.equalsIgnoreCase(CONFIG_FORMAT_PROPERTIES)) {
            delegate = new ValidatorConfigProperties(configResource);
        } else if (configFormat.equalsIgnoreCase(CONFIG_FORMAT_XML)) {
            delegate = new ValidatorConfigXml(configResource);
        }
        delegate.prepareValidators();
    }

    public Collection<String> getValidatorNames() {
        return delegate.getValidatorNames();
    }

    public Class getValidatorClass(String validatorName) {
        return delegate.getValidatorClass(validatorName);
    }

    public <T> T getValidatorProperty(String validatorName, String propertyName, Class<T> propertyType) {
        return delegate.getValidatorProperty(validatorName, propertyName, propertyType);
    }

    /**
     * Properties used for validation.
     * Applies on "properties".
     */
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
