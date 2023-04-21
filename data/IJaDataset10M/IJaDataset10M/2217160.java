package org.zeroexchange.model.location;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * @author black
 *
 */
@Embeddable
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private City city;

    private BigDecimal longitude;

    private BigDecimal latitude;

    @ManyToOne
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
