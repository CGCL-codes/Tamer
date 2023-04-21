package com.ideo.sweetdevria.config;

import java.util.Map;
import com.ideo.sweetdevria.config.elements.BuilderConfig;
import com.ideo.sweetdevria.config.elements.DrawerConfig;
import com.ideo.sweetdevria.config.elements.MessageResourcesConfig;
import com.ideo.sweetdevria.config.elements.PlugInConfig;

/**
 * RIA configuration.
 */
public interface IRIAConfig {

    public abstract BuilderConfig findBuilderConfig(String s);

    public abstract DrawerConfig findDrawerConfig(String drawerId);

    public abstract DrawerConfig[] findDrawerConfigs();

    public abstract MessageResourcesConfig findMessageResourcesConfig(String s);

    public abstract MessageResourcesConfig[] findMessageResourcesConfigs();

    public abstract void freeze();

    public abstract PlugInConfig[] findPlugInConfigs();

    public abstract Map getProperties();

    public abstract String getProperty(String key);
}
