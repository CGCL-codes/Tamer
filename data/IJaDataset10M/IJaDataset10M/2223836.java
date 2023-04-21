package com.flca.pstc42.dto;

public class DistanceDto {

    private double distance;

    private double travelDistance;

    private int googleStatus;

    private String statusMsg;

    private String googleResult;

    private AdresDto fromAdres;

    private AdresDto toAdres;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(double travelDistance) {
        this.travelDistance = travelDistance;
    }

    public int getGoogleStatus() {
        return googleStatus;
    }

    public void setGoogleStatus(int googleStatus) {
        this.googleStatus = googleStatus;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String googStatusMsg) {
        this.statusMsg = googStatusMsg;
    }

    public String getGoogleResult() {
        return googleResult;
    }

    public void setGoogleResult(String googleResult) {
        this.googleResult = googleResult;
    }

    public AdresDto getFromAdres() {
        return fromAdres;
    }

    public void setFromAdres(AdresDto fromAdres) {
        this.fromAdres = fromAdres;
    }

    public AdresDto getToAdres() {
        return toAdres;
    }

    public void setToAdres(AdresDto toAdres) {
        this.toAdres = toAdres;
    }

    public String toString() {
        return "distance=" + getDistance() + ",travelDistance=" + getTravelDistance() + ",status=" + getGoogleStatus() + ",googleResult=" + getGoogleResult();
    }
}
