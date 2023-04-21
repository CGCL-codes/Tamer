package com.memoire.vainstall.builder.gui;

import com.memoire.vainstall.VAGlobals;
import com.memoire.vainstall.builder.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;

/**
 * This panel is shown in the product dialog
 *
 * This is not just a view panel because it has a reference to the
 * VAIProductModel.
 * 
 * # Path of license file
 * vainstall.archive.license=src/doc/license.txt
 *
 * # Encoding of license file, Optional
 * #vainstall.archive.license.encoding = UTF-8
 *
 * @see javax.swing.JPanel
 *
 * @author Henrik Falk
 * @version $Id: ProductStepLicensePanel.java,v 1.1 2001/09/28 19:35:30 hfalk Exp $
 */
public class ProductStepLicensePanel extends JPanel implements FocusListener, ActionListener {

    private VAIProductModel model;

    private static final Border loweredBorder = new SoftBevelBorder(BevelBorder.LOWERED);

    RequiredTextField licenseFileNameField;

    JButton licenseFileNameButton;

    JTextField encodingField;

    public ProductStepLicensePanel() {
        setBorder(loweredBorder);
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints contraint = new GridBagConstraints();
        JLabel decriptionLabel = new JLabel();
        decriptionLabel.setBorder(BorderFactory.createTitledBorder("Description"));
        decriptionLabel.setText("This step shows the license for the product.");
        contraint.fill = GridBagConstraints.BOTH;
        contraint.insets = new Insets(16, 16, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 0;
        contraint.gridy = 0;
        contraint.gridwidth = 3;
        contraint.gridheight = 1;
        contraint.weightx = 1;
        contraint.weighty = 0;
        layout.setConstraints(decriptionLabel, contraint);
        add(decriptionLabel);
        JLabel licenseFileNameLabel = new JLabel();
        licenseFileNameLabel.setText("Filename of license file:");
        contraint.fill = GridBagConstraints.BOTH;
        contraint.insets = new Insets(16, 16, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 0;
        contraint.gridy = 1;
        contraint.gridwidth = 2;
        contraint.gridheight = 1;
        contraint.weightx = 1;
        contraint.weighty = 0;
        layout.setConstraints(licenseFileNameLabel, contraint);
        add(licenseFileNameLabel);
        licenseFileNameField = new RequiredTextField();
        licenseFileNameField.setEditable(false);
        licenseFileNameField.setColumns(40);
        contraint.fill = GridBagConstraints.NONE;
        contraint.insets = new Insets(16, 16, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 0;
        contraint.gridy = 2;
        contraint.gridwidth = 2;
        contraint.gridheight = 1;
        contraint.weightx = 0;
        contraint.weighty = 0;
        layout.setConstraints(licenseFileNameField, contraint);
        add(licenseFileNameField);
        licenseFileNameButton = new JButton();
        licenseFileNameButton.setText("Change");
        licenseFileNameButton.setMnemonic('C');
        licenseFileNameButton.setSize(licenseFileNameButton.getSize().width, licenseFileNameField.getSize().height);
        licenseFileNameButton.addActionListener(this);
        contraint.fill = GridBagConstraints.NONE;
        contraint.insets = new Insets(16, 0, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 2;
        contraint.gridy = 2;
        contraint.gridwidth = 1;
        contraint.gridheight = 1;
        contraint.weightx = 0;
        contraint.weighty = 0;
        layout.setConstraints(licenseFileNameButton, contraint);
        add(licenseFileNameButton);
        JLabel encodingLabel = new JLabel();
        encodingLabel.setText("Encoding of license file:");
        contraint.fill = GridBagConstraints.BOTH;
        contraint.insets = new Insets(16, 16, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 0;
        contraint.gridy = 3;
        contraint.gridwidth = 2;
        contraint.gridheight = 1;
        contraint.weightx = 1;
        contraint.weighty = 0;
        layout.setConstraints(encodingLabel, contraint);
        add(encodingLabel);
        encodingField = new JTextField();
        encodingField.addFocusListener(this);
        encodingField.setColumns(12);
        contraint.fill = GridBagConstraints.HORIZONTAL;
        contraint.insets = new Insets(16, 16, 0, 16);
        contraint.anchor = GridBagConstraints.WEST;
        contraint.gridx = 0;
        contraint.gridy = 4;
        contraint.gridwidth = 1;
        contraint.gridheight = 1;
        contraint.weightx = 0;
        contraint.weighty = 0;
        layout.setConstraints(encodingField, contraint);
        add(encodingField);
        JPanel fillPanel = new JPanel();
        contraint.fill = GridBagConstraints.BOTH;
        contraint.insets = new Insets(4, 4, 4, 4);
        contraint.anchor = GridBagConstraints.CENTER;
        contraint.gridx = 3;
        contraint.gridy = 5;
        contraint.gridwidth = 1;
        contraint.gridheight = 1;
        contraint.weightx = 1;
        contraint.weighty = 1;
        layout.setConstraints(fillPanel, contraint);
        add(fillPanel);
    }

    /**
     * save
     */
    public void save() {
    }

    /**
     * initialize the panel
     */
    public void initialize(VAIProductModel model) {
        this.model = model;
        licenseFileNameField.addFocusListener(this);
        licenseFileNameField.initialize(model, "License File");
        if (model.getLicenseName() != null) {
            licenseFileNameField.setText(model.getLicenseName());
        }
        if (model.getLicenseEncoding() != null) {
            encodingField.setText(model.getLicenseEncoding());
        }
    }

    /**
     * stop
     */
    public void stop() {
    }

    /**
     * Implement the actionPerformed method
     * @param evt the action event
     */
    public void actionPerformed(ActionEvent evt) {
        try {
            Object source = evt.getSource();
            if (source == licenseFileNameButton) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogType(JFileChooser.OPEN_DIALOG);
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                String rootDirectory = (String) model.getPropertyList().get("vainstall.user.dir");
                if (rootDirectory == null) {
                    rootDirectory = System.getProperty("user.dir");
                }
                jfc.setCurrentDirectory(new File(rootDirectory));
                int result = jfc.showDialog(new JFrame(), "Select License File");
                if (result == JFileChooser.APPROVE_OPTION) {
                    licenseFileNameField.setText(jfc.getSelectedFile().getAbsolutePath());
                    model.getPropertyList().put("vainstall.user.dir", jfc.getCurrentDirectory().getAbsolutePath());
                    model.setLicenseName(jfc.getSelectedFile().getAbsolutePath());
                }
            }
        } catch (Exception exc) {
            return;
        }
    }

    public void focusGained(FocusEvent evt) {
    }

    public void focusLost(FocusEvent evt) {
        if (evt.getSource() == encodingField) {
            if (encodingField.getText().length() > 0) {
                model.setLicenseEncoding(encodingField.getText());
            }
        }
    }
}
