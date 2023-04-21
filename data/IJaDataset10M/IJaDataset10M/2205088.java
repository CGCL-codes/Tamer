package jtk.project4.fleet.event;

import java.util.List;
import jtk.project4.fleet.domain.Equipment;
import jtk.project4.fleet.domain.Po;
import jtk.project4.fleet.event.BrowsePurchaseOrdersEvent.BrowsePurcaseOrdersEventType;
import jtk.project4.fleet.event.EquipmentEvent.EquipmentEventType;
import nl.coderight.jazz.Event;

public class BrowsePurchaseOrdersEvent extends Event<BrowsePurcaseOrdersEventType> {

    private List<Po> po;

    public enum BrowsePurcaseOrdersEventType {

        LOAD_SUCCESS, FILTER, ADD, EDIT, DELETE, SUBMIT, CANCEL, SELECT, CHANGE, VIEW
    }

    public BrowsePurchaseOrdersEvent(BrowsePurcaseOrdersEventType type) {
        super(type);
    }

    public void setPo(List<Po> po) {
        this.po = po;
    }

    public List<Po> getPo() {
        return po;
    }
}
