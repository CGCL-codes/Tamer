package com.surfacetension.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class STMenuBar extends JMenuBar implements MenuEnumeration {

    protected JMenu fileMenu;

    protected JMenu editMenu;

    public STMenuBar() {
        createMenuAndMenuItems();
        super.add(fileMenu);
        super.add(editMenu);
    }

    protected void createMenuAndMenuItems() {
        createFileMenu();
        createEditMenu();
    }

    private void createFileMenu() {
        fileMenu = new JMenu(new STMenuItem("file"));
        for (final File f : File.values()) {
            fileMenu.add(new JMenuItem(new STMenuItem(f.getValue())));
        }
    }

    private void createEditMenu() {
        editMenu = new JMenu("Edit");
        for (final Edit f : Edit.values()) {
            editMenu.add(new JMenuItem(new STMenuItem(f.getValue())));
        }
    }
}
