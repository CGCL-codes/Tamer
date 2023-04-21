package org.cashforward.ui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.cashforward.ui.internal.options.UIOptions;

final class SystemSettingsPanel extends javax.swing.JPanel {

    private final EntryOptionsPanelController controller;

    SystemSettingsPanel(final EntryOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        cbDebugOn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                controller.changed();
            }
        });
    }

    private void initComponents() {
        cbDebugOn = new javax.swing.JCheckBox();
        org.openide.awt.Mnemonics.setLocalizedText(cbDebugOn, org.openide.util.NbBundle.getMessage(SystemSettingsPanel.class, "SystemSettingsPanel.cbDebugOn.text"));
        cbDebugOn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDebugOnActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(cbDebugOn).addContainerGap(266, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(cbDebugOn).addContainerGap(265, Short.MAX_VALUE)));
    }

    private void cbDebugOnActionPerformed(java.awt.event.ActionEvent evt) {
    }

    void load() {
        cbDebugOn.setSelected(UIOptions.isDebuggingOn());
    }

    void store() {
        UIOptions.setDebuggingOn(cbDebugOn.isSelected());
    }

    boolean valid() {
        return true;
    }

    private javax.swing.JCheckBox cbDebugOn;
}
