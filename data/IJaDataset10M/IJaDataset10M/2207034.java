package messages.planesim;

import messages.*;
import java.io.Serializable;

/**
 *
 * @author Matthias Roth
 */
public class SimDataObject implements Serializable {

    private String name;

    private float lat;

    private float lon;

    private float alt;

    /**
     * Erstellt eine neue Instanz eines SimDataObject.
     *
     * @param po Das planeobject, welches aus den benoetigten DAten Breitengrad, 
     * Laengengrad, Hoehe und Namen besteht.
     */
    public SimDataObject(final PlaneObject po) {
        this.name = po.getName();
        this.lat = po.getLat();
        this.lon = po.getLon();
        this.alt = po.getAlt();
    }

    /**
     * Wird benutzt, um den Namen des simulierten Flugzeugs zu erhalten.
     *
     * @return Der Name des Flugzeugs als String.
     */
    public final String getName() {
        return name;
    }

    /**
     * Wird benutzt, um den Breitengrad des simulierten Flugzeugs zu erhalten.
     *
     * @return Der Breitengrad des Flugzeugs als float.
     */
    public final float getLat() {
        return lat;
    }

    /**
     * Wird benutzt, um den Laengengrad des simulierten Flugzeugs zu erhalten.
     *
     * @return Der Laengengrad des Flugzeugs als float.
     */
    public final float getLon() {
        return lon;
    }

    /**
     * Wird benutzt, um die Hoehe des simulierten Flugzeugs zu erhalten.
     *
     * @return Die Hoehe des Flugzeugs als float.
     */
    public final float getAlt() {
        return alt;
    }
}
