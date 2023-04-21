package com.tt.bnct.cytoscape.impl;

import org.springframework.stereotype.Component;
import com.tt.bnct.cytoscape.wizard.WizardPanelDescriptor;

@Component("selectGraphLevelTypePanelDescriptor")
public class SelectGraphLevelTypePanelDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "SELECT_GRAPH_LEVEL_TYPE_PANEL";

    public SelectGraphLevelTypePanelDescriptor() {
        super(IDENTIFIER, new SelectGraphLevelTypePanel());
    }

    public Object getNextPanelDescriptor() {
        return SelectGraphLevelPanelDescriptor.IDENTIFIER;
    }

    public Object getBackPanelDescriptor() {
        return SelectGraphTypePanelDescriptor.IDENTIFIER;
    }

    public void aboutToDisplayPanel() {
        String graphType = ((SelectGraphTypePanelDescriptor) getPanelDescriptor(SelectGraphTypePanelDescriptor.IDENTIFIER)).getGraphTypeSelected();
        if (SelectGraphTypePanel.ENZYME_GRAPH.equals(graphType)) {
            ((SelectGraphLevelTypePanel) getPanelComponent()).shouldPathwayExist(true);
        } else {
            ((SelectGraphLevelTypePanel) getPanelComponent()).shouldPathwayExist(false);
        }
    }

    public String getGraphLevelTypeSelected() {
        return ((SelectGraphLevelTypePanel) getPanelComponent()).getGraphLevelTypeSelected();
    }
}
