package net.sourceforge.homekeeper.core;

import java.sql.*;
import java.security.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  Lefteris Kororos
 */
public class LoginForm extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    private Logger log = null;

    /**
     * Creates new form LoginForm
     */
    public LoginForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        log = Logger.getLogger("net.sourceforge.homekeeper.core");
        log.debug("Now initializing Login Form");
        initComponents();
        this.getRootPane().setDefaultButton(this.btnCancel);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        pwdPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Login");
        setAlwaysOnTop(true);
        setBackground(java.awt.Color.white);
        setModal(true);
        setName("frmLogin");
        setResizable(false);
        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 24));
        jLabel3.setText("Welcome to HomeKeeper");
        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 18));
        jLabel4.setText("Please Login to proceed");
        jLabel1.setText("UserName:");
        txtUsername.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtUsernameCaretUpdate(evt);
            }
        });
        pwdPassword.addCaretListener(new javax.swing.event.CaretListener() {

            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                pwdPasswordCaretUpdate(evt);
            }
        });
        jLabel2.setText("Password:");
        btnOK.setText("OK");
        btnOK.setEnabled(false);
        btnOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel2).add(jLabel1)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(txtUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(pwdPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4).add(layout.createSequentialGroup().add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(btnCancel)))).add(layout.createSequentialGroup().add(23, 23, 23).add(jLabel3))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.linkSize(new java.awt.Component[] { btnCancel, btnOK }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(txtUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(pwdPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCancel).add(btnOK)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void txtUsernameCaretUpdate(javax.swing.event.CaretEvent evt) {
        if ((txtUsername.getText().length() != 0) && (pwdPassword.getPassword().length != 0)) {
            this.btnOK.setEnabled(true);
            this.getRootPane().setDefaultButton(this.btnOK);
        } else {
            this.btnOK.setEnabled(false);
            this.getRootPane().setDefaultButton(this.btnCancel);
        }
    }

    private void pwdPasswordCaretUpdate(javax.swing.event.CaretEvent evt) {
        if ((txtUsername.getText().length() != 0) && (pwdPassword.getPassword().length != 0)) {
            this.btnOK.setEnabled(true);
            this.getRootPane().setDefaultButton(this.btnOK);
        } else {
            this.btnOK.setEnabled(false);
            this.getRootPane().setDefaultButton(this.btnCancel);
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        PreparedStatement pStmt = null;
        ResultSet rset = null;
        try {
            String sql = "SELECT GROUP_ID from CORE_USERS" + " WHERE USERNAME = ?" + " AND PASSWORD = ?" + " AND DATE_INITIAL <= ?" + " AND DATE_FINAL    > ?" + " AND IND_STATUS = ?";
            pStmt = Database.getMyConnection().prepareStatement(sql);
            pStmt.setString(1, txtUsername.getText());
            pStmt.setString(2, getPwdMD5());
            pStmt.setDate(3, Utils.getTodaySql());
            pStmt.setDate(4, Utils.getTodaySql());
            pStmt.setString(5, "A");
            rset = pStmt.executeQuery();
            if (rset.next()) {
                User.setUserName(txtUsername.getText());
                User.setUserGroupId(rset.getInt("GROUP_ID"));
                this.dispose();
            } else {
                MessageBox.ok("Your name or password is incorrect. Try again", this);
                pwdPassword.setText("");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private String getPwdMD5() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            char[] passwd = pwdPassword.getPassword();
            String aString = new String(passwd);
            byte[] bytes = aString.getBytes();
            md.update(bytes);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Unknown Algorithm");
            System.exit(0);
        }
        byte[] md5byte = md.digest();
        String md5 = byteArrayToHexString(md5byte);
        return md5;
    }

    private String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private javax.swing.JButton btnCancel;

    private javax.swing.JButton btnOK;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPasswordField pwdPassword;

    private javax.swing.JTextField txtUsername;
}
