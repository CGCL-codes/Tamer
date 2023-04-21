package net.cattaka.rdbassistant.driver.mysql;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.swing.text.StdTextField;
import net.cattaka.util.MessageBundle;

public class MySqlRdbaConnectionInfoEditor extends RdbaConnectionInfoEditor {

    private static final long serialVersionUID = 1L;

    private StdTextField labelField;

    private StdTextField hostnameField;

    private StdTextField portField;

    private StdTextField databaseField;

    private StdTextField usernameField;

    private JPasswordField passwordField;

    public MySqlRdbaConnectionInfoEditor() {
        makeLayout();
    }

    private void makeLayout() {
        JLabel labelLabel = new JLabel(MessageBundle.getMessage("label"));
        JLabel hostnameLabel = new JLabel(MessageBundle.getMessage("hostname"));
        JLabel portLabel = new JLabel(MessageBundle.getMessage("port"));
        JLabel databaseLabel = new JLabel(MessageBundle.getMessage("database"));
        JLabel usernameLabel = new JLabel(MessageBundle.getMessage("username"));
        JLabel passwordLabel = new JLabel(MessageBundle.getMessage("password"));
        labelField = new StdTextField();
        hostnameField = new StdTextField();
        portField = new StdTextField();
        databaseField = new StdTextField();
        usernameField = new StdTextField();
        passwordField = new JPasswordField();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbl.setConstraints(labelLabel, gbc);
        gbc.gridy++;
        gbl.setConstraints(hostnameLabel, gbc);
        gbc.gridy++;
        gbl.setConstraints(portLabel, gbc);
        gbc.gridy++;
        gbl.setConstraints(databaseLabel, gbc);
        gbc.gridy++;
        gbl.setConstraints(usernameLabel, gbc);
        gbc.gridy++;
        gbl.setConstraints(passwordLabel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbl.setConstraints(labelField, gbc);
        gbc.gridy++;
        gbl.setConstraints(hostnameField, gbc);
        gbc.gridy++;
        gbl.setConstraints(portField, gbc);
        gbc.gridy++;
        gbl.setConstraints(databaseField, gbc);
        gbc.gridy++;
        gbl.setConstraints(usernameField, gbc);
        gbc.gridy++;
        gbl.setConstraints(passwordField, gbc);
        this.setLayout(gbl);
        this.add(labelLabel);
        this.add(labelField);
        this.add(hostnameLabel);
        this.add(hostnameField);
        this.add(portLabel);
        this.add(portField);
        this.add(databaseLabel);
        this.add(databaseField);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
    }

    @Override
    public boolean load(RdbaConnectionInfo rdbaConnectionInfo) {
        if (!(rdbaConnectionInfo instanceof MySqlRdbaConnectionInfo)) {
            return false;
        }
        MySqlRdbaConnectionInfo info = (MySqlRdbaConnectionInfo) rdbaConnectionInfo;
        labelField.setText(info.getLabel());
        hostnameField.setText(info.getHost());
        portField.setText(info.getPort().toString());
        databaseField.setText(info.getDatabase());
        usernameField.setText(info.getUserName());
        passwordField.setText(info.getPassword());
        return false;
    }

    @Override
    public RdbaConnectionInfo save() {
        MySqlRdbaConnectionInfo info = new MySqlRdbaConnectionInfo();
        info.setLabel(labelField.getText());
        info.setHost(hostnameField.getText());
        info.setPort(Integer.valueOf(portField.getText()));
        info.setDatabase(databaseField.getText());
        info.setUserName(usernameField.getText());
        info.setPassword(String.valueOf(passwordField.getPassword()));
        return info;
    }
}
