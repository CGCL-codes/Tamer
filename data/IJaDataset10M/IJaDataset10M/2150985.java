package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;

/**
 * BOOLEANTRIGGERCustomizer.java
 * Created on Sep 10, 2007, 3:05 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class BOOLEANTRIGGERCustomizer extends BaseCustomizer {

    private BOOLEANTRIGGER booleanTrigger;

    private JTextComponent target;

    public BOOLEANTRIGGERCustomizer(BOOLEANTRIGGER booleanTrigger, JTextComponent target) {
        super(booleanTrigger);
        this.booleanTrigger = booleanTrigger;
        this.target = target;
        HelpCtx.setHelpIDString(this, "BOOLEANTRIGGER_ELEM_HELPID");
        initComponents();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        fieldEventDiagramLabel = new javax.swing.JLabel();
        eventHintPanel = new javax.swing.JPanel();
        eventLabel1 = new javax.swing.JLabel();
        setFont(new java.awt.Font("Tahoma", 1, 11));
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        fieldEventDiagramLabel.setBackground(new java.awt.Color(255, 255, 255));
        fieldEventDiagramLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fieldEventDiagramLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/web3d/x3d/palette/items/resources/BooleanTriggerFieldEvents.png")));
        fieldEventDiagramLabel.setToolTipText("Create ROUTEs to connect input and output events");
        fieldEventDiagramLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(fieldEventDiagramLabel, gridBagConstraints);
        eventHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        eventHintPanel.setToolTipText("Create ROUTEs to connect input and output events");
        eventHintPanel.setLayout(new java.awt.GridBagLayout());
        eventLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventLabel1.setText("<html><p align='center'><b>BooleanTrigger</b> converts <i>set_triggerTime</i> events to boolean <i>triggerTrue</i> output events.</p> <p align='center'> <br /> <b>BooleanTrigger</b> has no initializable fields to modify.</p>");
        eventLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(BOOLEANTRIGGERCustomizer.class, "BOOLEANSEQUENCERCustomizer.eventLabel1.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        eventHintPanel.add(eventLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(eventHintPanel, gridBagConstraints);
    }

    private javax.swing.JPanel eventHintPanel;

    private javax.swing.JLabel eventLabel1;

    private javax.swing.JLabel fieldEventDiagramLabel;

    @Override
    public String getNameKey() {
        return "NAME_X3D_BOOLEANTRIGGER";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
    }
}
