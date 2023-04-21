package domein;

import java.util.Random;
import domein.item.Bonus;

/**
 * De klasse Personage.
 */
public abstract class Personage implements Bonus, Cloneable {

    private int geld;

    protected int schadepunten;

    private Vak hetVak;

    protected Categorie categorie;

    protected Random randomGenerator = new Random();

    /**
	 * Berekent het aantal resterende stappen door middel 
	 * van het aantal snelheidspunten.
	 *
	 * @return het huidige aantal stappen.
	 */
    public int gooiVerplaatsDobbelsteen() {
        int snelheidspunten = categorie.getSnelheidspunten() + bepaalSnelheidspuntenBonus();
        int aantalStappen = 0;
        for (int i = 0; i < snelheidspunten; i++) aantalStappen += (1 + randomGenerator.nextInt(6));
        return aantalStappen;
    }

    /**
	 * Berekent het aantal schedels door middel van het aantal aanvalspunten.
	 *
	 * @return het aantal schedels.
	 */
    public int gooiAanvalsdobbelsteen() {
        int aanvalspunten = categorie.getAanvalspunten() + bepaalAanvalspuntenBonus();
        int aantalSchedels = 0;
        for (int i = 0; i < aanvalspunten; i++) if (randomGenerator.nextInt(6) < 3) aantalSchedels++;
        return aantalSchedels;
    }

    /**
	 * Berekent het aantal schilden door middel 
	 * van het aantal verdedigingspunten
	 * en of het voor een speler of tegenstander is.
	 *
	 * @return het aantal schilden.
	 */
    public abstract int gooiVerdedigingsdobbelsteen();

    /**
	 * Geeft het aantal lichaamspunten weer.
	 *
	 * @return het aantal lichaamspunten.
	 */
    public int geefLichaamspunten() {
        int lichaamspunten = categorie.getLichaamspunten() + bepaalLichaamspuntenBonus() - schadepunten;
        return (lichaamspunten > 0 ? lichaamspunten : 0);
    }

    /**
	 * Controleert of het personage dood is.
	 *
	 * @return true, personage is dood.
	 */
    public abstract boolean isDood();

    /**
	 * Controleert of dit personage op het doorgeven vak staat.
	 *
	 * @param hetVak 
	 * het vak dat u doorgeeft.
	 * 
	 * @return true, indien het personage op het vak staat.
	 */
    public boolean staatOpVak(Vak hetVak) {
        return (this.hetVak == hetVak);
    }

    /**
	 * Get het geld van het personage.
	 *
	 * @return het geld van het personage.
	 */
    public int getGeld() {
        return geld;
    }

    /**
	 * Zet het geld van het personage.
	 *
	 * @param geld 
	 * het nieuwe geld van het personage.
	 */
    public void setGeld(int geld) {
        this.geld = geld;
    }

    /**
	 * Get de schadepunten van het personage.
	 *
	 * @return de schadepunten van het personage.
	 */
    public int getSchadepunten() {
        return schadepunten;
    }

    /**
	 * Zet de schadepunten van het personage.
	 *
	 * @param schadepunten 
	 * de nieuwe schadepunten van het personage.
	 */
    public void setSchadepunten(int schadepunten) {
        if (schadepunten >= 0) this.schadepunten = schadepunten; else this.schadepunten = 0;
    }

    /**
	 * Zet het personage op een vak.
	 *
	 * @param hetVak 
	 * het nieuwe vak waarop het personage komt te staan.
	 */
    public void setVak(Vak hetVak) {
        this.hetVak = hetVak;
    }

    /**
	 * Get de vak waar het personage op staat.
	 *
	 * @return het vak waar het personage op staat.
	 */
    public Vak getVak() {
        return hetVak;
    }

    /**
	 * Get de categorie van het personage.
	 *
	 * @return de categorie van het personage.
	 */
    public Categorie getCategorie() {
        return categorie;
    }

    /**
	 * Zet de categorie van het personage.
	 *
	 * @param categorie 
	 * de nieuwe categorie van het personage.
	 */
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int geefMaxLevenspunten() {
        return categorie.getLichaamspunten() + bepaalLichaamspuntenBonus();
    }

    @Override
    protected Object clone() {
        try {
            Personage personageKopie = (Personage) super.clone();
            personageKopie.categorie = (Categorie) categorie.clone();
            return personageKopie;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return (getClass().getSimpleName() + "\n\n" + "Soort: " + categorie.getNaam() + "\n" + "Geld: " + this.geld + "\n");
    }
}
