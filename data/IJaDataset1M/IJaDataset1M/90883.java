package org.jage.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Empty configuration.
 * @author Pawel Kedzior
 */
public class EmptyConfiguration implements IConfiguration {

    /**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Instance of an empty configuration.
	 */
    public static final IConfiguration INSTANCE = new EmptyConfiguration();

    /**
	 * @see org.jage.config.IConfiguration#getParameterGroup(java.lang.String)
	 */
    public IConfiguration getParameterGroup(String name) throws ConfigurationException {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterGroups(java.lang.String)
	 */
    public Collection<IConfiguration> getParameterGroups(String name) {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String)
	 */
    public Object getParameterValue(String name) {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, java.lang.Class, java.lang.Object)
	 */
    public <T> T getParameterValue(String name, Class<T> valueClass, T defaultValue) {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, java.lang.Class)
	 */
    public <T> T getParameterValue(String name, Class<T> valueClass) throws ConfigurationException {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, java.lang.String)
	 */
    public String getParameterValue(String name, String defaultValue) {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, int)
	 */
    public int getParameterValue(String name, int defaultValue) {
        return defaultValue;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, float)
	 */
    public float getParameterValue(String name, float defaultValue) {
        return defaultValue;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValue(java.lang.String, boolean)
	 */
    public boolean getParameterValue(String name, boolean defaultValue) {
        return defaultValue;
    }

    /**
	 * @see org.jage.config.IConfiguration#getParameterValues(java.lang.String, java.lang.Class)
	 */
    public <T> Collection<T> getParameterValues(String name, Class<T> valueClass) {
        return null;
    }

    /**
	 * @see org.jage.config.IConfiguration#hasParameter(java.lang.String)
	 */
    public boolean hasParameter(String name) {
        return false;
    }

    /**
	 * @see org.jage.config.IConfiguration#hasParameterGroup(java.lang.String)
	 */
    public boolean hasParameterGroup(String name) {
        return false;
    }

    /**
	 * @see org.jage.config.IConfiguration#parameterGroups()
	 */
    public Map<String, Collection<IConfiguration>> parameterGroups() {
        return Collections.emptyMap();
    }

    /**
	 * @see org.jage.config.IConfiguration#parameters()
	 */
    public Map<String, Collection<Object>> parameters() {
        return Collections.emptyMap();
    }
}
