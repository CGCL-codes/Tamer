package org.eclipse.update.internal.core;

import org.eclipse.update.core.model.*;
import org.eclipse.update.internal.model.*;

/**
 * 
 */
public class BaseSiteLocalFactory {

    public InstallConfigurationModel createInstallConfigurationModel() {
        return new InstallConfiguration();
    }

    public ConfigurationActivityModel createConfigurationActivityModel() {
        return new ConfigurationActivity();
    }

    public ConfiguredSiteModel createConfigurationSiteModel() {
        return new ConfiguredSite();
    }

    public ConfigurationPolicyModel createConfigurationPolicyModel() {
        return new ConfigurationPolicy();
    }

    /**
	 * 
	 */
    public ConfiguredSiteModel createConfigurationSiteModel(SiteModel site, int policy) {
        ConfiguredSiteModel configSite = this.createConfigurationSiteModel();
        configSite.setSiteModel(site);
        ConfigurationPolicyModel policyModel = this.createConfigurationPolicyModel();
        policyModel.setPolicy(policy);
        configSite.setConfigurationPolicyModel(policyModel);
        ((ConfigurationPolicy) policyModel).setConfiguredSiteModel(configSite);
        return configSite;
    }
}
