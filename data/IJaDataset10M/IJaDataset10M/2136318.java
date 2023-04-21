package com.tdcs.lords.client.dialog;

import com.tdcs.lords.client.Manager;
import com.tdcs.lords.client.display.ManageRecordsPanel;
import java.awt.BorderLayout;

/**
 *
 * @author  david
 */
public class MoveRecordsDialog extends javax.swing.JDialog {

    private ManageRecordsPanel mrp;

    /** Creates new form MoveRecordsDialog */
    public MoveRecordsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setManager(Manager mgr) {
        mrp = new ManageRecordsPanel(mgr);
        getContentPane().add(mrp, BorderLayout.CENTER);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Move/Combine Records");
        pack();
    }
}
