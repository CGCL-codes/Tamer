package com.anthonyeden.jtop.sentry;

import java.awt.*;
import javax.swing.*;
import com.anthonyeden.jtop.*;

public class LimitSentryEditor extends JPanel implements Editor {

    public LimitSentryEditor(LimitSentry sentry) {
        this.sentry = sentry;
        init();
    }

    public boolean save() {
        if (!verify()) {
            return false;
        }
        sentry.setField(fieldCombo.getSelectedItem().toString());
        sentry.setName(nameField.getText());
        sentry.setLimit(Float.parseFloat(limitField.getText()));
        return true;
    }

    public void revert() {
        nameField.setText(sentry.getName());
        fieldCombo.setSelectedItem(sentry.getField());
        limitField.setText(Float.toString(sentry.getLimit()));
    }

    protected boolean verify() {
        Object selectedField = fieldCombo.getSelectedItem();
        if (selectedField == null) {
            JOptionPane.showMessageDialog(null, "Please select a field", "Error", JOptionPane.ERROR_MESSAGE);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameLabel = new JLabel("Name:");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(nameLabel, gbc);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setColumns(16);
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(nameField, gbc);
        add(nameField);
        fieldLabel = new JLabel("Field:");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(fieldLabel, gbc);
        add(fieldLabel);
        fieldCombo = new JComboBox(sentry.getHost().getFieldNames());
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(fieldCombo, gbc);
        add(fieldCombo);
        limitLabel = new JLabel("Limit:");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(limitLabel, gbc);
        add(limitLabel);
        limitField = new JTextField();
        limitField.setColumns(16);
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(limitField, gbc);
        add(limitField);
    }

    private LimitSentry sentry;

    private JLabel nameLabel;

    private JTextField nameField;

    private JLabel fieldLabel;

    private JComboBox fieldCombo;

    private JLabel limitLabel;

    private JTextField limitField;
}
