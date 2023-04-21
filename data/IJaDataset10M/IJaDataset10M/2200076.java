package rothag.models.monuments;

import rothag.models.ModelBase;

/**
 *
 * @author vincent
 */
public class PetitePyramide extends ModelBase implements Monument {

    private String nom;

    private int nbOuvrierNecessaire;

    private int pointsMax;

    private int pointsMin;

    private boolean realise;

    public PetitePyramide() {
        nom = "Petite Pyramide";
        nbOuvrierNecessaire = 3;
        pointsMax = 1;
        pointsMin = 0;
        realise = false;
    }

    public String getNom() {
        return nom;
    }

    public int getNbOuvrierNecessaire() {
        return nbOuvrierNecessaire;
    }

    public int getPointsMax() {
        return pointsMax;
    }

    public int getPointsMin() {
        return pointsMin;
    }

    public boolean getRealise() {
        return realise;
    }

    public void setRealise(boolean val) {
        realise = val;
    }
}
