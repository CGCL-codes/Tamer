package org.mitre.ocil.refimpl.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.jdesktop.application.Action;
import org.mitre.ocil.refimpl.gui.common.DocUtilities;

public class AboutBox extends javax.swing.JDialog {

    public AboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
        File licenseFile = DocUtilities.getFile(new File(OCILInterpreterApp.getHomeDirectory()), OCILInterpreterApp.getLicenseFilename());
        try {
            BufferedReader in = new BufferedReader(new FileReader(licenseFile));
            String str;
            String text = "";
            while ((str = in.readLine()) != null) {
                text += str + "\n";
            }
            licenseTextPanel.setText(text);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Action
    public void closeAboutBox() {
        setVisible(false);
    }

    private void initComponents() {
        closeButton = new javax.swing.JButton();
        javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        licenseTextPanel = new javax.swing.JTextPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.mitre.ocil.refimpl.gui.OCILInterpreterApp.class).getContext().getResourceMap(AboutBox.class);
        setTitle(resourceMap.getString("title"));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.mitre.ocil.refimpl.gui.OCILInterpreterApp.class).getContext().getActionMap(AboutBox.class, this);
        closeButton.setAction(actionMap.get("closeAboutBox"));
        closeButton.setName("closeButton");
        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize() + 4));
        appTitleLabel.setText(resourceMap.getString("Application.title"));
        appTitleLabel.setName("appTitleLabel");
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel.setText(resourceMap.getString("versionLabel.text"));
        versionLabel.setName("versionLabel");
        appVersionLabel.setText(resourceMap.getString("Application.version"));
        appVersionLabel.setName("appVersionLabel");
        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text"));
        vendorLabel.setName("vendorLabel");
        appVendorLabel.setText(resourceMap.getString("Application.vendor"));
        appVendorLabel.setName("appVendorLabel");
        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text"));
        homepageLabel.setName("homepageLabel");
        appHomepageLabel.setText(resourceMap.getString("Application.homepage"));
        appHomepageLabel.setName("appHomepageLabel");
        appDescLabel.setText(resourceMap.getString("appDescLabel.text"));
        appDescLabel.setName("appDescLabel");
        jScrollPane1.setName("jScrollPane1");
        licenseTextPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4));
        licenseTextPanel.setEditable(false);
        licenseTextPanel.setText(resourceMap.getString("licenseTextPanel.text"));
        licenseTextPanel.setFocusable(false);
        licenseTextPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        licenseTextPanel.setName("licenseTextPanel");
        jScrollPane1.setViewportView(licenseTextPanel);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(appDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE).addComponent(closeButton, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(versionLabel).addComponent(vendorLabel).addComponent(homepageLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(appVersionLabel).addComponent(appVendorLabel).addComponent(appHomepageLabel))).addComponent(appTitleLabel)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(appTitleLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appDescLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(appVersionLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(vendorLabel).addComponent(appVendorLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(homepageLabel).addComponent(appHomepageLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(closeButton).addContainerGap()));
        pack();
    }

    private javax.swing.JButton closeButton;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextPane licenseTextPanel;
}
