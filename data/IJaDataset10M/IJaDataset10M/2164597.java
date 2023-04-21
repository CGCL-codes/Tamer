package org.pushingpixels.substance.api.tabbed;

import java.awt.Component;
import javax.swing.JTabbedPane;

/**
 * Listener on tab closing. This class is part of officially supported API.
 * 
 * @author Kirill Grouchnikov
 */
public interface TabCloseListener extends BaseTabCloseListener {

    /**
	 * Called when a tab is about to be closed.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabComponent
	 *            Tab component to be closed.
	 */
    public void tabClosing(JTabbedPane tabbedPane, Component tabComponent);

    /**
	 * Called when a tab is closed.
	 * 
	 * @param tabbedPane
	 *            Tabbed pane.
	 * @param tabComponent
	 *            Tab component closed.
	 */
    public void tabClosed(JTabbedPane tabbedPane, Component tabComponent);
}
