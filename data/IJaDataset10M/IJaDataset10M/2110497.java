package pk.edu.lums.cs5710w08.siteplanner;

/**
 *
 * @author Midhat
 */
public class Antenna {

    private double lat;

    private double lng;

    private double orientation;

    private double power;

    private double gain;

    private double height;

    private double frequency;

    private double beamwidth = 65;

    private int id;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getBeamwidth() {
        return beamwidth;
    }

    public void setBeamwidth(double beamwidth) {
        this.beamwidth = beamwidth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
