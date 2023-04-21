package com.anthonyeden.lib.config;

import java.io.InputStream;
import java.io.Reader;

/** 
 * Standard interface which is used to retrieve a Configuration from an 
 * InputStream or Reader.
 * 
 * @author Anthony Eden
 * @since 2.0
 */
public interface ConfigurationFactory {

    /**
     * Get the root Configuration object from the specified InputStream.
     *
     * @param id The id of the configuration (file path, URL, etc)
     * @param in The InputStream
     * @return The Configuration object
     * @throws ConfigurationException
     */
    public Configuration getConfiguration(String id, InputStream in) throws ConfigurationException;

    /**
     * Get the root Configuration object from the specified InputStream.
     *
     * @param id The id of the configuration (file path, URL, etc)
     * @param in The InputStream
     * @return The Configuration object
     * @throws ConfigurationException
     */
    public Configuration getConfiguration(String id, Reader in) throws ConfigurationException;
}
