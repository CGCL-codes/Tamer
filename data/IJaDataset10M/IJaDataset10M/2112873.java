package logique.reseau.classements;

/**
 * 
 * @author camille
 *
 */
public class Departementale extends Classement {

    private static Departementale singleton;

    private final String intitule;

    private Departementale() {
        this.intitule = "departementale";
    }

    public static Departementale getInstance() {
        if (singleton == null) {
            singleton = new Departementale();
        }
        return singleton;
    }

    @Override
    public String intitule() {
        return intitule;
    }
}
