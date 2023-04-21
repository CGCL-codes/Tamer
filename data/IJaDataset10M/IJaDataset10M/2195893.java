package org.xlim.sic.ig.exploitation.impl;

import java.io.File;
import javax.swing.JPanel;
import org.openide.util.NbBundle;

public final class SaveSimulationToXMLVisualPanel1 extends JPanel {

    /** Creates new form SaveSimulationToXMLVisualPanel1 */
    public SaveSimulationToXMLVisualPanel1() {
        initComponents();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SaveSimulationToXMLVisualPanel1.class, "SaveSimulationToXMLVisualPanel1.getName");
    }

    public File getSelection() {
        jFileChooser1.approveSelection();
        return jFileChooser1.getSelectedFile();
    }

    private void initComponents() {
        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser1.setControlButtonsAreShown(false);
        jFileChooser1.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        jFileChooser1.setSelectedFile(new java.io.File("/./output.xml"));
        jFileChooser1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE));
    }

    private javax.swing.JFileChooser jFileChooser1;
}
