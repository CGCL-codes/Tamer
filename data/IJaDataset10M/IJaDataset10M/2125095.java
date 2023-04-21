package logique.reseau.classements;

/**
 * 
 * @author camille
 *
 */
public class Autoroute extends Classement {

    private static Autoroute singleton;

    private final String intitule;

    private Autoroute() {
        this.intitule = "autoroute";
    }

    public static Autoroute getInstance() {
        if (singleton == null) {
            singleton = new Autoroute();
        }
        return singleton;
    }

    @Override
    public String intitule() {
        return intitule;
    }
}
