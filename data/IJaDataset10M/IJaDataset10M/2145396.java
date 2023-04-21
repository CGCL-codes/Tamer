package com.cidero.control;

import java.util.logging.Logger;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import com.cidero.util.AppPreferences;

/**
 *  Server options configuration dialog
 *
 *  Supported Options:
 *
 *  - Patch for Windows Media Connect discovery bug
 *    WMC (due to bug in Windows XP UPnP stack actually) doesn't respond
 *    to search requests from a control point on the same host as the server.
 *    The workaround is to enable manual search on the local host
 *
 */
public class ServerOptionsDialog extends JDialog {

    private static final Logger logger = Logger.getLogger("com.cidero.control");

    MediaController controller;

    AppPreferences pref;

    JPanel serverSpecificPanel = new JPanel();

    JCheckBox wmcManualSearchCheckBox = new JCheckBox("Enable discovery bug patch for Windows Media Connect on local host");

    JPanel buttonPanel = new JPanel();

    JButton okButton = new JButton("OK");

    JButton applyButton = new JButton("Apply");

    JButton cancelButton = new JButton("Cancel");

    /** 
   * Dialog is a singleton
   */
    static ServerOptionsDialog dialog = null;

    public static ServerOptionsDialog getInstance() {
        if (dialog == null) dialog = new ServerOptionsDialog(MediaController.getInstance());
        return dialog;
    }

    /** 
   * Constructor
   */
    private ServerOptionsDialog(MediaController controller) {
        super(controller.getFrame(), "Server Options", false);
        this.controller = controller;
        pref = controller.getPreferences();
        setUIFromPreferences();
        setLocationRelativeTo(controller.getFrame());
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout serverSpecificLayout = new GridBagLayout();
        serverSpecificPanel.setLayout(serverSpecificLayout);
        serverSpecificPanel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Server-Specific Options"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        serverSpecificLayout.setConstraints(wmcManualSearchCheckBox, gbc);
        serverSpecificPanel.add(wmcManualSearchCheckBox);
        cp.add(serverSpecificPanel, BorderLayout.CENTER);
        okButton.addActionListener(new OkActionListener());
        applyButton.addActionListener(new ApplyActionListener());
        cancelButton.addActionListener(new CancelActionListener());
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        cp.add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
   *  Button action listeners
   */
    public class OkActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            updatePreferences();
            setVisible(false);
        }
    }

    public class ApplyActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            updatePreferences();
        }
    }

    public class CancelActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setUIFromPreferences();
            setVisible(false);
        }
    }

    public void setUIFromPreferences() {
        wmcManualSearchCheckBox.setSelected(pref.getBoolean("wmcDiscoveryBugPatchEnable", false));
    }

    public void updatePreferences() {
        if (wmcManualSearchCheckBox.isSelected()) {
            pref.putBoolean("wmcDiscoveryBugPatchEnable", true);
        } else {
            pref.putBoolean("wmcDiscoveryBugPatchEnable", false);
            pref.put("wmcDeviceDescriptionURL", "");
        }
    }
}
