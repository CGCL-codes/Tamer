package com.anthonyeden.jtop.sentry;

import java.awt.*;
import javax.swing.*;

public class DefaultSentryEditor extends JPanel implements Editor {

    public DefaultSentryEditor(Sentry sentry) {
        this.sentry = sentry;
        init();
        revert();
    }

    public boolean save() {
        if (!verify()) {
            return false;
        }
        sentry.setName(nameField.getText());
        return true;
    }

    public void revert() {
        nameField.setText(sentry.getName());
    }

    protected boolean verify() {
        if (nameField.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter a name.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public Component getComponent() {
        return this;
    }

    private void init() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameLabel = new JLabel("Name:");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(nameLabel, gbc);
        add(nameLabel);
        nameField = new JTextField();
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(nameField, gbc);
        add(nameField);
    }

    private Sentry sentry;

    private JLabel nameLabel;

    private JTextField nameField;
}
