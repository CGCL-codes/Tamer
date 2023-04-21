package com.bemol.kernel.plugin.service;

import java.util.jar.JarFile;
import com.bemol.kernel.plugin.PluginContext;
import com.bemol.kernel.service.Service;
import com.bemol.kernel.service.ServiceException;

/**
 * @author samuelgmartinez
 *
 */
public interface PluginService extends Service {

    public PluginContext loadPlugin(String path) throws ServiceException;

    public PluginContext loadPlugin(JarFile file) throws ServiceException;
}
