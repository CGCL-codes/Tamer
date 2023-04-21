package jtk.project4.fleet.action;

import jtk.project4.fleet.event.TireInventoryEvent;
import jtk.project4.fleet.event.VendorEvent;
import jtk.project4.fleet.event.TireInventoryEvent.TireInventoryEventType;
import jtk.project4.fleet.event.VendorEvent.VendorEventType;
import nl.coderight.jazz.action.Action;

public class TireCloseAction extends Action {

    public TireCloseAction() {
        super(new TireInventoryEvent(TireInventoryEventType.CLOSE));
        setText("close");
    }
}
