package com.kapil.framework.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Factory to initialize different kinds of message readers. This factory takes away the responsibility from the
 * consumer of the framework to initialize readers. Any change in underlying implementations will not impact application
 * code.
 * </p>
 */
public class MessageReaderFactory {

    /** The INSTANCE. */
    private static MessageReaderFactory INSTANCE;

    /**
     * Only constructor which is made private, forcing a Singleton instance.
     */
    private MessageReaderFactory() {
    }

    /**
     * Gets the single instance of MessageReaderFactory.
     * 
     * @return instance of MessageReaderFactory
     */
    public static MessageReaderFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageReaderFactory();
        }
        return INSTANCE;
    }

    /**
     * Creates a new instance of the {@link ResourceBundleMessageReader} for multiple data sources.
     * 
     * @param dataSources list of data sources needed to initialise the bundle reader
     * 
     * @return new instance of the {@link ResourceBundleMessageReader}
     */
    public IMessageReader getReader(String type, List<String> dataSources) {
        if ("ResourceBundle".equals(type)) {
            return new ResourceBundleMessageReader(dataSources);
        } else {
            throw new RuntimeException("Message Reader Factory can not return any instance of type [" + type + "]");
        }
    }

    /**
     * Creates a new instance of the {@link ResourceBundleMessageReader} for a single data source.
     * 
     * @param dataSource the data source
     * 
     * @return new instance of the {@link ResourceBundleMessageReader}
     */
    public IMessageReader getReader(String type, String dataSource) {
        List<String> dataSourceList = new ArrayList<String>();
        dataSourceList.add(dataSource);
        return getReader(type, dataSourceList);
    }

    /**
     * Creates a new instance of the {@link XMLConfigurationReader} for a single data source.
     * 
     * 
     * @param dataSource the data source
     * @return new instance of the {@link ResourceBundleMessageReader}
     */
    public IMessageReader getXmlConfigurationReader(final String dataSourceName) {
        return new XMLConfigurationReader(dataSourceName);
    }
}
