package org.vikamine.swing.subgroup.event.connect;

import org.vikamine.swing.subgroup.event.CurrentSGEvent;
import org.vikamine.swing.subgroup.event.listener.CurrentSGListener;
import org.vikamine.swing.subgroup.view.currentSubgroup.FourFieldsPanelController;

/**
 * @author Tobias Vogele
 */
public class CurrentSGToFourFieldsPanelWire implements CurrentSGListener {

    private FourFieldsPanelController ffController;

    public CurrentSGToFourFieldsPanelWire() {
        super();
    }

    public CurrentSGToFourFieldsPanelWire(FourFieldsPanelController ffController) {
        this.ffController = ffController;
    }

    public FourFieldsPanelController getFourFieldsPanelController() {
        return ffController;
    }

    public void setFourFieldsPanelController(FourFieldsPanelController ffController) {
        this.ffController = ffController;
    }

    public void subgroupStructureChanged(CurrentSGEvent eve) {
        getFourFieldsPanelController().setSG(eve.getSubgroup());
    }

    public void subgroupZoomed(CurrentSGEvent eve) {
        getFourFieldsPanelController().setSG(eve.getSubgroup());
    }
}
