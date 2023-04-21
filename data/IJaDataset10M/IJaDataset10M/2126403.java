package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.log.ConfigurationResolver;
import org.xml.sax.InputSource;
import java.io.File;

/**
 * This interface enables certain methods available in
 * javax.servlet.ServletContext to be abstracted from the runtime environment.
 * 
 * @mock.generate
 */
public interface ConfigContext {

    /**
     * Resolves the location specified for the main mcs configuration file and
     * return the corresponding InputSource.
     *
     * @return the InputSource for the main mcs configuration file
     * @throws ConfigurationException if unable to get an InputSource for the
     *                                main mcs configuration file
     */
    InputSource getMainConfigInputSource() throws ConfigurationException;

    /**
     * Resolves a path relative to the location of the main configuration file.
     * If the specified path is absolute it returns the File object that
     * corresponds to that path.
     *
     * @param path the path to resolve
     * @param mustExist true if the file is expected to be exist
     * @return the resolved File object or null if the file doesn't exist, but
     * it is expected to exist.
     * @throws ConfigurationException if a relative path was supplied and the
     * configuration file path against which to resolve it is null
     */
    public File getConfigRelativeFile(String path, boolean mustExist) throws ConfigurationException;

    /**
     * Gets the location of the log4j configuration file. This may be an
     * absolute or relative path to a file on the filesystem or to a resource
     * loadable with a ClassLoader.
     *
     * @return the location of the configuration file, or null if none was
     * specified
     */
    String getLog4jLocation();

    /**
     * Gets a ConfigurationResolver instance which can be used to resolve
     * references to configuration resources or files against the servlet
     * context.
     *
     * @param paramName the name of the resolver
     * @return the resolver
     */
    ConfigurationResolver getConfigurationResolver(String paramName);
}
