package desktop;

import org.jdesktop.application.Action;

public class DesktopAboutBox extends javax.swing.JDialog {

    public DesktopAboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    @Action
    public void closeAboutBox() {
        dispose();
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
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(desktop.DesktopApp.class).getContext().getResourceMap(DesktopAboutBox.class);
        setTitle(resourceMap.getString("title"));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(desktop.DesktopApp.class).getContext().getActionMap(DesktopAboutBox.class, this);
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
        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon"));
        imageLabel.setName("imageLabel");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(imageLabel).add(18, 18, 18).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(versionLabel).add(vendorLabel).add(homepageLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(appVersionLabel).add(appVendorLabel).add(appHomepageLabel))).add(org.jdesktop.layout.GroupLayout.LEADING, appTitleLabel).add(org.jdesktop.layout.GroupLayout.LEADING, appDescLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE).add(closeButton)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(imageLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createSequentialGroup().addContainerGap().add(appTitleLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(appDescLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(versionLabel).add(appVersionLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(vendorLabel).add(appVendorLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(homepageLabel).add(appHomepageLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE).add(closeButton).addContainerGap()));
        pack();
    }

    private javax.swing.JButton closeButton;
}
