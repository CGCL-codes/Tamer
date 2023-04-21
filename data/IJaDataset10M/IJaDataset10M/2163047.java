package org.jbudget.gui.templ;

/**
 *
 * @author  petrov
 */
public class InitializeWisardPage1 extends javax.swing.JPanel {

    /** Creates new form FirstLoginWisardPage1 */
    public InitializeWisardPage1() {
        initComponents();
    }

    private void initComponents() {
        mainPanel = new org.jdesktop.swingx.JXTitledPanel();
        infoLabel1 = new javax.swing.JLabel();
        infoLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        passwordLabel1 = new javax.swing.JLabel();
        passwordField1 = new javax.swing.JPasswordField();
        passwordLabel2 = new javax.swing.JLabel();
        passwordField2 = new javax.swing.JPasswordField();
        passwordsMatchLabel = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanel.setTitle("Initializing New Data Directory");
        mainPanel.setTitleFont(new java.awt.Font("Dialog", 1, 16));
        mainPanel.setPreferredSize(new java.awt.Dimension(650, 230));
        infoLabel1.setText("Step1: Set up password for the 'system' account. ");
        infoLabel2.setText("'system' is the name of the administrator account in jBudget.");
        passwordLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
        passwordLabel1.setText("Password:");
        passwordField1.setFont(new java.awt.Font("Dialog", 0, 16));
        passwordLabel2.setFont(new java.awt.Font("Dialog", 1, 16));
        passwordLabel2.setText("Retype Password:");
        passwordField2.setFont(new java.awt.Font("Dialog", 0, 16));
        passwordsMatchLabel.setFont(new java.awt.Font("Dialog", 1, 14));
        passwordsMatchLabel.setText("test");
        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel.getContentContainer());
        mainPanel.getContentContainer().setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE).add(mainPanelLayout.createSequentialGroup().addContainerGap().add(infoLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 469, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(118, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE).add(infoLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 563, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()).add(mainPanelLayout.createSequentialGroup().add(140, 140, 140).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(passwordLabel1).add(passwordLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, passwordsMatchLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(passwordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(passwordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanelLayout.createSequentialGroup().addContainerGap().add(infoLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(infoLabel2).add(16, 16, 16).add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(25, 25, 25).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(passwordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(passwordLabel1)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(passwordField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(passwordLabel2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(passwordsMatchLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE).addContainerGap()));
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JLabel infoLabel1;

    private javax.swing.JLabel infoLabel2;

    private javax.swing.JSeparator jSeparator1;

    private org.jdesktop.swingx.JXTitledPanel mainPanel;

    public javax.swing.JPasswordField passwordField1;

    public javax.swing.JPasswordField passwordField2;

    private javax.swing.JLabel passwordLabel1;

    private javax.swing.JLabel passwordLabel2;

    public javax.swing.JLabel passwordsMatchLabel;
}
