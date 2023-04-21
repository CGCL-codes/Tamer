package hr.fer.zemris.java.tecaj_5.vjezba.zadatak_1;

/**
 * The Class Grupa.
 */
public class Grupa implements Comparable<Grupa> {

    private String oznakaGrupe;

    private double[] maxBodovi;

    private char[] tocniOdgovori;

    /**
	 * Instancira novu grupu.
	 * 
	 * @param oznakaGrupe oznaka grupe
	 * @param brojZadataka broj zadataka u grupi
	 */
    public Grupa(String oznakaGrupe, int brojZadataka) {
        this.oznakaGrupe = oznakaGrupe;
        maxBodovi = new double[brojZadataka];
        tocniOdgovori = new char[brojZadataka];
    }

    /**
	 * Dohvaca polje u kojem se nalaze maksimalni bodovi za zadatke
	 * 
	 * @return max bodovi
	 */
    public double[] getMaxBodovi() {
        return maxBodovi;
    }

    /**
	 * Dohvaca polje u kojem se nalaze svi tocni odgovori
	 * 
	 * @return tocni odgovori
	 */
    public char[] getTocniOdgovori() {
        return tocniOdgovori;
    }

    /**
	 * Dohvaca maksimalan broj bodova za odredjeni zadatak
	 * 
	 * @param zadatak the zadatak
	 * 
	 * @return maksimalan broj bodova
	 */
    public double getMaxBodovi(int zadatak) {
        return maxBodovi[zadatak];
    }

    /**
	 * Postavlja maksimalan broj bodova za odredjeni zadatak.
	 * 
	 * @param zadatak zadatak
	 * @param bodovi bodovi
	 */
    public void setMaxBodovi(int zadatak, double bodovi) {
        maxBodovi[zadatak] = bodovi;
    }

    /**
	 * Dohvaca tocan odgovor odredjenog zadatka
	 * 
	 * @param zadatak zadatak
	 * 
	 * @return tocan odgovor
	 */
    public char getTocanOdgovor(int zadatak) {
        return tocniOdgovori[zadatak];
    }

    /**
	 * Postavlja tocan odgovor za odredjeni zadatak
	 * 
	 * @param zadatak zadatak
	 * @param odgovor odgovor
	 */
    public void setTocniOdgovori(int zadatak, char odgovor) {
        tocniOdgovori[zadatak] = odgovor;
    }

    /**
	 * Dohvaca oznaku grupe
	 * 
	 * @return oznaka grupe
	 */
    public String getOznakaGrupe() {
        return oznakaGrupe;
    }

    @Override
    public int compareTo(Grupa arg0) {
        if (arg0 == null) return 1;
        return oznakaGrupe.compareTo(arg0.oznakaGrupe);
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == null) return false;
        if (!(arg0 instanceof Grupa)) return false;
        Grupa grupa = (Grupa) arg0;
        return oznakaGrupe.equals(grupa.oznakaGrupe);
    }

    @Override
    public int hashCode() {
        return oznakaGrupe.hashCode();
    }
}
