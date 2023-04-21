package com.esp.model;

import java.util.Date;

/**
 * @author Echo
 * 
 */
public class ActiveDevice {

    private Integer id = 0;

    private Integer device_sn = 0;

    private Date conn_datetime = new Date();

    private String person_id = null;

    private Boolean hiden = false;

    private Double latitude = null;

    private Double longtitude = null;

    private Boolean loged_in = false;

    private Double distance = 0D;

    private String direction = "no information";

    /**************************************************************************
	 * refresh action
	 * ************************************************************************
	 */
    public void refreshInfomation(ActiveDevice anotherDev) {
        double latDistance = latitude - anotherDev.getLatitude();
        double lonDistance = longtitude - anotherDev.getLongtitude();
        this.distance = Math.floor(Math.hypot(latDistance, lonDistance));
        if (lonDistance >= 0 && latDistance >= 0) {
            this.direction = "NorthWest";
        } else if (lonDistance >= 0 && latDistance < 0) {
            this.direction = "SouthWest";
        } else if (lonDistance < 0 && latDistance < 0) {
            this.direction = "SouthEast";
        } else if (lonDistance < 0 && latDistance >= 0) {
            this.direction = "NorthEast";
        } else {
            this.direction = "no information";
        }
    }

    /**************************************************************************
	 * getter/setter
	 * ************************************************************************
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(Integer device_sn) {
        this.device_sn = device_sn;
    }

    public Date getConn_datetime() {
        return conn_datetime;
    }

    public void setConn_datetime(Date conn_datetime) {
        this.conn_datetime = conn_datetime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Boolean getLoged_in() {
        return loged_in;
    }

    public void setLoged_in(Boolean loged_in) {
        this.loged_in = loged_in;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public Boolean getHiden() {
        return hiden;
    }

    public void setHiden(Boolean hiden) {
        this.hiden = hiden;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
