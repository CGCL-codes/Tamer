package edu.calpoly.csc.plantidentification.objects;

import java.util.Date;

public class Identification {

    public int id;

    public int plantId;

    public String txt;

    public double lat;

    public double lng;

    private String date;

    private String imageLocation;

    private String imageAttribution;

    public String plantScientificName;

    public String user;

    public int shared;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateAsString() {
        return date;
    }

    public Date getDate() {
        Date d;
        try {
            d = new Date(Date.parse(this.date));
        } catch (IllegalArgumentException e) {
            d = new Date(Date.UTC(0, 0, 0, 0, 0, 0));
        }
        return d;
    }

    public void setImage(String imageLocation, String imageAttribution) {
        this.imageLocation = imageLocation;
        this.imageAttribution = imageAttribution;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getImageAttribution() {
        return imageAttribution;
    }
}
