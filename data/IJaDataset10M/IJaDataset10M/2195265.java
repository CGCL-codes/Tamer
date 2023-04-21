package playground.mzilske.freight.events;

import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.freight.carrier.Shipment;
import org.matsim.core.api.experimental.events.Event;

public class TSPShipmentPickUpEvent implements Event {

    private Id tspId;

    private Shipment shipment;

    private double time;

    public TSPShipmentPickUpEvent(Id tspId, Shipment shipment, double time) {
        super();
        this.tspId = tspId;
        this.shipment = shipment;
        this.time = time;
    }

    @Override
    public double getTime() {
        return time;
    }

    public Id getTspId() {
        return tspId;
    }

    public Shipment getShipment() {
        return shipment;
    }

    @Override
    public Map<String, String> getAttributes() {
        return null;
    }
}
