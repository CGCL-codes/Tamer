package net.aetherial.gis.garmin;

import java.io.*;
import java.util.*;

public class Position {

    private long latitude;

    private long longitude;

    public Position(long lat, long lon) {
        latitude = lat;
        longitude = lon;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public int getDegreeLatitude() {
        float flat = (float) latitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flat > 180) return (int) (360 - flat); else return (int) flat;
    }

    public int getDegreeLongitude() {
        float flong = (float) longitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong > 180) return (int) (360 - flong); else return (int) flong;
    }

    public float getFloatDegreeLatitude() {
        float flong = (float) latitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong > 180) return (360 - flong); else return flong;
    }

    public float getFloatDegreeLongitude() {
        float flong = (float) longitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong > 180) return (360 - flong); else return flong;
    }

    public double getDoubleDegreeLatitude() {
        double dlong = (double) latitude * ((double) 180.0 / (double) Math.pow(2, 31));
        if (dlong > 180) return (360 - dlong); else return dlong;
    }

    public double getDoubleDegreeLongitude() {
        double dlong = (double) longitude * ((double) 180.0 / (double) Math.pow(2, 31));
        if (dlong > 180) return (360 - dlong); else return dlong;
    }

    public int getMinuteLatitude() {
        float flat = (float) latitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flat > 180) flat = 360 - flat;
        float fraction = flat - (float) getDegreeLatitude();
        float latMin = fraction * 60;
        return (int) latMin;
    }

    public int getMinuteLongitude() {
        float flong = (float) longitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong > 180) flong = 360 - flong;
        float fraction = flong - (float) getDegreeLongitude();
        float longMin = fraction * 60;
        return (int) longMin;
    }

    public float getSecondLatitude() {
        float flat = (float) latitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flat > 180) flat = 360 + flat;
        float fraction = flat - (float) getDegreeLatitude();
        fraction = fraction - ((float) getMinuteLatitude() / 60);
        return fraction * 3600;
    }

    public float getSecondLongitude() {
        float flong = (float) longitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong > 180) flong = 360 - flong;
        float fraction = flong - (float) getDegreeLongitude();
        fraction = fraction - ((float) getMinuteLongitude() / 60);
        return fraction * 3600;
    }

    public int getLatOrientation() {
        if (getDegreeLatitude() > 0) return NORTH; else return SOUTH;
    }

    public int getLongOrientation() {
        if (getDegreeLongitude() > 0) return EAST; else return WEST;
    }

    public char getLongOrientChar() {
        float flong = (float) longitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flong < 180) return 'E'; else return 'W';
    }

    public char getLatOrientChar() {
        float flat = (float) latitude * ((float) 180.0 / (float) Math.pow(2, 31));
        if (flat < 180) return 'N'; else return 'S';
    }

    public static final int NORTH = 0;

    public static final int SOUTH = 1;

    public static final int EAST = 2;

    public static final int WEST = 3;
}
