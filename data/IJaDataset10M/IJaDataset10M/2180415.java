package com.jgraph.example;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * A simple menu bar
 */
public class GraphEdXMenuBar extends JMenuBar {

    /**
	 * JGraph Factory instance for random new graphs
	 */
    protected JGraphGraphFactory graphFactory = null;

    public GraphEdXMenuBar(final GraphEdX app, JGraphGraphFactory factory) {
        graphFactory = factory;
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new AbstractAction("Load graph") {

            public void actionPerformed(ActionEvent e) {
                app.deserializeGraph();
            }
        }));
        fileMenu.add(new JMenuItem(new AbstractAction("Save graph") {

            public void actionPerformed(ActionEvent e) {
                app.serializeGraph();
            }
        }));
        fileMenu.add(new JMenuItem(new AbstractAction("Set background image") {

            public void actionPerformed(ActionEvent e) {
                app.setBackgroundImage();
            }
        }));
        add(fileMenu);
    }

    /**
	 * helper for creating radio button menu items
	 * 
	 * @param group
	 *            the <code>ButtonGroup</code> of the item
	 * @param action
	 *            the <code>Action</code> associated with the item
	 * @return the menu item
	 */
    public JRadioButtonMenuItem createRadioMenuItem(ButtonGroup group, Action action) {
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift " + String.valueOf(action.getValue("shortcut")).substring(0, 1).toUpperCase()));
        group.add(menuItem);
        return menuItem;
    }
}
