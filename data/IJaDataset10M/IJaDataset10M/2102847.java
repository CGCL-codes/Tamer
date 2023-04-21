package jtk.project4.fleet.action;

import jtk.project4.fleet.event.AddMaintenanceTaskEvent;
import jtk.project4.fleet.event.AddMaintenanceTaskEvent.AddMaintenanceTaskEventType;
import nl.coderight.jazz.action.Action;

public class CancelMaintenanceTaskAction extends Action {

    public CancelMaintenanceTaskAction() {
        super(new AddMaintenanceTaskEvent(AddMaintenanceTaskEventType.CANCEL));
        setText("button.cancel");
    }
}
