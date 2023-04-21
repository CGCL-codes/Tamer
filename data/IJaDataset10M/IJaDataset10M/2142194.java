package com.go.trove.util.plugin;

import com.go.trove.log.Log;
import com.go.trove.util.ConfigSupport;
import com.go.trove.util.PropertyMap;

/**
 * A support class the provides a default implementation for the 
 * PluginFactoryConfig interface.
 *
 * @author Scott Jappinen
 * @version <!--$$Revision: 3 $-->-<!--$$JustDate:--> 01/03/23 <!-- $-->
 */
public class PluginFactoryConfigSupport extends ConfigSupport implements PluginFactoryConfig {

    private PluginContext mPluginContext;

    public PluginFactoryConfigSupport(PropertyMap properties, Log log, PluginContext context) {
        super(properties, log);
        mPluginContext = context;
    }

    /**
     * Returns a reference to the PluginContext.
     * 
     * @returns PluginContext the PluginContext object.
     */
    public PluginContext getPluginContext() {
        return mPluginContext;
    }
}
