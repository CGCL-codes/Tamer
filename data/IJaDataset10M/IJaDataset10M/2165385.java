package org.rjam.alert.action;

/**
 *
 * @author  Tony Bringardner
 */
public class EditActionEmail extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    private ActionEmail action;

    /** Creates new form EditActionEmail */
    public EditActionEmail(ActionEmail action) {
        this();
        setAction(action);
    }

    private void setAction(ActionEmail action) {
        this.action = action;
        senderFld.setText(action.getSender());
        smtpHostFld.setText(action.getSmtpHost());
        subjectFld.setText(action.getSubject());
        recipientsList.setListData(action.getRecipients().toArray());
        editActionEmpty1.setAction(action);
        revalidate();
        updateUI();
    }

    /** Creates new form EditActionEmail */
    public EditActionEmail() {
        initComponents();
        recipientsList.setListData(new Object[0]);
    }

    @SuppressWarnings("serial")
    private void initComponents() {
        northPanel = new javax.swing.JPanel();
        southPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        editActionEmpty1 = new org.rjam.alert.action.EditActionEmpty();
        emailPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        senderFld = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        smtpHostFld = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        recipientsList = new javax.swing.JList();
        addRecipientsButton = new javax.swing.JButton();
        addRecipientFld = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        subjectFld = new javax.swing.JTextField();
        setPreferredSize(new java.awt.Dimension(900, 220));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());
        add(northPanel, java.awt.BorderLayout.PAGE_START);
        southPanel.setLayout(new java.awt.BorderLayout());
        add(southPanel, java.awt.BorderLayout.SOUTH);
        jTabbedPane1.addTab("Message Template", editActionEmpty1);
        emailPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabel1.setText("Sender:");
        emailPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 80, 20));
        senderFld.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                senderFldActionPerformed(evt);
            }
        });
        senderFld.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                senderFldFocusLost(evt);
            }
        });
        emailPanel.add(senderFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 300, -1));
        jLabel2.setText("SMTP Host:");
        emailPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 110, 20));
        smtpHostFld.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smtpHostFldActionPerformed(evt);
            }
        });
        smtpHostFld.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                smtpHostFldFocusLost(evt);
            }
        });
        emailPanel.add(smtpHostFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 300, -1));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Recipients"));
        recipientsList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(recipientsList);
        emailPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 280, -1));
        addRecipientsButton.setText("Add Recipient");
        addRecipientsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRecipientsButtonActionPerformed(evt);
            }
        });
        emailPanel.add(addRecipientsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 140, -1));
        emailPanel.add(addRecipientFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 270, -1));
        jButton1.setText("Remove Seleted");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        emailPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));
        jLabel3.setText("Subject:");
        emailPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 70, 20));
        subjectFld.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectFldActionPerformed(evt);
            }
        });
        subjectFld.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                subjectFldFocusLost(evt);
            }
        });
        emailPanel.add(subjectFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 300, -1));
        jTabbedPane1.addTab("Email Config", emailPanel);
        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }

    private void subjectFldFocusLost(java.awt.event.FocusEvent evt) {
        action.setSubject(subjectFld.getText());
    }

    private void smtpHostFldFocusLost(java.awt.event.FocusEvent evt) {
        action.setSmtpHost(smtpHostFld.getText());
    }

    private void senderFldFocusLost(java.awt.event.FocusEvent evt) {
        action.setSender(senderFld.getText());
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] sel = recipientsList.getSelectedValues();
        if (sel != null && sel.length > 0) {
            for (int idx = 0; idx < sel.length; idx++) {
                action.removeRrecipient(sel[idx].toString());
            }
            updateList();
        }
    }

    private void addRecipientsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        action.addRecipient(addRecipientFld.getText());
        updateList();
    }

    private void updateList() {
        recipientsList.setListData(action.getRecipients().toArray());
        revalidate();
        updateUI();
    }

    private void subjectFldActionPerformed(java.awt.event.ActionEvent evt) {
        action.setSubject(subjectFld.getText());
    }

    private void smtpHostFldActionPerformed(java.awt.event.ActionEvent evt) {
        action.setSmtpHost(smtpHostFld.getText());
    }

    private void senderFldActionPerformed(java.awt.event.ActionEvent evt) {
        action.setSender(senderFld.getText());
    }

    private javax.swing.JTextField addRecipientFld;

    private javax.swing.JButton addRecipientsButton;

    private org.rjam.alert.action.EditActionEmpty editActionEmpty1;

    private javax.swing.JPanel emailPanel;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JPanel northPanel;

    private javax.swing.JList recipientsList;

    private javax.swing.JTextField senderFld;

    private javax.swing.JTextField smtpHostFld;

    private javax.swing.JPanel southPanel;

    private javax.swing.JTextField subjectFld;
}
