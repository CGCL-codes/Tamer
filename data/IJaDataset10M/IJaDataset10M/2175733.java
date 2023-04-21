package de.shandschuh.jaolt.gui.listener.maintabbedpane.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.shandschuh.jaolt.gui.maintabbedpane.ListJPanel;

public class SelectAllObjectsJMenuItemListener implements ActionListener {

    private ListJPanel<?> listJPanel;

    public SelectAllObjectsJMenuItemListener(ListJPanel<?> listJPanel) {
        this.listJPanel = listJPanel;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        listJPanel.selectAllObjects();
    }
}
