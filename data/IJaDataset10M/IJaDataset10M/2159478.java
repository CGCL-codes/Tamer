package com.ma_la.myRunning.domain;

import java.io.Serializable;

/**
 * PoJo fuer eine Koordinate
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class GeoCoords implements Serializable {

    private static final long serialVersionUID = -7020863150746564647L;

    private Long id;

    private Double lat;

    private Double lon;

    public Long getId() {
        return id;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
