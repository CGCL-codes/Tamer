package com.pbxworkbench.campaign.ui.actions;

import java.awt.event.ActionEvent;
import java.util.Set;
import com.pbxworkbench.campaign.ui.nodes.PbxProfileNode;
import com.pbxworkbench.commons.ui.Images;
import com.pbxworkbench.commons.ui.NodeSelectionListener;
import com.pbxworkbench.commons.ui.NodeSelectionMgr;

public class EditSelectedPbxProfileAction extends BaseCampaignAction implements NodeSelectionListener {

    public EditSelectedPbxProfileAction() {
        super("Edit", Images.getDefaultImage(EditSelectedPbxProfileAction.class));
        NodeSelectionMgr.getDefault().addSelectionListener(this);
    }

    public void actionPerformed(ActionEvent arg0) {
        PbxProfileNode pbxNode = (PbxProfileNode) NodeSelectionMgr.getDefault().getSelectedNode();
        getController().editPbxProfile(pbxNode.getPbxProfile());
    }

    public void onNodeSelectionChanged(Set selectedNodes) {
        boolean enable = selectedNodes.size() == 1 && selectedNodes.iterator().next() instanceof PbxProfileNode;
        setEnabled(enable);
    }
}
