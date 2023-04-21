package org.monet.kernel.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import org.monet.kernel.agents.AgentFilesystem;
import org.monet.kernel.constants.SiteFiles;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.exceptions.SystemException;

public abstract class ApplicationConfiguration {

    private Properties oProperties;

    protected ApplicationConfiguration(String sApplication) {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        InputStream isConfigFileContent = AgentFilesystem.getInputStream(oMonetConfiguration.getUserDataDir() + Strings.BAR45 + sApplication + SiteFiles.CONFIG);
        this.oProperties = new Properties();
        try {
            this.oProperties.loadFromXML(isConfigFileContent);
            this.parse();
            isConfigFileContent.close();
        } catch (IOException oException) {
            throw new SystemException("Load configuration file", sApplication, oException);
        }
    }

    private void parse() {
        Iterator<Object> oIterator = this.oProperties.keySet().iterator();
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        while (oIterator.hasNext()) {
            String sKey = (String) oIterator.next();
            String sValue = (String) this.oProperties.get(sKey);
            sValue = sValue.replaceAll("::MONETDIR::", oMonetConfiguration.getFrameworkDir());
            sValue = sValue.replaceAll("::MONETUSERDATADIR::", oMonetConfiguration.getUserDataDir());
            sValue = sValue.replaceAll("::MONETAPPDATADIR::", oMonetConfiguration.getAppDataDir());
            sValue = sValue.replaceAll("::MONETDOMAIN::", oMonetConfiguration.getDomain());
            this.oProperties.setProperty(sKey, sValue);
        }
    }

    protected String getBaseUserDataDir() {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        return oMonetConfiguration.getUserDataApplicationsDir();
    }

    protected String getBasePath() {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        return oMonetConfiguration.getPath();
    }

    public String getValue(String sName) {
        if (!this.oProperties.containsKey(sName)) return Strings.EMPTY;
        return this.oProperties.get(sName).toString();
    }

    public String getDomain() {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        return oMonetConfiguration.getDomain();
    }

    public Integer getPort() {
        org.monet.kernel.configuration.Configuration oMonetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        return oMonetConfiguration.getPort();
    }

    public String getApiUrl() {
        return this.getServletUrl() + ".api";
    }

    public Integer getApiPort() {
        return this.getPort();
    }

    public String getTempDir() {
        return this.getUserDataDir() + "/temp";
    }

    public String getLanguagesUrl() {
        return this.getUrl() + "/languages";
    }

    public String getJavascriptUrl() {
        return this.getUrl() + "/javascript";
    }

    public String getStylesUrl() {
        return this.getUrl() + "/styles";
    }

    public abstract String getUrl();

    public abstract String getServletUrl();

    public abstract String getOutOfServiceUrl();

    public abstract String getUserDataDir();

    public abstract String getImagesPath();
}
