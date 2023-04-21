package massim.map;

import java.io.Serializable;

public class Location implements Serializable {

    public static final int NO_DATA = -1;

    public final Observation observation;

    public final float orientation;

    public final float certainty;

    public final float errorRadius;

    public final long timeTag;

    public Location(Observation obs, float orientation, float certainty) {
        this(obs, orientation, certainty, NO_DATA);
    }

    public Location(Observation obs, float orientation, float certainty, float errorRadius) {
        this.observation = obs;
        this.orientation = orientation;
        this.certainty = certainty;
        this.errorRadius = errorRadius;
        this.timeTag = System.currentTimeMillis();
    }

    public String toString() {
        return observation.x + "," + observation.y + " [Orientation " + orientation + " degrees, certainty " + certainty + ", error radius " + errorRadius + " cm]";
    }
}
