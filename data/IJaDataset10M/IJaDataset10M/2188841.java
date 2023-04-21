package com.pallas.unicore.client.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *  Dialog to login with username and password.
 *
 *@author     Ralf Ratering
 *@version    $Id: LoginDialog.java,v 1.1 2004/05/25 14:58:47 rmenday Exp $
 */
public class LoginDialog extends GenericDialog {

    static ResourceBundle res = ResourceBundle.getBundle("com.pallas.unicore.client.dialogs.ResourceStrings");

    /**
	 *  Minimal main
	 *
	 *@param  args  The command line arguments
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.show();
        LoginDialog dialog = new LoginDialog(frame, "TEST: PasswordDialog");
        dialog.show();
    }

    private char[] password;

    private JPasswordField passwordField = new JPasswordField(15);

    private String username;

    private JTextField usernameField = new JTextField(15);

    /**
	 *  Constructor builds gui and sets a title
	 *
	 *@param  title   dialog title
	 *@param  parent  dialog parent - important for modal dialogs
	 */
    public LoginDialog(JFrame parent, String title) {
        super(parent, title, true, OK_CANCEL);
        buildComponents();
        pack();
    }

    /**
	 *  Takes current textfield entry as password
	 *
	 *@return    true if dialog can be disposed
	 */
    protected boolean applyValues() {
        username = usernameField.getText();
        password = passwordField.getPassword();
        return true;
    }

    /**
	 *  Add a password field to the parent dialog
	 */
    private void buildComponents() {
        JLabel usernameLabel = new JLabel(res.getString("USERNAME"));
        JLabel passwordLabel = new JLabel(res.getString("PASSWORD"));
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        JPanel subPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        subPanel.add(usernamePanel);
        subPanel.add(passwordPanel);
        subPanel.setBorder(new EmptyBorder(5, 20, 5, 20));
        getContentPane().add(subPanel, BorderLayout.CENTER);
    }

    /**
	 *  Sets password to null
	 *
	 *@return    true if dialog can be disposed
	 */
    protected boolean cancelValues() {
        password = null;
        return true;
    }

    /**
	 *  Get the result from the dialog
	 *
	 *@return    null if cancel was pressed and a char array if ok was pressed
	 */
    public char[] getPassword() {
        return password;
    }

    /**
	 *  Get the entered user name
	 *
	 *@return    name as string
	 */
    public String getUsername() {
        return username;
    }

    /**
	 *  Put focus into password field and make okButton default
	 */
    public void show() {
        usernameField.requestFocus();
        super.show();
    }
}
