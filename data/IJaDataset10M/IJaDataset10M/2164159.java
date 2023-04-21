package rothag.models.desastres;

import rothag.models.ModelBase;

/**
 * @version 0.1
 * @author gulian.lorini
 */
public class Secheresse extends ModelBase implements Desastre {

    private String description;

    /**
     * Constructeur
     */
    public Secheresse() {
        description = "La secheresse touche vos villes ! Vous perdez 2 points...";
    }

    /**
     * Retourne la description du désastre
     * @return Description
     */
    public String getDescription() {
        return description;
    }
}
