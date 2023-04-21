package org.vrforcad.controller.online;

import java.awt.Rectangle;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.vrforcad.controller.gui.AppMenu;
import sun.misc.BASE64Encoder;

/**
 * The user account dialog for collaborative work environment.
 *  
 * @version 1.2 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class UserAccountDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel jLabelUserName = null;

    private JTextField jTextFieldUserName = null;

    private JButton jButtonLogin = null;

    private JButton jButtonCreateUser = null;

    private JLabel jLabelPassw = null;

    private JPasswordField jPasswordFieldPassw = null;

    private NetworkServer ns;

    private AppMenu appMenu;

    public UserAccountDialog(AppMenu appMenu, NetworkServer ns) {
        this.appMenu = appMenu;
        this.ns = ns;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 190);
        this.setTitle("Users list");
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
            jLabelPassw = new JLabel();
            jLabelPassw.setBounds(new Rectangle(25, 60, 98, 25));
            jLabelPassw.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelPassw.setText("Password");
            jLabelUserName = new JLabel();
            jLabelUserName.setBounds(new Rectangle(25, 25, 98, 25));
            jLabelUserName.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelUserName.setText("User Name");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(jLabelUserName, null);
            jContentPane.add(getJTextFieldUserName(), null);
            jContentPane.add(getJPasswordFieldPassw(), null);
            jContentPane.add(getJButtonCreateUser(), null);
            jContentPane.add(getJButtonLogin(), null);
            jContentPane.add(jLabelPassw, null);
            jContentPane.add(getJPasswordFieldPassw(), null);
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
            jTextFieldUserName.setBounds(new Rectangle(129, 25, 141, 25));
            jTextFieldUserName.setText(ns.getNetworkSettings().getUserName());
        }
        return jTextFieldUserName;
    }

    /**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonLogin() {
        if (jButtonLogin == null) {
            jButtonLogin = new JButton();
            jButtonLogin.setBounds(new Rectangle(170, 115, 115, 24));
            jButtonLogin.setText("Login");
            jButtonLogin.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jPasswordFieldPassw.getPassword().length == 0) {
                        passwordIssueDialog();
                    } else {
                        String user = jTextFieldUserName.getText();
                        String passwd = new String(jPasswordFieldPassw.getPassword());
                        if (!passwd.isEmpty()) {
                            if (!ns.getNetworkSettings().getUserPassword().equals(passwd)) {
                                String hashPassw = getMAC(passwd);
                                System.out.println("SHA passwd = " + hashPassw);
                                ns.getNetworkSettings().setUserName(user);
                                ns.getNetworkSettings().setUserPassword(hashPassw);
                                ns.saveNetworkConfigs();
                            }
                            checkLoginCredentials();
                        }
                    }
                }
            });
        }
        return jButtonLogin;
    }

    private void passwordIssueDialog() {
        JOptionPane.showMessageDialog(this, "The password can't be empty", "Password problem", JOptionPane.ERROR_MESSAGE);
    }

    private void checkLoginCredentials() {
        if (appMenu.login(this)) dispose();
    }

    /**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonCreateUser() {
        if (jButtonCreateUser == null) {
            jButtonCreateUser = new JButton();
            jButtonCreateUser.setBounds(new Rectangle(15, 115, 115, 24));
            jButtonCreateUser.setText("New User");
            jButtonCreateUser.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    CreateUserDialog createUserDialog = new CreateUserDialog(ns);
                    createUserDialog.setLocationRelativeTo(null);
                    createUserDialog.setVisible(true);
                    disposeDialog();
                }
            });
        }
        return jButtonCreateUser;
    }

    private void disposeDialog() {
        this.dispose();
    }

    /**
	 * This method initializes jPasswordFieldPassw	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    private JPasswordField getJPasswordFieldPassw() {
        if (jPasswordFieldPassw == null) {
            jPasswordFieldPassw = new JPasswordField();
            jPasswordFieldPassw.setBounds(new Rectangle(129, 60, 141, 25));
            jPasswordFieldPassw.setText(ns.getNetworkSettings().getUserPassword());
        }
        return jPasswordFieldPassw;
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
}
