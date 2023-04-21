package com.umc.gui.manager;

import org.jdesktop.application.Action;

public class UMCAboutBox extends javax.swing.JDialog {

    public UMCAboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
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
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.umc.gui.manager.GUI.class).getContext().getResourceMap(UMCAboutBox.class);
        setTitle(resourceMap.getString("title"));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.umc.gui.manager.GUI.class).getContext().getActionMap(UMCAboutBox.class, this);
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
        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text"));
        homepageLabel.setName("homepageLabel");
        appHomepageLabel.setText(resourceMap.getString("Application.homepage"));
        appHomepageLabel.setName("appHomepageLabel");
        appDescLabel.setText(resourceMap.getString("appDescLabel.text"));
        appDescLabel.setName("appDescLabel");
        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon"));
        imageLabel.setName("imageLabel");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(appDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE).addGap(168, 168, 168)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(appTitleLabel).addContainerGap(375, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(versionLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appVersionLabel).addContainerGap(453, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(homepageLabel).addGap(34, 34, 34).addComponent(appHomepageLabel).addContainerGap(237, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(545, Short.MAX_VALUE).addComponent(closeButton).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(imageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appTitleLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appDescLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(appVersionLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(homepageLabel).addComponent(appHomepageLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(closeButton).addContainerGap()));
        getAccessibleContext().setAccessibleName(resourceMap.getString("aboutBox.AccessibleContext.accessibleName"));
        pack();
    }

    private javax.swing.JButton closeButton;
}
