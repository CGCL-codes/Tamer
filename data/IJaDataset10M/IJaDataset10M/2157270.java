package sightmusic.work.actions;

import prefuse.visual.VisualItem;
import java.awt.event.ActionEvent;
import model.user.User;
import sightmusic.FlowMap.FlowMapInterface;
import sightmusic.FlowMap.FlowMapManagerOne;
import sightmusic.FlowMap.TreeViewInterface;
import sightmusic.MUI;

public class ActionSelectionPeriodOneFlowMap extends ActionSelectionUserFlowMap {

    private static final long serialVersionUID = 1L;

    public ActionSelectionPeriodOneFlowMap(FlowMapInterface flow, TreeViewInterface tree) {
        super(flow, tree);
    }

    public void actionPerformed(ActionEvent e) {
        String date = ((VisualItem) e.getSource()).get("label").toString();
        int duree = FlowMapManagerOne.getInstance().getDuree(date);
        User user = FlowMapManagerOne.getInstance().getCurrentUser();
        FlowMapManagerOne.getInstance().setDate(date);
        FlowMapManagerOne.getInstance().setDuree(duree);
        FlowMapManagerOne.getInstance().addListener();
        MUI.getInstance().Title.setText(user.getPrenom() + " : " + date);
    }
}
