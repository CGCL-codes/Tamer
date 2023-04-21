package com.qasystems.qstudio.java.gui.dialog;

import com.qasystems.international.MessageResource;
import com.qasystems.qstudio.java.classloader.Trier;
import com.qasystems.swing.ButtonDialog;
import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.JComboBox;

/**
 * This class implements a modal dialog for setting the debug level.
 */
public class DebugLevelDialog extends ButtonDialog {

    public static final int OK = 0;

    public static final int CANCEL = 1;

    private JComboBox BOX = new JComboBox();

    /**
   * Default constructor
   */
    public DebugLevelDialog(Frame parent) {
        super(parent);
        init();
    }

    /**
   * Default constructor
   */
    public DebugLevelDialog(Dialog parent) {
        super(parent);
        init();
    }

    private void init() {
        final MessageResource resources = MessageResource.getClientInstance();
        setTitle(resources.getString("WINDOWTITLE_025"));
        setButtons(new String[] { resources.getString("BUTTON_005"), resources.getString("BUTTON_001") });
        BOX.addItem(resources.getString("COMBOBOX_004"));
        BOX.addItem(resources.getString("COMBOBOX_005"));
        BOX.addItem(resources.getString("COMBOBOX_006"));
        BOX.addItem(resources.getString("COMBOBOX_007"));
        BOX.setSelectedIndex(Trier.getDefaultDebugLevel());
        setContent(BOX);
        pack();
    }

    protected void onActionPerformed(int action) {
        if (action == CANCEL) {
            dispose();
        } else if (action == OK) {
            Trier.setDefaultDebugLevel(BOX.getSelectedIndex());
            dispose();
        } else {
        }
    }
}
