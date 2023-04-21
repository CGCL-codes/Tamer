package com.google.code.sagetvaddons.sagealert.server;

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.PluginAPI;
import java.util.Map;
import org.apache.log4j.Logger;
import sage.SageTVEventListener;
import com.google.code.sagetvaddons.sagealert.server.events.PluginStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.PluginStoppedEvent;

/**
 * @author dbattams
 *
 */
public class PluginEventsListener implements SageTVEventListener {

    private static final Logger LOG = Logger.getLogger(PluginEventsListener.class);

    private static final PluginEventsListener INSTANCE = new PluginEventsListener();

    public static final PluginEventsListener get() {
        return INSTANCE;
    }

    /**
	 * 
	 */
    private PluginEventsListener() {
    }

    @SuppressWarnings("unchecked")
    public void sageEvent(String arg0, Map arg1) {
        LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
        PluginAPI.Plugin p = API.apiNullUI.pluginAPI.Wrap(arg1.get("Plugin"));
        if (CoreEventsManager.PLUGIN_STARTED.equals(arg0)) SageAlertEventHandlerManager.get().fire(new PluginStartedEvent(p, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLUGIN_STARTED))); else if (CoreEventsManager.PLUGIN_STOPPED.equals(arg0)) SageAlertEventHandlerManager.get().fire(new PluginStoppedEvent(p, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLUGIN_STOPPED))); else LOG.error("Unsupported event received! [" + arg0 + "]");
    }
}
