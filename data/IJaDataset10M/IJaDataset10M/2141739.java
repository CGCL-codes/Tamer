package jsattrak.gui;

import jsattrak.objects.AbstractSatellite;
import jsattrak.utilities.J3DEarthComponent;

/**
 *
 * @author  sgano
 */
public class JTerrainProfileDialog extends javax.swing.JDialog {

    private boolean okHit = false;

    JSatTrak app;

    J3DEarthComponent j3dDialog;

    /** Creates new form JTerrainProfileDialog */
    public JTerrainProfileDialog(java.awt.Frame parent, boolean modal, JSatTrak app, J3DEarthComponent j3dDialog) {
        super(parent, modal);
        this.app = app;
        this.j3dDialog = j3dDialog;
        initComponents();
        try {
            showTPCheckBox.setSelected(j3dDialog.getTerrainProfileEnabled());
            satComboBox.removeAllItems();
            for (AbstractSatellite sat : app.getSatHash().values()) {
                satComboBox.addItem(sat.getName());
            }
            satComboBox.setSelectedItem(j3dDialog.getTerrainProfileSat());
            longSpanTextField.setText("" + j3dDialog.getTerrainProfileLongSpan());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        showTPCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        satComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        longSpanTextField = new javax.swing.JTextField();
        applyButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Terrain Profile Settings");
        showTPCheckBox.setText("Show Terrain Profiler");
        showTPCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTPCheckBoxActionPerformed(evt);
            }
        });
        jLabel1.setText("Profiler Satellite:");
        satComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel2.setText("+/- Longitude Span [deg]:");
        longSpanTextField.setText("10.0");
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(showTPCheckBox).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(longSpanTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(satComboBox, 0, 129, Short.MAX_VALUE)))))).addGroup(layout.createSequentialGroup().addComponent(applyButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(showTPCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(satComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(longSpanTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(applyButton).addComponent(okButton).addComponent(cancelButton))));
        pack();
    }

    private void showTPCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean updateMaps = saveSettings();
        app.forceRepainting(updateMaps);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean updateMaps = saveSettings();
        okHit = true;
        app.forceRepainting(updateMaps);
        try {
            this.dispose();
        } catch (Exception e) {
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            this.dispose();
        } catch (Exception e) {
        }
    }

    private javax.swing.JButton applyButton;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField longSpanTextField;

    private javax.swing.JButton okButton;

    private javax.swing.JComboBox satComboBox;

    private javax.swing.JCheckBox showTPCheckBox;

    private boolean saveSettings() {
        boolean updateMapData = false;
        j3dDialog.setTerrainProfileSat((String) satComboBox.getSelectedItem());
        try {
            j3dDialog.setTerrainProfileLongSpan(Double.parseDouble(longSpanTextField.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        j3dDialog.setTerrainProfileEnabled(showTPCheckBox.isSelected());
        return updateMapData;
    }
}
