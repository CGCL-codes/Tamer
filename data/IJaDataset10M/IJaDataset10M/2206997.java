package org.imogene.ws.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Implementation of a GeoField bean
 * @author MEDES-IMPS
 */
@XmlRootElement(name = "geofield")
@XmlType(propOrder = { "latitude", "longitude" })
public class GeoField {

    private Double latitude;

    private Double longitude;

    public GeoField() {
        super();
    }

    @XmlElement(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @XmlElement(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
