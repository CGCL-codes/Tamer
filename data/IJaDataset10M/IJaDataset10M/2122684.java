package com.diotalevi.ubernotes.plugins.api;

/**
 *
 * @author Filippo Diotalevi
 */
public interface Configuration {

    public void setProperty(GenericPlugin plugin, String key, String value);

    public String getProperty(GenericPlugin plugin, String key);

    public void initialize(String environment);
}
