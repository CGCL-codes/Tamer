package com.wisc.csvParser.plugins;

import com.wisc.csvParser.plugins.DataFilterApprise.events;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author  lukewinslow
 */
public class JPanelFilterApprise extends javax.swing.JPanel {

    private DataFilterApprise filter;

    /** Creates new form JPanelFilterApprise */
    public JPanelFilterApprise(DataFilterApprise filter) {
        initComponents();
        this.filter = filter;
        filter.addEventHandler(new DataFilterApprise.IEventHandler() {

            @Override
            public void eventRaised(events e) {
                handleEvent(e);
            }
        });
        individualErrorOption.setSelected(true);
        updateDisplay();
    }

    public void updateDisplay() {
        valuesRemovedLabel.setText("Values Removed: " + Long.toString(filter.getCountFiltered()));
        startedCB.setSelected(filter.isStarted());
        String valsToRmv = "";
        String varsToFilter = "";
        double[] vals = filter.getValuesToRemove();
        for (int i = 0; i < vals.length; i++) {
            if (i != vals.length - 1) {
                valsToRmv += Double.toString(vals[i]) + ",";
            } else {
                valsToRmv += Double.toString(vals[i]);
            }
        }
        ArrayList<String> vars = filter.getVariablesToFilter();
        for (int i = 0; i < vars.size(); i++) {
            varsToFilter += vars.get(i);
            if (i != vars.size() - 1) {
                varsToFilter += ",";
            }
        }
        variablesToFilterTB.setText(varsToFilter);
        valuesToFilterTB.setText(valsToRmv);
        if (filter.getRemoveType() == DataFilterApprise.removeTypes.removeErrorOnly) {
            individualErrorOption.setSelected(true);
        } else if (filter.getRemoveType() == DataFilterApprise.removeTypes.removeAllSameVariable) {
            allSameVariableOption.setSelected(true);
        } else if (filter.getRemoveType() == DataFilterApprise.removeTypes.removeAllBelow) {
            valuesBelowOption.setSelected(true);
        }
        startedCB.setSelected(filter.isStarted());
        globalSetEnabled(!filter.isStarted());
    }

    public void handleEvent(DataFilterApprise.events e) {
        if (e == DataFilterApprise.events.newVariableSeen) {
        } else if (e == DataFilterApprise.events.reconfigured) {
            updateDisplay();
        } else if (e == DataFilterApprise.events.started) {
            startedCB.setSelected(true);
            globalSetEnabled(false);
        } else if (e == DataFilterApprise.events.stopped) {
            startedCB.setSelected(false);
            globalSetEnabled(true);
        } else if (e == DataFilterApprise.events.valueFiltered) {
            valuesRemovedLabel.setText("Values Removed: " + Long.toString(filter.getCountFiltered()));
        }
    }

    public void globalSetEnabled(boolean isEnabled) {
        variablesToFilterTB.setEnabled(isEnabled);
        valuesToFilterTB.setEnabled(isEnabled);
        allSameVariableOption.setEnabled(isEnabled);
        individualErrorOption.setEnabled(isEnabled);
        valuesBelowOption.setEnabled(isEnabled);
        saveButton.setEnabled(isEnabled);
    }

    private void initComponents() {
        removalTypeGroup = new javax.swing.ButtonGroup();
        startedCB = new javax.swing.JCheckBox();
        variablesToFilterTB = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        valuesToFilterTB = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        valuesRemovedLabel = new javax.swing.JLabel();
        allSameVariableOption = new javax.swing.JRadioButton();
        individualErrorOption = new javax.swing.JRadioButton();
        valuesBelowOption = new javax.swing.JRadioButton();
        startedCB.setText("Started");
        startedCB.setEnabled(false);
        variablesToFilterTB.setText("WATER_TEMP");
        jLabel1.setText("Variable Types (Separate with ',')");
        jLabel2.setText("Values to Filter (Separate with ',')");
        valuesToFilterTB.setText("-99999,-6999,NaN");
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        valuesRemovedLabel.setText("Values Removed: 0");
        removalTypeGroup.add(allSameVariableOption);
        allSameVariableOption.setText("Remove all of same variable");
        removalTypeGroup.add(individualErrorOption);
        individualErrorOption.setSelected(true);
        individualErrorOption.setText("Remove individual error");
        removalTypeGroup.add(valuesBelowOption);
        valuesBelowOption.setText("Remove values below (greater offset)");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(64, 64, 64).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(variablesToFilterTB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 252, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 239, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 74, Short.MAX_VALUE)).add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(valuesRemovedLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(layout.createSequentialGroup().add(valuesToFilterTB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(valuesBelowOption).add(allSameVariableOption).add(individualErrorOption).add(saveButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(startedCB).addContainerGap(42, Short.MAX_VALUE)))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(startedCB).add(jLabel1)).add(7, 7, 7).add(variablesToFilterTB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(7, 7, 7).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(valuesToFilterTB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(39, 39, 39).add(allSameVariableOption).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(individualErrorOption).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(valuesBelowOption).add(12, 12, 12).add(saveButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(valuesRemovedLabel).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String[] vals = valuesToFilterTB.getText().split(",");
        String[] vars = variablesToFilterTB.getText().split(",");
        double[] parsed;
        ArrayList<String> newVars = new ArrayList<String>();
        for (String s : vars) {
            if (s.compareToIgnoreCase("") != 0) newVars.add(s);
        }
        parsed = new double[vals.length];
        try {
            for (int i = 0; i < parsed.length; i++) {
                parsed[i] = Double.valueOf(vals[i]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Check number formats.");
            e.printStackTrace();
            return;
        }
        filter.setValuesToRemove(parsed);
        filter.setVariablesToFilter(newVars);
        if (individualErrorOption.isSelected()) {
            filter.setRemoveType(DataFilterApprise.removeTypes.removeErrorOnly);
        } else if (allSameVariableOption.isSelected()) {
            filter.setRemoveType(DataFilterApprise.removeTypes.removeAllSameVariable);
        } else if (valuesBelowOption.isSelected()) {
            filter.setRemoveType(DataFilterApprise.removeTypes.removeAllBelow);
        }
    }

    private javax.swing.JRadioButton allSameVariableOption;

    private javax.swing.JRadioButton individualErrorOption;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.ButtonGroup removalTypeGroup;

    private javax.swing.JButton saveButton;

    private javax.swing.JCheckBox startedCB;

    private javax.swing.JRadioButton valuesBelowOption;

    private javax.swing.JLabel valuesRemovedLabel;

    private javax.swing.JTextField valuesToFilterTB;

    private javax.swing.JTextField variablesToFilterTB;
}
