package com.worldwizards;

import javax.swing.JDialog;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane;
import java.awt.Insets;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

public class PlaytimeRecFieldEditor extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JPanel statPanel = null;

    private JCheckBox useSecondarySPD = null;

    private JCheckBox useSecondaryDEX = null;

    private JCheckBox useSecondaryEGO = null;

    private JButton setStatBoxesButton = null;

    private JPanel mainPanel = null;

    private JPanel combatSwitchesPane = null;

    private JCheckBox egoBased = null;

    protected boolean setToggle = false;

    private JCheckBox useSecondaryREC = null;

    private JCheckBox useSecondaryBODY = null;

    private JCheckBox useSecondarySTUN = null;

    /**
	 * This method initializes 
	 * 
	 */
    public PlaytimeRecFieldEditor() {
        initialize();
    }

    public PlaytimeRecFieldEditor(HeroPlaytimeRecord rec) {
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setSize(new Dimension(439, 248));
        this.setLayout(new BorderLayout());
        this.add(getMainPanel());
    }

    /**
	 * This method initializes statPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getStatPanel() {
        if (statPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 2;
            gridBagConstraints8.gridy = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 3;
            gridBagConstraints5.gridy = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridwidth = 3;
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 2;
            gridBagConstraints2.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            statPanel = new JPanel();
            statPanel.setName("Secondary Stats");
            statPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "SecondaryStats"));
            statPanel.setLayout(new GridBagLayout());
            statPanel.add(getUseSecondaryDEX(), gridBagConstraints1);
            statPanel.add(getUseSecondaryEGO(), gridBagConstraints2);
            statPanel.add(getSetStatBoxesButton(), gridBagConstraints3);
            statPanel.add(getUseSecondarySPD(), new GridBagConstraints());
            statPanel.add(getUseSecondaryREC(), gridBagConstraints5);
            statPanel.add(getUseSecondaryBODY(), gridBagConstraints7);
            statPanel.add(getUseSecondarySTUN(), gridBagConstraints8);
        }
        return statPanel;
    }

    /**
	 * This method initializes useSecondarySPD	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondarySPD() {
        if (useSecondarySPD == null) {
            useSecondarySPD = new JCheckBox();
            useSecondarySPD.setText("Use Secondary SPD");
        }
        return useSecondarySPD;
    }

    /**
	 * This method initializes useSecondaryDEX	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondaryDEX() {
        if (useSecondaryDEX == null) {
            useSecondaryDEX = new JCheckBox();
            useSecondaryDEX.setText("Use Secondary DEX");
        }
        return useSecondaryDEX;
    }

    /**
	 * This method initializes useSecondaryEGO	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondaryEGO() {
        if (useSecondaryEGO == null) {
            useSecondaryEGO = new JCheckBox();
            useSecondaryEGO.setText("Use Secondary EGO");
        }
        return useSecondaryEGO;
    }

    /**
	 * This method initializes setStatBoxesButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSetStatBoxesButton() {
        if (setStatBoxesButton == null) {
            setStatBoxesButton = new JButton();
            setStatBoxesButton.setText("Check all Secondary Stat Boxes");
            setStatBoxesButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!setToggle) {
                        setToggle = true;
                        useSecondaryDEX.getModel().setSelected(true);
                        useSecondarySPD.getModel().setSelected(true);
                        useSecondaryEGO.getModel().setSelected(true);
                        useSecondaryBODY.getModel().setSelected(true);
                        useSecondarySTUN.getModel().setSelected(true);
                        useSecondaryREC.getModel().setSelected(true);
                        setStatBoxesButton.setText("Unheck all Secondary Stat Boxes");
                    } else {
                        setToggle = false;
                        useSecondaryDEX.getModel().setSelected(false);
                        useSecondarySPD.getModel().setSelected(false);
                        useSecondaryEGO.getModel().setSelected(false);
                        useSecondaryBODY.getModel().setSelected(false);
                        useSecondarySTUN.getModel().setSelected(false);
                        useSecondaryREC.getModel().setSelected(false);
                        setStatBoxesButton.setText("Check all Secondary Stat Boxes");
                    }
                }
            });
        }
        return setStatBoxesButton;
    }

    /**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridwidth = 3;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(1, 0, 0, 0);
            gridBagConstraints.ipady = 10;
            gridBagConstraints.ipadx = 10;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getStatPanel(), gridBagConstraints);
            mainPanel.add(getCombatSwitchesPane(), gridBagConstraints6);
        }
        return mainPanel;
    }

    /**
	 * This method initializes combatSwitchesPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getCombatSwitchesPane() {
        if (combatSwitchesPane == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 0;
            combatSwitchesPane = new JPanel();
            combatSwitchesPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Combat Switches"));
            combatSwitchesPane.setLayout(new GridBagLayout());
            combatSwitchesPane.add(getEgoBased(), gridBagConstraints4);
        }
        return combatSwitchesPane;
    }

    /**
	 * This method initializes egoBased	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getEgoBased() {
        if (egoBased == null) {
            egoBased = new JCheckBox();
            egoBased.setText("Use EGO for SPD");
        }
        return egoBased;
    }

    public void setRecord(HeroPlaytimeRecord rec) {
        useSecondaryDEX.getModel().setSelected(rec.isUseSecondaryDEX());
        useSecondarySPD.getModel().setSelected(rec.isUseSecondarySPD());
        useSecondaryEGO.getModel().setSelected(rec.isUseSecondaryEGO());
        useSecondaryBODY.getModel().setSelected(rec.isUseSecondaryBODY());
        useSecondarySTUN.getModel().setSelected(rec.isUseSecondarySTUN());
        useSecondaryREC.getModel().setSelected(rec.isUseSecondaryREC());
        egoBased.getModel().setSelected(rec.isEGOBased());
    }

    public void getRecord(HeroPlaytimeRecord rec) {
        rec.setUseSecondaryDEX(useSecondaryDEX.getModel().isSelected());
        rec.setUseSecondarySPD(useSecondarySPD.getModel().isSelected());
        rec.setUseSecondaryEGO(useSecondaryEGO.getModel().isSelected());
        rec.setUseSecondaryBODY(useSecondaryBODY.getModel().isSelected());
        rec.setUseSecondarySTUN(useSecondarySTUN.getModel().isSelected());
        rec.setUseSecondaryREC(useSecondaryREC.getModel().isSelected());
        rec.setEGOBased(egoBased.getModel().isSelected());
    }

    /**
	 * This method initializes useSecondaryREC	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondaryREC() {
        if (useSecondaryREC == null) {
            useSecondaryREC = new JCheckBox();
            useSecondaryREC.setText("Use Secondary REC");
        }
        return useSecondaryREC;
    }

    /**
	 * This method initializes useSecondaryBODY	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondaryBODY() {
        if (useSecondaryBODY == null) {
            useSecondaryBODY = new JCheckBox();
            useSecondaryBODY.setText("Use Secondary BODY");
        }
        return useSecondaryBODY;
    }

    /**
	 * This method initializes useSecondarySTUN	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUseSecondarySTUN() {
        if (useSecondarySTUN == null) {
            useSecondarySTUN = new JCheckBox();
            useSecondarySTUN.setText("Use Secondary STUN");
        }
        return useSecondarySTUN;
    }
}
