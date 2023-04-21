package messages;

import framework.IPlaneDataObject;
import java.io.Serializable;

/**
 *
 * @author m0ng
 */
public class PlaneDataObject implements IPlaneDataObject, Serializable {

    private String senderIP;

    private float lat;

    private float lon;

    private float alt;

    private float speed;

    private float heading;

    private String nick;

    public PlaneDataObject(String senderIP, String senderNick, float lat, float lon, float alt, float speed, float heading) {
        this.senderIP = senderIP;
        this.nick = senderNick;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.speed = Math.round(speed);
        this.heading = heading;
    }

    public float getLatitude() {
        return lat;
    }

    public float getLongitude() {
        return lon;
    }

    public float getAltitude() {
        return alt;
    }

    public String getIP() {
        return senderIP;
    }

    public String getNick() {
        return nick;
    }

    public float getSpeed() {
        return speed;
    }

    public float getHeading() {
        return heading;
    }
}
