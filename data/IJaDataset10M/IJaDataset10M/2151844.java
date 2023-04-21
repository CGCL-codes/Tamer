package org.sf.jspread.gui.adapter;

import java.awt.event.*;
import org.sf.jspread.gui.*;

public class HelpSpreadAdapter implements ActionListener {

    SpreadGUI adaptee;

    public HelpSpreadAdapter(SpreadGUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuHelpAboutActionPerformed(e);
    }
}
