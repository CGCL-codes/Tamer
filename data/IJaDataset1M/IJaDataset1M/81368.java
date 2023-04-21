package rescuecore.objects;

import rescuecore.RescueConstants;

/**
   Encapsulation of a TYPE_POLICE_OFFICE object
   @see RescueConstants#TYPE_POLICE_OFFICE
 */
public class PoliceOffice extends Building {

    public PoliceOffice() {
        super();
    }

    public PoliceOffice(int x, int y, int floors, int attributes, boolean ignition, int fieryness, int brokenness, int[] entrances, int code, int ground, int total, int[] apexes, int temperature, int importance) {
        super(x, y, floors, attributes, ignition, fieryness, brokenness, entrances, code, ground, total, apexes, temperature, importance);
    }

    public PoliceOffice(Building other) {
        super(other);
    }

    public int getType() {
        return RescueConstants.TYPE_POLICE_OFFICE;
    }
}
