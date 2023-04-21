package com.sistemask.sc.frames.core;

import com.sistemask.sc.core.util.JFrameUtil;
import com.sistemask.sc.core.util.PropUtil;
import com.sistemask.sc.frames.core.util.HelpFrameUtil;
import com.sistemask.sc.model.config.Config;
import com.sistemask.sc.model.singleton.Data;
import java.util.Properties;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class LanguageJFrame extends javax.swing.JFrame {

    public LanguageJFrame() {
        initComponents();
        JFrameUtil.setFrameIcon(this);
    }

    void refresh() {
        HelpFrameUtil.setLanguagesInDropDown(this);
    }

    public JComboBox getDropDown() {
        return jComboBox1;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sistemask/sc/resources/properties/Language", Data.getInstance().getLocale());
        setTitle(bundle.getString("LabelLanguage"));
        setResizable(false);
        jLabel1.setText(bundle.getString("LabelLanguage"));
        jButton1.setText(bundle.getString("LabelAcept"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(43, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Properties _prop = new Properties();
        if (!Data.getInstance().getLocale().getLanguage().equals(jComboBox1.getSelectedItem().toString())) {
            _prop.setProperty(Config.LANGUAGE_PROPERTIE, jComboBox1.getSelectedItem().toString());
            PropUtil.save(Config.DATA_FILE_NAME, _prop);
            java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sistemask/sc/resources/properties/Language", Data.getInstance().getLocale());
            JOptionPane.showMessageDialog(this, bundle.getString("MsgInfoRestartConfig"), bundle.getString("LabelInformation"), JOptionPane.INFORMATION_MESSAGE);
        }
        this.dispose();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JLabel jLabel1;
}
