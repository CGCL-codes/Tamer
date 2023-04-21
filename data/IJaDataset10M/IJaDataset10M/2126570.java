package net.messze.jimposition.transformations;

import net.messze.jimposition.*;

public class ParityFilterPanel extends javax.swing.JPanel {

    /**
     * Creates new form NoSettingsPanel
     */
    public ParityFilterPanel() {
        initComponents();
    }

    public Boolean isEven() {
        return new Boolean(evenPagesRadio.isSelected());
    }

    public void setEven(Boolean even) {
        if (even.booleanValue()) evenPagesRadio.setSelected(true); else oddPagesRadio.setSelected(true);
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        evenPagesRadio = new javax.swing.JRadioButton();
        oddPagesRadio = new javax.swing.JRadioButton();
        jLabel2.setText("Selected pages:");
        evenPagesRadio.setText("Even pages");
        evenPagesRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        evenPagesRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        oddPagesRadio.setText("Odd pages");
        oddPagesRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        oddPagesRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(evenPagesRadio).add(oddPagesRadio)).addContainerGap(59, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(evenPagesRadio).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(oddPagesRadio).addContainerGap(21, Short.MAX_VALUE)));
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JRadioButton evenPagesRadio;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JRadioButton oddPagesRadio;
}
