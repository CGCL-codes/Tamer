package com.crimerank.data;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.android.maps.GeoPoint;

public class BusRoute implements Serializable {

    private static final long serialVersionUID = 7764218619013252440L;

    String directionNum;

    String directionText;

    String tripHeadsign;

    String routeId;

    int latSpan;

    int lonSpan;

    ArrayList<GeoPoint> route;

    int latCenter;

    int lonCenter;

    ArrayList<String> schedule = new ArrayList<String>();

    public BusRoute() {
    }

    public BusRoute(String routeId, String directionNum, String directionText, String tripHeadsign) {
        this.routeId = routeId;
        this.directionNum = directionNum;
        this.directionText = directionText;
        this.tripHeadsign = tripHeadsign;
    }

    public void setSchedule(String t) {
        this.schedule.add(t);
    }

    public ArrayList<String> getSchedule() {
        return schedule;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public void setCenter(int lat, int lon) {
        this.latCenter = lat;
        this.lonCenter = lon;
    }

    public int getLatCenter() {
        return latCenter;
    }

    public int getLonCenter() {
        return lonCenter;
    }

    public int getLatSpan() {
        return latSpan;
    }

    public void setLatSpan(int latSpan) {
        this.latSpan = latSpan;
    }

    public int getLonSpan() {
        return lonSpan;
    }

    public void setLonSpan(int lonSpan) {
        this.lonSpan = lonSpan;
    }

    public String getDirectionNum() {
        return directionNum;
    }

    public void setDirectionNum(String directionNum) {
        this.directionNum = directionNum;
    }

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }

    public ArrayList<GeoPoint> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<GeoPoint> route) {
        this.route = route;
    }
}
