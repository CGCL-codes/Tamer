package playground.marcel.pt.transitSchedule;

import org.matsim.interfaces.basic.v01.Id;

public class RouteProfile {

    private final Id id;

    public RouteProfile(final Id id) {
        this.id = id;
    }

    public Id getId() {
        return this.id;
    }
}
