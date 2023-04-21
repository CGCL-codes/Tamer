package com.FindHotel;

import com.google.android.maps.GeoPoint;

public class Hotel {

    private GeoPoint point;

    private String name;

    private double lat;

    private double lng;

    private String phone;

    private String desc;

    private String id;

    public Hotel(String id, String name, String phone, double latitude, double longitude, String description) {
        this.setId(id);
        this.name = name;
        this.lat = latitude;
        this.lng = longitude;
        this.phone = phone;
        this.setDesc(description);
        point = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
    }

    public GeoPoint getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
