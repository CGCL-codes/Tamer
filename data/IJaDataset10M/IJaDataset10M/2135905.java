package hu.aitia.ds.spatial.demo.sm16;

import hu.aitia.ds.spatial.core.AgentState;
import hu.aitia.ds.spatial.space.Location;

public class PredatorState implements AgentState {

    private static final long serialVersionUID = -5696108286188394578L;

    protected Location location = null;

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getPerceptionDistance() {
        return 1;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setPerceptionDistance(int distance) {
        ;
    }
}
