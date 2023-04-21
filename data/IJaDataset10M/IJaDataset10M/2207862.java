package jtk.project4.fleet.action;

import jtk.project4.fleet.event.PMScheduleSetupEvent;
import jtk.project4.fleet.event.PMScheduleSetupEvent.PMScheduleSetupEventType;
import nl.coderight.jazz.action.Action;

public class CancelPMScheduleSetupAction extends Action {

    public CancelPMScheduleSetupAction() {
        super(new PMScheduleSetupEvent(PMScheduleSetupEventType.CANCEL));
        setText("button.close");
    }
}
