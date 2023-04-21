package org.dhcc.utils.gui;

import javax.swing.JDialog;
import org.dhcc.DHCCPanel;
import org.dhcc.gui.BasicDHCCPanel;

public class DHCCDialog extends JDialog {

    private static final long serialVersionUID = -138558058737120728L;

    public DHCCDialog() {
        this.setIconImage(IconManager.getInstance(BasicDHCCPanel.class, "/org/dhcc/gui/dhcc_iconset_black_24").getImage("window-icon"));
        this.setLocationRelativeTo(DHCCPanel.getMainFrame());
    }
}
