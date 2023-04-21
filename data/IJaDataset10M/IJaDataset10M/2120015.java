package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;

/**
 * NURBSSWUNGSURFACECustomizer.java
 * Created on 26 December 2011
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey and Don Brutzman
 * @version $Id$
 */
public class NURBSSWUNGSURFACECustomizer extends BaseCustomizer {

    private NURBSSWUNGSURFACE nurbsSwungSurface;

    private JTextComponent target;

    /** Creates new form NURBSSWUNGSURFACECustomizer */
    public NURBSSWUNGSURFACECustomizer(NURBSSWUNGSURFACE nurbsSwungSurface, JTextComponent target) {
        super(nurbsSwungSurface);
        this.nurbsSwungSurface = nurbsSwungSurface;
        this.target = target;
        HelpCtx.setHelpIDString(this, "NURBSSWUNGSURFACE_ELEM_HELPID");
        initComponents();
        ccwCB.setSelected(nurbsSwungSurface.isCcw());
        solidCB.setSelected(nurbsSwungSurface.isSolid());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jTextField2 = new javax.swing.JTextField();
        dEFUSEpanel1 = getDEFUSEpanel();
        ccwCB = new javax.swing.JCheckBox();
        solidCB = new javax.swing.JCheckBox();
        nodeHintPanel = new javax.swing.JPanel();
        hintLabel = new javax.swing.JLabel();
        jTextField2.setText("jTextField2");
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        ccwCB.setText("ccw");
        ccwCB.setToolTipText("ccw = counterclockwise: ordering of vertex coordinates orientation");
        ccwCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 160, 3, 3);
        add(ccwCB, gridBagConstraints);
        solidCB.setText("solid");
        solidCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 120, 3, 3);
        add(solidCB, gridBagConstraints);
        nodeHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nodeHintPanel.setLayout(new java.awt.GridBagLayout());
        hintLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hintLabel.setText("<html><p align='center'><b>NurbsSwungSurface</b> geometry is similar to an Extrusion node, containing a pair of </p> <p align='center'><i>profileCurve</i> and <i>trajectoryCurve</i> <b>ContourPolyline2D|NurbsCurve2D</b> nodes</p>");
        hintLabel.setToolTipText("close this panel to add children nodes");
        hintLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        nodeHintPanel.add(hintLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(nodeHintPanel, gridBagConstraints);
    }

    private javax.swing.JCheckBox ccwCB;

    private org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1;

    private javax.swing.JLabel hintLabel;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JPanel nodeHintPanel;

    private javax.swing.JCheckBox solidCB;

    @Override
    public String getNameKey() {
        return "NAME_X3D_NURBSSWUNGSURFACE";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        nurbsSwungSurface.setCcw(ccwCB.isSelected());
        nurbsSwungSurface.setSolid(solidCB.isSelected());
    }
}
