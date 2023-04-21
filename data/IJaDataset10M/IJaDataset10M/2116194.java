package org.hkothari.mcheck;

import org.hkothari.mcheck.mCheckProperties;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

class preferencesDialog extends JDialog {

    static final long serialVersionUID = 8168950645934219542L;

    private mCheckProperties applicationProperties;

    private JPanel preferencesPane;

    private JCheckBox obstructiveModeCheck;

    private JPanel okPane;

    private JButton okButton;

    public preferencesDialog(JFrame parent, String newConfigFilePath) {
        super(parent, "Preferences", true);
        final String configFilePath = newConfigFilePath;
        applicationProperties = new mCheckProperties();
        applicationProperties.read(new File(configFilePath));
        preferencesPane = new JPanel();
        obstructiveModeCheck = new JCheckBox("Obstructive Mode (Alerts every 15 minutes when there are incomplete items.)", Boolean.parseBoolean(applicationProperties.getProperty("obstructiveMode", "false")));
        obstructiveModeCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                applicationProperties.setProperty("obstructiveMode", Boolean.toString(obstructiveModeCheck.isSelected()));
            }
        });
        preferencesPane.add(obstructiveModeCheck);
        getContentPane().add(preferencesPane);
        okPane = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                applicationProperties.write(new File(configFilePath));
                dispose();
            }
        });
        okPane.add(okButton);
        getContentPane().add(okPane, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
