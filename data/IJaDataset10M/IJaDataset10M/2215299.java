package org.in4ama.editor.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** Class for storing all Inforama Studio plugins */
public class PluginMgr implements ApplicationContextAware {

    private final Map<String, InforamaPlugin> plugins = new HashMap<String, InforamaPlugin>();

    /** Retrieves and stores all plugins */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Map<?, ?> pluginMap = context.getBeansOfType(InforamaPlugin.class);
        for (Object p : pluginMap.values()) {
            InforamaPlugin plugin = (InforamaPlugin) p;
            plugins.put(plugin.getName(), plugin);
        }
    }

    /** Returns the registered plugins */
    public Collection<InforamaPlugin> getPlugins() {
        return plugins.values();
    }

    /** Returns the plugin by name, null if plugin not found */
    public InforamaPlugin getPlugin(String name) {
        return plugins.get(name);
    }
}
