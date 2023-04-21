package org.eaiframework.config.xml;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eaiframework.ChannelFactory;
import org.eaiframework.ChannelManager;
import org.eaiframework.FilterDescriptorRegistry;
import org.eaiframework.FilterFactory;
import org.eaiframework.FilterManager;
import org.eaiframework.MessageConsumerFactory;
import org.eaiframework.MessageProducerFactory;
import org.eaiframework.config.Configuration;
import org.eaiframework.config.ConfigurationException;
import org.eaiframework.impl.ChannelManagerImpl;
import org.eaiframework.impl.FilterDescriptorRegistryImpl;
import org.eaiframework.impl.FilterFactoryImpl;
import org.eaiframework.impl.FilterManagerImpl;
import org.eaiframework.impl.SingletonMessageSenderFactoryImpl;
import org.eaiframework.impl.jdk.JdkChannelFactory;
import org.eaiframework.impl.jdk.JdkMessageConsumerFactory;
import org.eaiframework.impl.jdk.JdkMessageProducerFactory;

/**
 * Change.
 */
public abstract class XMLConfiguration implements Configuration {

    private Log log = LogFactory.getLog(XMLConfiguration.class);

    protected FilterManager filterManager;

    protected FilterDescriptorRegistry filterDescriptorRegistry;

    protected ChannelManager channelManager;

    protected ChannelsConfigHelper channelsConfigHelper;

    protected FiltersDescriptorsConfigHelper filtersDescriptorsConfigHelper;

    protected FiltersConfigHelper filtersConfigHelper;

    public void configure() throws ConfigurationException {
        log.info("configuring eaiframework ...");
        channelsConfigHelper = new ChannelsConfigHelper();
        filtersDescriptorsConfigHelper = new FiltersDescriptorsConfigHelper();
        filtersConfigHelper = new FiltersConfigHelper();
        checkDependencies();
        channelsConfigHelper.setChannelManager(channelManager);
        filtersDescriptorsConfigHelper.setFilterDescriptorRegistry(filterDescriptorRegistry);
        filtersConfigHelper.setFilterManager(filterManager);
        configureIncludedFilterDescriptors();
        try {
            InputStream[] channelsConfigFiles = this.getChannelsInputStreams();
            log.debug("configuring " + channelsConfigFiles.length + " channel config files ... ");
            for (InputStream is : channelsConfigFiles) {
                channelsConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            throw new ConfigurationException(e);
        }
        try {
            InputStream[] filtersDescriptorsConfigFiles = this.getFiltersDescriptorsInputStreams();
            log.debug("configuring " + filtersDescriptorsConfigFiles.length + " filter descriptors config files ... ");
            for (InputStream is : filtersDescriptorsConfigFiles) {
                filtersDescriptorsConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            throw new ConfigurationException(e);
        }
        try {
            InputStream[] filtersConfigFiles = this.getFiltersInputStreams();
            log.debug("configuring " + filtersConfigFiles.length + " filters config files ... ");
            for (InputStream is : filtersConfigFiles) {
                filtersConfigHelper.configure(is);
            }
        } catch (IOException e) {
            channelManager.destroyAllChannels();
            filterManager.destroyAllFilters();
            throw new ConfigurationException(e);
        }
        log.info("<< eaiframework configured >>");
    }

    private void configureIncludedFilterDescriptors() throws ConfigurationException {
        ClassLoader classloader = this.getClass().getClassLoader();
        log.debug("configuring included filter descriptors ... ");
        filtersDescriptorsConfigHelper.configure(classloader.getResourceAsStream("channel-purger.xml"));
        filtersDescriptorsConfigHelper.configure(classloader.getResourceAsStream("property-content-router.xml"));
        filtersDescriptorsConfigHelper.configure(classloader.getResourceAsStream("property-filter.xml"));
        filtersDescriptorsConfigHelper.configure(classloader.getResourceAsStream("recepient-list.xml"));
        log.debug("included filter descriptors configured.");
    }

    private void checkDependencies() {
        if (channelManager == null) {
            log.debug("using default channel manager ... ");
            channelManager = new ChannelManagerImpl();
            ChannelFactory channelFactory = new JdkChannelFactory();
            channelManager.setChannelFactory(channelFactory);
        }
        if (filterDescriptorRegistry == null) {
            log.debug("using default filter descriptor registry ... ");
            filterDescriptorRegistry = new FilterDescriptorRegistryImpl();
        }
        if (filterManager == null) {
            log.debug("using default filter manager ... ");
            filterManager = new FilterManagerImpl();
            FilterFactory filterFactory = new FilterFactoryImpl();
            MessageConsumerFactory messageConsumerFactory = new JdkMessageConsumerFactory();
            MessageProducerFactory messageProducerFactory = new JdkMessageProducerFactory();
            SingletonMessageSenderFactoryImpl messageSenderFactory = SingletonMessageSenderFactoryImpl.getInstance();
            messageSenderFactory.setChannelManager(channelManager);
            messageSenderFactory.setMessageProducerFactory(messageProducerFactory);
            FilterManagerImpl filterManagerImpl = (FilterManagerImpl) filterManager;
            filterManagerImpl.setChannelManager(channelManager);
            filterManagerImpl.setFilterDescriptorRegistry(filterDescriptorRegistry);
            filterManagerImpl.setMessageConsumerFactory(messageConsumerFactory);
            filterManagerImpl.setMessageSenderFactory(messageSenderFactory);
            filterManagerImpl.setFilterFactory(filterFactory);
        }
    }

    public void destroy() throws ConfigurationException {
        log.info("destroying eaiframework ...");
        filterManager.destroyAllFilters();
        channelManager.destroyAllChannels();
        filterDescriptorRegistry.removeAllFilterDescriptors();
        log.info("<< eaiframework destroyed >>");
    }

    protected abstract InputStream[] getFiltersInputStreams() throws IOException;

    protected abstract InputStream[] getFiltersDescriptorsInputStreams() throws IOException;

    protected abstract InputStream[] getChannelsInputStreams() throws IOException;

    /**
	 * @return the filterManager
	 */
    public FilterManager getFilterManager() {
        return filterManager;
    }

    /**
	 * @param filterManager the filterManager to set
	 */
    public void setFilterManager(FilterManager filterManager) {
        this.filterManager = filterManager;
    }

    /**
	 * @return the filterDescriptorRegistry
	 */
    public FilterDescriptorRegistry getFilterDescriptorRegistry() {
        return filterDescriptorRegistry;
    }

    /**
	 * @param filterDescriptorRegistry the filterDescriptorRegistry to set
	 */
    public void setFilterDescriptorRegistry(FilterDescriptorRegistry filterDescriptorRegistry) {
        this.filterDescriptorRegistry = filterDescriptorRegistry;
    }

    /**
	 * @return the channelManager
	 */
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
	 * @param channelManager the channelManager to set
	 */
    public void setChannelManager(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }
}
