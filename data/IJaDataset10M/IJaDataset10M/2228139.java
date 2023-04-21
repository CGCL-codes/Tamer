package mctplugin;

/**
 * Listens for plugin (add/remove) events.
 * @author m.weber
 */
public interface PluginListener {

    /** plugin was added */
    public void pluginAdded(Plugin plugin);

    /** plugin was removed */
    public void pluginRemoved(Plugin plugin);
}
