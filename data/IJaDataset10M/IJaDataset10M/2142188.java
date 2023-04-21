package net.aetherial.gis.garmin;

import net.aetherial.gis.garmin.*;

abstract class Waypoint {

    public abstract String getIdentifier();

    public abstract Position getPosition();

    public abstract String getComment();

    public abstract MapSymbol getSymbol();
}
