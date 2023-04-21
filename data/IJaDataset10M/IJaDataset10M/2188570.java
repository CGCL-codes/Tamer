package com.qasystems.qstudio.java.gui;

import com.qasystems.qstudio.java.gui.observation.TableControl;
import com.qasystems.qstudio.java.gui.observation.ViewSelectorPanel;
import java.util.Vector;

/**
 * This class ensures that the menu entries are available in every
 * context. Subclasses should implement the user interface components
 * and must create an association between the user interface object
 * and the underlying menu entries.
 */
public abstract class SelectorMenu {

    private static Vector[] menuEntries = null;

    /**
   * Default constructor
   */
    public SelectorMenu() {
        super();
        final ViewSelectorPanel panel = new ViewSelectorPanel(TableControl.getViewSelection());
        menuEntries = panel.getSelectorMenuEntries();
    }

    /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
    public Vector[] getEntries() {
        return (menuEntries);
    }

    /**
   * Create the menu items (interface).
   */
    public abstract void createGuiMenuItems();
}
