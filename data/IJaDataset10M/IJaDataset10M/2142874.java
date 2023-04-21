package org.jmeld.ui.settings;

import org.jmeld.*;
import org.jmeld.ui.*;
import org.jmeld.util.*;
import org.jmeld.settings.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SaveSettingsDialog {

    private JMeldPanel meldPanel;

    private boolean ok;

    public SaveSettingsDialog(JMeldPanel meldPanel) {
        this.meldPanel = meldPanel;
    }

    public void show() {
        JOptionPane pane;
        JDialog dialog;
        pane = new JOptionPane(getSaveSettings(), JOptionPane.WARNING_MESSAGE);
        pane.setOptionType(JOptionPane.YES_NO_OPTION);
        dialog = pane.createDialog(meldPanel, "Save settings");
        dialog.setResizable(true);
        try {
            dialog.show();
            if (ObjectUtil.equals(pane.getValue(), JOptionPane.YES_OPTION)) {
                ok = true;
            }
        } finally {
            dialog.dispose();
        }
    }

    public boolean isOK() {
        return ok;
    }

    public void doSave() {
        JMeldSettings.getInstance().save();
    }

    private JComponent getSaveSettings() {
        return new SaveSettingsPanel();
    }
}
