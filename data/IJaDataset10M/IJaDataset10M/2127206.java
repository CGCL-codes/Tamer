package jsat2me;

/**
 *
 * @author netom
 */
public class SatellitePosition {

    private double azimuth;

    private double elevation;

    public SatellitePosition(double azimuth, double elevation) {
        this.azimuth = azimuth;
        this.elevation = elevation;
    }

    /**
     * @return the azimuth
     */
    public double getAzimuth() {
        return azimuth;
    }

    /**
     * @param azimuth the azimuth to set
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    /**
     * @return the elevation
     */
    public double getElevation() {
        return elevation;
    }

    /**
     * @param elevation the elevation to set
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
}
