package net.fuhrparkservice;

public class NaiveFilliale {

    private static final int increment = 5;

    private String ort;

    private Fahrzeug[] fuhrpark;

    private int anzahl = 0;

    public NaiveFilliale() {
        super();
        this.fuhrpark = new Fahrzeug[20];
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public Fahrzeug[] getFuhrpark() {
        return fuhrpark;
    }

    public void addFahrzeug(Fahrzeug neuesFahrzeug) {
        if (anzahl > fuhrpark.length) {
            vergroessereFuhrpark();
        }
        fuhrpark[anzahl] = neuesFahrzeug;
        anzahl++;
    }

    private void vergroessereFuhrpark() {
        Fahrzeug[] neuerFurhpark = new Fahrzeug[fuhrpark.length + increment];
        for (int i = 0; i < fuhrpark.length; i++) {
            neuerFurhpark[i] = fuhrpark[i];
        }
        this.fuhrpark = neuerFurhpark;
    }
}
