package org.jsmiparser.util.token;

import org.jsmiparser.util.location.Location;

public abstract class AbstractToken implements Token {

    private Location location;

    protected AbstractToken(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (getLocation() != null) {
            result.append(getLocation().toString());
        } else {
            result.append("<hardcoded>");
        }
        result.append(Location.SEPARATOR);
        result.append(getObject().toString());
        return result.toString();
    }
}
