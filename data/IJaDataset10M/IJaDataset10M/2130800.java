package jtk.project4.fleet.action;

import jtk.project4.fleet.event.AddAttachmentEvent;
import jtk.project4.fleet.event.AddAttachmentEvent.AddAttachmentEventType;
import nl.coderight.jazz.action.Action;

public class CancelAddAttachmentAction extends Action {

    public CancelAddAttachmentAction() {
        super(new AddAttachmentEvent(AddAttachmentEventType.CANCEL));
        setText("button.cancel");
    }
}
