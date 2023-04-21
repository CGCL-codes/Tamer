package za.co.data.framework.modler;

import za.co.data.util.DBUtils;
import za.co.data.util.ModlerTools;
import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author darryl
 */
public class DatabaseSettingsFrame extends javax.swing.JFrame {

    /**
     * Creates new form DatabaseSettingsFrame
     */
    public DatabaseSettingsFrame() {
        initComponents();
        cbDatabaseTypes.setModel(createDbTypesModel());
        cbDatabaseTypes.setSelectedItem(DBUtils.getHibernateDatabaseType());
        tfDatabaseName.setText(DBUtils.getHibernateDatabaseName());
        tfUserName.setText(DBUtils.getHibernateUserName());
        tfPassword.setText(DBUtils.getHibernatePassword());
        tfServer.setText(DBUtils.getHibernateServer());
        ModlerTools.centerOnScreen(this, false);
    }

    private ComboBoxModel createDbTypesModel() {
        return new DefaultComboBoxModel(DBUtils.getDatabaseTypes());
    }

    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfUserName = new javax.swing.JTextField();
        tfServer = new javax.swing.JTextField();
        tfDatabaseName = new javax.swing.JTextField();
        tfPassword = new javax.swing.JPasswordField();
        cbDatabaseTypes = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnOk = new JButton();
        btnCancel = new JButton();
        btnTestConnection = new JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Database Settings");
        setIconImage(Icons.DATABASE.getImage());
        jLabel1.setText("Username");
        jLabel2.setText("Password");
        jLabel3.setText("Server");
        jLabel4.setText("Database Name");
        cbDatabaseTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbDatabaseTypes.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDatabaseTypesActionPerformed(evt);
            }
        });
        jLabel5.setText("Database Type");
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("*");
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("*");
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("*");
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("*");
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        btnTestConnection.setText("Test Connection");
        btnTestConnection.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConnectionActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(btnOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE).addComponent(btnCancel)).addComponent(btnTestConnection)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(btnTestConnection).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnOk).addComponent(btnCancel)).addContainerGap()));
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tfServer, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2)).addGap(72, 72, 72).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tfPassword)).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tfUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)))).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE).addComponent(jLabel9)).addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(cbDatabaseTypes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(tfDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(mainPanelLayout.createSequentialGroup().addGap(106, 106, 106).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(tfUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(tfServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(tfDatabaseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbDatabaseTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(27, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(12, 12, 12).addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void cbDatabaseTypesActionPerformed(java.awt.event.ActionEvent evt) {
        tfDatabaseName.setText("");
        tfUserName.setText("");
        tfPassword.setText("");
        tfServer.setText("");
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
        saveSettings();
    }

    public void saveSettings() {
        if (!tfDatabaseName.getText().isEmpty() && !tfUserName.getText().isEmpty() && tfPassword.getPassword().length > 0 && !tfServer.getText().isEmpty()) {
            String databaseURL = DBUtils.getConnectionString(cbDatabaseTypes.getSelectedItem() + "");
            databaseURL = databaseURL.replace("<HOST>", tfServer.getText());
            databaseURL = databaseURL.replace("<DB>", tfDatabaseName.getText());
            databaseURL = databaseURL.replace("<PORT>", DBUtils.getDefaultPort(cbDatabaseTypes.getSelectedItem() + ""));
            try {
                DBUtils.setDatabaseConnection(databaseURL, DBUtils.getDriverName(cbDatabaseTypes.getSelectedItem() + ""), tfUserName.getText(), new String(tfPassword.getPassword()), cbDatabaseTypes.getSelectedItem() + "");
                dispose();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseSettingsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnTestConnectionActionPerformed(java.awt.event.ActionEvent evt) {
        if (DBUtils.testConnection(tfUserName.getText(), new String(tfPassword.getPassword()), tfServer.getText(), tfDatabaseName.getText(), cbDatabaseTypes.getSelectedItem() + "")) {
            JOptionPane.showMessageDialog(this, "Connection Successful", "Connection Test", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Connection Failed", "Connection Test", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JButton btnCancel;

    private JButton btnOk;

    private JButton btnTestConnection;

    private javax.swing.JComboBox cbDatabaseTypes;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JTextField tfDatabaseName;

    private javax.swing.JPasswordField tfPassword;

    private javax.swing.JTextField tfServer;

    private javax.swing.JTextField tfUserName;

    public void hideYesNo() {
        btnCancel.setVisible(false);
        btnOk.setVisible(false);
        jPanel1.setOpaque(false);
    }
}
