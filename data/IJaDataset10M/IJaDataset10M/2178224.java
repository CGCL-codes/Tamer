package org.octave.ide.script.virtualconsole.ui;

import java.io.File;
import javax.swing.JFileChooser;
import org.octave.ide.script.virtualconsole.octave.OctaveConfig;
import org.openide.util.NbPreferences;

final class OctaveConfigurationPanel extends javax.swing.JPanel {

    private final OctaveConfigurationOptionsPanelController controller;

    public static String OCTAVE_HOME_KEY = "OCTAVE_HOME";

    OctaveConfigurationPanel(OctaveConfigurationOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        octaveHomeTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        setBackground(java.awt.Color.white);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, "Octave Home:");
        octaveHomeTextField.setText("Select Path");
        octaveHomeTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                octaveHomeTextFieldActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, "Browse...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(octaveHomeTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(jButton1).add(octaveHomeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void octaveHomeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String filename = octaveHomeTextField.getText();
        JFileChooser chooser = new JFileChooser(new File(filename));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            octaveHomeTextField.setText(selectedFile.getAbsolutePath());
            controller.changed();
        }
    }

    void load() {
        octaveHomeTextField.setText(NbPreferences.forModule(OctaveConfigurationPanel.class).get(OCTAVE_HOME_KEY, OctaveConfig.octaveHome));
    }

    void store() {
        NbPreferences.forModule(OctaveConfigurationPanel.class).put(OCTAVE_HOME_KEY, octaveHomeTextField.getText());
        OctaveConfig.octaveHome = octaveHomeTextField.getText();
        OctaveConfig.prog = OctaveConfig.octaveHome + File.separator + "bin" + File.separator + "octave";
    }

    boolean valid() {
        return true;
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField octaveHomeTextField;
}
