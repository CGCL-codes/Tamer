package gov.ca.bdo.modeling.dsm2.map.client.map;

import gov.ca.dsm2.input.model.Reservoir;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;

public class ReservoirDragHandler implements MarkerDragEndHandler {

    private final Reservoir reservoir;

    private final ReservoirOverlayManager manager;

    public ReservoirDragHandler(ReservoirOverlayManager manager, Reservoir reservoir) {
        this.reservoir = reservoir;
        this.manager = manager;
    }

    public void onDragEnd(MarkerDragEndEvent event) {
        LatLng latLng = event.getSender().getLatLng();
        reservoir.setLatitude(latLng.getLatitude());
        reservoir.setLongitude(latLng.getLongitude());
        manager.refresh(reservoir);
    }
}
