package jtk.project4.fleet.action;

import jtk.project4.fleet.event.TripInformationEquipmentEvent;
import jtk.project4.fleet.event.TripInformationEquipmentEvent.TripInformationEquipmentEventType;
import nl.coderight.jazz.action.Action;

public class TripInformationEquipmentAction extends Action {

    public TripInformationEquipmentAction() {
        super(new TripInformationEquipmentEvent(TripInformationEquipmentEventType.VIEW));
        setLongDescription("TripInformationEquipment.add.desc");
        setText("TripInformationEquipment.add");
    }
}
