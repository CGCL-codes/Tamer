package com.ehs.pm.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.ehs.common.gui.HPanel;

/**
 * 
 * @author swaram
 */
public class PnlUser extends HPanel {

    private static final long serialVersionUID = 7660939183313957956L;

    private HPanel pnlMain;

    private HPanel pnlButtons;

    private JLabel lblUserName;

    private JTextField txfUserName;

    private JLabel lblPassword;

    private JPasswordField passwordField;

    private JLabel lblFirstName;

    private JTextField txfFirstName;

    private JLabel lblLastName;

    private JTextField txfLastName;

    private JLabel lblActive;

    private JCheckBox chbActive;

    private JLabel lblRole;

    private JComboBox cmbRole;

    private JButton btnOK;

    private JButton btnCancel;

    private JInternalFrame parent;

    private HPanel pnlLabels;

    private HPanel pnlFields;

    public PnlUser() {
        try {
            initComponents();
        } catch (Exception ex) {
            Logger.getLogger(PnlUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PnlUser(JInternalFrame parent) {
        this.parent = parent;
        try {
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() throws Exception {
        pnlMain = new HPanel();
        pnlButtons = new HPanel();
        pnlLabels = new HPanel();
        pnlFields = new HPanel();
        lblUserName = new JLabel();
        txfUserName = new JTextField();
        lblPassword = new JLabel();
        passwordField = new JPasswordField();
        lblFirstName = new JLabel();
        txfFirstName = new JTextField();
        lblLastName = new JLabel();
        txfLastName = new JTextField();
        lblActive = new JLabel();
        chbActive = new JCheckBox();
        lblRole = new JLabel();
        cmbRole = new JComboBox();
        btnOK = new JButton();
        btnCancel = new JButton();
        this.setLayout(new BorderLayout());
        this.add(pnlMain, BorderLayout.CENTER);
        pnlMain.setLayout(new BorderLayout());
        pnlMain.add(pnlLabels, BorderLayout.WEST);
        pnlMain.add(pnlFields, BorderLayout.CENTER);
        pnlMain.setBorder(BorderFactory.createTitledBorder("Enter Information:"));
        pnlLabels.setLayout(new GridLayout(6, 1, 5, 5));
        pnlFields.setLayout(new GridLayout(6, 1, 5, 5));
        lblUserName.setText("User Name: ");
        pnlLabels.add(lblUserName);
        pnlFields.add(txfUserName);
        lblPassword.setText("Password: ");
        pnlLabels.add(lblPassword);
        pnlFields.add(passwordField);
        lblFirstName.setText("First Name: ");
        pnlLabels.add(lblFirstName);
        pnlFields.add(txfFirstName);
        lblLastName.setText("Last Name: ");
        pnlLabels.add(lblLastName);
        pnlFields.add(txfLastName);
        lblActive.setText("Active: ");
        pnlLabels.add(lblActive);
        pnlFields.add(chbActive);
        lblRole.setText("Role: ");
        pnlLabels.add(lblRole);
        pnlFields.add(cmbRole);
        this.add(pnlButtons, BorderLayout.SOUTH);
        pnlButtons.setBorder(BorderFactory.createEtchedBorder());
        pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.add(btnOK);
        btnOK.setText("OK");
        pnlButtons.add(btnCancel);
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeAction();
            }
        });
    }

    private void closeAction() {
        if (parent != null) {
            parent.dispose();
        }
    }
}
