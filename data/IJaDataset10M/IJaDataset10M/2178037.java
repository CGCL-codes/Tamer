package playground.gregor.sim2d_v2.simulation.floor;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author laemmel
 * 
 */
public class ForceLocation {

    private Force f = null;

    private final Coordinate c;

    private EnvironmentDistances ed;

    public ForceLocation(EnvironmentDistances ed) {
        this.ed = ed;
        this.c = ed.getLocation();
    }

    public ForceLocation(Force f, Coordinate c) {
        this.f = f;
        this.c = c;
    }

    public Coordinate getLocation() {
        return this.c;
    }

    public Force getForce() {
        return this.f;
    }

    public EnvironmentDistances getEnvironmentDistances() {
        return this.ed;
    }

    public void setForce(Force f) {
        this.f = f;
        this.ed = null;
    }
}
