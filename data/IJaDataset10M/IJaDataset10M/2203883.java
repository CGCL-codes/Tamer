package ngs;

import java.awt.Graphics;
import java.io.*;
import ngs.model.*;

/**
 * A players presence in the virtual world.  Maintains their location and mobility model.  Avatars cán move in the virtual world..
 */
public class Avatar {

    /**
	 * The players location.
	 */
    private Location location = null;

    /**
	 * The mobility model.
	 */
    private MobilityModel model = null;

    /**
	 * Dead reckoning tollerance.  Set to -1 to displable dead reckoning.
	 */
    private double alpha = -1.0;

    /**
	 * The angle the node is traveling at.  Used for dead reckoning.
	 */
    private double angle = 0.0;

    /**
	 * Area of Interest (AoI).
	 */
    private final int aoi;

    /**
	 * Default constructor.
	 *
	 * @param model The mobility model to use.
	 * @param alpha The tolerance value to use for dead reckoning (-1 to disable).
	 * @param aoi The Area of Interest of this node.
	 */
    public Avatar(final MobilityModel model, final double alpha, final int aoi) {
        this.model = model;
        this.alpha = alpha;
        this.aoi = aoi;
        location = model.getLocation();
    }

    /**
	 * Constructor for the avatar without an AoI.
	 *
	 * @param model The mobility model to use.
	 * @param alpha The tolerance value to use for dead reckoning (-1 to disable).
	 */
    public Avatar(final MobilityModel model, final double alpha) {
        this.model = model;
        this.alpha = alpha;
        this.aoi = 0;
        location = model.getLocation();
    }

    /**
	 * Performs an update for the node.
	 *
	 * @param currentTime The time being simulated.
	 * @return true if an update needs to be sent, else false.
	 */
    public boolean update(final double currentTime) {
        boolean update = true;
        Location newLocation = model.move();
        if (alpha >= 0.0) {
            double newAngle = location.angle(newLocation);
            double smaller = 0.0;
            double larger = 0.0;
            if (newAngle > angle) {
                smaller = angle;
                larger = newAngle;
            } else {
                smaller = newAngle;
                larger = angle;
            }
            if ((Math.abs(newAngle - angle) < alpha) || (Math.abs(smaller - (larger - (2 * Math.PI))) < alpha)) {
                update = false;
            } else {
                angle = newAngle;
            }
        }
        location = newLocation;
        return update;
    }

    /**
	 * Returns the location of this avatar.
	 *
	 * @return avatar's location.
	 */
    public Location getLocation() {
        return location;
    }

    /**
	 * Get the AoI of the avatar.
	 *
	 * @return the AoI.
	 */
    public int getAoI() {
        return aoi;
    }

    /**
	 * Is this location within the AoI of this player's avatar.
	 *
	 * @param location The location to check.
	 * @return true if it is within the AoI, else false.
	 */
    public boolean withinAoI(final Location location) {
        boolean inAoI = false;
        if (Math.abs(location.getX() - this.location.getX()) <= aoi) {
            if (Math.abs(location.getY() - this.location.getY()) <= aoi) {
                inAoI = (this.location.distance(location) <= aoi);
            }
        }
        return inAoI;
    }

    /**
	 * Paint this avatar's position
	 *
	 * @param g Graphics object.
	 */
    public void paint(Graphics g) {
        g.fillRect(location.getX() - 2, location.getY() - 2, 5, 5);
    }
}
