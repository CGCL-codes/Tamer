package eu.vph.predict.vre.in_silico.value.configuration;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract class for configuration settings.
 *
 * @author Geoff Williams
 */
public abstract class AbstractConfiguration implements Serializable {

    private static final long serialVersionUID = 5196504624715267773L;

    private boolean defaultConfigurationForType;

    private String identifier;

    private static final Log log = LogFactory.getLog(AbstractConfiguration.class);

    /**
   * Initialising constructor.
   * 
   * @param identifier Something which the user identifies with to identify the configuration.
   * @param defaultConfigurationForType Flag if this is the default configuration for type.
   */
    public AbstractConfiguration(final String identifier, final boolean defaultConfigurationForType) {
        log.debug("~.conf.AbstractConfiguration(String) : Creating [" + identifier + "]");
        setIdentifier(identifier);
        setDefaultConfigurationForType(defaultConfigurationForType);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("AbstractConfiguration - Identifier [");
        sb.append(getIdentifier());
        sb.append("], defaultConfigurationForType [");
        sb.append(isDefaultConfigurationForType());
        sb.append("]");
        return sb.toString();
    }

    /**
   * @return the identifier
   */
    public String getIdentifier() {
        return identifier;
    }

    /**
   * @param identifier the identifier to set
   */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
   * @return the defaultConfigurationForType
   */
    public boolean isDefaultConfigurationForType() {
        return defaultConfigurationForType;
    }

    /**
   * @param defaultConfigurationForType the defaultConfigurationForType to set
   */
    public void setDefaultConfigurationForType(boolean defaultConfigurationForType) {
        this.defaultConfigurationForType = defaultConfigurationForType;
    }
}
