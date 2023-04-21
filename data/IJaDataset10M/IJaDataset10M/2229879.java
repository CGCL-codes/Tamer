package jtk.project4.fleet.action;

import jtk.project4.fleet.event.NewEditScheduleEvent;
import jtk.project4.fleet.event.NewEditScheduleEvent.NewEditScheduleEventType;
import nl.coderight.jazz.action.Action;

public class CancelNewEditScheduleAction extends Action {

    public CancelNewEditScheduleAction() {
        super(new NewEditScheduleEvent(NewEditScheduleEventType.CANCEL));
        setText("button.cancel");
    }
}
