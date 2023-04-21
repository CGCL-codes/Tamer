package com.go.trove.util.plugin;

import com.go.trove.util.PropertyMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * This class is responsible for creating plugins based from a 
 * configuration block.
 *
 * @author Scott Jappinen, Travis Greer
 * @version <!--$$Revision: 3 $-->-<!--$$JustDate:-->  2/26/02 <!-- $-->
 */
public class PluginFactory {

    private static final String cClassKey = "class";

    private static final String cInitKey = "init";

    private static final String cPluginsKey = "plugins";

    public static final Plugin createPlugin(String name, PluginFactoryConfig config) throws PluginFactoryException {
        Plugin result;
        String className = config.getProperties().getString(cClassKey);
        PropertyMap props = config.getProperties().subMap(cInitKey);
        try {
            Class clazz = Class.forName(className);
            result = (Plugin) clazz.newInstance();
            PluginConfig pluginConfig = new PluginConfigSupport(props, config.getLog(), config.getPluginContext(), name);
            result.init(pluginConfig);
        } catch (PluginException e) {
            throw new PluginFactoryException(e);
        } catch (ClassNotFoundException e) {
            throw new PluginFactoryException(e);
        } catch (InstantiationException e) {
            throw new PluginFactoryException(e);
        } catch (IllegalAccessException e) {
            throw new PluginFactoryException(e);
        }
        return result;
    }

    public static final Plugin[] createPlugins(PluginFactoryConfig config) throws PluginFactoryException {
        Plugin[] result;
        Map plugins;
        PropertyMap properties = config.getProperties().subMap(cPluginsKey);
        Set keySet = properties.subMapKeySet();
        plugins = new HashMap(keySet.size());
        Iterator iterator = keySet.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            String name = (String) iterator.next();
            PropertyMap initProps = properties.subMap(name);
            PluginFactoryConfig conf = new PluginFactoryConfigSupport(initProps, config.getLog(), config.getPluginContext());
            plugins.put(name, createPlugin(name, conf));
        }
        Map registeredPlugins = config.getPluginContext().getPlugins();
        keySet = registeredPlugins.keySet();
        iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            if (!plugins.containsKey(name)) {
                plugins.put(name, registeredPlugins.get(name));
            }
        }
        result = new Plugin[plugins.size()];
        result = (Plugin[]) plugins.values().toArray(result);
        return result;
    }
}
