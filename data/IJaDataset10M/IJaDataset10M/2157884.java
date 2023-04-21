package org.dijkstromania.controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.dijkstromania.DView;
import org.dijkstromania.view.GridPanel;

/**
 * Kontrolliert MenuBar
 * @author Hong-Son Dang Nguyen, Mayooran Thillainathan, Firas Jradi
 *
 */
public class MenuBarItemController extends AbstractController implements ItemListener {

    public MenuBarItemController(Invoker i, DView v) {
        super(i, v);
    }

    public void itemStateChanged(ItemEvent e) {
        GridPanel grid = view.getTabsPanel().getGridPanel();
        if (grid.gridActivated()) grid.setGrid(false); else grid.setGrid(true);
        grid.repaint();
    }
}
