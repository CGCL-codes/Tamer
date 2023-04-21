package fr.itris.glips.svgeditor.io.module;

import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.managers.export.*;

/**
 * the class of the manager used to build and handle the state 
 * of the export menu items of the export menu
 * @author Jordi SUC
 */
public class ExportMenuManager {

    /**
	 * the menu
	 */
    private JMenu menu = new JMenu();

    /**
	 * the array of the menu items
	 */
    private JMenuItem[] menuItems = new JMenuItem[FileExport.formats.length];

    /**
	 * the constructor of the class
	 */
    public ExportMenuManager() {
        createMenuItems();
    }

    /**
	 * creates the menu items
	 */
    protected void createMenuItems() {
        int[] formats = FileExport.formats;
        ActionListener listener = null;
        for (int i = 0; i < formats.length; i++) {
            final int index = i;
            menuItems[i] = new JMenuItem(FileExport.prefixLabels[i]);
            menu.add(menuItems[i]);
            listener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    SVGHandle handle = Editor.getEditor().getHandlesManager().getCurrentHandle();
                    Editor.getEditor().getIOManager().getFileExportManager().export(handle, index, menuItems[index]);
                }
            };
            menuItems[i].addActionListener(listener);
        }
    }

    /**
	 * @return the menu containing the export menu items
	 */
    public JMenu getMenu() {
        return menu;
    }

    /**
	 * handles the state of the items
	 * @param currentHandle the current handle
	 */
    public void handleItemsState(SVGHandle currentHandle) {
        for (int i = 0; i < FileExport.formats.length; i++) {
            menuItems[i].setEnabled(currentHandle != null);
        }
    }
}
