package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;

/**
 * TOUCHSENSORCustomizer.java
 * Created on August 15, 2007, 2:25 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class TOUCHSENSORCustomizer extends BaseCustomizer {

    private TOUCHSENSOR touchSensor;

    private JTextComponent target;

    /** Creates new form TOUCHSENSORCustomizer */
    public TOUCHSENSORCustomizer(TOUCHSENSOR touchSensor, JTextComponent target) {
        super(touchSensor);
        this.touchSensor = touchSensor;
        this.target = target;
        HelpCtx.setHelpIDString(this, "TOUCHSENSOR_ELEM_HELPID");
        initComponents();
        enabledCB.setSelected(touchSensor.isEnabled());
        descriptionTA.setText(touchSensor.getDescription());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        dEFUSEpanel1 = getDEFUSEpanel();
        descriptionLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTA = new javax.swing.JTextArea();
        enabledLabel = new javax.swing.JLabel();
        enabledCB = new javax.swing.JCheckBox();
        eventHintPanel = new javax.swing.JPanel();
        eventsLabel = new javax.swing.JLabel();
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        descriptionLabel.setText("description");
        descriptionLabel.setToolTipText("User description of sensor action");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(descriptionLabel, gridBagConstraints);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(23, 20));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(166, 40));
        descriptionTA.setColumns(20);
        descriptionTA.setLineWrap(true);
        descriptionTA.setRows(4);
        descriptionTA.setToolTipText("User description of sensor action");
        descriptionTA.setWrapStyleWord(true);
        descriptionTA.setPreferredSize(new java.awt.Dimension(164, 36));
        jScrollPane1.setViewportView(descriptionTA);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jScrollPane1, gridBagConstraints);
        enabledLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        enabledLabel.setText("enabled");
        enabledLabel.setToolTipText("Enables/disables node operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(enabledLabel, gridBagConstraints);
        enabledCB.setToolTipText("Enables/disables node operation");
        enabledCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(enabledCB, gridBagConstraints);
        eventHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        eventHintPanel.setLayout(new java.awt.GridBagLayout());
        eventsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventsLabel.setText("<html>Output events include <b>isActive</b>, <b>isOver</b>, <b>touchTime</b>, <b>hitPoint</b>, <b>hitNormal</b>, <b>hitTexCoord</b>");
        eventsLabel.setToolTipText("Create a ROUTE to connect output events");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        eventHintPanel.add(eventsLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(eventHintPanel, gridBagConstraints);
    }

    private org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JTextArea descriptionTA;

    private javax.swing.JCheckBox enabledCB;

    private javax.swing.JLabel enabledLabel;

    private javax.swing.JPanel eventHintPanel;

    private javax.swing.JLabel eventsLabel;

    private javax.swing.JScrollPane jScrollPane1;

    @Override
    public String getNameKey() {
        return "NAME_X3D_TOUCHSENSOR";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        touchSensor.setEnabled(enabledCB.isSelected());
        touchSensor.setDescription(descriptionTA.getText().trim());
    }
}
