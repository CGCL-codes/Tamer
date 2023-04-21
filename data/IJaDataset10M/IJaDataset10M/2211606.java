package com.xdh.export.ui;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

public class ExportAboutBox extends JDialog {

    private JButton closeButton;

    public ExportAboutBox(Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(this.closeButton);
    }

    @Action
    public void closeAboutBox() {
        dispose();
    }

    private void initComponents() {
        this.closeButton = new JButton();
        JLabel appTitleLabel = new JLabel();
        JLabel versionLabel = new JLabel();
        JLabel appVersionLabel = new JLabel();
        JLabel vendorLabel = new JLabel();
        JLabel appVendorLabel = new JLabel();
        JLabel homepageLabel = new JLabel();
        JLabel appHomepageLabel = new JLabel();
        JLabel appDescLabel = new JLabel();
        JLabel imageLabel = new JLabel();
        setDefaultCloseOperation(2);
        ResourceMap resourceMap = ((ExportApp) Application.getInstance(ExportApp.class)).getContext().getResourceMap(ExportAboutBox.class);
        setTitle(resourceMap.getString("title", new Object[0]));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        ActionMap actionMap = ((ExportApp) Application.getInstance(ExportApp.class)).getContext().getActionMap(ExportAboutBox.class, this);
        this.closeButton.setAction(actionMap.get("closeAboutBox"));
        this.closeButton.setName("closeButton");
        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | 0x1, appTitleLabel.getFont().getSize() + 4));
        appTitleLabel.setText(resourceMap.getString("Application.title", new Object[0]));
        appTitleLabel.setName("appTitleLabel");
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | 0x1));
        versionLabel.setText(resourceMap.getString("versionLabel.text", new Object[0]));
        versionLabel.setName("versionLabel");
        appVersionLabel.setText(resourceMap.getString("Application.version", new Object[0]));
        appVersionLabel.setName("appVersionLabel");
        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | 0x1));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text", new Object[0]));
        vendorLabel.setName("vendorLabel");
        appVendorLabel.setText(resourceMap.getString("Application.vendor", new Object[0]));
        appVendorLabel.setName("appVendorLabel");
        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | 0x1));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text", new Object[0]));
        homepageLabel.setName("homepageLabel");
        appHomepageLabel.setText(resourceMap.getString("Application.homepage", new Object[0]));
        appHomepageLabel.setName("appHomepageLabel");
        appDescLabel.setText(resourceMap.getString("appDescLabel.text", new Object[0]));
        appDescLabel.setName("appDescLabel");
        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon"));
        imageLabel.setName("imageLabel");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(imageLabel).add(18, 18, 18).add(layout.createParallelGroup(2).add(1, layout.createSequentialGroup().add(layout.createParallelGroup(1).add(versionLabel).add(vendorLabel).add(homepageLabel)).addPreferredGap(0).add(layout.createParallelGroup(1).add(appVersionLabel).add(appVendorLabel).add(appHomepageLabel))).add(1, appTitleLabel).add(1, appDescLabel, -1, 316, 32767).add(this.closeButton)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(1).add(imageLabel, -2, -1, 32767).add(layout.createSequentialGroup().addContainerGap().add(appTitleLabel).addPreferredGap(0).add(appDescLabel).addPreferredGap(0).add(layout.createParallelGroup(3).add(versionLabel).add(appVersionLabel)).addPreferredGap(0).add(layout.createParallelGroup(3).add(vendorLabel).add(appVendorLabel)).addPreferredGap(0).add(layout.createParallelGroup(3).add(homepageLabel).add(appHomepageLabel)).addPreferredGap(0, 14, 32767).add(this.closeButton).addContainerGap()));
        pack();
    }
}
