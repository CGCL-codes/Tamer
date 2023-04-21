package com.jme3.gde.core.codeless;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JPanel;
import org.openide.filesystems.FileChooserBuilder;

public final class CodelessProjectWizardVisualPanel1 extends JPanel {

    /** Creates new form CodelessProjectWizardVisualPanel1 */
    public CodelessProjectWizardVisualPanel1() {
        initComponents();
    }

    @Override
    public String getName() {
        return "Specify Folders";
    }

    public String getProjectPath() {
        return jTextField1.getText();
    }

    public String getAssetsPath() {
        return jTextField2.getText();
    }

    public void setListener(ActionListener listener) {
        jTextField1.addActionListener(listener);
        jTextField2.addActionListener(listener);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jPanel1.border.title")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel5.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel6.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel7.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel8.text"));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel7).addComponent(jLabel5).addComponent(jLabel8)).addContainerGap(22, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7).addGap(18, 18, 18).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE).addComponent(jLabel8).addContainerGap()));
        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel3.text"));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel1.text"));
        jToolBar1.add(jLabel1);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 21, Short.MAX_VALUE));
        jToolBar1.add(jPanel2);
        jTextField1.setText(org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jTextField1.text"));
        jToolBar1.add(jTextField1);
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jButton1.text"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel2.text"));
        jToolBar2.add(jLabel2);
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 0));
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 21, Short.MAX_VALUE));
        jToolBar2.add(jPanel3);
        jTextField2.setText(org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jTextField2.text"));
        jToolBar2.add(jTextField2);
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jButton2.text"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton2);
        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CodelessProjectWizardVisualPanel1.class, "CodelessProjectWizardVisualPanel1.jLabel4.text"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        FileChooserBuilder builder = new FileChooserBuilder(System.getProperty("user.home"));
        builder.setDirectoriesOnly(true);
        builder.setTitle("Select Project Folder");
        File file = builder.showOpenDialog();
        if (file != null) {
            jTextField1.setText(file.getPath());
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        File path = new File(jTextField1.getText());
        if (path.isDirectory()) {
            FileChooserBuilder builder = new FileChooserBuilder(path.getAbsolutePath());
            builder.setDirectoriesOnly(true);
            builder.setTitle("Select Assets Folder");
            File file = builder.showOpenDialog();
            if (file != null) {
                try {
                    jTextField2.setText(file.getAbsolutePath().substring(path.getAbsolutePath().length(), file.getAbsolutePath().length()));
                } catch (Exception e) {
                }
            }
        }
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JToolBar jToolBar2;
}
