package org.monet.reportgenerator.server.configuration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.monet.reportgenerator.server.configuration.Configuration;
import org.monet.reportgenerator.server.configuration.ServerConfigurator;
import org.monet.reportgenerator.server.control.log.Logger;
import org.monet.reportgenerator.shared.exception.ApplicationException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigurationImpl implements Configuration {

    private Logger logger;

    private ServerConfigurator serverConfigurator;

    private Properties properties;

    private org.monet.kernel.configuration.Configuration monetConfiguration;

    @Inject
    public ConfigurationImpl(Logger logger, ServerConfigurator serverConfigurator) {
        this.logger = logger;
        this.serverConfigurator = serverConfigurator;
        this.properties = new Properties();
        try {
            String configurationPath = this.serverConfigurator.getUserPath() + File.separator + "reportoffice.config";
            this.properties.loadFromXML(new FileInputStream(configurationPath));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new ApplicationException("Error loading configuration", e);
        }
    }

    public String getTemplateDir() {
        return this.serverConfigurator.getUserPath() + File.separator + "templates";
    }

    @Override
    public String getUrl() {
        if (this.monetConfiguration == null) this.monetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        String url = this.monetConfiguration.getUrl();
        return url;
    }

    @Override
    public String getDocServerUrl() {
        if (this.monetConfiguration == null) this.monetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        String url = this.monetConfiguration.getValue(org.monet.kernel.configuration.Configuration.COMPONENT_DOCUMENTS_MONET_URL);
        return url;
    }

    @Override
    public String getReportServiceUrl() {
        logger.debug("getReportServiceUrl(%s)");
        return this.properties.getProperty(Configuration.REPORTSERVICE_URL);
    }

    @Override
    public String getProperty(String key) {
        String value = this.properties.getProperty(key);
        if (value != null) return value; else throw new ApplicationException(String.format("Configuration error, key '%s' not found", key));
    }
}
