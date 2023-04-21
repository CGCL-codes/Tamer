package de.eqc.srcds.configuration;

import java.util.Map;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;

public interface Configuration {

    <T> T getValue(final String key, final Class<T> dataType) throws ConfigurationException;

    <T> void setValue(final String key, final T value) throws ConfigurationException;

    void removeValue(final String key) throws ConfigurationException;

    String toXml();

    <T> Map<ConfigurationKey<?>, String> getData();
}
