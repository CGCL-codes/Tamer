package org.vikamine.gui.subgroup.event.connect;

import org.vikamine.gui.subgroup.editors.zoomtable.CommonZoomTablesController;
import org.vikamine.gui.subgroup.event.CurrentSGEvent;
import org.vikamine.gui.subgroup.event.listener.CurrentSGListener;

public class CurrentSGToZoomTableWire implements CurrentSGListener {

    private CommonZoomTablesController zoomController;

    public CurrentSGToZoomTableWire() {
        super();
    }

    public CurrentSGToZoomTableWire(CommonZoomTablesController zoomController) {
        this.zoomController = zoomController;
    }

    public CommonZoomTablesController getZoomController() {
        return zoomController;
    }

    public void setZoomController(CommonZoomTablesController zoomController) {
        this.zoomController = zoomController;
    }

    public void subgroupZoomed(CurrentSGEvent eve) {
        getZoomController().updateAttributeInfoSupportingInteractiveSearch(true);
    }

    public void subgroupStructureChanged(CurrentSGEvent eve) {
        if (eve.getSubgroup() == null) {
            getZoomController().clearZoomTreeModels();
        } else {
            getZoomController().updateZoomTables();
        }
        getZoomController().updateAttributeInfoSupportingInteractiveSearch(true);
    }
}
