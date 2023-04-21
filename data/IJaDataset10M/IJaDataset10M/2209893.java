package de.hpi.eworld.simulationstatistic;

import org.apache.log4j.Logger;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import de.hpi.eworld.networkview.GraphController;
import de.hpi.eworld.networkview.extensionpoints.EventHandlerProviderInterface;

/**
 * The implementing class for the interface EventHanhdlerProviderInterface,
 * which is an extension point of the network view plugin.
 */
public class EventHandlerProvider implements EventHandlerProviderInterface {

    public void init(GraphController networkView) {
        PluginManager pManager = PluginManager.lookup(this);
        SimulationStatisticPlugin plugin = null;
        try {
            plugin = (SimulationStatisticPlugin) pManager.getPlugin(SimulationStatisticPlugin.PLUGIN_ID);
        } catch (PluginLifecycleException e) {
            Logger log = Logger.getLogger(this.getClass());
            log.error("Error while initializing visualizer event handler", e);
        }
        plugin.initializeEventHandler(networkView);
    }
}
