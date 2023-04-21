package org.nist.usarui;

/**
 * A class describing an available starting pose for the robot.
 *
 * @author Stephen Carlson (NIST)
 */
public class StartPose {

    private final Vec3 location;

    private final Vec3 rotation;

    private final String tag;

    /**
	 * Creates a new starting pose.
	 *
	 * @param location the starting location
	 * @param rotation the starting rotation
	 * @param tag the player start's tag
	 */
    public StartPose(Vec3 location, Vec3 rotation, String tag) {
        this.location = location;
        this.rotation = rotation;
        this.tag = tag;
    }

    /**
	 * Gets the starting location.
	 *
	 * @return the starting location
	 */
    public Vec3 getLocation() {
        return location;
    }

    /**
	 * Gets the starting rotation.
	 *
	 * @return the starting rotation
	 */
    public Vec3 getRotation() {
        return rotation;
    }

    /**
	 * Gets whether this pose has a generic, unhelpful tag.
	 *
	 * @return whether the tag is useless
	 */
    public boolean isGeneric() {
        return tag.equalsIgnoreCase("PlayerStart");
    }

    /**
	 * Gets the start tag.
	 *
	 * @return the player start "tag"
	 */
    public String getTag() {
        return tag;
    }

    public String toString() {
        String str = tag;
        if (isGeneric()) str = location.toString();
        return str;
    }
}
