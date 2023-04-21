package de.hpi.eworld.networkview.events;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import de.hpi.eworld.networkview.GraphController;
import de.hpi.eworld.networkview.extensionpoints.EventHandlerProviderInterface;

/**
 * The implemenation class of the Interface EventHandlerProviderInterface,
 * which is an extension point of the network view plugin.
 */
public class EventHandlerProvider implements EventHandlerProviderInterface {

    /**
	 * Initalizes the plugin.
	 * @param networkView the network view displaying the loaded map
	 * @author Jonas Truemper
	 */
    public void init(GraphController networkView) {
        PluginManager pManager = PluginManager.lookup(this);
        EventsPlugin plugin = null;
        try {
            plugin = (EventsPlugin) pManager.getPlugin(EventsPlugin.PLUGIN_ID);
        } catch (PluginLifecycleException e) {
            Logger.getLogger(this.getClass()).error("Error while initializing EventDock event handler", e);
        }
        plugin.initializeEventHandling(networkView);
    }
}
