package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * COORDINATEINTERPOLATOR2DCustomizer.java
 * Created on Sep 11, 2007, 3:05 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class COORDINATEINTERPOLATOR2DCustomizer extends BaseCustomizer {

    private COORDINATEINTERPOLATOR2D ci2d;

    private JTextComponent target;

    private boolean insertCommas, insertLineBreaks = false;

    private int TUPLE_SIZE = 2;

    /** Creates new form COORDINATEINTERPOLATOR2DCustomizer */
    public COORDINATEINTERPOLATOR2DCustomizer(COORDINATEINTERPOLATOR2D ci2d, JTextComponent target) {
        super(ci2d);
        this.ci2d = ci2d;
        this.target = target;
        HelpCtx.setHelpIDString(this, "COORDINATEINTERPOLATOR2D_ELEM_HELPID");
        initComponents();
        keyTupleTable.setTitle("CoordinateInterpolator2D keys and corresponding 2D x-y coordinate arrays");
        keyTupleTable.setAddColumnButtonTooltip("Add column of coordinate pairs");
        keyTupleTable.setRemoveColumnButtonTooltip("Remove column of coordinate pairs");
        keyTupleTable.setAddRowButtonTooltip("Add row of keyed coordinate pairs");
        keyTupleTable.setRemoveRowButtonTooltip("Remove row of keyed coordinate pairs");
        keyTupleTable.setColumnsLabelText("columns of coordinate pairs");
        keyTupleTable.setRowsLabelText("rows of keyed coordinate pairs");
        keyTupleTable.setDefaultTupleValues(new String[] { "0", "0" });
        keyTupleTable.setColumnWidthAndResizeStrategy(true, 50);
        String[][] saa = ci2d.getKeysAndValues();
        keyTupleTable.setData(TUPLE_SIZE, saa);
        if (saa.length == 0) {
            String[][] defaultRow = new String[1][4];
            defaultRow[0] = new String[] { "0", "0", "0" };
            keyTupleTable.setData(TUPLE_SIZE, defaultRow);
            keyTupleTable.setData(TUPLE_SIZE, saa);
        }
        keyTupleTable.setInsertCommas(ci2d.isInsertCommas());
        keyTupleTable.setInsertLineBreaks(ci2d.isInsertLineBreaks());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        keyTupleTable = new org.web3d.x3d.palette.items.ExpandableKeyTupleTable();
        sortByKeyButton = new javax.swing.JButton();
        uniformKeyIntervalsButton = new javax.swing.JButton();
        eventHintPanel = new javax.swing.JPanel();
        eventLabel1 = new javax.swing.JLabel();
        eventLabel2 = new javax.swing.JLabel();
        setLayout(new java.awt.GridBagLayout());
        dEFUSEpanel1.setMinimumSize(new java.awt.Dimension(198, 77));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(keyTupleTable, gridBagConstraints);
        sortByKeyButton.setText(org.openide.util.NbBundle.getMessage(COORDINATEINTERPOLATOR2DCustomizer.class, "NORMALINTERPOLATORCustomizer.sortByKeyButton.text"));
        sortByKeyButton.setToolTipText("sort rows according to monotonically increasing keys");
        sortByKeyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByKeyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(sortByKeyButton, gridBagConstraints);
        uniformKeyIntervalsButton.setText("uniform key intervals");
        uniformKeyIntervalsButton.setToolTipText("set uniform key intervals from 0 to 1 for identical time durations between keyValue changes");
        uniformKeyIntervalsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniformKeyIntervalsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(uniformKeyIntervalsButton, gridBagConstraints);
        eventHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        eventHintPanel.setLayout(new java.awt.GridBagLayout());
        eventLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventLabel1.setText("<html>Primary input event is <b>set_fraction</b>");
        eventLabel1.setToolTipText("Create a ROUTE to connect input and output events");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        eventHintPanel.add(eventLabel1, gridBagConstraints);
        eventLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventLabel2.setText("<html>Primary output event is <b>value_changed</b>");
        eventLabel2.setToolTipText("Create a ROUTE to connect input and output events");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        eventHintPanel.add(eventLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(eventHintPanel, gridBagConstraints);
    }

    private void sortByKeyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String[][] saa = keyTupleTable.getData();
        if (saa.length == 0) return; else if (saa.length == 1) return;
        for (int i = 0; i < saa.length - 1; i++) {
            for (int j = i + 1; j < saa.length; j++) {
                if ((new SFFloat(saa[i][0])).getValue() > (new SFFloat(saa[j][0])).getValue()) {
                    String[] hold = saa[i];
                    saa[i] = saa[j];
                    saa[j] = hold;
                }
            }
        }
        keyTupleTable.setData(TUPLE_SIZE, saa);
    }

    private void uniformKeyIntervalsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String[][] saa = keyTupleTable.getData();
        if (saa.length == 0) return; else if (saa.length == 1) {
            saa[0][0] = "0";
        } else {
            float increment = 1.0f / ((float) saa.length - 1);
            for (int i = 0; i < saa.length; i++) {
                saa[i][0] = Float.toString(Math.round(i * increment * 10000.0f) / 10000.0f);
            }
        }
        keyTupleTable.setData(TUPLE_SIZE, saa);
    }

    private javax.swing.JPanel eventHintPanel;

    private javax.swing.JLabel eventLabel1;

    private javax.swing.JLabel eventLabel2;

    private org.web3d.x3d.palette.items.ExpandableKeyTupleTable keyTupleTable;

    private javax.swing.JButton sortByKeyButton;

    private javax.swing.JButton uniformKeyIntervalsButton;

    @Override
    public String getNameKey() {
        return "NAME_X3D_COORDINATEINTERPOLATOR2D";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        ci2d.setKeysAndValues(keyTupleTable.getData());
        ci2d.setInsertCommas(keyTupleTable.isInsertCommasSet());
        ci2d.setInsertLineBreaks(keyTupleTable.isInsertLineBreaksSet());
    }
}
