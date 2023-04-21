package com.novasurv.turtle.frontend.swing.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.novasurv.turtle.frontend.swing.data.SwingDataManager;
import com.novasurv.turtle.frontend.swing.prefs.PreferencesDialog;
import com.novasurv.turtle.frontend.swing.util.UiUtils;

/** @author Jason Dobies */
public class DisplayPreferencesActionListener implements ActionListener {

    private JFrame owner;

    private SwingDataManager dataManager;

    public DisplayPreferencesActionListener(JFrame owner, SwingDataManager dataManager) {
        this.owner = owner;
        this.dataManager = dataManager;
    }

    public void actionPerformed(ActionEvent e) {
        PreferencesDialog dialog = new PreferencesDialog(owner, dataManager);
        UiUtils.centerContainerOnFrame(dialog, owner);
        dialog.setVisible(true);
    }
}
