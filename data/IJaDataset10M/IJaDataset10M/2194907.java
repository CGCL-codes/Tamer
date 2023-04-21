package org.vrforcad.controller.online;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import sun.misc.BASE64Encoder;

/**
 * The user account dialog for collaborative work environment.
 *  
 * @version 1.3 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class CreateUserDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel jLabelUserName = null;

    private JTextField jTextFieldUserName = null;

    private JLabel jLabelDescription = null;

    private JTextField jTextFieldAboutUser = null;

    private JButton jButtonSave = null;

    private JLabel jLabelPassw = null;

    private JPasswordField jPasswordFieldPassw = null;

    private JLabel jLabelPasswAgain = null;

    private JPasswordField jPasswordFieldPasswAgain = null;

    private NetworkServer ns;

    private JPanel DepartmentPanel = null;

    private JPanel userPanel = null;

    private JLabel jLabelDepartmentKey = null;

    private JPasswordField jPasswordFieldDepartmentKey = null;

    private JLabel jLabelDepartmentInfo = null;

    public CreateUserDialog(NetworkServer ns) {
        this.ns = ns;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 323);
        this.setTitle("User name");
        this.setContentPane(getJContentPane());
        this.setAlwaysOnTop(true);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabelPasswAgain = new JLabel();
            jLabelPasswAgain.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelPasswAgain.setBounds(new Rectangle(-3, 130, 120, 25));
            jLabelPasswAgain.setText("Password again");
            jLabelPassw = new JLabel();
            jLabelPassw.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelPassw.setBounds(new Rectangle(4, 95, 98, 25));
            jLabelPassw.setText("Password");
            jLabelDescription = new JLabel();
            jLabelDescription.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelDescription.setBounds(new Rectangle(3, 55, 98, 25));
            jLabelDescription.setText("About User");
            jLabelUserName = new JLabel();
            jLabelUserName.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelUserName.setBounds(new Rectangle(4, 19, 98, 25));
            jLabelUserName.setText("User Name");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonSave(), null);
            jContentPane.add(getDepartmentPanel(), null);
            jContentPane.add(getUserPanel(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jTextFieldUserName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldUserName() {
        if (jTextFieldUserName == null) {
            jTextFieldUserName = new JTextField();
            jTextFieldUserName.setBounds(new Rectangle(116, 19, 141, 25));
        }
        return jTextFieldUserName;
    }

    /**
	 * This method initializes jTextFieldAboutUser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldAboutUser() {
        if (jTextFieldAboutUser == null) {
            jTextFieldAboutUser = new JTextField();
            jTextFieldAboutUser.setBounds(new Rectangle(116, 55, 141, 25));
        }
        return jTextFieldAboutUser;
    }

    /**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonSave() {
        if (jButtonSave == null) {
            jButtonSave = new JButton();
            jButtonSave.setBounds(new Rectangle(137, 256, 141, 24));
            jButtonSave.setText("Create User");
            jButtonSave.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jPasswordFieldPassw.getPassword().length == 0 || jPasswordFieldPasswAgain.getPassword().length == 0) {
                        emptyPasswordField();
                    } else if (jTextFieldAboutUser.getText().length() == 0) {
                        emptyUserAboutField();
                    } else if (jTextFieldUserName.getText().length() == 0) {
                        emptyUserField();
                    } else if (jPasswordFieldDepartmentKey.getPassword().length == 0) {
                        emptyDepartmentField();
                    } else {
                        String passwd = new String(jPasswordFieldPassw.getPassword());
                        String passwdAgain = new String(jPasswordFieldPasswAgain.getPassword());
                        if (passwd.equals(passwdAgain)) {
                            String hashPassw = getMAC(passwd);
                            ns.getNetworkSettings().setUserName(jTextFieldUserName.getText());
                            ns.getNetworkSettings().setUserDetails(jTextFieldAboutUser.getText());
                            ns.getNetworkSettings().setUserPassword(hashPassw);
                            ns.getNetworkSettings().setKey(new String(jPasswordFieldDepartmentKey.getPassword()));
                            if (ns.createNewUser()) {
                                ns.saveNetworkConfigs();
                                showConfirmationDialog();
                            } else userFailDialog();
                        } else {
                            passwordIssueDialog();
                        }
                    }
                }
            });
        }
        return jButtonSave;
    }

    private void emptyPasswordField() {
        JOptionPane.showMessageDialog(this, "The password field can't be empty", "Password empty", JOptionPane.ERROR_MESSAGE);
    }

    private void emptyUserAboutField() {
        JOptionPane.showMessageDialog(this, "The About User field can't be empty", "Details empty", JOptionPane.ERROR_MESSAGE);
    }

    private void emptyUserField() {
        JOptionPane.showMessageDialog(this, "The user field can't be empty", "User empty", JOptionPane.ERROR_MESSAGE);
    }

    private void emptyDepartmentField() {
        JOptionPane.showMessageDialog(null, "The department key can't be empty", "Department key empty", JOptionPane.ERROR_MESSAGE);
    }

    private void showConfirmationDialog() {
        JOptionPane.showMessageDialog(this, "The user was created. \nEnter the new user and password in User name dialog", "User Created", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void userFailDialog() {
        JOptionPane.showMessageDialog(this, "The user could not be created. \n Check the network settings.", "User create fail", JOptionPane.ERROR_MESSAGE);
    }

    private void passwordIssueDialog() {
        JOptionPane.showMessageDialog(this, "The password is not the same in the both field", "Password problem", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * This method initializes jPasswordFieldPassw	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    private JPasswordField getJPasswordFieldPassw() {
        if (jPasswordFieldPassw == null) {
            jPasswordFieldPassw = new JPasswordField();
            jPasswordFieldPassw.setBounds(new Rectangle(116, 95, 141, 25));
        }
        return jPasswordFieldPassw;
    }

    /**
	 * This method initializes jPasswordFieldPasswAgain	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    private JPasswordField getJPasswordFieldPasswAgain() {
        if (jPasswordFieldPasswAgain == null) {
            jPasswordFieldPasswAgain = new JPasswordField();
            jPasswordFieldPasswAgain.setBounds(new Rectangle(116, 130, 141, 25));
        }
        return jPasswordFieldPasswAgain;
    }

    /**
	 * SHA (Secure Hash Algorithm) for password. 
	 * @param password plain text password
	 * @return a hash password
	 */
    private String getMAC(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
        }
        try {
            md.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }

    /**
	 * This method initializes DepartmentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getDepartmentPanel() {
        if (DepartmentPanel == null) {
            jLabelDepartmentInfo = new JLabel();
            jLabelDepartmentInfo.setBounds(new Rectangle(51, 16, 22, 23));
            jLabelDepartmentInfo.setText("?");
            jLabelDepartmentInfo.setFont(new Font("Serif", Font.BOLD, 18));
            jLabelDepartmentInfo.setForeground(Color.blue);
            jLabelDepartmentInfo.setToolTipText("For a server with many departments a key keeps the users grouped. " + "The group's admin should provide the key. " + "A default key is already entered.");
            jLabelDepartmentKey = new JLabel();
            jLabelDepartmentKey.setBounds(new Rectangle(10, 16, 39, 23));
            jLabelDepartmentKey.setText("Key");
            DepartmentPanel = new JPanel();
            DepartmentPanel.setLayout(null);
            DepartmentPanel.setBounds(new Rectangle(12, 191, 273, 50));
            DepartmentPanel.setBorder(BorderFactory.createTitledBorder(null, "Department", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.blue));
            DepartmentPanel.add(jLabelDepartmentKey, null);
            DepartmentPanel.add(getJPasswordFieldDepartmentKey(), null);
            DepartmentPanel.add(jLabelDepartmentInfo, null);
        }
        return DepartmentPanel;
    }

    /**
	 * This method initializes userPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getUserPanel() {
        if (userPanel == null) {
            userPanel = new JPanel();
            userPanel.setLayout(null);
            userPanel.setBounds(new Rectangle(12, 12, 273, 167));
            userPanel.setBorder(BorderFactory.createTitledBorder(null, "User", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), Color.blue));
            userPanel.add(jLabelUserName, null);
            userPanel.add(getJTextFieldUserName(), null);
            userPanel.add(getJTextFieldAboutUser(), null);
            userPanel.add(jLabelDescription, null);
            userPanel.add(getJPasswordFieldPassw(), null);
            userPanel.add(jLabelPassw, null);
            userPanel.add(jLabelPasswAgain, null);
            userPanel.add(getJPasswordFieldPasswAgain(), null);
        }
        return userPanel;
    }

    /**
	 * This method initializes jPasswordFieldDepartmentKey	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    private JPasswordField getJPasswordFieldDepartmentKey() {
        if (jPasswordFieldDepartmentKey == null) {
            jPasswordFieldDepartmentKey = new JPasswordField();
            jPasswordFieldDepartmentKey.setBounds(new Rectangle(73, 16, 183, 23));
            jPasswordFieldDepartmentKey.setText(ns.getNetworkSettings().getKey());
        }
        return jPasswordFieldDepartmentKey;
    }
}
