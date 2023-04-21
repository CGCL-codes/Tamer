package de.byteholder.gpx;

public class GeoLon extends GeoCoord {

    static final char DIRECTION_WEST = 'W';

    static final char DIRECTION_EAST = 'E';

    public static void main(final String[] args) {
    }

    public GeoLon() {
        super();
    }

    public GeoLon(final double d) {
        super();
        set(d);
    }

    public GeoLon(final GeoLon lon) {
        super();
        set(lon);
    }

    public GeoLon(final String s) {
        super();
        set(s);
    }

    public void add(final GeoLon lon) {
        decimal += lon.decimal;
        if (decimal > 180 * faktg) decimal -= 360 * faktg;
        updateDegrees();
    }

    public void add(final GeoLon lon, final GeoLon a) {
        decimal = lon.decimal;
        this.add(a);
    }

    @Override
    public char directionMinus() {
        return DIRECTION_WEST;
    }

    @Override
    public char directionPlus() {
        return DIRECTION_EAST;
    }

    public void set(final GeoLon lon) {
        super.set(lon);
    }

    public void sub(final GeoLon lon) {
        decimal -= lon.decimal;
        if (decimal < -180 * faktg) decimal += 360 * faktg;
        updateDegrees();
    }

    public void sub(final GeoLon lon, final GeoLon s) {
        decimal = lon.decimal;
        this.sub(s);
    }
}
