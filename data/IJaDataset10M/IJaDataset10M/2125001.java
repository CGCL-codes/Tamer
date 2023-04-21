package com.tamanderic.smupload;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class PasswordReader {

    private static final String PROG_NAME = PasswordReader.class.getName();

    private JFrame mainWindow;

    private String title;

    private JDialog loginWindow;

    private JLabel loginNameLabel;

    private JTextField loginNameField;

    private JLabel passwordLabel;

    private JPasswordField passwordField;

    private JButton loginButton;

    private static void Usage() {
        System.out.println("Usage: " + PROG_NAME);
    }

    /************************************************************************
     * Local unit test
     ***********************************************************************/
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame mainWindow = new JFrame(PROG_NAME);
        mainWindow.getContentPane().setLayout(new BorderLayout());
        PasswordReader pr = new PasswordReader(mainWindow, "Test " + PROG_NAME, null);
        pr.read();
        System.out.println("Login: " + pr.getLoginName());
        System.out.println("Password: " + pr.getPassword());
        System.exit(0);
    }

    /************************************************************************
     * Constructor: Create the modal dialog in the but don't show it
     ***********************************************************************/
    public PasswordReader(JFrame mainWindow, String title, String defaultLogin) {
        this.mainWindow = mainWindow;
        this.title = title;
        this.loginWindow = new JDialog(mainWindow, title, true);
        loginWindow.setResizable(false);
        loginWindow.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        loginNameLabel = new JLabel("Login Name:", JLabel.TRAILING);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 2);
        loginWindow.add(loginNameLabel, constraints);
        if (defaultLogin == null) {
            this.loginNameField = new JTextField(18);
        } else {
            this.loginNameField = new JTextField(defaultLogin, 18);
        }
        constraints.gridx = 1;
        constraints.gridy = 0;
        loginWindow.add(loginNameField, constraints);
        passwordLabel = new JLabel("Password:", JLabel.TRAILING);
        constraints.gridx = 0;
        constraints.gridy = 1;
        loginWindow.add(passwordLabel, constraints);
        this.passwordField = new JPasswordField(18);
        constraints.gridx = 1;
        constraints.gridy = 1;
        loginWindow.add(passwordField, constraints);
        loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.PAGE_END;
        loginWindow.add(loginButton, constraints);
        this.loginButton.addActionListener(new ButtonActionListener(this, "Login"));
        loginWindow.pack();
        loginWindow.setLocationRelativeTo(null);
    }

    /************************************************************************
     * Hide the modal dialog
     ***********************************************************************/
    protected void hide() {
        this.loginWindow.setVisible(false);
    }

    /************************************************************************
     * Show the modal dialog
     ***********************************************************************/
    public void read() {
        this.loginNameField.selectAll();
        this.loginWindow.setVisible(true);
    }

    /************************************************************************
     * Return the read login name to the caller
     ***********************************************************************/
    public String getLoginName() {
        return this.loginNameField.getText();
    }

    /************************************************************************
     * Return the read password name to the caller
     ***********************************************************************/
    public String getPassword() {
        return new String(this.passwordField.getPassword());
    }
}

/************************************************************************
 * Class to support hiding the modal dialog on a button press
 ***********************************************************************/
class ButtonActionListener implements ActionListener {

    private PasswordReader passwordReader;

    private String hideCommand;

    public ButtonActionListener(PasswordReader passwordReader, String hideCommand) {
        this.passwordReader = passwordReader;
        this.hideCommand = hideCommand;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (this.hideCommand.equals(command)) {
            passwordReader.hide();
        }
    }
}
