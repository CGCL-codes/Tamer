package com.sistemask.sc.frames.core;

import com.sistemask.sc.core.util.JFrameUtil;
import com.sistemask.sc.model.singleton.Data;
import java.awt.Color;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;

public class EditorGuideJFrame extends javax.swing.JFrame {

    private Color colorDefault;

    public EditorGuideJFrame() {
        initComponents();
        JFrameUtil.setFrameIcon(this);
        ColorSelectionModel _modelColor;
        _modelColor = this.jColorChooser1.getSelectionModel();
        _modelColor.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jColorChooser1StateChanged(evt);
            }
        });
    }

    public void update() {
        Data _data = Data.getInstance();
        this.colorDefault = _data.getGuideConfigBean().getDefaultColor();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jColorChooser1 = new javax.swing.JColorChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sistemask/sc/resources/properties/Language", Data.getInstance().getLocale());
        jButton1.setText(bundle.getString("LabelAcept"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText(bundle.getString("LabelCancel"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jColorChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGap(120, 120, 120).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jColorChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton2).addComponent(jButton1)).addContainerGap(14, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        Data _data = Data.getInstance();
        _data.getGuideConfigBean().setDefaultColor(this.colorDefault);
        this.setVisible(false);
    }

    private void jColorChooser1StateChanged(ChangeEvent evt) {
        Data _data = Data.getInstance();
        _data.getGuideConfigBean().setDefaultColor(this.jColorChooser1.getColor());
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JColorChooser jColorChooser1;
}
