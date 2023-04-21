package net.sf.jpackit.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Build configuration manager defines interface to save and load BuildConfiguration 
 * @see net.sf.jpackit.config.BuildConfiguration
 * @author Kamil K. Shamgunov 
 */
public interface BuildConfigurationManager {

    /**
     * serialize BuildConfiguration into given OutputStream
     * @param buildConfiguration BuildConfiguration instance to save
     * @param oStream OutputStream to which write serialized BuildConfiguration
     * @throws java.io.IOException 
     */
    public void save(BuildConfiguration buildConfiguration, OutputStream oStream) throws IOException;

    /**
     * Load build configuration from given InputStream
     * @param iStream InputStream from where BuildConfiguration will be read.
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     * @return BuildConfiguration that is loaded from InputStream
     */
    public BuildConfiguration load(InputStream iStream) throws IOException, ClassNotFoundException;
}
