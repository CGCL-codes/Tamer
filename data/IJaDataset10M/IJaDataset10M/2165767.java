package com.daffodilwoods.mail.frame;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import com.daffodilwoods.mail.tools.ContactBO;
import com.daffodilwoods.mail.tools.ContactInfo;
import java.sql.*;
import com.daffodilwoods.mail.gui.Utility;
import java.util.*;
import java.awt.*;

public class AddressBookFrame extends JDialog {

    EditorFrame editorFrame;

    JTextField textField;

    JButton toButton, CcButton, BccButton, newContact, OkButton, cancelButton;

    JTextArea toArea, ccArea, bccArea;

    JScrollPane toScrollpane, ccScrollpane, bccScrollpane, tableScrollPane;

    JTable addressTable;

    DefaultTableModel tableModel = new DefaultTableModel();

    public AddressBookFrame(EditorFrame editorFrame) {
        super(editorFrame, "Address Book");
        this.editorFrame = editorFrame;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel label = new JLabel("Type name or select from list:");
        label.setBounds(16, 10, 300, 23);
        textField = new JTextField();
        textField.setEnabled(false);
        textField.setBounds(16, 35, 200, 23);
        textField.addKeyListener(new Target(this));
        toButton = new JButton("To ->");
        toButton.setBounds(200, 70, 70, 23);
        toButton.addActionListener(new Target(this));
        toArea = new JTextArea();
        toArea.setText(editorFrame.getToAddress());
        toScrollpane = new JScrollPane(toArea);
        toScrollpane.setBounds(280, 70, 200, 60);
        CcButton = new JButton("Cc ->");
        CcButton.setBounds(200, 140, 70, 23);
        CcButton.addActionListener(new Target(this));
        ccArea = new JTextArea();
        ccArea.setText(editorFrame.getCcAddress());
        ccScrollpane = new JScrollPane(ccArea);
        ccScrollpane.setBounds(280, 140, 200, 60);
        BccButton = new JButton("Bcc ->");
        BccButton.setBounds(200, 210, 70, 23);
        BccButton.addActionListener(new Target(this));
        bccArea = new JTextArea();
        bccArea.setText(editorFrame.getBccAddress());
        bccScrollpane = new JScrollPane(bccArea);
        bccScrollpane.setBounds(280, 210, 200, 60);
        OkButton = new JButton("OK");
        OkButton.setBounds(150, 330, 85, 23);
        OkButton.addActionListener(new Target(this));
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(245, 330, 85, 23);
        cancelButton.addActionListener(new Target(this));
        newContact = new JButton("New Contact");
        newContact.setBounds(16, 280, 120, 23);
        newContact.addActionListener(new Target(this));
        populateTableModel();
        addressTable = new JTable(tableModel);
        addressTable.setDefaultRenderer(JLabel.class, new DefaultTableCellRenderer());
        addressTable.setRowSelectionAllowed(true);
        tableScrollPane = new JScrollPane(addressTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setBounds(16, 70, 170, 200);
        panel.add(toButton);
        panel.add(CcButton);
        panel.add(BccButton);
        panel.add(toScrollpane);
        panel.add(ccScrollpane);
        panel.add(bccScrollpane);
        panel.add(OkButton);
        panel.add(cancelButton);
        panel.add(newContact);
        panel.add(tableScrollPane);
        getContentPane().add(panel);
        setSize(500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - (500 / 2), screenSize.height / 2 - (400 / 2));
        setVisible(true);
    }

    class Target implements ActionListener, KeyListener {

        AddressBookFrame addressBookFrame;

        public Target(AddressBookFrame addressBookFrame) {
            this.addressBookFrame = addressBookFrame;
        }

        public void actionPerformed(ActionEvent e) {
            String comma = ",";
            if (e.getSource() == newContact) {
                new AddContactFrame(addressBookFrame);
            } else if (e.getSource() == OkButton) {
                editorFrame.setToAddress(toArea.getText());
                editorFrame.setCcAddress(ccArea.getText());
                editorFrame.setBccAddress(bccArea.getText());
                dispose();
            } else if (e.getSource() == cancelButton) {
                dispose();
            } else {
                int row = addressTable.getSelectedRow();
                if (row == -1) {
                    Utility.showMessage(addressBookFrame, "Address is not selected", "Address Book", 1);
                    return;
                }
                String address = (String) tableModel.getValueAt(row, 1);
                if (e.getSource() == toButton && !isContactPresent(toArea.getText(), address)) {
                    toArea.append(address + comma);
                } else if (e.getSource() == CcButton && !isContactPresent(ccArea.getText(), address)) {
                    ccArea.append(address + comma);
                } else if (e.getSource() == BccButton && !isContactPresent(bccArea.getText(), address)) {
                    bccArea.append(address + comma);
                }
            }
        }

        public void keyTyped(KeyEvent k) {
            String str = textField.getText();
            search(str);
        }

        public void keyPressed(KeyEvent k) {
        }

        public void keyReleased(KeyEvent k) {
        }
    }

    private boolean isContactPresent(String text, String contact) {
        StringTokenizer tokenizer = new StringTokenizer(text, ",");
        boolean flag = false;
        while (tokenizer.hasMoreTokens()) {
            if (tokenizer.nextToken().equalsIgnoreCase(contact)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void search(String str) {
        Vector data = tableModel.getDataVector();
        String[] da = new String[data.size()];
        for (int i = 0; i < da.length; i++) {
            da[i] = (String) ((Vector) data.elementAt(i)).elementAt(0);
            String s = da[i].substring(0, str.length());
            if (s.equalsIgnoreCase(str)) {
            }
        }
    }

    private void populateTableModel() {
        tableModel.setColumnIdentifiers(new Object[] { "Name", "email" });
        try {
            ArrayList contacts = new ContactBO().getAllContacts();
            for (int i = 0; i < contacts.size(); i++) {
                ContactInfo contactInfo = (ContactInfo) contacts.get(i);
                tableModel.addRow(new Object[] { contactInfo.getName(), contactInfo.getEmail() });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utility.showMessage(this, "SQL ERROR", "Contacts", 0);
        }
    }

    public void refresh() {
        int count = tableModel.getRowCount();
        for (int i = count - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        populateTableModel();
    }
}
