package org.jalgo.module.ebnf;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class rootPaneListener implements ComponentListener {

    private MainController mainController;

    public rootPaneListener(MainController mainController) {
        this.mainController = mainController;
    }

    public void componentResized(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
    }

    public void componentShown(ComponentEvent arg0) {
        if (mainController.getCustomMenuListBackup() != null) {
            for (JMenu item : mainController.getCustomMenuListBackup()) {
                mainController.addMenu(item);
            }
        }
    }

    public void componentHidden(ComponentEvent arg0) {
        mainController.removeCustomMenu();
    }
}
