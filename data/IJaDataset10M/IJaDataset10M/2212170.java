package com.acme.services.examples;

import com.codemonster.surinam.examples.PropertyResourceBroker_1_0;
import com.codemonster.surinam.export.lifecycle.ServiceAdapter;
import com.codemonster.surinam.export.lifecycle.ServiceAvailability;
import com.codemonster.surinam.export.meta.ProviderImplementationInfo;
import java.util.Properties;

/**
 * This is our default implementation of a Property Resource Broker service that implements the
 * PropertyResourceBroker_1_0 Software Contract.<br>
 * <br>
 */
@ProviderImplementationInfo(author = "Sam Provencher", organization = "Surinam Project", version = "1.0", releaseDate = "9/26/2007", webAddress = "http://surinam.sourceforge.net", description = "One example implementation of a resource broker for the examples.")
public class PropertyResourceBrokerImpl extends ServiceAdapter implements PropertyResourceBroker_1_0 {

    /** It's okay to start with an empty state. */
    private Properties props = new Properties();

    public PropertyResourceBrokerImpl() {
        setServiceAvailability(ServiceAvailability.AVAILABLE);
    }

    /**
     * This is not thread safe... it's an example only.
     *
     * @param props
     */
    public void setProperties(Properties props) {
        this.props = props;
    }

    public Object getProperty(String key) {
        return props.get(key);
    }

    /**
     * This method allows services direct local access to the property map.
     */
    public Properties getPropertiesObject() {
        return props;
    }
}
