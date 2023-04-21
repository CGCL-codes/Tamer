package org.vcboard.view;

import java.awt.event.KeyEvent;
import javax.swing.*;
import org.apache.log4j.*;
import org.vcboard.*;
import org.vcboard.database.*;
import org.vcboard.util.*;

/**
 *
 * @author  Josh
 */
public class NewUser extends javax.swing.JFrame {

    private static Logger log = Logger.getLogger(NewUser.class);

    /**
    * Creates new form ChangePassword
    */
    public NewUser() {
        initComponents();
        WorkTimer.centerForm(this);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        desiredUsernameTextField = new javax.swing.JTextField();
        passwordTextField = new javax.swing.JPasswordField();
        verifyPasswordTextField = new javax.swing.JPasswordField();
        submitButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New User");
        setResizable(false);
        jLabel1.setText("Desired Username:");
        jLabel2.setText("Password:");
        jLabel3.setText("Verify Password:");
        desiredUsernameTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                highlightText(evt);
            }
        });
        desiredUsernameTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                handleEnter(evt);
            }
        });
        passwordTextField.setFont(new java.awt.Font("Tahoma", 0, 11));
        passwordTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                highlightText(evt);
            }
        });
        passwordTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                handleEnter(evt);
            }
        });
        verifyPasswordTextField.setFont(new java.awt.Font("Tahoma", 0, 11));
        verifyPasswordTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                highlightText(evt);
            }
        });
        verifyPasswordTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                handleEnter(evt);
            }
        });
        submitButton.setMnemonic('u');
        submitButton.setText("Create User");
        submitButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        cancelButton.setMnemonic('c');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(jLabel2).add(jLabel3)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(desiredUsernameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE).add(passwordTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE).add(verifyPasswordTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(submitButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancelButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(desiredUsernameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(passwordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(verifyPasswordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(submitButton).add(cancelButton)).addContainerGap()));
        pack();
    }

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createUser();
    }

    private void handleEnter(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createUser();
        }
    }

    private void highlightText(java.awt.event.FocusEvent evt) {
        ((JTextField) evt.getSource()).setSelectionStart(0);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NewUser().setVisible(true);
            }
        });
    }

    private void createUser() {
        String desiredUsername = desiredUsernameTextField.getText();
        char[] newPassC = passwordTextField.getPassword();
        String newPass = new String(newPassC);
        char[] verNewPassC = verifyPasswordTextField.getPassword();
        String verNewPass = new String(verNewPassC);
        if (!MySQLDbIf.getInstance().userExists(desiredUsername)) {
            if (newPass.equals(verNewPass)) {
                if (newPass.length() >= 5) {
                    MySQLDbIf.getInstance().createUser(desiredUsername, new MD5(newPass).toString());
                    WorkTimer.getAuth().setVisible(true);
                    dispose();
                } else {
                    log.error("New password is too short...");
                    JOptionPane.showMessageDialog(null, "Your password must be at least 5 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                log.error("New passwords do not match...");
                JOptionPane.showMessageDialog(null, "Your passwords do not match.  Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            log.error("Desired username already taken");
            JOptionPane.showMessageDialog(null, "The username you have entered is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextField desiredUsernameTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPasswordField passwordTextField;

    private javax.swing.JButton submitButton;

    private javax.swing.JPasswordField verifyPasswordTextField;
}
