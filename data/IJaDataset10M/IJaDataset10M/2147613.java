package org.promotego.api.geocoder;

public class GeocodeException extends Exception {

    public GeocodeException(String string, Exception e) {
        super(string, e);
    }
}
