package org.web3d.x3d.palette.items;

import java.awt.Dimension;
import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;

/**
 * MATRIX4VERTEXATTRIBUTECustomizer.java
 * Created on 16 January 2010
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class MATRIX4VERTEXATTRIBUTECustomizer extends BaseCustomizer {

    private MATRIX4VERTEXATTRIBUTE m4va;

    private JTextComponent target;

    public MATRIX4VERTEXATTRIBUTECustomizer(MATRIX4VERTEXATTRIBUTE m4va, JTextComponent target) {
        super(m4va);
        this.m4va = m4va;
        this.target = target;
        HelpCtx.setHelpIDString(this, "MATRIX4VERTEXATTRIBUTE_ELEM_HELPID");
        initComponents();
        nameTF.setMinimumSize(new Dimension(nameTF.getPreferredSize()));
        nameTF.setText(m4va.getName());
        expandableList.setTitle("4x4 matrix arrays");
        expandableList.setColumnTitles(new String[] { "#", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" });
        expandableList.doIndexInFirstColumn(true);
        String[][] saa = m4va.getValues();
        expandableList.setData(saa);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        contentModelLabel = new javax.swing.JLabel();
        nameTF = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        expandableList = new org.web3d.x3d.palette.items.ExpandableList();
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(dEFUSEpanel1, gridBagConstraints);
        contentModelLabel.setText("Matrix4VertexAttribute is used for shader field initialization");
        contentModelLabel.setToolTipText("PackagedShader describes a single program having one or more shaders and effects");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(contentModelLabel, gridBagConstraints);
        nameTF.setColumns(20);
        nameTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(nameTF, gridBagConstraints);
        nameLabel.setText("name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(nameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(expandableList, gridBagConstraints);
    }

    private void nameTFActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JLabel contentModelLabel;

    private org.web3d.x3d.palette.items.ExpandableList expandableList;

    private javax.swing.JLabel nameLabel;

    private javax.swing.JTextField nameTF;

    @Override
    public String getNameKey() {
        return "NAME_X3D_MATRIX4VERTEXATTRIBUTE";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        m4va.setName(nameTF.getText().trim());
        m4va.setValues(expandableList.getData());
    }
}
