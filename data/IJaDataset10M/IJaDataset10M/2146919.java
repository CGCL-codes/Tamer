package org.statcato.dialogs.file;

import org.statcato.*;
import java.text.MessageFormat;
import javax.swing.*;
import java.awt.print.PrinterException;

/**
 * A dialog that provides print options and prints the log window.
 * 
 * @author  Margaret Yau
 * @version %I%, %G%
 * @since 1.0
 */
public class PrintLogDialog extends StatcatoDialog {

    /** Creates new form PrintLogDialog */
    public PrintLogDialog(java.awt.Frame parent, boolean modal, Statcato app) {
        super(parent, modal);
        this.app = app;
        initComponents();
        getRootPane().setDefaultButton(PrintButton);
        setHelpFile("file-menu");
        name = "Print Log";
        description = "For printing the log window.";
        helpStrings.add("Specify the header and footer to be printed along " + "with the contents of the log window.");
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        HeaderCheckBox = new javax.swing.JCheckBox();
        FooterCheckBox = new javax.swing.JCheckBox();
        HeaderTextField = new javax.swing.JTextField();
        FooterTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        PrintButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Print Log");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Print Options"));
        HeaderCheckBox.setSelected(true);
        HeaderCheckBox.setText("Header:");
        HeaderCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HeaderCheckBoxActionPerformed(evt);
            }
        });
        FooterCheckBox.setSelected(true);
        FooterCheckBox.setText("Footer:");
        FooterCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FooterCheckBoxActionPerformed(evt);
            }
        });
        FooterTextField.setText("Page {0}");
        jLabel1.setText("- Enter {0} for page number.");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(15, 15, 15).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(HeaderCheckBox).addComponent(FooterCheckBox)).addGap(26, 26, 26).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(FooterTextField).addComponent(HeaderTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel1))).addContainerGap(19, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(HeaderCheckBox).addComponent(HeaderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(FooterCheckBox).addComponent(FooterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1).addContainerGap(20, Short.MAX_VALUE)));
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        PrintButton.setText("Print");
        PrintButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(128, 128, 128).addComponent(PrintButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(CancelButton))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { CancelButton, PrintButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(PrintButton).addComponent(CancelButton)).addContainerGap(23, Short.MAX_VALUE)));
        pack();
    }

    private void PrintButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JTextPane Text = app.getLogTextPane();
        MessageFormat header = null;
        if (HeaderCheckBox.isSelected()) {
            header = new MessageFormat(HeaderTextField.getText());
        }
        MessageFormat footer = null;
        if (FooterCheckBox.isSelected()) {
            footer = new MessageFormat(FooterTextField.getText());
        }
        try {
            boolean complete = Text.print(header, footer, true, null, null, true);
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing Complete", "Printing Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing Cancelled", "Printing Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Printing Result", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void HeaderCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (HeaderCheckBox.isSelected()) HeaderTextField.setEnabled(true); else HeaderTextField.setEnabled(false);
    }

    private void FooterCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (FooterCheckBox.isSelected()) FooterTextField.setEnabled(true); else FooterTextField.setEnabled(false);
    }

    private javax.swing.JButton CancelButton;

    private javax.swing.JCheckBox FooterCheckBox;

    private javax.swing.JTextField FooterTextField;

    private javax.swing.JCheckBox HeaderCheckBox;

    private javax.swing.JTextField HeaderTextField;

    private javax.swing.JButton PrintButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;
}
