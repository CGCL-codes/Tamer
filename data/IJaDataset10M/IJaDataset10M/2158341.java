package org.nightlabs.jfire.servermanager.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The server core J2EE configuration
 * @author Marco Schulze
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class J2eeCf extends JFireServerConfigPart implements Serializable {

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    private int initOrganisationOnStartupThreadCount = 0;

    private String j2eeDeployBaseDirectory;

    private String serverConfigurator;

    private Properties serverConfiguratorSettings;

    private List<String> availableServerConfigurators;

    @Override
    public void init() {
        if (j2eeDeployBaseDirectory == null) setJ2eeDeployBaseDirectory("../server/default/deploy/JFire.last/");
        if (serverConfigurator == null) setServerConfigurator("org.nightlabs.jfire.jboss.serverconfigurator.ServerConfiguratorJBossDerby");
        if (availableServerConfigurators == null) {
            availableServerConfigurators = new ArrayList<String>();
            availableServerConfigurators.add("org.nightlabs.jfire.jboss.serverconfigurator.ServerConfiguratorJBoss");
            availableServerConfigurators.add("org.nightlabs.jfire.jboss.serverconfigurator.ServerConfiguratorJBossDerby");
            availableServerConfigurators.add("org.nightlabs.jfire.jboss.serverconfigurator.ServerConfiguratorJBossMySQL");
        }
        if (serverConfiguratorSettings == null) serverConfiguratorSettings = new Properties();
        if (initOrganisationOnStartupThreadCount < 1) setInitOrganisationOnStartupThreadCount(4);
    }

    /**
	 * Get the availableServerConfigurators.
	 * @return the availableServerConfigurators
	 */
    public List<String> getAvailableServerConfigurators() {
        return availableServerConfigurators;
    }

    /**
	 * Set the availableServerConfigurators.
	 * @param availableServerConfigurators the availableServerConfigurators to set
	 */
    public void setAvailableServerConfigurators(List<String> availableServerConfigurators) {
        this.availableServerConfigurators = availableServerConfigurators;
        setChanged();
    }

    /**
	 * Get the j2eeDeployBaseDirectory.
	 * @return the j2eeDeployBaseDirectory
	 */
    public String getJ2eeDeployBaseDirectory() {
        return j2eeDeployBaseDirectory;
    }

    /**
	 * Set the j2eeDeployBaseDirectory.
	 * @param deployBaseDirectory the j2eeDeployBaseDirectory to set
	 */
    public void setJ2eeDeployBaseDirectory(String deployBaseDirectory) {
        j2eeDeployBaseDirectory = deployBaseDirectory;
        setChanged();
    }

    /**
	 * Get the serverConfigurator.
	 * @return the serverConfigurator
	 */
    public String getServerConfigurator() {
        return serverConfigurator;
    }

    /**
	 * Set the serverConfigurator.
	 * @param serverConfigurator the serverConfigurator to set
	 */
    public void setServerConfigurator(String serverConfigurator) {
        this.serverConfigurator = serverConfigurator;
        setChanged();
    }

    /**
	 * Get the serverConfiguratorSettings.
	 * @return the serverConfiguratorSettings
	 */
    public Properties getServerConfiguratorSettings() {
        return serverConfiguratorSettings;
    }

    /**
	 * Set the serverConfiguratorSettings.
	 * @param serverConfiguratorSettings the serverConfiguratorSettings to set
	 */
    public void setServerConfiguratorSettings(Properties serverConfiguratorSettings) {
        this.serverConfiguratorSettings = serverConfiguratorSettings;
        setChanged();
    }

    /**
	 * During server startup, all datastore-inits of all organisations are executed. If there are many organisations, this can take a while.
	 * In a multi-CPU-environment it makes sense to process multiple initialisations at the same time. Therefore it can be configured
	 * by this property how many threads shall perform the initialisation simultaneously. The default value is 4.
	 *
	 * @return the number of threads that are spawned during server-startup for doing the initialisation of multiple organisations simultaneously.
	 */
    public int getInitOrganisationOnStartupThreadCount() {
        return initOrganisationOnStartupThreadCount;
    }

    /**
	 * 
	 * @param initOrganisationOnStartupThreadCount a value >= 1 specifying how many threads are
	 */
    public void setInitOrganisationOnStartupThreadCount(int initOrganisationThreadCount) {
        if (initOrganisationThreadCount < 1) throw new IllegalArgumentException("initOrganisationOnStartupThreadCount < 1 !!!");
        this.initOrganisationOnStartupThreadCount = initOrganisationThreadCount;
        setChanged();
    }
}
