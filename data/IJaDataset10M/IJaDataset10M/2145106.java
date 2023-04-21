package net.sf.vorg.core.models;

import net.sf.vorg.core.enums.Directions;

public class Intersection {

    protected GeoLocation location;

    protected Directions direction;

    public Intersection(final GeoLocation location, final Directions direction) {
        this.location = location;
        this.direction = direction;
    }

    public Directions getDirection() {
        return direction;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setDirection(final Directions direction) {
        this.direction = direction;
    }

    public void setLocation(final GeoLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("[Intersection ");
        buffer.append("location=").append(location).append(",");
        buffer.append("direction=").append(direction).append("]");
        return buffer.toString();
    }
}
