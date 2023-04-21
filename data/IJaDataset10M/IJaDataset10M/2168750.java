package com.volantis.map.ics.configuration;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for producing ImageProcessor's.
 */
public abstract class ConfigurationParserFactory {

    private static final MetaDefaultFactory instance = new MetaDefaultFactory("com.volantis.map.ics.configuration.impl." + "DefaultConfigurationParserFactory", ConfigurationParserFactory.class.getClassLoader());

    /**
     * Creates an image processor with given factories. These factories wil be
     * used for building up image processing machines.
     *
     * @return ImageProcessor - image processor.
     */
    public abstract ConfigurationParser createConfigurationParser();

    /**
     * Gets an instance of ConfigurationParserFactory.
     *
     * @return ConfigurationParserFactory gets an instance of the factory.
     */
    public static ConfigurationParserFactory getInstance() {
        return (ConfigurationParserFactory) instance.getDefaultFactoryInstance();
    }
}
