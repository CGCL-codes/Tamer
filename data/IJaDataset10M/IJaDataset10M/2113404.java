package org.one.stone.soup.xapp;

import java.util.Enumeration;
import java.util.Hashtable;

public class XappPluginStore {

    private Hashtable plugins = new Hashtable();

    public XappPluginStore() {
    }

    public void addPlugin(String alias, XappPlugin plugin) {
        plugins.put(alias, plugin);
    }

    public void removePlugin(String alias) {
        plugins.remove(alias);
    }

    public XappPlugin getPlugin(String alias) {
        return (XappPlugin) plugins.get(alias);
    }

    public XappPlugin[] getPlugins() {
        XappPlugin[] pluginArray = new XappPlugin[plugins.size()];
        Enumeration enumeration = plugins.keys();
        int cursor = 0;
        while (enumeration.hasMoreElements()) {
            pluginArray[cursor] = (XappPlugin) plugins.get(enumeration.nextElement());
            cursor++;
        }
        return pluginArray;
    }

    public String[] getPluginNames() {
        String[] pluginArray = new String[plugins.size()];
        Enumeration enumeration = plugins.keys();
        int cursor = 0;
        while (enumeration.hasMoreElements()) {
            pluginArray[cursor] = (String) enumeration.nextElement();
            cursor++;
        }
        return pluginArray;
    }
}
