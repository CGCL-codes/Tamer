package org.dbreplicator.repconsole;

import javax.swing.*;

public class RCellEditor extends DefaultCellEditor {

    public RCellEditor(JTextField tf) {
        super(tf);
    }

    public boolean isCellEditable(java.util.EventObject anEvent) {
        return false;
    }
}
