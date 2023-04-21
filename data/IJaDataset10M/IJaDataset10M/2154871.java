package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * RECTANGLE2DCustomizer.java
 * Created on July 11, 2007, 1:41 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class RECTANGLE2DCustomizer extends BaseCustomizer {

    private RECTANGLE2D r2d;

    private JTextComponent target;

    /** Creates new form RECTANGLE2DCustomizer */
    public RECTANGLE2DCustomizer(RECTANGLE2D r2d, JTextComponent target) {
        super(r2d);
        this.r2d = r2d;
        this.target = target;
        HelpCtx.setHelpIDString(this, "RECTANGLE2D_ELEM_HELPID");
        initComponents();
        xTF.setText(r2d.getSizeX());
        yTF.setText(r2d.getSizeY());
        solidCB.setSelected(r2d.isSolid());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        xTF = new javax.swing.JTextField();
        yTF = new javax.swing.JTextField();
        solidCB = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dEFUSEpanel1 = getDEFUSEpanel();
        nodeHintPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        setLayout(new java.awt.GridBagLayout());
        xTF.setColumns(10);
        xTF.setToolTipText("width in x direction, saved as part of size field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 8);
        add(xTF, gridBagConstraints);
        yTF.setColumns(10);
        yTF.setToolTipText("height in y direction, saved as part of size field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 8);
        add(yTF, gridBagConstraints);
        solidCB.setToolTipText("solid true means draw only one side of polygons (backface culling on), solid false means draw both sides of polygons (backface culling off)");
        solidCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        add(solidCB, gridBagConstraints);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("width x");
        jLabel1.setToolTipText("width in x direction, saved as part of size field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jLabel1, gridBagConstraints);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("height y");
        jLabel2.setToolTipText("height in y direction, saved as part of size field");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jLabel2, gridBagConstraints);
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("solid");
        jLabel3.setToolTipText("solid true means draw only one side of polygons (backface culling on), solid false means draw both sides of polygons (backface culling off)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        nodeHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nodeHintPanel.setLayout(new java.awt.GridBagLayout());
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText("<html><p align='center'><b>Rectangle2D</b> defines <i>size</i> and <i>solid</i> (single-sided or double-sided)<br />for a single 2D quadrilateral");
        descriptionLabel.setToolTipText("Rectangle2D has no children");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 10, 3);
        nodeHintPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(nodeHintPanel, gridBagConstraints);
    }

    @Override
    public String getNameKey() {
        return "NAME_X3D_RECTANGLE2D";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        r2d.setSizeX(xTF.getText().trim());
        r2d.setSizeY(yTF.getText().trim());
        r2d.setSolid(solidCB.isSelected());
    }

    private org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel nodeHintPanel;

    private javax.swing.JCheckBox solidCB;

    private javax.swing.JTextField xTF;

    private javax.swing.JTextField yTF;
}
