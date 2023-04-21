package com.ractoc.pffj.registry.api;

import com.ractoc.pffj.api.BasePluginMessage;
import com.ractoc.pffj.configuration.Server;
import com.ractoc.pffj.core.Version;

/**
 * Message used to register a plugin with the registry.
 * @author ractoc
 * @version 2.0
 */
public class RegisterPluginMessage extends BasePluginMessage {

    private static final long serialVersionUID = Version.MAJOR_VERSION;

    private Server server;

    private String pluginId;

    /**
     * Constructor.
     */
    public RegisterPluginMessage() {
    }

    /**
     * Set the server address this Plugin can be accessed on.
     * @param serverParam the server to set
     */
    public final void setServer(final Server serverParam) {
        this.server = serverParam;
    }

    /**
     * Get the server address this Plugin can be accessed on.
     * @return the server
     */
    public final Server getServer() {
        return server;
    }

    /**
     * The plugin to register.
     * @param pluginIdParam the pluginId to set
     */
    public final void setPluginId(final String pluginIdParam) {
        this.pluginId = pluginIdParam;
    }

    /**
     * The plugin to register.
     * @return the pluginId
     */
    public final String getPluginId() {
        return pluginId;
    }
}
