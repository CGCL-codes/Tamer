package org.sf.jspread.gui.adapter;

import java.awt.event.*;
import org.sf.jspread.gui.*;

public class NewSpreadAdapter implements ActionListener {

    SpreadGUI adaptee;

    public NewSpreadAdapter(SpreadGUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.newSpreadActionPerformed(e);
    }
}
