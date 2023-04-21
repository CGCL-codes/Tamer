package photospace.meta;

import org.apache.commons.lang.builder.*;

public class Position implements java.io.Serializable, Cloneable {

    private Double latitude;

    private Double longitude;

    private Double altitude;

    public Position() {
    }

    public Position(Double latitude, Double longitude, Double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public boolean hasData() {
        return latitude != null || longitude != null || altitude != null;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Position)) return false;
        Position other = (Position) o;
        return new EqualsBuilder().append(latitude, other.getLatitude()).append(longitude, other.getLongitude()).append(altitude, other.getAltitude()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(latitude).append(longitude).append(altitude).toHashCode();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "Position[" + latitude + "," + longitude + "]";
    }
}
