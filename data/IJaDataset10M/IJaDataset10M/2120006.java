package net.sf.freesimrc.vatsim;

public class Metar {

    private String metar;

    private String station;

    public Metar(String station) {
        this.station = station;
    }

    public String getMetar() {
        return metar;
    }

    public void setMetar(String metar) {
        this.metar = metar;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
