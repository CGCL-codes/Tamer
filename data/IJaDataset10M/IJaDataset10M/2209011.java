package com.endlessloopsoftware.ego.client;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WorkingDialog extends JDialog {

    private JPanel panel1 = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    private JLabel jLabel1 = new JLabel();

    public WorkingDialog(JFrame frame, String title, boolean modal) {
        super(frame, title, modal);
        jbInit();
        pack();
    }

    public WorkingDialog() {
        this(null, "", false);
    }

    private void jbInit() {
        panel1.setLayout(borderLayout1);
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel1.setToolTipText("");
        jLabel1.setText("Working...");
        getContentPane().add(panel1);
        panel1.add(jLabel1, BorderLayout.CENTER);
    }
}
