package net.sourceforge.cruisecontrol.core;

public class BasicPluginProvider implements PluginProvider {

    private Class pluginClass;

    private String pluginName;

    public BasicPluginProvider() {
    }

    public BasicPluginProvider(String name, Class clazz) {
        this.pluginName = name;
        this.pluginClass = clazz;
    }

    public Class getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(Class pluginClass) {
        this.pluginClass = pluginClass;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
}
