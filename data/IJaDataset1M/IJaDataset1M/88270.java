package org.eu_acgt.taxy.plugin.acgt_services;

/**
 * Describes the plug-in <code>AcgtRepoPlugin</code>.
 * 
 * @see org.eu_acgt.taxy.plugin.acgt_services.AcgtRepoPlugin
 */
public class AcgtRepoPluginDescription implements uk.ac.ebi.taxy.PluginDescription {

    private String _pluginClassName = AcgtRepoPlugin.class.getName();

    public String getPluginClassName() {
        return _pluginClassName;
    }

    public String getName() {
        return "ACGT Biomedical Services Catalogue";
    }

    public String getDescription() {
        return "DESCRIPTION: \n" + "    Provides access to the ACGT Biomedical Services Catalogue.\n\n" + "Plug-in class:\n" + "    " + _pluginClassName;
    }
}
