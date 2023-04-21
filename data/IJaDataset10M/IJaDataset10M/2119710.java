package de.georg_gruetter.xhsi.model;

public class NavigationObject {

    public String name;

    public float lat;

    public float lon;

    public static int NO_TYPE_AIRPORT = 3;

    public static int NO_TYPE_FIX = 2;

    public static int NO_TYPE_NDB = 1;

    public static int NO_TYPE_VOR = 0;

    public NavigationObject(String name, float lat, float lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String toString() {
        return "Navigation object '" + this.name + " @ (" + this.lat + "," + this.lon + ") ";
    }
}
