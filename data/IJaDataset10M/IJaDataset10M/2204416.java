package com.jme3.gde.angelfont;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class AngelFontVisualPanel2 extends JPanel {

    String fontName = "";

    int fontSize = 16;

    int imageSize = 256;

    int paddingX = 0;

    int paddingY = 0;

    int letterSpacing = 0;

    String fileName = "";

    int style = Font.PLAIN;

    /** Creates new form AngelFontVisualPanel2 */
    public AngelFontVisualPanel2() {
        initComponents();
        jComboBox1.removeAllItems();
        jComboBox1.addItem("PLAIN");
        jComboBox1.addItem("ITALIC");
        jComboBox1.addItem("BOLD");
    }

    @Override
    public String getName() {
        return "Configure Font";
    }

    public void setFont(String name) {
        this.fontName = name;
        jTextField1.setText(fontName);
        jLabel10.setText(fontName);
        updateFont();
    }

    public String getFileName() {
        return jTextField1.getText();
    }

    private void updateFont() {
        jLabel3.setIcon(new ImageIcon(FontCreator.buildFont(fontName, getFileName(), imageSize, fontSize, style, paddingX, paddingY, letterSpacing, true).getImage()));
        jLabel3.repaint();
        jPanel1.repaint();
    }

    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jButton1.text"));
        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel3.text"));
        jPanel1.add(jLabel3);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jPanel2.border.title")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel7.text"));
        jTextField1.setText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jTextField1.text"));
        jTextField1.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jTextField1.toolTipText"));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel8.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel2.text"));
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(256), Integer.valueOf(64), null, Integer.valueOf(1)));
        jSpinner2.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jSpinner2.toolTipText"));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateImageSize(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel4.text"));
        jSpinner3.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jSpinner3.toolTipText"));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updatePaddingX(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel5.text"));
        jSpinner4.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jSpinner4.toolTipText"));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updatePaddingY(evt);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addComponent(jLabel5).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSpinner3, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jSpinner2).addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)).addComponent(jSpinner4, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel8).addGap(45, 45, 45)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jPanel3.border.title")));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel1.text"));
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(16), Integer.valueOf(1), null, Integer.valueOf(1)));
        jSpinner1.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jSpinner1.toolTipText"));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateFontSize(evt);
            }
        });
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jComboBox1.toolTipText"));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel6.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel9.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel10.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jLabel11.text"));
        jSpinner5.setToolTipText(org.openide.util.NbBundle.getMessage(AngelFontVisualPanel2.class, "AngelFontVisualPanel2.jSpinner5.toolTipText"));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                letterSpacingUpdate(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel11).addComponent(jLabel9).addComponent(jLabel1).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE).addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE).addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 78, Short.MAX_VALUE).addComponent(jSpinner5, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
    }

    private void updateFontSize(javax.swing.event.ChangeEvent evt) {
        fontSize = (Integer) jSpinner1.getValue();
        updateFont();
    }

    private void updateImageSize(javax.swing.event.ChangeEvent evt) {
        imageSize = (Integer) jSpinner2.getValue();
        updateFont();
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        if ("PLAIN".equals(jComboBox1.getSelectedItem())) {
            style = Font.PLAIN;
        } else if ("BOLD".equals(jComboBox1.getSelectedItem())) {
            style = Font.BOLD;
        } else if ("ITALIC".equals(jComboBox1.getSelectedItem())) {
            style = Font.ITALIC;
        }
        updateFont();
    }

    private void updatePaddingX(javax.swing.event.ChangeEvent evt) {
        paddingX = (Integer) jSpinner3.getValue();
        updateFont();
    }

    private void updatePaddingY(javax.swing.event.ChangeEvent evt) {
        paddingY = (Integer) jSpinner4.getValue();
        updateFont();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        fileName = jTextField1.getText();
    }

    private void letterSpacingUpdate(javax.swing.event.ChangeEvent evt) {
        letterSpacing = (Integer) jSpinner5.getValue();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JSpinner jSpinner1;

    private javax.swing.JSpinner jSpinner2;

    private javax.swing.JSpinner jSpinner3;

    private javax.swing.JSpinner jSpinner4;

    private javax.swing.JSpinner jSpinner5;

    private javax.swing.JTextField jTextField1;
}
