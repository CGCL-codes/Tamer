package xbrowser.plugin.event;

import xbrowser.util.XPluginObject;

public interface XPluginListener {

    public void pluginAdded(XPluginObject plugin);

    public void pluginRemoved(XPluginObject plugin);
}
