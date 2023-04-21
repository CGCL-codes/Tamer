package net.sourceforge.magex.mobile.GPS;

/**
 *
 * a dummy GPS class representing a void GPS provider
 */
public class GPSDummy extends GPSProvider {

    /**
     * contructor
     */
    protected GPSDummy() {
        super();
        type = GPS_NONE;
        updateState(GPS_OUT_OF_SERVICE);
    }

    /**
    * Reads data and state of GPS device and updates it to atributes. Each subclass must define how it does it.
    */
    protected void updateData() {
    }

    /**
    * checks if GPS services are available and updates GPS provider state accordingly
    */
    protected void checkAvailability() {
    }
}
