package cz.darmovzalt.gps;

import java.lang.*;

public class NmeaGpsAdapter implements NmeaGpsListener {

    public void positionChanged(float lat, float lon) {
    }

    public void altitudeChanged(float altitude, String unit) {
    }

    public void satelliteChangeBegin() {
    }

    public void satelliteChanged(int id, float elevation, float azimuth, float snr) {
    }

    public void satelliteChangeEnd() {
    }

    public void dateTimeChanged(long date, long time) {
    }

    public void dilutionOfPrecisionChanged(float horizontal, float vertical, float p) {
    }

    public void moveChanged(float sog, float tmg) {
    }
}
