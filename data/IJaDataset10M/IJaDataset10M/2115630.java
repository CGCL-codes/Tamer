package ez_squeeze_empire;

import org.jdesktop.application.Action;
import ez_squeeze_empire.EZ_Squeeze_EmpireView.*;

@SuppressWarnings("serial")
public class EZ_Squeeze_EmpireAboutBox extends javax.swing.JDialog {

    public EZ_Squeeze_EmpireAboutBox(java.awt.Frame parent) {
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
        javax.swing.JLabel homepageLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ez_squeeze_empire.EZ_Squeeze_EmpireApp.class).getContext().getResourceMap(EZ_Squeeze_EmpireAboutBox.class);
        setTitle(resourceMap.getString("title"));
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ez_squeeze_empire.EZ_Squeeze_EmpireApp.class).getContext().getActionMap(EZ_Squeeze_EmpireAboutBox.class, this);
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
        appVersionLabel.setText(EZ_Squeeze_EmpireView.getVersion() + "");
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
        homepageLabel1.setFont(homepageLabel1.getFont().deriveFont(homepageLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel1.setText(resourceMap.getString("homepageLabel1.text"));
        homepageLabel1.setName("homepageLabel1");
        appHomepageLabel1.setText(resourceMap.getString("appHomepageLabel1.text"));
        appHomepageLabel1.setName("appHomepageLabel1");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(imageLabel).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(appTitleLabel).addComponent(appDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE).addComponent(closeButton, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(versionLabel).addComponent(vendorLabel).addComponent(homepageLabel).addComponent(homepageLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(appHomepageLabel1).addComponent(appVersionLabel).addComponent(appVendorLabel).addComponent(appHomepageLabel)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(appTitleLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(appDescLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(versionLabel).addComponent(appVersionLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(vendorLabel).addComponent(appVendorLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(homepageLabel).addComponent(appHomepageLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(homepageLabel1).addComponent(appHomepageLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE).addComponent(closeButton).addContainerGap()));
        pack();
    }

    private javax.swing.JButton closeButton;
}
