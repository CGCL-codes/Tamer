package lelouet.datacenter.simulation.events;

import java.util.List;
import lelouet.datacenter.simulation.Event;
import lelouet.datacenter.simulation.EventHandler;
import lelouet.datacenter.simulation.vms.AVM;

public class VMSLAViolation extends VMEvent {

    public VMSLAViolation(AVM vm, long time) {
        super(vm, time);
    }

    @Override
    public List<Event> apply(EventHandler handler) {
        return handler.handleRequestUnhandled(this);
    }
}
