package com.amazonaws.eclipse.core;

import java.util.Dictionary;
import org.eclipse.core.runtime.Plugin;

/**
 * Utilities for working with AWS clients, such as creating consistent user
 * agent strings for different plugins.
 */
public class AwsClientUtils {

    /**
     * Forms a user-agent string for clients to send when making service calls,
     * indicating the name and version of this client.
     * 
     * @param pluginName
     *            The name of the plugin to use in the user agent string.
     * @param plugin
     *            The plugin from which to pull version information.
     * 
     * @return A user-agent string indicating what client and version are
     *         accessing AWS.
     */
    public String formUserAgentString(String pluginName, Plugin plugin) {
        String version = "???";
        if (plugin != null) {
            Dictionary headers = plugin.getBundle().getHeaders();
            version = (String) headers.get("Bundle-Version");
        }
        String userAgentValue = pluginName + "/" + version;
        return userAgentValue;
    }
}
