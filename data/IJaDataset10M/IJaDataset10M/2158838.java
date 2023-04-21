package jtk.project4.fleet.action;

import jtk.project4.fleet.event.AddNewLocationEvent;
import jtk.project4.fleet.event.AddNewLocationEvent.AddNewLocationEventType;
import nl.coderight.jazz.action.Action;

public class AddNewLocationAction extends Action {

    public AddNewLocationAction() {
        super(new AddNewLocationEvent(AddNewLocationEventType.ADD));
        setLongDescription("addNewLocation.desc");
        setText("newLocation.add");
    }
}
