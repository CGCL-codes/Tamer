package org.pushingpixels.substance.api.tabbed;

import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.SubstanceConstants.TabCloseKind;

/**
 * Callback for registering app-specific behaviour on tab close buttons. This
 * class is part of officially supported API.
 * 
 * @author Kirill Grouchnikov
 */
public interface TabCloseCallback {

    /**
	 * Invoked when the tab area (not close button) is clicked.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabIndex
	 *            Index of the tab under the click.
	 * @param mouseEvent
	 *            Mouse event.
	 * @return Tab close kind.
	 */
    public TabCloseKind onAreaClick(JTabbedPane tabbedPane, int tabIndex, MouseEvent mouseEvent);

    /**
	 * Invoked when the tab close button is clicked.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabIndex
	 *            Index of the tab under the click.
	 * @param mouseEvent
	 *            Mouse event.
	 * @return Tab close kind.
	 */
    public TabCloseKind onCloseButtonClick(JTabbedPane tabbedPane, int tabIndex, MouseEvent mouseEvent);

    /**
	 * Returns the tooltip for the tab area (not close button).
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabIndex
	 *            Index of the tab under the mouse.
	 * @return Tooltip for the tab area.
	 */
    public String getAreaTooltip(JTabbedPane tabbedPane, int tabIndex);

    /**
	 * Returns the tooltip for the tab close button.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabIndex
	 *            Index of the tab under the mouse.
	 * @return Tooltip for the tab close button.
	 */
    public String getCloseButtonTooltip(JTabbedPane tabbedPane, int tabIndex);
}
