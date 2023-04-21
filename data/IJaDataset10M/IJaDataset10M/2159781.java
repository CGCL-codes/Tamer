package mobat.bonesa;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.jdesktop.application.Action;

public class BonesaAboutBox extends javax.swing.JDialog {

    public BonesaAboutBox(java.awt.Frame parent) {
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
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bonesa");
        setFont(Bonesa.getDefaultFont());
        setModal(true);
        setName("aboutBox");
        setResizable(false);
        closeButton.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        closeButton.setText("Close");
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel.setText("Version");
        versionLabel.setName("versionLabel");
        appVersionLabel.setText(Bonesa.VERSION);
        appVersionLabel.setName("appVersionLabel");
        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText("By");
        vendorLabel.setName("vendorLabel");
        appVendorLabel.setText("Vrije Universiteit Amsterdam");
        appVendorLabel.setName("appVendorLabel");
        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText("Website");
        homepageLabel.setName("homepageLabel");
        appHomepageLabel.setText("http://www.cs.vu.nl/ci");
        appHomepageLabel.setName("appHomepageLabel");
        appDescLabel.setText("");
        appDescLabel.setName("appDescLabel");
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mobat/bonesa/resources/logo.png")));
        imageLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        imageLabel.setName("imageLabel");
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mobat/bonesa/resources/bonesa.png")));
        jLabel10.setName("jLabel10");
        jLabel11.setFont(jLabel11.getFont());
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Icons by DryIcons.com");
        jLabel11.setName("jLabel11");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11ClickIconCredits(evt);
            }
        });
        homepageLabel1.setFont(homepageLabel1.getFont().deriveFont(homepageLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel1.setText("Icons: ");
        homepageLabel1.setName("homepageLabel1");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(imageLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 467, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(119, 119, 119).add(appDescLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(336, 336, 336).add(closeButton)).add(layout.createSequentialGroup().add(85, 85, 85).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(versionLabel).add(vendorLabel).add(homepageLabel).add(homepageLabel1)).add(101, 101, 101).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(appHomepageLabel).add(appVersionLabel).add(appVendorLabel)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().add(appDescLabel).add(79, 79, 79)).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel10, 0, 0, Short.MAX_VALUE).add(layout.createSequentialGroup().add(imageLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18))).add(43, 43, 43).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(versionLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(vendorLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(homepageLabel)).add(layout.createSequentialGroup().add(appVersionLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(appVendorLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(appHomepageLabel))).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(homepageLabel1).add(jLabel11)).add(40, 40, 40).add(closeButton).add(19, 19, 19)));
        pack();
    }

    private void jLabel11ClickIconCredits(java.awt.event.MouseEvent evt) {
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        try {
            java.net.URI uri = new java.net.URI("http://dryicons.com/");
            desktop.browse(uri);
        } catch (Exception e) {
        }
    }

    private javax.swing.JButton closeButton;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;
}
