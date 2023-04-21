package ntorrent.settings.model;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

public class SettingsPluginListModel extends AbstractListModel {

    private SettingsPluginListItem[] plugins;

    public SettingsPluginListModel(final SettingsExtension[] plugins) {
        this.plugins = new SettingsPluginListItem[plugins.length];
        for (int x = 0; x < plugins.length; x++) {
            final String label = plugins[x].getSettingsDisplayName();
            this.plugins[x] = new SettingsPluginListItem(label, plugins[x]);
        }
    }

    @Override
    public Object getElementAt(int index) {
        return plugins[index];
    }

    @Override
    public int getSize() {
        return plugins.length;
    }
}
