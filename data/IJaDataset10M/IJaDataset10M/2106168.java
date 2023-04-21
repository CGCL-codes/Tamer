package org.signserver.adminws;

import java.util.Properties;
import org.signserver.common.GlobalConfiguration;

/**
 * Class holding the global configuration.
 *
 * @see GlobalConfiguration
 * @author Markus Kilås
 * @version $Id: WSGlobalConfiguration.java 1831 2011-08-10 12:27:36Z netmackan $
 */
public class WSGlobalConfiguration {

    /** serialVersionUID for this class. */
    private static final long serialVersionUID = 1;

    private Properties config;

    private String state;

    private String appVersion;

    private boolean clusterClassLoaderEnabled;

    private boolean useClassVersions;

    private boolean requireSigning;

    public WSGlobalConfiguration() {
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public boolean isClusterClassLoaderEnabled() {
        return clusterClassLoaderEnabled;
    }

    public void setClusterClassLoaderEnabled(boolean clusterClassLoaderEnabled) {
        this.clusterClassLoaderEnabled = clusterClassLoaderEnabled;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public boolean isRequireSigning() {
        return requireSigning;
    }

    public void setRequireSigning(boolean requireSigning) {
        this.requireSigning = requireSigning;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isUseClassVersions() {
        return useClassVersions;
    }

    public void setUseClassVersions(boolean useClassVersions) {
        this.useClassVersions = useClassVersions;
    }
}
