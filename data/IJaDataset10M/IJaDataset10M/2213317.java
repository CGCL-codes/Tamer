package cz.darmovzalt.gpstool;

import java.lang.*;
import java.io.*;
import java.util.*;
import cz.darmovzalt.gps.*;

public class DumpNmeaGpsListener implements NmeaGpsListener {

    protected PrintWriter pw;

    protected long date;

    public DumpNmeaGpsListener(PrintWriter pw) {
        this.pw = pw;
    }

    public void positionChanged(float lat, float lon) {
        pw.println("Position changed: " + lat + " : " + lon);
        pw.flush();
    }

    public void altitudeChanged(float altitude, String unit) {
        pw.println("Altitude changed: " + altitude + " " + unit);
        pw.flush();
    }

    public void satelliteChangeBegin() {
        pw.println("Satellite change begin");
        pw.flush();
    }

    public void satelliteChanged(int id, float elevation, float azimuth, float snr) {
        pw.println("Satellite changed: " + id + " (elev " + elevation + ", azimuth " + azimuth + ", snr " + snr + ")");
        pw.flush();
    }

    public void satelliteChangeEnd() {
        pw.println("Satellite change end");
        pw.flush();
    }

    public void dateTimeChanged(long date, long time) {
        if ((date != 0) && (date != this.date)) this.date = date;
        pw.println("Date/time changed: " + new Date(this.date + time) + " (" + (date + time) + " ms)");
        pw.flush();
    }

    public void dilutionOfPrecisionChanged(float horizontal, float vertical, float p) {
        pw.println("Dilution of precission changed: horizontal " + horizontal + ", vertical " + vertical + ", p " + p);
        pw.flush();
    }

    public void moveChanged(float sog, float tmg) {
        pw.println("Move changed: sog=" + sog + ", tmg=" + tmg);
        pw.flush();
    }
}
