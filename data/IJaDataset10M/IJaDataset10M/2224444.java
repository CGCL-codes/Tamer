package org.rapla.framework;

import org.apache.avalon.framework.configuration.Configuration;

public interface PluginDescriptor {

    public static final String PLUGIN_LIST = "plugin-list";

    public void provideServices(Container container, Configuration configuration);

    public Object getPluginMetaInfos(String key);
}
