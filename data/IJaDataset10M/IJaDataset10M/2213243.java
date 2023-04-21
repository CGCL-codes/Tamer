package org.opendicomviewer.plugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PluginManager {

    private static PluginManager instance;

    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    private HashMap<String, Plugin> pluginMap;

    private HashMap<String, Plugin> pluginIdMap;

    public PluginManager() {
        pluginMap = new HashMap<String, Plugin>();
        pluginIdMap = new HashMap<String, Plugin>();
    }

    public void register(String cuid, Plugin plugin) throws PluginException {
        Plugin old = pluginMap.get(cuid);
        if (old != null) {
            old.uninstall(cuid);
        }
        pluginMap.put(cuid, plugin);
        pluginIdMap.put(plugin.getId(), plugin);
        plugin.install(cuid);
    }

    public void register(String[] cuids, Plugin plugin) throws PluginException {
        for (String cuid : cuids) {
            register(cuid, plugin);
        }
    }

    public void unregister(String cuid) throws PluginException {
        Plugin plugin = pluginMap.get(cuid);
        if (plugin != null) {
            plugin.uninstall(cuid);
            pluginMap.remove(cuid);
        }
    }

    public Plugin getPlugin(String cuid) {
        return pluginMap.get(cuid);
    }

    public Plugin getPluginById(String id) {
        return pluginIdMap.get(id);
    }

    public List<Plugin> getPlugins() {
        return new LinkedList<Plugin>(pluginIdMap.values());
    }
}
