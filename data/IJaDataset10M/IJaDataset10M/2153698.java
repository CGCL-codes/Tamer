package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;

public class ClassPathLoader {

    /**
     * @return configuration loaded from classpath or null
     */
    @SuppressWarnings({ "unchecked" })
    public IMockitoConfiguration loadConfiguration() {
        Class configClass = null;
        try {
            configClass = (Class) Class.forName("org.mockito.configuration.MockitoConfiguration");
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            return (IMockitoConfiguration) configClass.newInstance();
        } catch (ClassCastException e) {
            throw new MockitoConfigurationException("\n" + "MockitoConfiguration class must implement org.mockito.configuration.IMockitoConfiguration interface.", e);
        } catch (Exception e) {
            throw new MockitoConfigurationException("\n" + "Unable to instantianate org.mockito.configuration.MockitoConfiguration class. Does it have a safe, no-arg constructor?", e);
        }
    }
}
