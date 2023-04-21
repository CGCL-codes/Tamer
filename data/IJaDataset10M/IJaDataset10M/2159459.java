package models.bodies;

import models.Location;
import models.Orientation;
import models.states.CollisionActionState;

/**
 * @author subodhkolla
 * 
 * A One way wall are select walls which allow the ghosts to go through
 * but not pacman. These walls are located normally in the center of 
 * the game where the ghosts get created. 
 *
 */
public class GhostPermeableWall implements Body, Wall {

    private static final String ID = "GHOSTWALL";

    private Location location;

    public GhostPermeableWall(Location L) {
        this.location = L;
    }

    public String getID() {
        return ID;
    }

    public Location getLocation() {
        return this.location;
    }

    public CollisionActionState getState() {
        return null;
    }

    public void setLocation(Location L) {
    }

    public void setState(CollisionActionState S) {
    }

    public Orientation getOrientation() {
        return null;
    }

    public boolean getDeath() {
        return false;
    }

    public void setDeath(boolean d) {
    }
}
