package com.bemol.kernel.repository;

import com.bemol.kernel.plugin.PluginConfiguration;

/**
 * Internal functions of Repository 
 * 
 * @author Samuel García Martínez
 *
 */
public interface RepositoryInternal extends Repository {

    /**
	 * Adds all persistence classes associated to given plugin
	 * 
	 * @param pluginConfig {@link PluginConfiguration} of given plugin
	 * @throws Exception if any error occurs
	 */
    public void addPlugin(PluginConfiguration pluginConfig) throws RepositoryException;
}
