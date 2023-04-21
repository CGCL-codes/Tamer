package de.byteholder.geoclipse.gpsd;

import java.util.Date;
import de.byteholder.gpx.GeoPosition;

public class GPSData {

    private GeoPosition geoPosition;

    private Date dateTime;

    private float heading;

    private float speed;

    private float altitude;

    public GPSData() {
    }

    /**
	 * Make a copy.
	 * 
	 * @param copyObj
	 */
    public GPSData(GPSData copyObj) {
        if (copyObj != null) {
            geoPosition = copyObj.geoPosition;
            dateTime = copyObj.dateTime;
            heading = copyObj.heading;
            speed = copyObj.speed;
            altitude = copyObj.altitude;
        }
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GPSData) {
            return equals((GPSData) other);
        } else {
            return false;
        }
    }
}
