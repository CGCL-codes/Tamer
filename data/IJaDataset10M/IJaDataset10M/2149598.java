package org.butu.core.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class BImages implements Images {

    private AbstractUIPlugin plugin;

    private String pluginId;

    private String pathPrefix;

    public BImages(AbstractUIPlugin plugin, String pluginId) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.pathPrefix = "";
    }

    public BImages(AbstractUIPlugin plugin, String pluginId, String pathPrefix) {
        this.plugin = plugin;
        this.pluginId = pluginId;
        this.pathPrefix = pathPrefix;
    }

    public ImageDescriptor getDescriptor(String key) {
        return plugin.getImageRegistry().getDescriptor(key);
    }

    public Image getImage(String key) {
        return plugin.getImageRegistry().get(key);
    }

    /**
     * �������� ����������� � ������ ����������� �������.
     * @param key ����
     * @param path ���� ������ ������� � �����������
     */
    public void putDescription(String key, String path) {
        plugin.getImageRegistry().put(key, AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, pathPrefix + path));
    }
}
