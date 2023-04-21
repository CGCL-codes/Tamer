package org.mars_sim.msp.ui.swing.tool.mission.create;

import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A dialog wizard for creating new missions.
 */
public class CreateMissionWizard extends JDialog {

    private JPanel infoPane;

    private JButton prevButton;

    private JButton nextButton;

    private JButton finalButton;

    private MissionDataBean missionBean;

    private List<WizardPanel> wizardPanels;

    private int displayPanelIndex;

    /**
	 * Constructor
	 * @param owner The owner frame.
	 */
    public CreateMissionWizard(Frame owner) {
        super(owner, "Create Mission Wizard", true);
        missionBean = new MissionDataBean();
        infoPane = new JPanel(new CardLayout());
        infoPane.setBorder(new MarsPanelBorder());
        add(infoPane, BorderLayout.CENTER);
        wizardPanels = new ArrayList<WizardPanel>();
        displayPanelIndex = 0;
        addWizardPanel(new TypePanel(this));
        addWizardPanel(new StartingSettlementPanel(this));
        JPanel bottomButtonPane = new JPanel();
        add(bottomButtonPane, BorderLayout.SOUTH);
        prevButton = new JButton("Previous");
        prevButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getCurrentWizardPanel().clearInfo();
                displayPanelIndex--;
                CardLayout layout = (CardLayout) infoPane.getLayout();
                layout.show(infoPane, getCurrentWizardPanel().getPanelName());
                nextButton.setEnabled(true);
                if (displayPanelIndex == 0) prevButton.setEnabled(false);
            }
        });
        prevButton.setEnabled(false);
        bottomButtonPane.add(prevButton);
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (getCurrentWizardPanel().commitChanges()) {
                    displayPanelIndex++;
                    setButtons(false);
                    CardLayout layout = (CardLayout) infoPane.getLayout();
                    WizardPanel currentPanel = getCurrentWizardPanel();
                    currentPanel.updatePanel();
                    layout.show(infoPane, currentPanel.getPanelName());
                }
            }
        });
        nextButton.setEnabled(false);
        bottomButtonPane.add(nextButton);
        finalButton = new JButton("Final");
        finalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (getCurrentWizardPanel().commitChanges()) {
                    missionBean.createMission();
                    dispose();
                }
            }
        });
        finalButton.setEnabled(false);
        bottomButtonPane.add(finalButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomButtonPane.add(cancelButton);
        pack();
        setSize(new Dimension(700, 550));
        setLocationRelativeTo(owner);
        setResizable(false);
        setVisible(true);
    }

    /**
	 * Gets the current displayed wizard panel.
	 * @return wizard panel.
	 */
    private WizardPanel getCurrentWizardPanel() {
        return wizardPanels.get(displayPanelIndex);
    }

    /**
	 * Sets the final wizard panel for the mission type.
	 */
    void setFinalWizardPanels() {
        int numPanels = wizardPanels.size();
        for (int x = 1; x < numPanels; x++) wizardPanels.remove(1);
        if (missionBean.getType().equals(MissionDataBean.TRAVEL_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new DestinationSettlementPanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.RESCUE_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new RendezvousVehiclePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.ICE_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new ProspectingSitePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.REGOLITH_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new ProspectingSitePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.EXPLORATION_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new ExplorationSitesPanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.TRADE_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new DestinationSettlementPanel(this));
            addWizardPanel(new TradeGoodsPanel(this, false));
            addWizardPanel(new TradeGoodsPanel(this, true));
        } else if (missionBean.getType().equals(MissionDataBean.MINING_MISSION)) {
            addWizardPanel(new StartingSettlementPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new LightUtilityVehiclePanel(this));
            addWizardPanel(new MiningSitePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.CONSTRUCTION_MISSION)) {
            addWizardPanel(new ConstructionSettlementPanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new ConstructionProjectPanel(this));
            addWizardPanel(new ConstructionVehiclePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.SALVAGE_MISSION)) {
            addWizardPanel(new SalvageSettlementPanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new SalvageProjectPanel(this));
            addWizardPanel(new SalvageVehiclePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.AREOLOGY_FIELD_MISSION)) {
            addWizardPanel(new StudyPanel(this));
            addWizardPanel(new LeadResearcherPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new FieldSitePanel(this));
        } else if (missionBean.getType().equals(MissionDataBean.BIOLOGY_FIELD_MISSION)) {
            addWizardPanel(new StudyPanel(this));
            addWizardPanel(new LeadResearcherPanel(this));
            addWizardPanel(new VehiclePanel(this));
            addWizardPanel(new MembersPanel(this));
            addWizardPanel(new FieldSitePanel(this));
        }
    }

    /**
	 * Adds a wizard panel to the list.
	 * @param newWizardPanel the wizard panel to add.
	 */
    private void addWizardPanel(WizardPanel newWizardPanel) {
        wizardPanels.add(newWizardPanel);
        infoPane.add(newWizardPanel, newWizardPanel.getPanelName());
    }

    /** 
	 * Get the mission data bean.
	 * @return mission data bean.
	 */
    MissionDataBean getMissionData() {
        return missionBean;
    }

    /**
	 * Sets previous, next and final buttons to be enabled or disabled.
	 * @param nextEnabled true if next/final button is enabled.
	 */
    void setButtons(boolean nextEnabled) {
        prevButton.setEnabled(displayPanelIndex > 0);
        if (nextEnabled) {
            nextButton.setEnabled(displayPanelIndex < (wizardPanels.size() - 1));
            finalButton.setEnabled(displayPanelIndex == (wizardPanels.size() - 1));
        } else {
            nextButton.setEnabled(false);
            finalButton.setEnabled(false);
        }
    }
}
