package org.snova.framework.shell.swing;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.snova.framework.plugin.Plugin;

/**
 *
 */
public interface GUIPlugin extends Plugin {

    public ImageIcon getIcon();

    public JPanel getConfigPanel();
}
