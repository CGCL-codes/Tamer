package a1;

public class Oberflache {

    private int obid;

    private String farbe, ausrichtung;

    Oberflache() {
        this(0, "white", "oben");
    }

    Oberflache(int obid) {
        this.obid = obid;
    }

    Oberflache(int obid, String farbe, String ausrichtung) {
        this(obid);
        this.farbe = farbe;
        this.ausrichtung = ausrichtung;
    }

    /************* Get-Set ***********************/
    public int getObid() {
        return obid;
    }

    public void setObid(int obid) {
        this.obid = obid;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public String getAusrichtung() {
        return ausrichtung;
    }

    public void setAusrichtung(String ausrichtung) {
        this.ausrichtung = ausrichtung;
    }
}
