package com.cube42.echoverse.account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.gui.DialogFoundation;
import com.cube42.util.gui.DialogUtils;

/**
 * Dialog box used to edit an account
 *
 * @author  Matt Paulin
 * @version $Id: AccountEditorDialog.java,v 1.2 2003/03/12 01:48:34 zer0wing Exp $
 */
public class AccountEditorDialog extends DialogFoundation {

    /**
     * The account being edited
     */
    private Account account;

    /**
     * Label for the username
     */
    private JLabel usernameLabel;

    /**
     * Text field used to edit the username
     */
    private JTextField usernameField;

    /**
     * Label for the password
     */
    private JLabel passwordLabel;

    /**
     * Text field used to edit the password
     */
    private JTextField passwordField;

    /**
     * Button used by the user to tell the widget to save the information
     */
    private JButton saveButton;

    /**
     * Button used by the user to tell the widget to cancel the operation
     */
    private JButton cancelButton;

    /**
     * Panel for selecting the entity shells available to the account
     */
    private ShellSelector shellSelector;

    /**
     * Set to true if it has been cancelled instead of saved
     */
    private boolean cancelled = true;

    /**
     * Constructs the AccountEditorDialog
     */
    public AccountEditorDialog() {
        super();
        this.shellSelector = new ShellSelector();
        this.usernameLabel = new JLabel("Username");
        this.usernameField = new JTextField();
        this.passwordLabel = new JLabel("Password");
        this.passwordField = new JTextField();
        this.saveButton = new JButton("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                saveAccount();
            }
        });
        this.cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelDialog();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(cancelButton);
        JPanel contentPanel = new JPanel();
        contentPanel.setMaximumSize(new Dimension(200, 170));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(this.usernameLabel);
        contentPanel.add(this.usernameField);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(this.passwordLabel);
        contentPanel.add(this.passwordField);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalBox());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(contentPanel);
        mainPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(this.shellSelector);
        mainPanel.add(Box.createHorizontalStrut(10));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cancelDialog();
            }
        });
        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelDialog();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveAccount();
                }
            }
        });
        this.setModal(true);
        this.setSize(700, 200);
    }

    /**
     * Allows the user to edit the supplied account
     *
     * @param   account     The account to edit
     * @param   newAccount  True if the account is a new account
     *                      this allows the user to edit the username
     */
    public void editAccount(Account account, boolean newAccount) {
        Cube42NullParameterException.checkNull(account, "account", "editAccount", this);
        this.account = account;
        this.cancelled = false;
        this.usernameField.setText(account.getUsername());
        this.usernameField.setEditable(newAccount);
        this.passwordField.setText(account.getPassword());
        this.shellSelector.setSelectedShells(this.account.getShellNames());
        this.setVisible(true);
    }

    /**
     * Returns the account contained in the dialog
     *
     * @return  The account contained in the dialog
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * Sets all the shell names to choose from
     *
     * @param   shells  The shell names to choose from
     */
    public void setAvailableShellNames(ShellNameCollection shells) {
        Cube42NullParameterException.checkNull(shells, "shells", "setEntityShells", this);
        this.shellSelector.setAvailableShells(shells);
    }

    /**
     * Returns true if the dialog was canceled
     *
     * @return  true if the dialog box was canceled
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels the dialog
     */
    void cancelDialog() {
        this.cancelled = true;
        this.setVisible(false);
    }

    /**
     * Saves the dialog information into the account
     */
    void saveAccount() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        if (username.trim().length() < 1) {
            DialogUtils.createErrorDialog("Must provide a username");
        } else {
            if (password.trim().length() < 1) {
                DialogUtils.createErrorDialog("Must provide a password");
            } else {
                this.account.setUsername(username);
                this.account.setPassword(password);
                this.account.setShellNames(this.shellSelector.getSelectedShells());
                this.setVisible(false);
            }
        }
    }
}
