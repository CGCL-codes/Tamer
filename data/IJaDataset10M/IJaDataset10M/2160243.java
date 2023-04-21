package edu.co.unal.bioing.jnukak3d.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Pinzon Fernandez
 */
public class nkKernel {

    private Map<String, nkPlugin> m_LoadedPlugins;

    private nkDockServer m_DockServer;

    private static nkKernel INSTANCE = null;

    private nkKernel() {
    }

    public nkDockServer getDockServer() {
        if (m_DockServer == null) m_DockServer = new nkDockServer();
        return m_DockServer;
    }

    public void loadPlugin(String fileName, nkPlugin plugin) {
        if (m_LoadedPlugins == null) m_LoadedPlugins = new HashMap<String, nkPlugin>();
        if (m_LoadedPlugins.containsKey(fileName) == false) {
            m_LoadedPlugins.put(fileName, plugin);
            plugin.registerPlugin(this);
        }
    }

    private static synchronized void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new nkKernel();
        }
    }

    public static nkKernel getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
}
