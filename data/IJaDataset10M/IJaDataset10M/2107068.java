package plattenverwaltung;

public class Platte {

    private int nummer;

    private double laenge;

    private double breite;

    public Platte(int nummer) {
        setNummer(nummer);
        laenge = 1;
        breite = 1;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        if (nummer > 0) this.nummer = nummer; else System.out.println("ung�ltige Plattennummer");
    }

    public double getLaenge() {
        return laenge;
    }

    public void setLaenge(double laenge) {
        if (laenge > 0) this.laenge = laenge; else System.out.println("L�nge muss gr��er als 0 sein");
    }

    public double getBreite() {
        return breite;
    }

    public void setBreite(double breite) {
        if (breite > 0) this.breite = breite; else System.out.println("Breite muss gr�0er als 0 sein");
    }

    public double berechneUmfang() {
        return 2 * laenge + 2 * breite;
    }

    public double berechneFlaeche() {
        return laenge * breite;
    }
}
