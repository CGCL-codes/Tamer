package de.miethxml.hawron.gui.context.viewer;

import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 */
public interface ViewerManager {

    public abstract JComponent getButtonPanel();

    public abstract JMenu getViewerMenu();

    public abstract void setCheckSupportedExtensions(boolean b);
}
