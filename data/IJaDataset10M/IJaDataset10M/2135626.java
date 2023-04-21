package org.openmim.icq2k;

import org.openmim.*;
import org.openmim.infrastructure.scheduler.Scheduler;
import org.openmim.transport_simpletcp.*;

/**
  Contains miscelanneous objects for the plugin instance context.
  <p>
  At the moment, contains a ICQ2KMessagingNetwork instance only.
  <p>
  Additionally, the ResourceManager instance can be got from the
  ICQ2KMessagingNetwork instance.
*/
public final class PluginContext {

    private final ICQ2KMessagingNetwork plugin;

    public PluginContext(ICQ2KMessagingNetwork plugin) {
        if (plugin == null) org.openmim.icq.util.joe.Lang.ASSERT_NOT_NULL(plugin, "plugin");
        this.plugin = plugin;
    }

    public final ICQ2KMessagingNetwork getICQ2KMessagingNetwork() {
        return plugin;
    }

    public final ICQ2KMessagingNetwork getPlug() {
        return plugin;
    }

    public final ResourceManager getResourceManager() {
        return plugin.getResourceManager();
    }

    public final Scheduler getScheduler() {
        return plugin.getScheduler();
    }

    public final AsyncOperationRegistry getAsyncOperationRegistry() {
        return plugin;
    }

    public final GlobalAopLimit getGal() {
        return plugin.gal;
    }

    public final SimpleTcp getSocketRegistry() {
        return plugin.socketRegistry;
    }
}
