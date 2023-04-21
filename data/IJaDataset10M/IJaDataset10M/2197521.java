package com.javathis.mapeditor;

import com.javathis.utilities.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Dialog displayed when the user request to change their preferences for the
 * Map Editor.
 */
public class PreferencesDialog extends JDialog {

    protected static final ResourceBundle MAIN_RESOURCE_BUNDLE = ResourceBundle.getBundle("com/javathis/mapeditor/properties/PreferencesDialog");

    private String newLnF;

    private TitledBorder LnFBorder;

    private JPanel mainPanel = new JPanel();

    private JPanel buttonPanel = new JPanel();

    private JRadioButton metalLnFRadioButton = new JRadioButton();

    private JRadioButton motifLnFRadioButton = new JRadioButton();

    private JRadioButton windowsLnFRadioButton = new JRadioButton();

    private JRadioButton macLnFRadioButton = new JRadioButton();

    private JButton cancelButton = new JButton();

    private JButton okButton = new JButton();

    private JPanel LnFPanel = new JPanel();

    private ButtonGroup LnFButtonGroup = new ButtonGroup();

    public PreferencesDialog(EditFrame owner, boolean modal, String currentLnF) {
        super(owner, modal);
        init();
        registerEvents();
        selectCurrentLnF(currentLnF);
    }

    private void selectCurrentLnF(String currentLnF) {
        if (currentLnF.equals(EditFrame.LNF_METAL)) {
            metalLnFRadioButton.setSelected(true);
        } else if (currentLnF.equals(EditFrame.LNF_MOTIF)) {
            motifLnFRadioButton.setSelected(true);
        } else if (currentLnF.equals(EditFrame.LNF_WINDOWS)) {
            windowsLnFRadioButton.setSelected(true);
        } else if (currentLnF.equals(EditFrame.LNF_MAC)) {
            macLnFRadioButton.setSelected(true);
        }
    }

    private void init() {
        this.setTitle(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("preferences.Text"));
        this.getContentPane().setLayout(new BorderLayout());
        this.setResizable(false);
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        LnFBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), MAIN_RESOURCE_BUNDLE.getString("LnFs.Text"));
        mainPanel.setLayout(new GridBagLayout());
        metalLnFRadioButton.setName("metal");
        metalLnFRadioButton.setText(MAIN_RESOURCE_BUNDLE.getString("metalLnF.Text"));
        metalLnFRadioButton.setMnemonic('M');
        metalLnFRadioButton.setEnabled(JTUtilities.isAvailableLookAndFeel(EditFrame.LNF_METAL));
        motifLnFRadioButton.setName("motif");
        motifLnFRadioButton.setText(MAIN_RESOURCE_BUNDLE.getString("motifLnF.Text"));
        motifLnFRadioButton.setMnemonic('O');
        motifLnFRadioButton.setEnabled(JTUtilities.isAvailableLookAndFeel(EditFrame.LNF_MOTIF));
        windowsLnFRadioButton.setName("windows");
        windowsLnFRadioButton.setText(MAIN_RESOURCE_BUNDLE.getString("windowsLnF.Text"));
        windowsLnFRadioButton.setMnemonic('W');
        windowsLnFRadioButton.setEnabled(JTUtilities.isAvailableLookAndFeel(EditFrame.LNF_WINDOWS));
        macLnFRadioButton.setName("mac");
        macLnFRadioButton.setText(MAIN_RESOURCE_BUNDLE.getString("macLnF.Text"));
        macLnFRadioButton.setMnemonic('A');
        macLnFRadioButton.setEnabled(JTUtilities.isAvailableLookAndFeel(EditFrame.LNF_MAC));
        cancelButton.setText(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("cancel.Text"));
        okButton.setText(JTUtilities.COMMON_RESOURCE_BUNDLE.getString("ok.Text"));
        LnFPanel.setLayout(new GridBagLayout());
        LnFPanel.setBorder(LnFBorder);
        mainPanel.add(LnFPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        LnFPanel.add(metalLnFRadioButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        LnFPanel.add(motifLnFRadioButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        LnFPanel.add(windowsLnFRadioButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        LnFPanel.add(macLnFRadioButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0, 0));
        buttonPanel.add(okButton, null);
        buttonPanel.add(cancelButton, null);
        LnFButtonGroup.add(metalLnFRadioButton);
        LnFButtonGroup.add(motifLnFRadioButton);
        LnFButtonGroup.add(windowsLnFRadioButton);
        LnFButtonGroup.add(macLnFRadioButton);
        this.getRootPane().setDefaultButton(okButton);
    }

    private void registerEvents() {
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) cancelButton.doClick();
            }
        });
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleOKButton();
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleCancelButton();
            }
        });
        macLnFRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleNewLnF(EditFrame.LNF_MAC);
            }
        });
        windowsLnFRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleNewLnF(EditFrame.LNF_WINDOWS);
            }
        });
        motifLnFRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleNewLnF(EditFrame.LNF_MOTIF);
            }
        });
        metalLnFRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleNewLnF(EditFrame.LNF_METAL);
            }
        });
    }

    private void handleOKButton() {
        ((EditFrame) getOwner()).setLookAndFeel(newLnF);
        dispose();
    }

    private void handleCancelButton() {
        dispose();
    }

    private void handleNewLnF(String newLnF) {
        this.newLnF = newLnF;
    }
}
