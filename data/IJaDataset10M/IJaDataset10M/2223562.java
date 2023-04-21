package de.haw.mussDasSein.model;

/**
 * @author Bettzueche
 *
 */
public class Polsterung extends Komponente {

    private PolsterArt polsterArt;

    public PolsterArt getPolsterArt() {
        return polsterArt;
    }

    public void setPolsterArt(PolsterArt polsterArt) {
        this.polsterArt = polsterArt;
    }

    public String getPolsterArtString() {
        return this.polsterArt.toString();
    }

    @Override
    protected Polsterung clone() {
        Polsterung clone = (Polsterung) super.clone();
        clone.polsterArt = this.polsterArt;
        return clone;
    }

    public enum PolsterArt {

        Leder, Textil
    }
}
