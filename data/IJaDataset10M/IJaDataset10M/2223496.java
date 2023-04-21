package com.pbxworkbench.campaign.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.pbxworkbench.campaign.ui.controller.CampaignController;
import com.pbxworkbench.commons.ui.NodeSelectionMgr;
import com.pbxworkbench.commons.ui.TypeBasedActionEnabler;

public abstract class BaseCampaignAction extends AbstractAction {

    private static CampaignController controller;

    public BaseCampaignAction(Class[] relevantINodeClasses) {
        NodeSelectionMgr.getDefault().addSelectionListener(new TypeBasedActionEnabler(relevantINodeClasses, this));
    }

    public BaseCampaignAction() {
        super();
    }

    public BaseCampaignAction(String arg0, Icon arg1) {
        super(arg0, arg1);
    }

    public BaseCampaignAction(String arg0) {
        super(arg0);
    }

    protected CampaignController getController() {
        return controller;
    }

    public static void setController(CampaignController c) {
        controller = c;
    }
}
