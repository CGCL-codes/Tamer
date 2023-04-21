package jtk.project4.fleet.event;

import java.util.List;
import jtk.project4.fleet.domain.Employee;
import jtk.project4.fleet.event.LaborEvent.LaborEventType;
import nl.coderight.jazz.Event;

public class LaborEvent extends Event<LaborEventType> {

    private List<String> employees;

    public enum LaborEventType {

        LOAD_SUCCESS, FILTER, ADD, EDIT, DELETE, SUBMIT, CANCEL, SELECT, CHANGE
    }

    public LaborEvent(LaborEventType type) {
        super(type);
    }
}
