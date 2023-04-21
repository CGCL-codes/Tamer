package com.ericdaugherty.mail.server.configuration;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.ericdaugherty.mail.server.constants.ConfigurationParamterContants;
import com.ericdaugherty.mail.server.users.PasswordManager;

/**
 * This class defines the dialog box used to create or edit a user.
 */
public class UserDialog extends JDialog implements ActionListener, ConfigurationToolConstants, ConfigurationParamterContants {

    public UserDialog(ConfigurationTool tool, boolean newUser, String username) {
        super(tool, true);
        _tool = tool;
        _newUser = newUser;
        _username = username;
        initialize();
        pack();
    }

    public boolean isNewUser() {
        return _newUser;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Ok")) {
            String username = null;
            String fullUsername = null;
            if (isNewUser()) {
                username = _userField.getText();
                if (username.length() <= 0) {
                    JOptionPane.showMessageDialog(this, "Username can not be empty", "Valiation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fullUsername = username + "@" + _domainCombo.getSelectedItem();
            }
            String password = String.valueOf(_passwordField.getPassword());
            if (isNewUser()) {
                if (password.length() <= 0) {
                    JOptionPane.showMessageDialog(this, "Password can not be empty", "Valiation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (isNewUser()) {
                String users = _tool._configuration.getProperty(USER_FILE_USERS);
                users += "," + fullUsername;
                _tool._configuration.setProperty(USER_FILE_USERS, users);
                String passwordHash = PasswordManager.encryptPassword(password);
                _tool._configuration.setProperty(USER_FILE_PASSWORD + fullUsername, passwordHash);
            } else {
                if (password.length() > 0) {
                    String passwordHash = PasswordManager.encryptPassword(password);
                    _tool._configuration.setProperty(USER_FILE_PASSWORD + getUsername(), passwordHash);
                }
            }
            _tool.setModified(true);
            dispose();
        } else if (command.equals("Cancel")) {
            dispose();
        }
    }

    private void initialize() {
        getContentPane().setLayout(new BorderLayout());
        Component label;
        JTextField field;
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        JPanel fieldsPanel = new JPanel(layout);
        label = _tool.initializeLabel(INFO_TITLE_USER_NAME, String.valueOf(COMMAND_INFO_USER_NAME));
        layout.setConstraints(label, constraints);
        fieldsPanel.add(label);
        if (isNewUser()) {
            JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            _userField = new JTextField(20);
            usernamePanel.add(_userField);
            usernamePanel.add(new JLabel("@"));
            String[] domains = _tool.parseCommaList(_tool._configuration.getProperty(GENERAL_DOMAINS));
            _domainCombo = new JComboBox(domains);
            usernamePanel.add(_domainCombo);
            layout.setConstraints(usernamePanel, constraints);
            fieldsPanel.add(usernamePanel);
        } else {
            JLabel usernameLabel = new JLabel(getUsername());
            layout.setConstraints(usernameLabel, constraints);
            fieldsPanel.add(usernameLabel);
        }
        label = _tool.initializeLabel(INFO_TITLE_USER_PASSWORD, String.valueOf(COMMAND_INFO_USER_PASSWORD));
        layout.setConstraints(label, constraints);
        fieldsPanel.add(label);
        _passwordField = new JPasswordField(20);
        layout.setConstraints(_passwordField, constraints);
        fieldsPanel.add(_passwordField);
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(fieldsPanel, BorderLayout.WEST);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private ConfigurationTool _tool = null;

    private boolean _newUser;

    private String _username;

    private JTextField _userField;

    private JComboBox _domainCombo;

    private JPasswordField _passwordField;
}
