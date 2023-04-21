package guitar;

/**
 *
 * @author martin
 */
public class TuningLibrary {

    private TuningLibrary() {
    }

    public static Neck EBGDAE = new Neck("EBGDAE");

    public static Neck getDefaultNeck() {
        return EBGDAE;
    }
}
